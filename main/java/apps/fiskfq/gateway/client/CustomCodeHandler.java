package apps.fiskfq.gateway.client;

import apps.fiskfq.PropertyManager;
import apps.fiskfq.gateway.domain.base.Tia;
import apps.fiskfq.gateway.domain.base.Toa;
import apps.fiskfq.repository.MybatisManager;
import apps.fiskfq.repository.dao.FsKfqSysCtlMapper;
import apps.fiskfq.repository.model.FsKfqSysCtl;
import apps.fiskfq.repository.model.FsKfqSysCtlExample;
import common.utils.StringPad;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 开发区非税
 */
public class CustomCodeHandler {

    private static Logger logger = LoggerFactory.getLogger(CustomCodeHandler.class);
    MybatisManager manager = new MybatisManager();

    public static byte[] encode(Tia tia, String srcCode, String desCode, String isSign, String authLen) {

        String msgdata = tia.toString();

        logger.info(msgdata);
        String txnDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String dataType = getSubstrBetweenStrs(msgdata, "<dataType>", "</dataType>");

        // 报文文档中的数据类型为交易码
        String msg = StringPad.leftPad4ChineseToByteLength(dataType, 6, " ")
                + StringPad.leftPad4ChineseToByteLength(srcCode, 15, " ")
                + StringPad.leftPad4ChineseToByteLength(desCode, 15, " ")
                + txnDate + isSign
                + StringPad.leftPad4ChineseToByteLength(authLen, 3, " ")
                // 15-保留字段
                + "               "
                + msgdata;
        int msglength = msg.getBytes().length + 8;
        String len = StringPad.leftPad4ChineseToByteLength("" + msglength, 8, "0");

        byte[] bytes = new byte[msglength];
        System.arraycopy(len.getBytes(), 0, bytes, 0, 8);
        System.arraycopy(msg.getBytes(), 0, bytes, 8, msglength - 8);
        logger.info("[本地客户端-开发区非税]发送报文长度：" + msglength);
        logger.info("[本地客户端-开发区非税]发送报文：" + msg);
        return bytes;
    }

    public static Toa decode(byte[] bytes) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        logger.info("[客户端接收-开发区非税]" + new String(bytes));

        String txnCode = new String(bytes, 0, 6).trim();
        String msgdata = new String(bytes, 69, bytes.length - 69);

        Class clazz = Class.forName("apps.fiskfq.gateway.domain.txn.Toa" + txnCode);
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
