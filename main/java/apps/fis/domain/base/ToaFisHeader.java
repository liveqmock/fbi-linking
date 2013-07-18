package apps.fis.domain.base;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

public class ToaFisHeader implements Serializable {
    @XStreamAsAttribute
    public String TYPE = "1";                      // TYPE=��1�� ����������Ϊ��������
    @XStreamAsAttribute
    public String CODE = "";                       // ������
    public String STATUS = "";                     // ״̬
    public String ERRCODE = "";                    // ������
    public String MESSAGE = "";                    // ��Ϣ
}
