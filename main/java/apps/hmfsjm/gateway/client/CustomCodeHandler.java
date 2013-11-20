package apps.hmfsjm.gateway.client;

import apps.hmfsjm.PropertyManager;
import apps.hmfsjm.gateway.domain.base.Tia;
import apps.hmfsjm.gateway.domain.base.Toa;
import common.utils.MD5Helper;
import common.utils.StringPad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-11-14
 * Time: 上午10:14
 * To change this template use File | Settings | File Templates.
 */
public class CustomCodeHandler {

    private static Logger logger = LoggerFactory.getLogger(CustomCodeHandler.class);

    public static byte[] encode(Tia tia) {
        String msgdata = tia.toString();
        String wsysid = PropertyManager.getProperty("socket.wsysid");
        String txnDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String txnCode = getSubstrBetweenStrs(msgdata, "<TXN_CODE>", "</TXN_CODE>");
        String mac = MD5Helper.getMD5String(msgdata + txnDate + wsysid);

        String msg = "1.00" + StringPad.rightPad4ChineseToByteLength(wsysid, 10, " ")
                + StringPad.rightPad4ChineseToByteLength(txnCode, 10, " ")
                + txnDate + mac + msgdata;
        int msglength = msg.getBytes().length + 8;
        String len = StringPad.rightPad4ChineseToByteLength("" + msglength, 8, " ");

        byte[] bytes = new byte[msglength];
        System.arraycopy(len.getBytes(), 0, bytes, 0, 8);
        System.arraycopy(msg.getBytes(), 0, bytes, 8, msglength - 8);
        logger.info("[本地客户端]已发送报文长度：" + msglength);
        logger.info("[本地客户端]发送报文：" + msg);
        return bytes;
    }

    public static Toa decode(byte[] bytes) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        logger.info("[客户端接收]" + new String(bytes));

        String wsysid = new String(bytes, 4, 10).trim();
        String txnCode = new String(bytes, 14, 10).trim();
        String txnDate = new String(bytes, 24, 8).trim();
        String mac = new String(bytes, 32, 32);
        String msgdata = new String(bytes, 64, bytes.length - 64);
        String md5 = MD5Helper.getMD5String(msgdata + txnDate + wsysid);
        logger.info("交易日期：" + txnDate + " 系统ID:" + wsysid);
        if (!md5.equalsIgnoreCase(mac)) {
            logger.error("校验失败,MAC不一致.本地生成md5:" + md5 + " 服务端mac:" + mac);
            throw new RuntimeException("报文校验失败,MAC不一致");
        }
        Class clazz = Class.forName("apps.hmfsjm.gateway.domain.txn.Toa" + txnCode);
        Toa tmptoa = (Toa) clazz.newInstance();
        Toa toa = tmptoa.toBean(msgdata);
        return toa;
    }

    static String getSubstrBetweenStrs(String fromStr, String startStr, String endStr) {
        int length = startStr.length();
        int start = fromStr.indexOf(startStr) + length;
        int end = fromStr.indexOf(endStr);
        return fromStr.substring(start, end);
    }
}
