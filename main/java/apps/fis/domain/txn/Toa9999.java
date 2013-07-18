package apps.fis.domain.txn;

import apps.fis.domain.base.Toa;
import apps.fis.domain.base.ToaFisHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 3.4.10.2	待补票开票信息反馈
 */

@XStreamAlias("ROOT")
public class Toa9999 extends Toa {
    public ToaFisHeader HEAD = new ToaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
    }

    @Override
    public String toString() {
        this.HEAD.CODE = "9999";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa9999.class);
        return xs.toXML(this);
    }

    @Override
    public Toa9999 getToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa9999.class);
        return (Toa9999) xs.fromXML(xml);
    }
}
