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
 *3.4.10.1	待补票开票信息申请
 */

@XStreamAlias("ROOT")
public class Tia3027 extends Tia {
    public TiaFisHeader HEAD = new TiaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        public Data DATA = new Data();
    }

    public static class Data implements Serializable {
        public String XZQH = "";                         // 行政区划	C6	NO	YES	行政区划编码，按国家标准编码
        /*
        6、	SQRQ和DBPYWXH可任选一或全选，但不能全为空；例如：选择申请日期，财政返回相应日期的所有补票信息；选择业务序号，财政返回相应业务序号的补票信息。
         */
        public String SQRQ = "";                         // 申请日期
        public String DBPYWXH = "";                      // 业务序号	C50	NO	YES	待补票的唯一ID
    }

    @Override
    public String toString() {
        this.HEAD.CODE = "3027";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Tia3027.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + xs.toXML(this);
    }

    @Override
    public Tia3027 getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Tia3027.class);
        return (Tia3027) xs.fromXML(xml);
    }
}
