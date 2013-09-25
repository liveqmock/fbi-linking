package apps.fisjz.online.action;

import apps.fisjz.enums.TxnRtnCode;
import apps.fisjz.repository.dao.FsSysAreaConfigMapper;
import apps.fisjz.repository.model.FsSysAreaConfig;
import gateway.domain.LFixedLengthProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public abstract class AbstractTxnAction {
    private static Logger logger = LoggerFactory.getLogger(AbstractTxnAction.class);

    @Autowired
    FsSysAreaConfigMapper areaConfigMapper;

    @Transactional
    public LFixedLengthProtocol run(LFixedLengthProtocol tia) {
        try {
            tia.rtnCode = "0000";

/*
            //���⴦��ȥ�����ĵ�body������β�ָ���
            int length = tia.msgBody.length;
            if (length > 2) {
                byte[] buf = new byte[length - 2];
                System.arraycopy(tia.msgBody, 1, buf, 0, buf.length);
                tia.msgBody = buf;
            }
*/

            return process(tia);
        } catch (Exception e) {
            logger.error("Actionҵ�������", e);
            throw new RuntimeException(e.getMessage() == null ? TxnRtnCode.TXN_EXECUTE_FAILED.toRtnMsg() : e.getMessage());
        }
    }

    abstract protected LFixedLengthProtocol process(LFixedLengthProtocol tia) throws Exception;


    protected boolean getResponseResult(List<Map> rtnList) {
        for (Map map : rtnList) {
            String result = (String) map.get("RESULT");
            if (result != null) {
                if ("SUCCESS".equals(result.toUpperCase())) {
                    return true;
                }else{ //���⴦���ظ��տ�����
                    String msg = (String) map.get("MESSAGE");
                    if (msg.contains("��ȷ���տ�����ظ�������")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected String getResponseErrMsg(List<Map> rtnList) {
        for (Map map : rtnList) {
            String result = (String) map.get("RESULT");
            if (result != null) {
                if (!"SUCCESS".equals(result.toUpperCase())) {
                    return (String) map.get("MESSAGE");
                }
            }
        }
        return "";
    }

    protected  String getApplicationidByAreaCode(String areaCode){
        FsSysAreaConfig config = areaConfigMapper.selectByPrimaryKey(areaCode);
        if (config == null) {
            throw new RuntimeException("��ȡӦ��ID��,������=" + areaCode);
        }
        return config.getAppId();
    }
    protected  String getBankCodeByAreaCode(String areaCode){
        FsSysAreaConfig config = areaConfigMapper.selectByPrimaryKey(areaCode);
        if (config == null) {
            throw new RuntimeException("��ȡ�������,������=" + areaCode);
        }
        return config.getBankCode();
    }
    protected  String getFinorgByAreaCode(String areaCode){
        FsSysAreaConfig config = areaConfigMapper.selectByPrimaryKey(areaCode);
        if (config == null) {
            throw new RuntimeException("��ȡ���������,������=" + areaCode);
        }
        return config.getFinorgCode();
    }
}
