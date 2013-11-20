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
 * 退款单处理
 */
public class RefundService {
    private static final Logger logger = LoggerFactory.getLogger(RefundService.class);
    MybatisManager manager = new MybatisManager();

    // 按单号查询退款单
    public HmfsJmRefund qryRefundByNo(String refundNo) {
        SqlSession session = manager.getSessionFactory().openSession();
        HmfsJmRefundMapper mapper = session.getMapper(HmfsJmRefundMapper.class);
        HmfsJmRefundExample example = new HmfsJmRefundExample();
        example.createCriteria().andBillnoEqualTo(refundNo);
        List<HmfsJmRefund> refunds = mapper.selectByExample(example);
        session.close();
        return (refunds.size() > 0) ? refunds.get(0) : null;
    }

    // 保存缴款单，已存在则更新
    public boolean saveRefundBill(HmfsJmRefund refund) throws IllegalAccessException {
        if (RefundQryStatus.VALAID.getCode().equals(refund.getBillStsCode())) {
            SqlSession session = manager.getSessionFactory().openSession();
            HmfsJmRefundMapper mapper = session.getMapper(HmfsJmRefundMapper.class);
            HmfsJmRefundExample example = new HmfsJmRefundExample();
            example.createCriteria().andBillnoEqualTo(refund.getBillno());
            List<HmfsJmRefund> refunds = mapper.selectByExample(example);
            int cnt = 0;
            if (refunds.size() > 0) {
                // 已存在则更新
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
            throw new RuntimeException("缴款单保存失败，状态：" + refund.getBillStsCode() + refund.getBillStsTitle());
        }
    }
}
