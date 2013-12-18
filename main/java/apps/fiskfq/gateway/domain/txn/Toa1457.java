package apps.fiskfq.gateway.domain.txn;

import apps.fiskfq.gateway.domain.base.Toa;
import apps.fiskfq.gateway.domain.base.xml.Signs;
import apps.fiskfq.gateway.domain.base.xml.ToaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Serializable;

/**
 * �ֹ�Ʊ¼���ִ
 */

@XStreamAlias("Root")
public class Toa1457 extends Toa {
    public ToaHeader Head = new ToaHeader();
    public Body Body = new Body();
    public Signs Signs = new Signs();


    public static class Body implements Serializable {

        public Object Object = new Object();
    }

    public static class Object implements Serializable {

        public Record Record = new Record();
    }


    /*
   chr_id	�ɿ���ID
billtype_code	�ɿ�����ʽ����
billtype_name	�ɿ�����ʽ����
bill_no	Ʊ��
verify_no	�ɿ���ȫƱ��У����
makedate	��Ʊ����
ien_code	ִ�յ�λҵ����
ien_name	ִ�յ�λ����
set_year	���
succ_code	OK


     */
    public static class Record implements Serializable {

        public String chr_id = "";
        public String billtype_code = "";
        public String billtype_name = "";
        public String bill_no = "";
        public String verify_no = "";
        public String makedate = "";
        public String ien_code = "";
        public String ien_name = "";
        public String set_year = "";
        public String succ_code = "";
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa1457.class);
        return (Toa1457) xs.fromXML(xml);
    }

}
