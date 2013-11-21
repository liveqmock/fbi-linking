package apps.hmfsjm.online.service;

import apps.hmfsjm.enums.*;
import apps.hmfsjm.gateway.client.SyncSocketClient;
import apps.hmfsjm.gateway.domain.base.Toa;
import apps.hmfsjm.gateway.domain.txn.Tia1001;
import apps.hmfsjm.gateway.domain.txn.Toa1001;
import apps.hmfsjm.repository.MybatisManager;
import apps.hmfsjm.repository.dao.HmfsJmRefundMapper;
import apps.hmfsjm.repository.dao.HmfsJmVoucherMapper;
import apps.hmfsjm.repository.model.HmfsJmBill;
import apps.hmfsjm.repository.model.HmfsJmVoucher;
import apps.hmfsjm.repository.model.HmfsJmVoucherExample;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 1500630 票据领用
 */
public class Txn1500630Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn1500630Service.class);
    MybatisManager manager = new MybatisManager();

    public boolean process(String branchID, String tellerID, long startNo, long endNo) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

        SqlSession session = manager.getSessionFactory().openSession();
        try {
            HmfsJmVoucherMapper vchMapper = session.getMapper(HmfsJmVoucherMapper.class);
            HmfsJmVoucherExample example = new HmfsJmVoucherExample();
            long cnt = 0;
            for (long i = startNo; i <= endNo; i++) {
                example.clear();
                example.createCriteria().andVchNumEqualTo(String.valueOf(i));
                if (vchMapper.countByExample(example) > 0) {
                    throw new RuntimeException("存在重复已领用票据号" + i);
                }
                HmfsJmVoucher record = new HmfsJmVoucher();
                record.setVchSts(VoucherStatus.CHECK.getCode());
                record.setVchNum(String.valueOf(i));
                record.setTxnDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
                record.setTxnTime(new SimpleDateFormat("HHmmss").format(new Date()));
                record.setOperId(tellerID);
                record.setDeptId(branchID);
                record.setSendFlag(SendFlag.UNSEND.getCode());
                cnt += vchMapper.insert(record);
            }
            if (cnt == (endNo - startNo + 1)) {
                session.commit();
                return true;
            } else {
                throw new RuntimeException("票据未完全领用");
            }
        } catch (Exception e) {
            session.rollback();
            logger.error("领用票据保存失败", e);
            String errmsg = e.getMessage();
            if (StringUtils.isEmpty(errmsg)) {
                throw new RuntimeException("领用票据保存失败");
            } else
                throw new RuntimeException(errmsg);
        } finally {
            if (session != null) session.close();
        }
    }

}
