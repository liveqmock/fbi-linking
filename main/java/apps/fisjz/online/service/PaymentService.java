package apps.fisjz.online.service;

import apps.fisjz.domain.staring.T2010Response.TOA2010PaynotesInfo;
import apps.fisjz.domain.staring.T2010Response.TOA2010PaynotesItem;
import apps.fisjz.repository.dao.FsJzfPaymentInfoMapper;
import apps.fisjz.repository.dao.FsJzfPaymentItemMapper;
import apps.fisjz.repository.model.FsJzfPaymentInfo;
import apps.fisjz.repository.model.FsJzfPaymentInfoExample;
import apps.fisjz.repository.model.FsJzfPaymentItem;
import apps.fisjz.repository.model.FsJzfPaymentItemExample;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public FsJzfPaymentInfo findLocalDbPaymentInfo(String areaCode, String notesCode, String checkCode,String billType){
        FsJzfPaymentInfoExample example = new FsJzfPaymentInfoExample();
        example.createCriteria()
                .andNotescodeEqualTo(notesCode)
                .andCheckcodeEqualTo(checkCode)
                .andBilltypeEqualTo(billType)
                .andAreaCodeEqualTo(areaCode)
                .andArchiveFlagEqualTo("0");
        List<FsJzfPaymentInfo> recordList  = paymentInfoMapper.selectByExample(example);
        if (recordList.size() == 1) {
              return recordList.get(0);
        }else if (recordList.size() == 0) {
            return null;
        } else {
            throw new RuntimeException("�ظ���¼��");
        }
    }

    //��ʼ���ɿ�������Ϣ������Ŀ��Ϣ
    public int initPaymentInfoAndPaymentItem(String areaCode, String branchId, String tellerId,
                                             TOA2010PaynotesInfo paynotesInfo,
                                             List<TOA2010PaynotesItem> paynotesItems) throws Exception {

        FsJzfPaymentInfoExample example = new FsJzfPaymentInfoExample();
        example.createCriteria()
                .andNotescodeEqualTo(paynotesInfo.getNotescode())
                .andBilltypeEqualTo(paynotesInfo.getBilltype())
                .andAreaCodeEqualTo(areaCode)
                .andArchiveFlagEqualTo("0");

        List<FsJzfPaymentInfo> recordList  = paymentInfoMapper.selectByExample(example);
        if (recordList.size() == 0) {
            //��ʼ������
            FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
            BeanUtils.copyProperties(fsJzfPaymentInfo, paynotesInfo);
            insertPaymentInfo_init(areaCode, branchId, tellerId, fsJzfPaymentInfo);

            //��ʼ������Ŀ��ϸ��
            FsJzfPaymentItemExample itemExample = new FsJzfPaymentItemExample();
            itemExample.createCriteria().andMainidEqualTo(fsJzfPaymentInfo.getBillid());
            List<FsJzfPaymentItem> itemList = paymentItemMapper.selectByExample(itemExample);
            if (itemList.size() > 0) {
                throw new RuntimeException("��BillId�µ�����Ŀ��Ϣ�Ѵ���.");
            }
            for (TOA2010PaynotesItem paynotesItem : paynotesItems) {
                FsJzfPaymentItem fsJzfPaymentItem = new FsJzfPaymentItem();
                BeanUtils.copyProperties(fsJzfPaymentItem, paynotesItem);
                paymentItemMapper.insert(fsJzfPaymentItem);
            }
            return 0; //��ʼ��
        }else{
            return 1;  //�Ѵ���
        }
    }

    //��ͨ�ɿ���ɿ� (�ȼ���Ƿ����ظ���¼)
    public int processPaymentPay(String areaCode, String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
        FsJzfPaymentInfoExample example = new FsJzfPaymentInfoExample();
        example.createCriteria()
                .andNotescodeEqualTo(paymentInfo.getNotescode())
                .andBilltypeEqualTo(paymentInfo.getBilltype())
                .andAreaCodeEqualTo(areaCode)
                .andArchiveFlagEqualTo("0");

        List<FsJzfPaymentInfo> recordList  = paymentInfoMapper.selectByExample(example);
        if (recordList.size() > 1) {
            logger.error("�ظ���¼����һ����" + paymentInfo.toString());
            throw new RuntimeException("�ظ���¼����һ��!");
        }else if (recordList.size() == 1) {
            FsJzfPaymentInfo record = recordList.get(0);
            if ("0".equals(record.getFbBookFlag())) { //��ʼ��¼
                //����
                processPaymentInfo_Pay(areaCode, branchId, tellerId, record);
                paymentInfoMapper.updateByPrimaryKey(record);
            } else { //�����ظ��ɿ����й鵵���������Զ�����
                //�����ظ���¼Ϊ�ѹ鵵��¼
                record.setArchiveFlag("1");
                record.setArchiveOperBankid(branchId);
                record.setArchiveOperId(tellerId);
                record.setArchiveOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
                record.setArchiveOperTime(new SimpleDateFormat("HHmmss").format(new Date()));
                paymentInfoMapper.updateByPrimaryKey(record);
                //�����¼�¼
                processPaymentInfo_Pay(areaCode, branchId, tellerId, paymentInfo);
                paymentInfoMapper.insert(paymentInfo);
            }
        }else{
            return -1;
        }
        return 0;
    }
    //��ͨ�ɿ���ɿ�˴���
    public void processPaymentPayAccount(String areaCode, String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
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
                .andAreaCodeEqualTo(areaCode)
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

    //��ѯʱ��ʼ����¼
    private void insertPaymentInfo_init(String areaCode, String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
        paymentInfo.setOperBankid(branchId);
        paymentInfo.setOperId(tellerId);
        paymentInfo.setOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        paymentInfo.setOperTime(new SimpleDateFormat("HHmmss").format(new Date()));

        paymentInfo.setHostBookFlag("0");
        paymentInfo.setHostChkFlag("0");

        paymentInfo.setFbBookFlag("0");
        paymentInfo.setFbChkFlag("0");

        //������¼��־
        paymentInfo.setArchiveFlag("0");
        paymentInfo.setAreaCode(areaCode);

        paymentInfoMapper.insert(paymentInfo);
    }

    //�ɿ�
    private void processPaymentInfo_Pay(String areaCode, String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
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
    }
}
