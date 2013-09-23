package apps.fisjz.thirdparty;

import apps.fisjz.gateway.financebureau.NontaxBankService;
import apps.fisjz.gateway.financebureau.NontaxServiceFactory;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 * Time: ÏÂÎç1:06
 * To change this template use File | Settings | File Templates.
 */
public class BurlapTestClient {
    public static void main(String argv[]) {
        //queryAllElementCode();
        queryNontaxPayment();
    }

    public static void queryAllElementCode(){
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List rtnlist = service.queryAllElementCode("TJHQ.INDEX", "BANK", 2013);
        System.out.println(rtnlist.toString());
    }
    public static void queryNontaxPayment(){
        NontaxBankService service = NontaxServiceFactory.getInstance().getNontaxBankService();
        List rtnlist = service.queryNontaxPayment("", "", "", "", "120000010101", "MNX1E6", "0");
        System.out.println(rtnlist.toString());
    }
}
