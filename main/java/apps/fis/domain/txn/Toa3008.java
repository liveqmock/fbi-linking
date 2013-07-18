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
 *3.4.5.2	单位名称信息反馈
 */

@XStreamAlias("ROOT")
public class Toa3008 extends Toa {
    public ToaFisHeader HEAD = new ToaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        public Data DATA = new Data();
    }

    public static class Data implements Serializable {
        /*
        */
        public String ZSDWBM = "";                           // 执收单位编码
        public String ZSDWMC = "";                           // 执收单位名称
        public String ZGBMBM = "";                           // 主管部门编码
        public String ZGBMMC = "";                           // 主管部门名称
        public String BYZD1 = "";                            // 备用字段
        public String BYZD2 = "";                            // 备用字段
        public String BYZD3 = "";                            // 备用字段
    }

    @Override
    public String toString() {
        this.HEAD.CODE = "3008";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa3008.class);
        return xs.toXML(this);
    }

    @Override
    public Toa3008 getToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa3008.class);
        return (Toa3008) xs.fromXML(xml);
    }
}
