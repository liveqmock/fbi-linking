package apps.fiskfq.gateway.domain.base.xml;

import apps.fiskfq.PropertyManager;

import java.io.Serializable;

public class TiaHeader implements Serializable {
    public String src = "";                   // ���ͷ�����
    public String des = "";                   // ���շ�����
    public String dataType = "";
    public String msgId = "";                 // ���ı�ʶ��
    public String msgRef = "";                // ���Ĳο���  ����������ʱ���Ĳο���ͬ���ı�ʶ��
    public String workDate = "";              // ��������
}
