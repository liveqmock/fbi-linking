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
 *3.4.12.1	不明款信息上传申请
 */

@XStreamAlias("ROOT")
public class Tia3004 extends Tia {
    public TiaFisHeader HEAD = new TiaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        public Data DATA = new Data();
    }

    public static class Data implements Serializable {
        public String XZQH = "";                         // 行政区划	C6	NO	YES	行政区划编码，按国家标准编码
        public String BMKYWXH = "";                      // 不明款业务序号	C50	YES	如果此待补票为之前不明款转变成的，此字段对应为之前不明款的业务序号。
        public String CZZHZH = "";                       // 财政专户账号	C40	NO
        public String JKFS = "";                         // 缴款方式	C3	NO		03电汇04转账支票05现金通存
        public String YT = "";                           // 用途	C100	YES
        public String JYRQ = "";                         // 交易日期	DATE	NO
        public String JE = "";                           // 金额	N12,4	NO
        public String BYZD1 = "";                        // 备用字段1	C100	YES
        public String BYZD2 = "";                        // 备用字段2	C100	YES
        public String BYZD3 = "";                        // 备用字段3	C100	YES
        public String BYZD4 = "";                        // 备用字段4	C100	YES
        public String BYZD5 = "";                        // 备用字段5	C100	YES
        public String BYZD6 = "";                        // 备用字段6	C100	YES
        public String BYZD7 = "";                        // 备用字段7	C100	YES

    }

    @Override
    public String toString() {
        this.HEAD.CODE = "3004";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Tia3004.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + xs.toXML(this);
    }

    @Override
    public Tia3004 getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Tia3004.class);
        return (Tia3004) xs.fromXML(xml);
    }
}
