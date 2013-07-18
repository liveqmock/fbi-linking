package gateway.component;

import apps.fis.enums.TxnRtnCode;
import common.utils.MD5Helper;
import common.utils.PropertyManager;
import gateway.domain.LFixedLengthProtocol;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ��������ݴ�������
 */
public class ServerChannelHandler extends SimpleChannelHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServerChannelHandler.class);

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent me) {
        LFixedLengthProtocol toa = null;
        try {
            byte[] dataBytes = (byte[]) me.getMessage();
            logger.info("[Linking����]" + new String(dataBytes));

            LFixedLengthProtocol tia = new LFixedLengthProtocol();
            tia.assembleFields(dataBytes);
            toa = tia;
            logger.info("[Linking����]-[Staring������:" + tia.txnCode + "] [��ˮ��:" + tia.serialNo + "]");
            logger.info("[Linking����]-[Staring��Ա:" + tia.tellerID + "] [����:" + tia.branchID + "]");
            String mac = tia.mac;
            String userID = PropertyManager.getProperty("app.userid." + tia.ueserID.trim().toLowerCase());
            String md5 = MD5Helper.getMD5String(new String(tia.msgBody) + tia.txnTime.substring(0, 8) + userID);
            // TODO md5 У��
            /*if (!mac.equals(md5)) {
                throw new RuntimeException("7010|MACУ��ʧ�ܡ�");
            }*/
            // �����ķַ�������Ӧ��
            toa = new AppsDispatcher().dispatch(tia);
            logger.info("[Linking��Ӧ]-[Staring������:" + toa.txnCode + "] [��ˮ��:" + toa.serialNo + "][������:" + toa.rtnCode + "]");
            me.getChannel().write(toa);
        } catch (Exception e) {
            String exmsg = e.getMessage();
            logger.error("�����쳣." + exmsg, e);
            if (toa == null) {
                toa = new LFixedLengthProtocol();
                toa.txnTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            }
            if (exmsg == null) {
                toa.rtnCode = "9000";
                toa.msgBody = "ϵͳ�쳣.".getBytes();
            } else {
                if (exmsg.contains("|")) {
                    String errmsg[] = exmsg.split("\\|");
                    toa.rtnCode = errmsg[0];
                    toa.msgBody = errmsg[1].getBytes();
                } else {
                    toa.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
                    toa.msgBody = exmsg.getBytes();
                }
            }
            me.getChannel().write(toa);
        } finally {
            me.getChannel().disconnect();
            me.getChannel().close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
