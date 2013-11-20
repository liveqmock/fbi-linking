package apps.hmfsjm.online.service;

import apps.hmfsjm.repository.MybatisManager;
import apps.hmfsjm.enums.RefundQryStatus;
import apps.hmfsjm.repository.dao.HmfsJmRefundMapper;
import apps.hmfsjm.repository.model.HmfsJmRefund;
import apps.hmfsjm.repository.model.HmfsJmRefundExample;
import common.utils.ObjectFieldsCopier;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * �˿����
 */
public class RefundService {
    private static final Logger logger = LoggerFactory.getLogger(RefundService.class);
    MybatisManager manager = new MybatisManager();

    // �����Ų�ѯ�˿
    public HmfsJmRefund qryRefundByNo(String refundNo) {
        SqlSession session = manager.getSessionFactory().openSession();
        HmfsJmRefundMapper mapper = session.getMapper(HmfsJmRefundMapper.class);
        HmfsJmRefundExample example = new HmfsJmRefundExample();
        example.createCriteria().andBillnoEqualTo(refundNo);
        List<HmfsJmRefund> refunds = mapper.selectByExample(example);
        session.close();
        return (refunds.size() > 0) ? refunds.get(0) : null;
    }

    // ����ɿ���Ѵ��������
    public boolean saveRefundBill(HmfsJmRefund refund) throws IllegalAccessException {
        if (RefundQryStatus.VALAID.getCode().equals(refund.getBillStsCode())) {
            SqlSession session = manager.getSessionFactory().openSession();
            HmfsJmRefundMapper mapper = session.getMapper(HmfsJmRefundMapper.class);
            HmfsJmRefundExample example = new HmfsJmRefundExample();
            example.createCriteria().andBillnoEqualTo(refund.getBillno());
            List<HmfsJmRefund> refunds = mapper.selectByExample(example);
            int cnt = 0;
            if (refunds.size() > 0) {
                // �Ѵ��������
                HmfsJmRefund origRefund = refunds.get(0);
                ObjectFieldsCopier.copyFields(refund, origRefund);
                cnt = mapper.updateByPrimaryKey(origRefund);
                session.close();
                return cnt == 1;
            } else {
                cnt = mapper.insert(refund);
                session.close();
                return cnt == 1;
            }
        } else {
            throw new RuntimeException("�ɿ����ʧ�ܣ�״̬��" + refund.getBillStsCode() + refund.getBillStsTitle());
        }
    }
}
