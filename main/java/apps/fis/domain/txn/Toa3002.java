package apps.fis.domain.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import apps.fis.domain.base.Toa;
import apps.fis.domain.base.ToaFisHeader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  3.4.2.2	ʵ����Ϣ����
 */

@XStreamAlias("ROOT")
public class Toa3002 extends Toa {
    public ToaFisHeader HEAD = new ToaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {

        public Data DATA = new Data();

        public static class Data implements Serializable {
            public String PJZL = "";                         // 1 Ʊ������	C10	NO	YES	��7�ֵ����нɿ��Ʊ�����ࣨ���������һ��
            public String JKSBH = "";                        // 2 �ɿ�����	C20	NO	YES		JKSBH
            public String XZQH = "";                         // 3 ��������	C6	NO		��������������λ	XZQH

            @XStreamImplicit
            public List<Xmmx> xmmxes = new ArrayList<Xmmx>();

            @XStreamAlias("XMMX")
            public static class Xmmx implements Serializable {

                public String SRXMBM = "";                          // ������Ŀ����	C42	NO	YES	����ɿ������ֹ�Ʊ����Ŀ����ĺ���λΪ����λ��Ŀ����У���롱
                public String JLLX = "";                            // ��¼����	C3	NO		1 ��˰���� 3 ִ�յ�λ���տ�
                public String JE = "";                               // ���	N12,2	NO
                public String SRZHMC = "";                          // �����˻�����	C80	NO
                public String SRHMC = "";                           // ����������	 C80	NO
                public String SRHLX = "";                           // ����������	 C4	NO
                public String SRZH = "";                            // �����˺�	C40	NO
            }
        }

    }


    @Override
    public String toString() {
        this.HEAD.CODE = "3002";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa3002.class);
        return xs.toXML(this);
    }

    @Override
    public Toa3002 getToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa3002.class);
        return (Toa3002) xs.fromXML(xml);
    }
}
