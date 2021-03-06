package apps.hmfsjm.online.service;

import apps.hmfsjm.repository.MybatisManager;
import apps.hmfsjm.enums.BillBookType;
import apps.hmfsjm.enums.BillStsFlag;
import apps.hmfsjm.gateway.client.SyncSocketClient;
import apps.hmfsjm.gateway.domain.base.Toa;
import apps.hmfsjm.gateway.domain.txn.Tia2001;
import apps.hmfsjm.gateway.domain.txn.Toa2001;
import apps.hmfsjm.repository.dao.HmfsJmActMapper;
import apps.hmfsjm.repository.dao.HmfsJmActTxnMapper;
import apps.hmfsjm.repository.dao.HmfsJmBillMapper;
import apps.hmfsjm.repository.model.HmfsJmAct;
import apps.hmfsjm.repository.model.HmfsJmActExample;
import apps.hmfsjm.repository.model.HmfsJmActTxn;
import apps.hmfsjm.repository.model.HmfsJmBill;
import common.utils.ObjectFieldsCopier;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 1500611 缴款单缴款确认 业务逻辑 每个缴款单号对应的分户有且只有一个，销户时需将原分户删除，重新开户
 */
public class Txn1500611Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn1500611Service.class);
    private BillService billService = new BillService();
    MybatisManager manager = new MybatisManager();

    public Toa process(String tellerID, String branchID, String serialNo, String billNo) {

        Tia2001 tia = new Tia2001();
        tia.BODY.PAY_BILLNO = billNo;
        tia.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        HmfsJmBill bill = billService.qryBillByNo(billNo);
        if (bill == null) {
            throw new RuntimeException("缴款单号不存在:" + billNo);
        }
        bill.setCfmTxnCode("2001");
        bill.setActSerialNo(serialNo);
        bill.setOperId(tellerID);
        bill.setDeptId(branchID);
        bill.setStsFlag(BillStsFlag.BOOKED.getCode());
        SqlSession session = null;
        try {
            session = manager.getSessionFactory().openSession();
            HmfsJmBillMapper mapper = session.getMapper(HmfsJmBillMapper.class);
            mapper.updateByPrimaryKey(bill);
            HmfsJmActMapper actMapper = session.getMapper(HmfsJmActMapper.class);
            HmfsJmActExample actExample = new HmfsJmActExample();
            actExample.createCriteria().andHouseAccountEqualTo(bill.getHouseAccount());
            List<HmfsJmAct> actList = actMapper.selectByExample(actExample);
            if (actList.size() == 0) {   // 开户
                HmfsJmAct act = new HmfsJmAct();
                ObjectFieldsCopier.copyFields(bill, act);
                act.setBalAmt(bill.getTxnAmt());
                act.setIntAmt(new BigDecimal("0.00"));
                act.setMngAmt(new BigDecimal("0.00"));
                actMapper.insert(act);
                logger.info("分户开户成功，分户号:" + bill.getHouseAccount() + " 缴款单号：" + bill.getBillno());
                // 记账明细
            } else {   // 已开户，视为续缴.
                HmfsJmAct act = actList.get(0);
                act.setBalAmt(act.getBalAmt().add(bill.getTxnAmt()));
                actMapper.updateByPrimaryKey(act);
                logger.info("余额更新成功，分户号:" + bill.getHouseAccount() + " 单号：" + bill.getBillno());
            }
            HmfsJmActTxn txn = new HmfsJmActTxn();
            ObjectFieldsCopier.copyFields(bill, txn);
            txn.setActSerialNo(serialNo);
            txn.setTxnCode("2001");
            txn.setBookType(BillBookType.DEPOSIT.getCode());
            HmfsJmActTxnMapper acttxnMapper = session.getMapper(HmfsJmActTxnMapper.class);
            acttxnMapper.insert(txn);
            logger.info("[2001-缴款确认-请求] 流水号：" + tia.INFO.REQ_SN + " 单号：" + tia.BODY.PAY_BILLNO);
            // 交易发起
            Toa2001 toa = (Toa2001) new SyncSocketClient().onRequest(tia);
            if (toa == null) throw new RuntimeException("网络异常。");

            logger.info("[2001-缴款确认-响应] 流水号：" + toa.INFO.REQ_SN +
                    " 单号：" + toa.BODY.PAY_BILLNO +
                    " 状态码：" + toa.BODY.BILL_STS_CODE +
                    " 状态说明：" + toa.BODY.BILL_STS_TITLE);
            session.commit();
            return toa;
        } catch (Exception e) {
            session.rollback();
            return null;
        } finally {
            if (session != null) session.close();
        }
    }
}