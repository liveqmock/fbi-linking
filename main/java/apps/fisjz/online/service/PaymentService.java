package apps.fisjz.online.service;

import apps.fisjz.repository.dao.FsJzfPaymentInfoMapper;
import apps.fisjz.repository.model.FsJzfPaymentInfo;
import apps.fisjz.repository.model.FsJzfPaymentInfoExample;
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
 * 缴款书缴款、手工缴款、退付等业务处理
 * User: zhanrui
 * Date: 13-9-23
 * Time: 下午1:46
 */
@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    FsJzfPaymentInfoMapper paymentInfoMapper;

    //普通缴款书缴款 (先检查是否有重复记录)
    public int processPaymentPay(String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
        FsJzfPaymentInfoExample example = new FsJzfPaymentInfoExample();
        example.createCriteria()
                .andNotescodeEqualTo(paymentInfo.getNotescode())
                .andBilltypeEqualTo(paymentInfo.getBilltype())
                .andArchiveFlagEqualTo("0");

        List<FsJzfPaymentInfo> recordList  = paymentInfoMapper.selectByExample(example);
        if (recordList.size() > 1) {
            logger.error("重复记录超过一条！" + paymentInfo.toString());
            throw new RuntimeException("重复记录超过一条!");
        }else if (recordList.size() == 1) {
            //更新重复记录为已归档记录
            FsJzfPaymentInfo record = recordList.get(0);
            record.setArchiveFlag("1");
            record.setArchiveOperBankid(branchId);
            record.setArchiveOperId(tellerId);
            record.setArchiveOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
            record.setArchiveOperTime(new SimpleDateFormat("HHmmss").format(new Date()));
            paymentInfoMapper.updateByPrimaryKey(record);
            //插入新纪录
            insertPaymentPay(branchId,tellerId,paymentInfo);
            return 1;
        }else{
            //插入新纪录
            insertPaymentPay(branchId,tellerId,paymentInfo);
        }
        return 0;
    }
    //普通缴款书缴款到账处理
    public void processPaymentPayAccount(String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
        String notesCode = paymentInfo.getNotescode();
        if (StringUtils.isEmpty(notesCode)) {
            throw new RuntimeException("票据编号不能为空!");
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
            throw new RuntimeException("此票据编号不能对应多条记录!");
        }

        FsJzfPaymentInfo record = recordList.get(0);
        record.setRecfeeflag("1");
        paymentInfoMapper.updateByPrimaryKey(record);
    }

    //退付缴款书确认
    public void processRefundPaymentPay(String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
        paymentInfo.setOperBankid(branchId);
        paymentInfo.setOperId(tellerId);
        paymentInfo.setOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        paymentInfo.setOperTime(new SimpleDateFormat("HHmmss").format(new Date()));

        //主机记账成功
        paymentInfo.setHostBookFlag("1");
        paymentInfo.setHostChkFlag("0");

        //财政记账成功
        paymentInfo.setFbBookFlag("1");
        paymentInfo.setFbChkFlag("0");
        paymentInfoMapper.insert(paymentInfo);
    }


    //=============
    private void insertPaymentPay(String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
        paymentInfo.setOperBankid(branchId);
        paymentInfo.setOperId(tellerId);
        paymentInfo.setOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        paymentInfo.setOperTime(new SimpleDateFormat("HHmmss").format(new Date()));

        //主机记账成功
        paymentInfo.setHostBookFlag("1");
        paymentInfo.setHostChkFlag("0");

        //财政记账成功
        paymentInfo.setFbBookFlag("1");
        paymentInfo.setFbChkFlag("0");

        //正常记录标志
        paymentInfo.setArchiveFlag("0");
        paymentInfoMapper.insert(paymentInfo);
    }
}
