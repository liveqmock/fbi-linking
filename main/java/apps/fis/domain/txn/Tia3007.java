package apps.fis.domain.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import apps.fis.domain.base.Tia;
import apps.fis.domain.base.TiaFisHeader;

import java.io.Serializable;

/**
 *3.4.6.1	��Ŀ������Ϣ����
 */

@XStreamAlias("ROOT")
public class Tia3007 extends Tia {
    public TiaFisHeader HEAD = new TiaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        public Data DATA = new Data();
    }

    public static class Data implements Serializable {
        public String XZQH = "";                         // ��������	C6	NO
        public String ZSDWBM = "";                       // ִ�յ�λ����	C42	NO
        public String SRXMBM = "";                       // ������Ŀ����	C42	NO		�ֹ�Ʊ����Ʊ����Ϣ����д����Ŀ����
        public String BYZD1 = "";                        // �����ֶ�1	C100	YES
        public String BYZD2 = "";                        // �����ֶ�2	C100	YES
        public String BYZD3 = "";                        // �����ֶ�3	C100	YES
    }

    @Override
    public String toString() {
        this.HEAD.CODE = "3007";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Tia3007.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + xs.toXML(this);
    }

    @Override
    public Tia3007 getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Tia3007.class);
        return (Tia3007) xs.fromXML(xml);
    }
}
