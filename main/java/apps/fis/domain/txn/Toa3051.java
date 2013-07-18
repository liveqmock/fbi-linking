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
 * 3.4.10.2	待补票开票信息反馈
 */

@XStreamAlias("ROOT")
public class Toa3051 extends Toa {
    public ToaFisHeader HEAD = new ToaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        @XStreamImplicit
        public List<Data> dataList = new ArrayList<Data>();

        @XStreamAlias("DATA")
        public static class Data implements Serializable {
            public String DBPYWXH = "";                 // 待补票业务序号
            public String PJZL = "";                    // 新补票的票据种类
            public String XBPH = "";                    // XBPH为新补票号，待补票票据作废后重新补票的票据编号。若待补票票据作废后未重新补票，则新补票号为空；
        }
    }



    @Override
    public String toString() {
        this.HEAD.CODE = "3051";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa3051.class);
        return xs.toXML(this);
    }

    @Override
    public Toa3051 getToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa3051.class);
        return (Toa3051) xs.fromXML(xml);
    }
}
