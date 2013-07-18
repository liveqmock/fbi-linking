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
 *3.4.8.2	待补票上传信息反馈
 */

@XStreamAlias("ROOT")
public class Toa3026 extends Toa {
    public ToaFisHeader HEAD = new ToaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {

        public Data DATA = new Data();
    }

    public static class Data implements Serializable {
        public String QRBZ = "";                  // 0未确认；1确认正确；2确认错误；3已补票
        public String QRXX = "";                  // 确认信息
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
