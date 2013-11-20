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
 * 缴款单处理
 */
public class BillService {
    private static final Logger logger = LoggerFactory.getLogger(BillService.class);
    MybatisManager manager = new MybatisManager();

    // 按单号查询缴款单
    public HmfsJmBill qryBillByNo(String billNo) {
        SqlSession session = manager.getSessionFactory().openSession();
        HmfsJmBillMapper mapper = session.getMapper(HmfsJmBillMapper.class);
        HmfsJmBillExample example = new HmfsJmBillExample();
        example.createCriteria().andBillnoEqualTo(billNo);
        List<HmfsJmBill> bills = mapper.selectByExample(example);
        session.close();
        return (bills.size() > 0) ? bills.get(0) : null;
    }

    // 保存缴款单，已存在则更新
    public boolean saveDepositBill(HmfsJmBill bill) throws IllegalAccessException {
        if (BillQryStatus.VALAID.getCode().equals(bill.getBillStsCode())) {
            SqlSession session = manager.getSessionFactory().openSession();
            HmfsJmBillMapper mapper = session.getMapper(HmfsJmBillMapper.class);
            HmfsJmBillExample example = new HmfsJmBillExample();
            example.createCriteria().andBillnoEqualTo(bill.getBillno());
            List<HmfsJmBill> bills = mapper.selectByExample(example);
            int cnt = 0;
            if (bills.size() > 0) {
                // 已存在则更新
                HmfsJmBill origBill = bills.get(0);
                ObjectFieldsCopier.copyFields(bill, origBill);
                cnt = mapper.updateByPrimaryKey(origBill);
                session.close();
                return cnt == 1;
            } else {
                cnt = mapper.insert(bill);
                session.close();
                return cnt == 1;
            }
        } else {
            throw new RuntimeException("缴款单保存失败，状态：" + bill.getBillStsCode() + bill.getBillStsTitle());
        }
    }
}
