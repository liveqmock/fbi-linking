package apps.fis.domain.base;

import apps.fis.SystemParameter;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

public class TiaFisHeader implements Serializable {
    @XStreamAsAttribute
    public String TYPE = "0";                                    //	TYPE=��0�� ��������Ϊ��������
    @XStreamAsAttribute
    public String CODE = "";                                     // ������
    public String YHLB = SystemParameter.YHLB;                   // �������
    public String USERNAME = SystemParameter.USERNAME;           // �û���
    public String PASSWORD = SystemParameter.PASSWORD;           // ����
}
