package apps.fisjz.online.action;

import apps.fisjz.domain.financebureau.FbPaynotesInfo;
import apps.fisjz.domain.staring.T2011Request.TIA2011;
import apps.fisjz.domain.staring.T2011Response.TOA2011;
import apps.fisjz.gateway.financebureau.NontaxBankService;
import apps.fisjz.gateway.financebureau.NontaxServiceFactory;
import common.dataformat.SeperatedTextDataFormat;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1532011缴款书缴款
 * zhanrui  20130923
 */
@Component
public class Txn1532011Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532011Action.class);

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // 解析报文体
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2011Request");
        TIA2011 tia = (TIA2011)dataFormat.fromMessage(new String(msg.msgBody), "TIA2011");

        logger.info("[1532011缴款书缴款] 网点号:" + msg.branchID + " 柜员号:" + msg.tellerID + " 缴款书编号:" + tia.getPaynotesInfo().getNotescode());

        //与第三方机构通讯
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();

        List<FbPaynotesInfo> paramList = new ArrayList<FbPaynotesInfo>();
        FbPaynotesInfo fbPaynotesInfo = new FbPaynotesInfo();
        BeanUtils.copyProperties(fbPaynotesInfo, tia.getPaynotesInfo());
        paramList.add(fbPaynotesInfo);
        List rtnlist = service.updateNontaxPayment("appid", "bank", "" + 2013, "", paramList);
        logger.debug("[1532011缴款书缴款] 请求报文信息（发往财政）:" + fbPaynotesInfo);


        Map responseMap = (Map) rtnlist.get(0);
        String rtnResult = (String)responseMap.get("RESULT");
        String rtnMsg = "";
        boolean isSuccess = false;
        if (rtnResult != null && !"".equals(rtnResult)) {
            if ("SUCCESS".equals(rtnResult)) {
                isSuccess = true;
            }else{
                rtnMsg =  (String)responseMap.get("MESSAGE");
            }
        }
        //本地DB处理


        //组响应报文
        TOA2011 toa = new TOA2011();
        if (isSuccess) {
            toa.setRtnCode("0000");
        } else {
            toa.setRtnCode("1000");
            toa.setRtnMsg(rtnMsg); //TODO 长度处理？
        }

        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(toa.getClass().getName(), toa);

        dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2011Response");
        String toaMsg = (String)dataFormat.toMessage(modelObjectsMap);

        msg.msgBody = toaMsg.getBytes();
        return msg;
    }

}
