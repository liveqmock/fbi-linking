package apps.fisjz.online.service;

import apps.fisjz.domain.staring.T2060Request.TIA2060;
import apps.fisjz.repository.dao.FsJzfPaymentInfoMapper;
import apps.fisjz.repository.dao.FsJzfPaymentItemMapper;
import apps.fisjz.repository.model.FsJzfPaymentInfo;
import apps.fisjz.repository.model.FsJzfPaymentInfoExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 对账处理
 * User: zhanrui
 * Date: 13-9-23
 * Time: 下午1:46
 */
@Service
public class ActChkService {
    private static final Logger logger = LoggerFactory.getLogger(ActChkService.class);
    @Autowired
    FsJzfPaymentInfoMapper paymentInfoMapper;
    @Autowired
    FsJzfPaymentItemMapper paymentItemMapper;

    //与主机对账
    public void processHostCheck(TIA2060 tia) {
        String areaCode = tia.getAreacode();
        String startDate = tia.getStartdate();
        String endDate = tia.getEnddate();
        int totalNum = 0;
        BigDecimal totalAmt;
        try {
            Date date = new SimpleDateFormat("yyyyMMdd").parse(startDate);
            date = new SimpleDateFormat("yyyyMMdd").parse(endDate);
            totalNum = Integer.parseInt(tia.getItemNum());
            totalAmt = new BigDecimal(tia.getTotalamt());
        } catch (Exception e) {
            throw new RuntimeException("日期转换错误.");
        }


    }

    private List<FsJzfPaymentInfo> getNormalPaymentInfo(String areaCode, String startDate, String endDate) {
        //正常缴款类型
        List<String> billTypes = new ArrayList<String>();
        billTypes.add("0");
        billTypes.add("1");   //手工票

        FsJzfPaymentInfoExample example = new FsJzfPaymentInfoExample();
        example.createCriteria()
                .andBankacctdateBetween(startDate, endDate)
                .andBankacctdateIn(billTypes)
                .andAreaCodeEqualTo(areaCode)
                .andArchiveFlagEqualTo("0");
        return paymentInfoMapper.selectByExample(example);
    }
}
