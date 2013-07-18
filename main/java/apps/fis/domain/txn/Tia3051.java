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
 *3.4.11.1	待补票票据作废信息申请
 */

@XStreamAlias("ROOT")
public class Tia3051 extends Tia {
    public TiaFisHeader HEAD = new TiaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        public Data DATA = new Data();
    }

    public static class Data implements Serializable {
        public String XZQH = "";                         // 行政区划	C6	NO	YES	行政区划编码，按国家标准编码
        /*
        SQRQ和DBPYWXH可任选一或全选，但不能全为空；若业务序号不为空，则财政反馈业务序号对应的待补票票据作废相关信息；
        若业务序号为空、申请日期不为空，则财政反馈对应日期的待补票票据作废信息。
        建议银行每日对账前按日期获取待补票作废信息，然后把系统中已作废的补票票据信息进行作废或删除。         */
        public String SQRQ = "";                         // 申请日期
        public String DBPYWXH = "";                      // 业务序号	C50	NO	YES	待补票的唯一ID
    }

    @Override
    public String toString() {
        this.HEAD.CODE = "3051";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Tia3051.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + xs.toXML(this);
    }

    @Override
    public Tia3051 getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Tia3051.class);
        return (Tia3051) xs.fromXML(xml);
    }
}
