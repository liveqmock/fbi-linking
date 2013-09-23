package apps.fisjz.gateway.financebureau;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 * Time: 下午1:24
 */
public interface NontaxBankService {
    //查询基础数据
    List queryAllElementCode(String applicationid, String elementcode, int year);

    //缴款书信息查询接口
    List queryNontaxPayment(String applicationid, String bank, String year, String finorg, String notescode, String checkcode, String billtype);

    //缴款书收款接口
    List updateNontaxPayment(String applicationid, String bank, String year, String finorg, List paramList);

    //缴款书到账接口
    List accountNontaxPayment(String applicationid, String bank, String year, String finorg, List paramList);

    //(手工）一般缴款书
    List insertNontaxPayment(String applicationid, String bank, String year, String finorg, List paramList);

    //退付缴款书信息查询
    List queryRefundNontaxPayment(String applicationid, String bank, String year, String finorg, String refundapplycode, String notescode);

    //退付缴款书确认
    List updateRefundNontaxPayment(String applicationid, String bank, String year, String finorg, List paramList);

    //缴款书冲销
    List cancelNontaxPayment(String applicationid, String bank, String year, String finorg, List paramList);

    //补缴款书信息查询
    List queryExtraNontaxPayment(String applicationid, String bank, String year, String finorg, String anumber);

    //缴款书明细信息上传(对账)
    List insertBankNontaxPayment(String applicationid, String bank, String year, String finorg, List paramList);

    //未明款项上传接口(对账)
    List insertBankUnknowData(String applicationid, String bank, String year, String finorg, List paramList);
}
