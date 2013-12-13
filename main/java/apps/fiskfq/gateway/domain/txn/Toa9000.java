package apps.fiskfq.gateway.domain.txn;

import apps.fiskfq.gateway.domain.base.Toa;
import apps.fiskfq.gateway.domain.base.xml.ToaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Serializable;

/**
 * ͨ�����׻�ִ
 */

@XStreamAlias("Root")
public class Toa9000 extends Toa {
    public ToaHeader Head = new ToaHeader();
    public Body Body = new Body();


    public static class Body implements Serializable {

        public Object Object = new Object();
    }

    public static class Object implements Serializable {

        public Record Record = new Record();
    }


    public static class Record implements Serializable {

        /*
        ori_datatype	ԭ��������	NString	4		M
        ori_send_orgcode	ԭ���𷽱���	NString	[1,15]		M
        ori_entrust_date	ԭί������	Date		����������	M
        Result	����������	String	4		M
        add_word	����	GBString	[1,60]		O
         */
        public String ori_datatype = "";
        public String ori_send_orgcode = "";
        public String ori_entrust_date = "";
        public String Result = "";
        public String add_word = "";
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa9000.class);
        return (Toa9000) xs.fromXML(xml);
    }
}
