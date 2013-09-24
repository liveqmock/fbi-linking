package apps.fisjz.online.action;

import apps.fisjz.domain.staring.T2010Request.TIA2010;
import apps.fisjz.domain.staring.T2010Response.TOA2010;
import apps.fisjz.domain.staring.T2010Response.TOA2010PaynotesInfo;
import apps.fisjz.domain.staring.T2010Response.TOA2010PaynotesItem;
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
 * 1532010缴款书查询
 * zhanrui  20130922
 */

@Component
public class Txn1532010Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532010Action.class);

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        //解析特色平台请求报文体
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2010Request");
        TIA2010 tia = (TIA2010)dataFormat.fromMessage(new String(msg.msgBody), "TIA2010");
        logger.info("[1532010缴款书信息查询] 网点号:" + msg.branchID + " 柜员号:" + msg.tellerID + " 缴款书编号:" + tia.getNotescode());

        //与财政局通讯
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List rtnlist = service.queryNontaxPayment(getApplicationidByAreaCode(tia.getAreacode()),
                getBankCodeByAreaCode(tia.getAreacode()),
                tia.getYear(),
                getFinorgByAreaCode(tia.getAreacode()),
                tia.getNotescode(),tia.getCheckcode(),tia.getBilltype());

        //判断财政局响应结果
        if (!getResponseResult(rtnlist)) {
            throw  new RuntimeException(getResponseErrMsg(rtnlist));
        }

        //处理财政局响应报文 1：处理财政局返回的缴款书主信息
        TOA2010 toa = new TOA2010();
        TOA2010PaynotesInfo paynotesInfo = new TOA2010PaynotesInfo();
        Map responseContentMap = (Map) rtnlist.get(0);
        BeanUtils.populate(paynotesInfo, responseContentMap);
        toa.setPaynotesInfo(paynotesInfo);

        //处理财政局响应报文 2：处理财政局返回的缴款书明细子项目信息
        List<TOA2010PaynotesItem> paynotesItems = new ArrayList<TOA2010PaynotesItem>();
        List details = (List)responseContentMap.get("details");
        for (Object detail : details) {
            TOA2010PaynotesItem item = new TOA2010PaynotesItem();
            BeanUtils.populate(item, (Map)detail);
            paynotesItems.add(item);
        }

        //处理财政局响应报文 3：计算明细子项目条数
        toa.setPaynotesItems(paynotesItems);
        toa.setItemNum("" + paynotesItems.size());

        //组特色平台响应报文
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(toa.getClass().getName(), toa);
        modelObjectsMap.put(toa.getPaynotesInfo().getClass().getName(),  toa.getPaynotesInfo());
        dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2010Response");
        String result = (String)dataFormat.toMessage(modelObjectsMap);
        msg.msgBody = result.getBytes();
        return msg;
    }
}
