package apps.hmfsjm.online.service;

import apps.hmfsjm.enums.BillBookType;
import apps.hmfsjm.enums.BillStsFlag;
import apps.hmfsjm.enums.SendFlag;
import apps.hmfsjm.enums.VoucherStatus;
import apps.hmfsjm.gateway.client.SyncSocketClient;
import apps.hmfsjm.gateway.domain.base.Toa;
import apps.hmfsjm.gateway.domain.txn.Tia1001;
import apps.hmfsjm.gateway.domain.txn.Toa1001;
import apps.hmfsjm.repository.MybatisManager;
import apps.hmfsjm.repository.dao.HmfsJmRefundMapper;
import apps.hmfsjm.repository.dao.HmfsJmVoucherMapper;
import apps.hmfsjm.repository.model.HmfsJmBill;
import apps.hmfsjm.repository.model.HmfsJmVoucher;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 1500630 Ʊ������
 */
public class Txn1500630Service {

    private static final Logger logger = LoggerFactory.getLogger(Txn1500630Service.class);
    MybatisManager manager = new MybatisManager();

    public boolean process(String branchID, String tellerID, long startNo, long endNo) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {

        SqlSession session = manager.getSessionFactory().openSession();
        try {
            HmfsJmVoucherMapper vchMapper = session.getMapper(HmfsJmVoucherMapper.class);
            long cnt = 0;
            for (long i = startNo; i <= endNo; i++) {
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
            if (cnt == (endNo - startNo)) {
                return true;
            } else {
                throw new RuntimeException("Ʊ��δ��ȫ����");
            }
        } catch (Exception e) {
            session.rollback();
            logger.error("����Ʊ�ݱ���ʧ��", e);
            throw new RuntimeException("����Ʊ�ݱ���ʧ��");
        } finally {
            if (session != null) session.close();
        }
    }

}
