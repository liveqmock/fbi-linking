package apps.hmfsjm.online.service;

import apps.hmfsjm.enums.VoucherStatus;
import apps.hmfsjm.repository.MybatisManager;
import apps.hmfsjm.repository.dao.common.CommonMapper;
import apps.hmfsjm.repository.model.VoucherBill;
import common.utils.StringPad;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 1500632 Ʊ��ʹ�������ѯ
 */
public class Txn1500632Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn1500632Service.class);
    MybatisManager manager = new MybatisManager();

    public String process(String date8) {

        SqlSession session = manager.getSessionFactory().openSession();
        CommonMapper mapper = session.getMapper(CommonMapper.class);
        List<VoucherBill> vchList = mapper.qryVoucherBills(date8);
        String vchs = transVchsToStr(vchList);
        return vchs;
    }

    private String transVchsToStr(List<VoucherBill> vchList) {
        if (vchList == null || vchList.isEmpty()) {
            return null;
        } else {
            StringBuilder vchBuilder = new StringBuilder();
            vchBuilder.append(StringPad.rightPad4ChineseToByteLength("Ʊ�ݺ�", 22, " "));
            vchBuilder.append("  ״̬  ");
            vchBuilder.append(StringPad.rightPad4ChineseToByteLength("�ɿ�� ", 22, " "));
            vchBuilder.append("  ���|");
            for (VoucherBill vb : vchList) {
                vchBuilder.append(StringPad.rightPad4ChineseToByteLength(vb.getVchnum(), 24, " "));
                vchBuilder.append(VoucherStatus.valueOfAlias(vb.getVchsts()).getTitle());
                vchBuilder.append("  ");
                vchBuilder.append(StringPad.rightPad4ChineseToByteLength(vb.getBillno(), 24, " "));
                vchBuilder.append(vb.getTxnamt());
                vchBuilder.append("|");
            }
            return vchBuilder.toString();
        }
    }
}
