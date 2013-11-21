package apps.hmfsjm.online.service;

import apps.hmfsjm.repository.MybatisManager;
import apps.hmfsjm.enums.BillQryStatus;
import apps.hmfsjm.repository.dao.HmfsJmBillMapper;
import apps.hmfsjm.repository.model.HmfsJmBill;
import apps.hmfsjm.repository.model.HmfsJmBillExample;
import common.utils.ObjectFieldsCopier;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * �ɿ����
 */
public class BillService {
    private static final Logger logger = LoggerFactory.getLogger(BillService.class);
    MybatisManager manager = new MybatisManager();

    // �����Ų�ѯ�ɿ
    public HmfsJmBill qryBillByNo(String billNo) {
        SqlSession session = manager.getSessionFactory().openSession();
        HmfsJmBillMapper mapper = session.getMapper(HmfsJmBillMapper.class);
        HmfsJmBillExample example = new HmfsJmBillExample();
        example.createCriteria().andBillnoEqualTo(billNo);
        List<HmfsJmBill> bills = mapper.selectByExample(example);
        session.close();
        return (bills.size() > 0) ? bills.get(0) : null;
    }

    // ����ɿ���Ѵ��������
    public boolean saveDepositBill(HmfsJmBill bill) throws IllegalAccessException {
        if (BillQryStatus.VALAID.getCode().equals(bill.getBillStsCode())) {
            SqlSession session = manager.getSessionFactory().openSession();
            HmfsJmBillMapper mapper = session.getMapper(HmfsJmBillMapper.class);
            HmfsJmBillExample example = new HmfsJmBillExample();
            example.createCriteria().andBillnoEqualTo(bill.getBillno());
            List<HmfsJmBill> bills = mapper.selectByExample(example);
            int cnt = 0;
            if (bills.size() > 0) {
                // �Ѵ��������
                HmfsJmBill origBill = bills.get(0);
                String pkid = origBill.getPkid();
                ObjectFieldsCopier.copyFields(bill, origBill);
                origBill.setPkid(pkid);
                cnt = mapper.updateByPrimaryKey(origBill);
                session.commit();
                session.close();
                return cnt == 1;
            } else {
                cnt = mapper.insert(bill);
                session.commit();
                session.close();
                return cnt == 1;
            }
        } else {
            throw new RuntimeException("״̬��" + bill.getBillStsCode() + bill.getBillStsTitle());
        }
    }
}
