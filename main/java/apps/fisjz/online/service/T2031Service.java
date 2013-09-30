package apps.fisjz.online.service;

import apps.fisjz.domain.financebureau.FbPaynotesInfo4Refund;
import apps.fisjz.domain.financebureau.FbResponseChkInfo;
import apps.fisjz.domain.staring.T2031Request.TIA2031;
import apps.fisjz.enums.TxnRtnCode;
import apps.fisjz.gateway.financebureau.NontaxBankService;
import apps.fisjz.gateway.financebureau.NontaxServiceFactory;
import apps.fisjz.repository.dao.FsJzfPaymentInfoMapper;
import apps.fisjz.repository.dao.FsJzfPaymentItemMapper;
import apps.fisjz.repository.model.FsJzfPaymentInfo;
import apps.fisjz.repository.model.FsJzfPaymentInfoExample;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 缴款书退付业务处理
 * User: zhanrui
 * Date: 13-9-23
 */
@Service
public class T2031Service {
    private static final Logger logger = LoggerFactory.getLogger(T2031Service.class);

    @Autowired
    private ServiceHelper helper;
    @Autowired
    FsJzfPaymentInfoMapper paymentInfoMapper;
    @Autowired
    FsJzfPaymentItemMapper paymentItemMapper;

    //退付缴款书信息查询
    public FsJzfPaymentInfo selectPaymentInfo(Map paramMap) {
        TIA2031 tia = (TIA2031) paramMap.get("tia");
        String areacode = tia.getAreacode();
        String notescode = tia.getPaynotesInfo().getNotescode();
        String refundapplycode = tia.getPaynotesInfo().getRefundapplycode();

        FsJzfPaymentInfoExample example = new FsJzfPaymentInfoExample();
        example.createCriteria()
                .andRefundapplycodeEqualTo(refundapplycode)
                .andNotescodeEqualTo(notescode)
                .andAreaCodeEqualTo(areacode)
                .andCanceldateEqualTo("99999999")    //非冲销记录
                .andArchiveFlagEqualTo("0");
        List<FsJzfPaymentInfo> recordList = paymentInfoMapper.selectByExample(example);
        if (recordList.size() == 1) {
            return recordList.get(0);
        } else if (recordList.size() == 0) {
            return null;
        } else {
            throw new RuntimeException("重复记录！");
        }
    }

    //普通退付缴款书缴款
    @SuppressWarnings("unchecked")
    @Transactional
    public void processTxn(Map paramMap){
        String branchId = (String)paramMap.get("branchId");
        String tellerId = (String)paramMap.get("tellerId");
        TIA2031 tia =  (TIA2031)paramMap.get("tia");
        String areacode = tia.getAreacode();

        //与财政局通讯
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List<FbPaynotesInfo4Refund> paramList = new ArrayList<FbPaynotesInfo4Refund>();
        FbPaynotesInfo4Refund fbPaynotesInfo = new FbPaynotesInfo4Refund();
        try {
            BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        } catch (Exception e) {
            throw new RuntimeException("报文处理错误.", e);
        }
        paramList.add(fbPaynotesInfo);
        logger.info("[1532031退付缴款确认] 请求报文信息（发往财政）:" + fbPaynotesInfo.toString());
        List rtnlist = service.updateRefundNontaxPayment(
                helper.getApplicationidByAreaCode(tia.getAreacode()),
                helper.getBankCodeByAreaCode(tia.getAreacode()),
                tia.getYear(),
                helper.getFinorgByAreaCode(tia.getAreacode()),
                paramList);

        if (helper.getResponseResult(rtnlist)) { //缴款退付成功
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
                paramMap.put("rtnMsg", "缴款退付交易失败!明细核对不符!");
                return;
            }

            //全部处理成功 更新标志和处理时间
            FsJzfPaymentInfo record = selectPaymentInfo(paramMap);
            try {
                BeanUtils.copyProperties(record, tia.getPaynotesInfo());
            } catch (Exception e) {
                throw new RuntimeException("报文处理错误.", e);
            }

            stuffPaymentInfoBean_pay(areacode, branchId, tellerId, record);
            paymentInfoMapper.updateByPrimaryKey(record);

            paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
            String rtnMsg = helper.getResponseErrMsg(rtnlist);
            if (StringUtils.isEmpty(rtnMsg)) {
                paramMap.put("rtnMsg", "缴款退付成功.");
            } else {
                paramMap.put("rtnMsg", helper.getResponseErrMsg(rtnlist));
            }
        } else { //缴款失败
            paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
            paramMap.put("rtnMsg", helper.getResponseErrMsg(rtnlist));
        }
    }


    //=======private method=========================================
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
