package apps.fiskfq.gateway.domain.base.xml;

import java.io.Serializable;

public class ToaHeader implements Serializable {
    public String src = "";                   // ���ͷ�����
    public String des = "";                   // ���շ�����
    public String dataType = "";
    public String msgId = "";                 // ���ĺ�
    public String msgRef = "";                // ���Ĳο���  ����������ʱ���Ĳο���ͬ���ı�ʶ��
    public String workDate = "";              // ��������
}
