package apps.hmfsjm.online.service;

import apps.hmfsjm.repository.MybatisManager;
import apps.hmfsjm.enums.BillBookType;
import apps.hmfsjm.enums.BillStsFlag;
import apps.hmfsjm.gateway.client.SyncSocketClient;
import apps.hmfsjm.gateway.domain.base.Toa;
import apps.hmfsjm.gateway.domain.txn.Tia3002;
import apps.hmfsjm.gateway.domain.txn.Toa3002;
import apps.hmfsjm.repository.dao.HmfsJmActMapper;
import apps.hmfsjm.repository.dao.HmfsJmActTxnMapper;
import apps.hmfsjm.repository.dao.HmfsJmRefundMapper;
import apps.hmfsjm.repository.model.*;
import common.utils.ObjectFieldsCopier;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 1500621 退款确认 业务逻辑
 */
public class Txn1500621Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn1500621Service.class);
    private RefundService refundService = new RefundService();
    MybatisManager manager = new MybatisManager();

    public Toa process(String tellerID, String serialNo, String refundNo) {

        Tia3002 tia = new Tia3002();
        tia.BODY.REFUND_BILLNO = refundNo;
        tia.INFO.REQ_SN = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        HmfsJmRefund refund = refundService.qryRefundByNo(refundNo);
        if (refund == null) {
            throw new RuntimeException("退款单号不存在:" + refundNo);
        }
        refund.setCfmTxnCode("3002");
        refund.setActSerialNo(serialNo);
        refund.setOperId(tellerID);
        refund.setStsFlag(BillStsFlag.BOOKED.getCode());
        SqlSession session = manager.getSessionFactory().openSession();
        try {
            HmfsJmRefundMapper refundMapper = session.getMapper(HmfsJmRefundMapper.class);
            refundMapper.updateByPrimaryKey(refund);
            HmfsJmActMapper actMapper = session.getMapper(HmfsJmActMapper.class);
            HmfsJmActExample actExample = new HmfsJmActExample();
            actExample.createCriteria().andHouseAccountEqualTo(refund.getHouseAccount());
            List<HmfsJmAct> actList = actMapper.selectByExample(actExample);
            if (actList.size() == 0) {
                logger.error("退款分户账号不存在，分户号:" + refund.getHouseAccount() + " 退款单号：" + refund.getBillno());
                throw new RuntimeException("退款分户账号不存在，分户号:" + refund.getHouseAccount() + " 退款单号：" + refund.getBillno());
            } else {   // 退款
                HmfsJmAct act = actList.get(0);
                act.setBalAmt(act.getBalAmt().subtract(refund.getTxnAmt()));
                actMapper.updateByPrimaryKey(act);
                logger.info("余额更新成功，分户号:" + refund.getHouseAccount() + " 单号：" + refund.getBillno());
            }
            HmfsJmActTxn txn = new HmfsJmActTxn();
            ObjectFieldsCopier.copyFields(refund, txn);
            txn.setActSerialNo(serialNo);
            txn.setTxnCode("3002");
            txn.setBookType(BillBookType.REFUND.getCode());
            HmfsJmActTxnMapper acttxnMapper = session.getMapper(HmfsJmActTxnMapper.class);
            acttxnMapper.insert(txn);
            logger.info("[3002-退款确认-请求] 流水号：" + tia.INFO.REQ_SN + " 单号：" + tia.BODY.REFUND_BILLNO);
            // 交易发起
            Toa3002 toa = (Toa3002) new SyncSocketClient().onRequest(tia);
            if (toa == null) throw new RuntimeException("网络异常。");

            logger.info("[3002-退款确认-响应] 流水号：" + toa.INFO.REQ_SN +
                    " 单号：" + toa.BODY.REFUND_BILLNO +
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