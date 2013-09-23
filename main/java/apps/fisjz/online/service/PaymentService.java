package apps.fisjz.online.service;

import apps.fisjz.repository.dao.FsJzfPaymentInfoMapper;
import apps.fisjz.repository.model.FsJzfPaymentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    //普通缴款书缴款
    public void processPaymentPay(String branchId, String tellerId, FsJzfPaymentInfo paymentInfo){
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
}
