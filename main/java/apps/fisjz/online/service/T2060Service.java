package apps.fisjz.online.service;

import apps.fisjz.domain.financebureau.FbPaynotesInfo4ChkAct;
import apps.fisjz.domain.staring.T2060Request.TIA2060;
import apps.fisjz.enums.TxnRtnCode;
import apps.fisjz.gateway.financebureau.NontaxBankService;
import apps.fisjz.gateway.financebureau.NontaxServiceFactory;
import apps.fisjz.repository.dao.FsJzfPaymentInfoMapper;
import apps.fisjz.repository.dao.FsJzfPaymentItemMapper;
import apps.fisjz.repository.dao.common.ActChkMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 对账处理
 * User: zhanrui
 * Date: 13-9-26
 */
@Service
public class T2060Service {
    private static final Logger logger = LoggerFactory.getLogger(T2060Service.class);

    @Autowired
    private ServiceHelper helper;
    @Autowired
    FsJzfPaymentInfoMapper paymentInfoMapper;
    @Autowired
    FsJzfPaymentItemMapper paymentItemMapper;
    @Autowired
    ActChkMapper actChkMapper;


    @SuppressWarnings("unchecked")
    @Transactional
    public void processTxn(Map paramMap) {
        if (processHostChk(paramMap)) {
            processThirdPartyChk(paramMap);
        } else {
            paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
            paramMap.put("rtnMsg", "主机对账失败.");
        }
    }

    private boolean processHostChk(Map paramMap) {
        TIA2060 tia = (TIA2060) paramMap.get("tia");
        String areacode = tia.getAreacode();
        BigDecimal hostTotalamt = new BigDecimal(tia.getTotalamt());
        int hostTotalcnt = Integer.parseInt(tia.getItemNum());
        String txndate = tia.getStartdate();

        int localTotalcnt = actChkMapper.selectHostChkTotalCount(areacode, txndate);
        BigDecimal localTotalamt = actChkMapper.selectHostChkTotalAmt(areacode, txndate);

        if (localTotalcnt == hostTotalcnt && localTotalamt.compareTo(hostTotalamt) == 0) {
            //更新本地数据库信息：主机对账成功
            actChkMapper.updateHostChkSuccessFlag(areacode, txndate);
            return true;
        } else {
            return true;
        }
    }

    private boolean processThirdPartyChk(Map paramMap) {
        TIA2060 tia = (TIA2060) paramMap.get("tia");
        String areacode = tia.getAreacode();
        String txndate = tia.getStartdate();

        List<FbPaynotesInfo4ChkAct> actChkInfoList = actChkMapper.selectDetailsForThirdParty(areacode, txndate);
        handleChkInfo4ThirdParty(actChkInfoList, txndate);

        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List rtnlist = service.insertBankNontaxPayment(
                helper.getApplicationidByAreaCode(tia.getAreacode()),
                helper.getBankCodeByAreaCode(tia.getAreacode()),
                tia.getYear(),
                helper.getFinorgByAreaCode(tia.getAreacode()),
                actChkInfoList);

        if (helper.getResponseResult(rtnlist)) {
            paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
            paramMap.put("rtnMsg", "对账成功!");
            return true;
        } else {
            paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_FAILED.getCode());
            paramMap.put("rtnMsg", helper.getResponseErrMsg(rtnlist));
            return false;
        }
    }

    private void handleChkInfo4ThirdParty(List<FbPaynotesInfo4ChkAct> actChkInfoList, String txnDate){
        for (FbPaynotesInfo4ChkAct info4ChkAct : actChkInfoList) {
            info4ChkAct.setStartdate(txnDate);
            info4ChkAct.setEnddate(txnDate);
        }
    }
}
