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
 *3.4.8.2	����Ʊ�ϴ���Ϣ����
 */

@XStreamAlias("ROOT")
public class Toa3026 extends Toa {
    public ToaFisHeader HEAD = new ToaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {

        public Data DATA = new Data();
    }

    public static class Data implements Serializable {
        public String QRBZ = "";                  // 0δȷ�ϣ�1ȷ����ȷ��2ȷ�ϴ���3�Ѳ�Ʊ
        public String QRXX = "";                  // ȷ����Ϣ
    }
    @Override
    public String toString() {
        this.HEAD.CODE = "3026";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa3026.class);
        return xs.toXML(this);
    }

    @Override
    public Toa3026 getToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa3026.class);
        return (Toa3026) xs.fromXML(xml);
    }
}
