package apps.fiskfq.gateway.client;

import apps.fiskfq.PropertyManager;
import apps.fiskfq.gateway.domain.base.Tia;
import apps.fiskfq.gateway.domain.base.Toa;
import common.utils.StringPad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 黄岛非税
 */
public class CustomCodeHandler {

    private static Logger logger = LoggerFactory.getLogger(CustomCodeHandler.class);

    public static byte[] encode(Tia tia) {

        String msgdata = tia.toString();

        logger.info(msgdata);
        String txnDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String dataType = getSubstrBetweenStrs(msgdata, "<dataType>", "</dataType>");

        // 报文文档中的数据类型为交易码
        String msg = StringPad.leftPad4ChineseToByteLength(dataType, 6, " ")
                + StringPad.leftPad4ChineseToByteLength(PropertyManager.getProperty("src.code"), 15, " ")
                + StringPad.leftPad4ChineseToByteLength(PropertyManager.getProperty("des.code"), 15, " ")
                + txnDate + PropertyManager.getProperty("is.sign")
                // TODO 授权码长度 + 保留字段
                + "000" + "               "
//                + "0" + "DVJfrWuqXA5TFdzC8HWqYOYIkoGJs5g1iXROT7vSkb4=".length() + "               "
                + msgdata;
        int msglength = msg.getBytes().length + 8;
        String len = StringPad.leftPad4ChineseToByteLength("" + msglength, 8, "0");

        byte[] bytes = new byte[msglength];
        System.arraycopy(len.getBytes(), 0, bytes, 0, 8);
        System.arraycopy(msg.getBytes(), 0, bytes, 8, msglength - 8);
        logger.info("[本地客户端-黄岛非税]发送报文长度：" + msglength);
        logger.info("[本地客户端-黄岛非税]发送报文：" + msg);
        return bytes;
    }

    public static Toa decode(byte[] bytes) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        logger.info("[客户端接收-黄岛非税]" + new String(bytes));

        String txnCode = new String(bytes, 0, 6).trim();
        String msgdata = new String(bytes, 69, bytes.length - 69);

        Class clazz = Class.forName("apps.fiskfq.gateway.domain.txn.Toa" + txnCode);
        Toa tmptoa = (Toa) clazz.newInstance();
        Toa toa = tmptoa.toBean(msgdata);
        logger.info("Toa is ok");
        return toa;
    }

    static String getSubstrBetweenStrs(String fromStr, String startStr, String endStr) {
        int length = startStr.length();
        int start = fromStr.indexOf(startStr) + length;
        int end = fromStr.indexOf(endStr);
        return fromStr.substring(start, end);
    }
}
