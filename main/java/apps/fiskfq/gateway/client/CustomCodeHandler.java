package apps.fiskfq.gateway.client;

import apps.fiskfq.PropertyManager;
import apps.fiskfq.gateway.domain.base.Tia;
import apps.fiskfq.gateway.domain.base.Toa;
import apps.fiskfq.gateway.domain.txn.Toa9906;
import apps.fiskfq.gateway.domain.txn.Toa9910;
import apps.fiskfq.repository.MybatisManager;
import apps.fiskfq.repository.dao.FsKfqSysCtlMapper;
import apps.fiskfq.repository.model.FsKfqSysCtl;
import apps.fiskfq.repository.model.FsKfqSysCtlExample;
import common.utils.StringPad;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 开发区非税
 */
public class CustomCodeHandler {

    private static Logger logger = LoggerFactory.getLogger(CustomCodeHandler.class);
    MybatisManager manager = new MybatisManager();

    public static byte[] encode(Tia tia, String srcCode, String desCode,
                                String isSign, String authLen, String authCode) throws UnsupportedEncodingException {

        String msgdata = tia.toString();

        logger.info(msgdata);
        String txnDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String dataType = getSubstrBetweenStrs(msgdata, "<dataType>", "</dataType>");

        // 报文文档中的数据类型为交易码
        String msg = StringPad.leftPad4ChineseToByteLength(dataType, 6, " ")
                + StringPad.leftPad4ChineseToByteLength(srcCode, 15, " ")
                + StringPad.leftPad4ChineseToByteLength(desCode, 15, " ")
                + txnDate + isSign
                + StringPad.leftPad4ChineseToByteLength(authLen, 3, "0")
                + "               " + authCode
                + msgdata;
        int msglength = msg.getBytes().length + 8;
        String len = StringPad.leftPad4ChineseToByteLength("" + msglength, 8, "0");

        byte[] bytes = new byte[msglength];
        System.arraycopy(len.getBytes(), 0, bytes, 0, 8);
        System.arraycopy(msg.getBytes("GBK"), 0, bytes, 8, msglength - 8);
        logger.info("[本地客户端-开发区非税]发送报文长度：" + len);
        logger.info("[本地客户端-开发区非税]发送报文:" + msg);
        return bytes;
    }

    public static Toa decode(byte[] bytes) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        logger.info("[客户端接收-开发区非税]" + new String(bytes));

        String txnCode = new String(bytes, 0, 6).trim();
        int authLen = Integer.parseInt(new String(bytes, 51, 3));
        String msgdata = new String(bytes, 69 + authLen, bytes.length - 69 - authLen);

        Toa toa = null;
        if ("9910".equals(txnCode)) {
            toa = new Toa9910();
            Toa9910 toa9910 = (Toa9910) toa.toBean(msgdata);
            logger.info("返回报文对象实例化完成 9910");
            throw new RuntimeException(toa9910.Body.Object.Record.result + toa9910.Body.Object.Record.add_word);

        } else if ("9906".equals(txnCode)) {
            toa = new Toa9906();
            Toa9906 toa9906 = (Toa9906) toa.toBean(msgdata);
            logger.info("返回报文对象实例化完成 9906");
            // TODO 保存授权码和授权码长度

        } else {
            Class clazz = Class.forName("apps.fiskfq.gateway.domain.base.ToaXml");
            Toa tmptoa = (Toa) clazz.newInstance();
            toa = tmptoa.toBean(msgdata);
            logger.info("返回报文对象实例化完成 Toaxml");

        }
        return toa;
    }


    static String getSubstrBetweenStrs(String fromStr, String startStr, String endStr) {
        int length = startStr.length();
        int start = fromStr.indexOf(startStr) + length;
        int end = fromStr.indexOf(endStr);
        return fromStr.substring(start, end);
    }
}
