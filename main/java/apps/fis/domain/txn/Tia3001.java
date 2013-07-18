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
 * 市财政非税-缴款书应收信息申请
 */

@XStreamAlias("ROOT")
public class Tia3001 extends Tia {
    public TiaFisHeader HEAD = new TiaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        public Data DATA = new Data();
    }

    public static class Data implements Serializable {
        /*
        PJZL 为票据种类（编码见附件一），各银行可做成可配置。
        JKSBH  为缴款书编号
         */
        public String PJZL = "";
        public String JKSBH = "";
    }

    @Override
    public String toString() {
        this.HEAD.CODE = "3001";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Tia3001.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + xs.toXML(this);
    }

    @Override
    public Tia3001 getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Tia3001.class);
        return (Tia3001) xs.fromXML(xml);
    }
}
