package apps.fis.domain.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import apps.fis.domain.base.Toa;
import apps.fis.domain.base.ToaFisHeader;

import java.io.Serializable;

/**
 *3.4.6.2	��Ŀ������Ϣ����
 */

@XStreamAlias("ROOT")
public class Toa3007 extends Toa {
    public ToaFisHeader HEAD = new ToaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        public Data DATA = new Data();
    }

    public static class Data implements Serializable {

        public String SRXMBM = "";                           // ������Ŀ����	C40	NO
        public String SRXMMC = "";                           // ������Ŀ����	C100	NO
        public String ZJXZBM = "";                           // �ʽ����ʱ���	C40	NO
        public String ZJXZMC = "";                           // �ʽ���������	C100	NO
        public String YSKMBM = "";                           // Ԥ���Ŀ����	C20	YES
        public String YSKMMC = "";                           // Ԥ���Ŀ����	C100	YES
        public String SRXMDW = "";                           // ������Ŀ��λ	C40	YES
        public String SRXMBZ = "";                           // ������Ŀ��׼	C100	YES
        public String BYZD1 = "";                            // �����ֶ�
        public String BYZD2 = "";                            // �����ֶ�
        public String BYZD3 = "";                            // �����ֶ�
    }

    @Override
    public String toString() {
        this.HEAD.CODE = "3007";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa3007.class);
        return xs.toXML(this);
    }

    @Override
    public Toa3007 getToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa3007.class);
        return (Toa3007) xs.fromXML(xml);
    }
}
