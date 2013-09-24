package apps.fisjz.online.action;

import apps.fisjz.domain.staring.T2030Request.TIA2030;
import apps.fisjz.domain.staring.T2030Response.TOA2030;
import apps.fisjz.domain.staring.T2030Response.TOA2030PaynotesInfo;
import apps.fisjz.domain.staring.T2030Response.TOA2030PaynotesItem;
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
 * 1532030 �ɿ��˸���ѯ
 * zhanrui  20130924
 */

@Component
public class Txn1532030Action extends AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(Txn1532030Action.class);

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        //������ɫƽ̨��������
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2030Request");
        TIA2030 tia = (TIA2030) dataFormat.fromMessage(new String(msg.msgBody), "TIA2030");
        logger.info("[1532030�˸��ɿ����ѯ] �����:" + msg.branchID + " ��Ա��:" + msg.tellerID + " �˸��ɿ�����:" + tia.getRefundapplycode());

        //�������ͨѶ
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List rtnlist = service.queryRefundNontaxPayment(
                FISJZ_APPLICATIONID,
                FISJZ_BANK,
                tia.getYear(),
                getFinorgByAreaCode(tia.getAreacode()),
                tia.getRefundapplycode(),
                tia.getNotescode());

        //�жϲ�������Ӧ���
        if (!getResponseResult(rtnlist)) {
            throw new RuntimeException(getResponseErrMsg(rtnlist));
        }

        //�����������Ӧ���� 1����������ַ��صĽɿ�������Ϣ
        TOA2030 toa = new TOA2030();
        TOA2030PaynotesInfo paynotesInfo = new TOA2030PaynotesInfo();
        Map responseContentMap = (Map) rtnlist.get(0);
        BeanUtils.populate(paynotesInfo, responseContentMap);
        toa.setPaynotesInfo(paynotesInfo);

        //�����������Ӧ���� 2����������ַ��صĽɿ�����ϸ����Ŀ��Ϣ
        List<TOA2030PaynotesItem> paynotesItems = new ArrayList<TOA2030PaynotesItem>();
        List details = (List) responseContentMap.get("details");
        for (Object detail : details) {
            TOA2030PaynotesItem item = new TOA2030PaynotesItem();
            BeanUtils.populate(item, (Map) detail);
            paynotesItems.add(item);
        }

        //�����������Ӧ���� 3��������ϸ����Ŀ����
        toa.setPaynotesItems(paynotesItems);
        toa.setItemNum("" + paynotesItems.size());

        //����ɫƽ̨��Ӧ����
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(toa.getClass().getName(), toa);
        modelObjectsMap.put(toa.getPaynotesInfo().getClass().getName(), toa.getPaynotesInfo());
        dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2030Response");
        String result = (String) dataFormat.toMessage(modelObjectsMap);
        msg.msgBody = result.getBytes();
        return msg;
    }
}
