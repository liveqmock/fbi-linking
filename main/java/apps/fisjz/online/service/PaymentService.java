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
            throw new RuntimeException("重复记录！");
        }
    }

    //初始化缴款书主信息和子项目信息
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
            //初始化主表
            FsJzfPaymentInfo fsJzfPaymentInfo = new FsJzfPaymentInfo();
            BeanUtils.copyProperties(fsJzfPaymentInfo, paynotesInfo);
            insertPaymentInfo_init(areaCode, branchId, tellerId, fsJzfPaymentInfo);

            //初始化子项目明细表
            FsJzfPaymentItemExample itemExample = new FsJzfPaymentItemExample();
            itemExample.createCriteria().andMainidEqualTo(fsJzfPaymentInfo.getBillid());
            List<FsJzfPaymentItem> itemList = paymentItemMapper.selectByExample(itemExample);
            if (itemList.size() > 0) {
                throw new RuntimeException("此BillId下的子项目信息已存在.");
            }
            for (TOA2010PaynotesItem paynotesItem : paynotesItems) {
                FsJzfPaymentItem fsJzfPaymentItem = new FsJzfPaymentItem();
                BeanUtils.copyProperties(fsJzfPaymentItem, paynotesItem);
                paymentItemMapper.insert(fsJzfPaymentItem);
            }
            return 0; //初始化
        }else{
            return 1;  //已存在
        }
    }

    //普通缴款书缴款 (先检查是否有重复记录)
    public int processPaymentPay(String areaCode, String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
        FsJzfPaymentInfoExample example = new FsJzfPaymentInfoExample();
        example.createCriteria()
                .andNotescodeEqualTo(paymentInfo.getNotescode())
                .andBilltypeEqualTo(paymentInfo.getBilltype())
                .andAreaCodeEqualTo(areaCode)
                .andArchiveFlagEqualTo("0");

        List<FsJzfPaymentInfo> recordList  = paymentInfoMapper.selectByExample(example);
        if (recordList.size() > 1) {
            logger.error("重复记录超过一条！" + paymentInfo.toString());
            throw new RuntimeException("重复记录超过一条!");
        }else if (recordList.size() == 1) {
            FsJzfPaymentInfo record = recordList.get(0);
            if ("0".equals(record.getFbBookFlag())) { //初始记录
                //更新
                processPaymentInfo_Pay(areaCode, branchId, tellerId, record);
                paymentInfoMapper.updateByPrimaryKey(record);
            } else { //属于重复缴款，则进行归档处理，用于自动冲正
                //更新重复记录为已归档记录
                record.setArchiveFlag("1");
                record.setArchiveOperBankid(branchId);
                record.setArchiveOperId(tellerId);
                record.setArchiveOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
                record.setArchiveOperTime(new SimpleDateFormat("HHmmss").format(new Date()));
                paymentInfoMapper.updateByPrimaryKey(record);
                //插入新纪录
                processPaymentInfo_Pay(areaCode, branchId, tellerId, paymentInfo);
                paymentInfoMapper.insert(paymentInfo);
            }
        }else{
            return -1;
        }
        return 0;
    }
    //普通缴款书缴款到账处理
    public void processPaymentPayAccount(String areaCode, String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
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
                .andAreaCodeEqualTo(areaCode)
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

    //查询时初始化记录
    private void insertPaymentInfo_init(String areaCode, String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
        paymentInfo.setOperBankid(branchId);
        paymentInfo.setOperId(tellerId);
        paymentInfo.setOperDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        paymentInfo.setOperTime(new SimpleDateFormat("HHmmss").format(new Date()));

        paymentInfo.setHostBookFlag("0");
        paymentInfo.setHostChkFlag("0");

        paymentInfo.setFbBookFlag("0");
        paymentInfo.setFbChkFlag("0");

        //正常记录标志
        paymentInfo.setArchiveFlag("0");
        paymentInfo.setAreaCode(areaCode);

        paymentInfoMapper.insert(paymentInfo);
    }

    //缴款
    private void processPaymentInfo_Pay(String areaCode, String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
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
        paymentInfo.setAreaCode(areaCode);
    }
}
