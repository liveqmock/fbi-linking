package apps.fisjz.online.service;

import apps.fisjz.domain.staring.T2010Response.TOA2010PaynotesInfo;
import apps.fisjz.domain.staring.T2010Response.TOA2010PaynotesItem;
import apps.fisjz.repository.dao.FsJzfPaymentInfoMapper;
import apps.fisjz.repository.dao.FsJzfPaymentItemMapper;
import apps.fisjz.repository.model.FsJzfPaymentInfo;
import apps.fisjz.repository.model.FsJzfPaymentInfoExample;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * �ɿ���ɿ�ֹ��ɿ�˸���ҵ����
 * User: zhanrui
 * Date: 13-9-23
 * Time: ����1:46
 */
@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    FsJzfPaymentInfoMapper paymentInfoMapper;
    @Autowired
    FsJzfPaymentItemMapper paymentItemMapper;

    public int processInitPaymentInfoAndPaymentItem(TOA2010PaynotesInfo paynotesInfo, List<TOA2010PaynotesItem> paynotesItems) throws InvocationTargetException, IllegalAccessException {
        FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();

        FsJzfPaymentInfoExample example = new FsJzfPaymentInfoExample();
        example.createCriteria()
                .andNotescodeEqualTo(paynotesInfo.getNotescode())
                .andBilltypeEqualTo(paynotesInfo.getBilltype())
                .andArchiveFlagEqualTo("0");

        List<FsJzfPaymentInfo> recordList  = paymentInfoMapper.selectByExample(example);
        if (recordList.size() == 0) {
            BeanUtils.copyProperties(paynotesInfo, fsJzfPaymentInfo);
            fsJzfPaymentInfo.setArchiveFlag("0");
            paymentInfoMapper.insert(fsJzfPaymentInfo);
            return 0;
        }else{
            return 1;
        }
    }

    //��ͨ�ɿ���ɿ� (�ȼ���Ƿ����ظ���¼)
    public int processPaymentPay(String areaCode, String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
        FsJzfPaymentInfoExample example = new FsJzfPaymentInfoExample();
        example.createCriteria()
                .andNotescodeEqualTo(paymentInfo.getNotescode())
                .andBilltypeEqualTo(paymentInfo.getBilltype())
                .andArchiveFlagEqualTo("0");

        List<FsJzfPaymentInfo> recordList  = paymentInfoMapper.selectByExample(example);
        if (recordList.size() > 1) {
            logger.error("�ظ���¼����һ����" + paymentInfo.toString());
            throw new RuntimeException("�ظ���¼����һ��!");
        }else if (recordList.size() == 1) {
            //�����ظ���¼Ϊ�ѹ鵵��¼
            FsJzfPaymentInfo record = recordList.get(0);
            record.setArchiveFlag("1");
            record.setArchiveOperBankid(branchId);
            record.setArchiveOperId(tellerId);
            record.setArchiveOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
            record.setArchiveOperTime(new SimpleDateFormat("HHmmss").format(new Date()));
            paymentInfoMapper.updateByPrimaryKey(record);
            //�����¼�¼
            insertPaymentPay(areaCode, branchId,tellerId,paymentInfo);
            return 1;
        }else{
            //�����¼�¼
            insertPaymentPay(areaCode, branchId,tellerId,paymentInfo);
        }
        return 0;
    }
    //��ͨ�ɿ���ɿ�˴���
    public void processPaymentPayAccount(String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
        String notesCode = paymentInfo.getNotescode();
        if (StringUtils.isEmpty(notesCode)) {
            throw new RuntimeException("Ʊ�ݱ�Ų���Ϊ��!");
        }

        FsJzfPaymentInfoExample example = new FsJzfPaymentInfoExample();
        List<String> billTypes = new ArrayList<String>();
        billTypes.add("0");
        billTypes.add("1");
        example.createCriteria()
                .andNotescodeEqualTo(notesCode)
                .andBilltypeIn(billTypes)
                .andArchiveFlagEqualTo("0");

        List<FsJzfPaymentInfo> recordList  = paymentInfoMapper.selectByExample(example);
        if (recordList.size() != 1) {
            throw new RuntimeException("��Ʊ�ݱ�Ų��ܶ�Ӧ������¼!");
        }

        FsJzfPaymentInfo record = recordList.get(0);
        record.setRecfeeflag("1");
        paymentInfoMapper.updateByPrimaryKey(record);
    }

    //�˸��ɿ���ȷ��
    public void processRefundPaymentPay(String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
        paymentInfo.setOperBankid(branchId);
        paymentInfo.setOperId(tellerId);
        paymentInfo.setOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        paymentInfo.setOperTime(new SimpleDateFormat("HHmmss").format(new Date()));

        //�������˳ɹ�
        paymentInfo.setHostBookFlag("1");
        paymentInfo.setHostChkFlag("0");

        //�������˳ɹ�
        paymentInfo.setFbBookFlag("1");
        paymentInfo.setFbChkFlag("0");
        paymentInfoMapper.insert(paymentInfo);
    }


    //=============
    private void insertPaymentPay(String areaCode,String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
        paymentInfo.setOperBankid(branchId);
        paymentInfo.setOperId(tellerId);
        paymentInfo.setOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        paymentInfo.setOperTime(new SimpleDateFormat("HHmmss").format(new Date()));

        //�������˳ɹ�
        paymentInfo.setHostBookFlag("1");
        paymentInfo.setHostChkFlag("0");

        //�������˳ɹ�
        paymentInfo.setFbBookFlag("1");
        paymentInfo.setFbChkFlag("0");

        //������¼��־
        paymentInfo.setArchiveFlag("0");

        paymentInfo.setAreaCode(areaCode);
        paymentInfoMapper.insert(paymentInfo);
    }
}
