package apps.fisjz.online.service;

import apps.fisjz.repository.dao.FsSysAreaConfigMapper;
import apps.fisjz.repository.model.FsSysAreaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-26
 */
@Service
public class ServiceHelper {
    private static final Logger logger = LoggerFactory.getLogger(ServiceHelper.class);

    @Autowired
    private FsSysAreaConfigMapper areaConfigMapper;

    protected boolean getResponseResult(List<Map> rtnList) {
        for (Map map : rtnList) {
            String result = (String) map.get("RESULT");
            if (result != null) {
                if ("SUCCESS".equals(result.toUpperCase())) {
                    return true;
                }else{ //���⴦���ظ��տ�����
                    return false;
/*
                    String msg = (String) map.get("MESSAGE");
                    if (msg.contains("��ȷ���տ�����ظ�������")) {
                        return true;
                    }
*/
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
