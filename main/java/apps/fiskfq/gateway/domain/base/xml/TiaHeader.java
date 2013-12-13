package apps.fiskfq.gateway.domain.base.xml;

import apps.fiskfq.PropertyManager;

import java.io.Serializable;

public class TiaHeader implements Serializable {
    public String src = PropertyManager.getProperty("src.code");                   // ���ͷ�����
    public String des = PropertyManager.getProperty("des.code");                   // ���շ�����
    public String dataType = "";
    public String msgId = "";                 // ���ı�ʶ��
    public String msgRef = "";                // ���Ĳο���  ����������ʱ���Ĳο���ͬ���ı�ʶ��
    public String workDate = "";              // ��������
}
