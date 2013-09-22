package apps.fisjz.gateway.financebureau;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 * Time: обнГ1:24
 * To change this template use File | Settings | File Templates.
 */
public interface NontaxBankService {
    List queryAllElementCode(String applicationid, String elementCode, int year);
}
