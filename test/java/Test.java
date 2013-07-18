import apps.fis.enums.PendingVchFlag;
import common.utils.StringPad;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-6-19
 * Time: ионГ10:34
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) {

        String xmno = "0123456789";
        System.out.println(new StringBuffer(xmno).delete(8,10));
    }
}
