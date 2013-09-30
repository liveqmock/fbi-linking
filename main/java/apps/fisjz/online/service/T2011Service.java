package apps.fisjz.online.service;

import apps.fisjz.domain.financebureau.FbPaynotesInfo;
import apps.fisjz.domain.financebureau.FbResponseChkInfo;
import apps.fisjz.domain.staring.T2011Request.TIA2011;
import apps.fisjz.enums.TxnRtnCode;
import apps.fisjz.gateway.financebureau.NontaxBankService;
import apps.fisjz.gateway.financebureau.NontaxServiceFactory;
import apps.fisjz.repository.dao.FsJzfPaymentInfoMapper;
import apps.fisjz.repository.dao.FsJzfPaymentItemMapper;
import apps.fisjz.repository.model.FsJzfPaymentInfo;
import apps.fisjz.repository.model.FsJzfPaymentInfoExample;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 缴款书缴款业务处理
 * User: zhanrui
 * Date: 13-9-23
 */
@Service
public class T2011Service {
    private static final Logger logger = LoggerFactory.getLogger(T2011Service.class);

    @Autowired
    private ServiceHelper helper;
    @Autowired
    FsJzfPaymentInfoMapper paymentInfoMapper;
    @Autowired
    FsJzfPaymentItemMapper paymentItemMapper;


     //缴款书信息查询
    public FsJzfPaymentInfo selectPaymentInfo(Map paramMap){
        TIA2011 tia =  (TIA2011)paramMap.get("tia");
        String areacode = tia.getAreacode();
        String notescode = tia.getPaynotesInfo().getNotescode();
        String checkcode = tia.getPaynotesInfo().getCheckcode();
        String billtype = tia.getPaynotesInfo().getBilltype();

        FsJzfPaymentInfoExample example = new FsJzfPaymentInfoExample();
        example.createCriteria()
                .andNotescodeEqualTo(notescode)
                .andCheckcodeEqualTo(checkcode)
                .andBilltypeEqualTo(billtype)
                .andCanceldateEqualTo("99999999")    //非冲销记录
                .andAreaCodeEqualTo(areacode)
                .andArchiveFlagEqualTo("0");
        List<FsJzfPaymentInfo> recordList  = paymentInfoMapper.selectByExample(example);
        if (recordList.size() == 1) {
              return recordList.get(0);
        }else if (recordList.size() == 0) {
            return null;
        } else {
            throw new RuntimeException("重复记录！");
        }
    }

    //普通缴款书缴款 需发起自动到账交易
    //@Transactional  不做事务处理！！
    @SuppressWarnings("unchecked")
    public void processTxn(Map paramMap){
        String branchId = (String)paramMap.get("branchId");
        String tellerId = (String)paramMap.get("tellerId");
        TIA2011 tia =  (TIA2011)paramMap.get("tia");
        String areacode = tia.getAreacode();

        List rtnlist = new ArrayList();
        FsJzfPaymentInfo fsJzfPaymentInfo = selectPaymentInfo(paramMap);
        String fbBookFlag = fsJzfPaymentInfo.getFbBookFlag();

        try {
            BeanUtils.copyProperties(fsJzfPaymentInfo, tia.getPaynotesInfo());
        } catch (Exception e) {
            throw new RuntimeException("报文处理错误.", e);
        }

        if ("1".equals(fbBookFlag)) {  //已记账但未到账，此时到账标志一定为0
            //自动发起缴款确认交易
            if (processPayConfirmTxn(fsJzfPaymentInfo, paramMap) == 0) {
                //全部处理成功 更新标志和处理时间
                fsJzfPaymentInfo.setRecfeeflag("1");  //!!
                paymentInfoMapper.updateByPrimaryKey(fsJzfPaymentInfo);
                paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
                paramMap.put("rtnMsg", "缴款到账处理成功。");
            }
            return;
        }

        //与财政局通讯  （记账、到账标志都为 0 的情况）
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo> paramList = new ArrayList<FbPaynotesInfo>();
        FbPaynotesInfo fbPaynotesInfo = new FbPaynotesInfo();
        try {
            BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        } catch (Exception e) {
            throw new RuntimeException("报文处理错误.", e);
        }

        paramList.add(fbPaynotesInfo);
        logger.info("[1532011缴款书缴款] 请求报文信息（发往财政）:" + fbPaynotesInfo.toString());
        rtnlist = service.updateNontaxPayment(helper.getApplicationidByAreaCode(tia.getAreacode()),
                helper.getBankCodeByAreaCode(tia.getAreacode()),
                tia.getYear(),
                helper.getFinorgByAreaCode(tia.getAreacode()),
                paramList);

        if (helper.getResponseResult(rtnlist)) { //缴款成功
            //检查返回的缴款信息是否与请求的报文一致
            Map responseContentMap = (Map) rtnlist.get(0);
            FbResponseChkInfo respInfo = new FbResponseChkInfo();
            try {
                BeanUtils.populate(respInfo, responseContentMap);
            } catch (Exception e) {
                throw new RuntimeException("报文处理错误.", e);
            }
            if (!fbPaynotesInfo.getBillid().equals(respInfo.getBillid()) ||
                    !fbPaynotesInfo.getPaynotescode().equals(respInfo.getPaynotescode()) ||
                    !fbPaynotesInfo.getNotescode().equals(respInfo.getNotescode())
                    ) {
                paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
                paramMap.put("rtnMsg", "缴款交易失败!明细核对不符!");
                return;
            }

            //不做事务处理   更新标志和处理时间
            stuffPaymentInfoBean_pay(areacode, branchId, tellerId, fsJzfPaymentInfo);
            paymentInfoMapper.updateByPrimaryKey(fsJzfPaymentInfo);

            //自动发起缴款确认交易
            if (processPayConfirmTxn(fsJzfPaymentInfo, paramMap) == 0) { //此时不会出现返回1的情况（重复交易）
                //处理成功更新标志
                fsJzfPaymentInfo.setRecfeeflag("1");  //!!
                paymentInfoMapper.updateByPrimaryKey(fsJzfPaymentInfo);
                paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
                paramMap.put("rtnMsg", "缴款到账处理成功。");
            }
        } else { //缴款失败
            String rtnMsg = helper.getResponseErrMsg(rtnlist);
            //TODO
            if (rtnMsg.contains("已确认收款，不能重复操作！")) {
                paramMap.put("rtnCode", TxnRtnCode.TXN_PAY_REPEATED.getCode());
            } else {
                paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
            }
            paramMap.put("rtnMsg", rtnMsg);
        }
    }

    /**
     * 发起缴款确认交易  （此方法中不做数据库处理，避免独立事务）
     * 返回 0：到账处理成功   1：处理失败，失败原因是重复到账交易  -1：交易处理失败
     */
    @SuppressWarnings("unchecked")
    private int processPayConfirmTxn(FsJzfPaymentInfo fsJzfPaymentInfo, Map paramMap) {
        TIA2011 tia =  (TIA2011)paramMap.get("tia");

        //与财政局通讯
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo> paramList = new ArrayList<FbPaynotesInfo>();
        FbPaynotesInfo fbPaynotesInfo = new FbPaynotesInfo();
        try {
            BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        } catch (Exception e) {
            throw new RuntimeException("报文处理错误.", e);
        }

        paramList.add(fbPaynotesInfo);
        logger.info("[1532012缴款书到帐确认] 请求报文信息（发往财政）:" + fbPaynotesInfo.toString());
        List rtnlist =  service.accountNontaxPayment(
                helper.getApplicationidByAreaCode(tia.getAreacode()),
                helper.getBankCodeByAreaCode(tia.getAreacode()),
                tia.getYear(),
                helper.getFinorgByAreaCode(tia.getAreacode()),
                paramList);

        //判断财政局响应结果
        if (helper.getResponseResult(rtnlist)) { //到帐确认成功
            //检查返回的缴款信息是否与请求的报文一致
            Map responseContentMap = (Map) rtnlist.get(0);
            FbResponseChkInfo respInfo = new FbResponseChkInfo();
            try {
                BeanUtils.populate(respInfo, responseContentMap);
            } catch (Exception e) {
                throw new RuntimeException("报文处理错误.", e);
            }
            if (!fbPaynotesInfo.getBillid().equals(respInfo.getBillid()) ||
                    !fbPaynotesInfo.getPaynotescode().equals(respInfo.getPaynotescode()) ||
                    !fbPaynotesInfo.getNotescode().equals(respInfo.getNotescode())
                    ) {
                paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
                paramMap.put("rtnMsg", "到账交易失败!明细核对不符!");
                return -1;
            }
            //业务成功
            return 0;
        }else{ //财政返回失败
            String rtnMsg = helper.getResponseErrMsg(rtnlist);
            //TODO
            if (rtnMsg.contains("已确认收款，不能重复操作！")) {
                paramMap.put("rtnCode", TxnRtnCode.TXN_PAY_REPEATED.getCode());
                paramMap.put("rtnMsg", rtnMsg);
                return 1;
            } else {
                paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
                paramMap.put("rtnMsg", rtnMsg);
                return -1;
            }
        }
    }

    //PaymentInfoBean 填写基本信息 for 初始记账
    private void stuffPaymentInfoBean_pay(String areaCode, String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
        paymentInfo.setOperBankid(branchId);
        paymentInfo.setOperId(tellerId);
        paymentInfo.setOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        paymentInfo.setOperTime(new SimpleDateFormat("HHmmss").format(new Date()));

        //主机记账成功
        paymentInfo.setHostBookFlag("1");
        paymentInfo.setHostChkFlag("0");

        //财政记账成功
        paymentInfo.setFbBookFlag("1");
        paymentInfo.setFbChkFlag("0");

        //正常记录标志
        paymentInfo.setArchiveFlag("0");
        paymentInfo.setAreaCode(areaCode);
    }
}
