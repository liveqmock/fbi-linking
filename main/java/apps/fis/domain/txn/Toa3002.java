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
 *  3.4.2.2	实收信息反馈
 */

@XStreamAlias("ROOT")
public class Toa3002 extends Toa {
    public ToaFisHeader HEAD = new ToaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {

        public Data DATA = new Data();

        public static class Data implements Serializable {
            public String PJZL = "";                         // 1 票据种类	C10	NO	YES	共7种到银行缴款的票据种类（编码见附件一）
            public String JKSBH = "";                        // 2 缴款书编号	C20	NO	YES		JKSBH
            public String XZQH = "";                         // 3 行政区划	C6	NO		行政区划编码六位	XZQH

            @XStreamImplicit
            public List<Xmmx> xmmxes = new ArrayList<Xmmx>();

            @XStreamAlias("XMMX")
            public static class Xmmx implements Serializable {

                public String SRXMBM = "";                          // 收入项目编码	C42	NO	YES	如果缴款书是手工票，项目编码的后两位为“单位项目关联校验码”
                public String JLLX = "";                            // 记录类型	C3	NO		1 非税收入 3 执收单位代收款
                public String JE = "";                               // 金额	N12,2	NO
                public String SRZHMC = "";                          // 收入账户名称	C80	NO
                public String SRHMC = "";                           // 收入行名称	 C80	NO
                public String SRHLX = "";                           // 收入行类型	 C4	NO
                public String SRZH = "";                            // 收入账号	C40	NO
            }
        }

    }


    @Override
    public String toString() {
        this.HEAD.CODE = "3002";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa3002.class);
        return xs.toXML(this);
    }

    @Override
    public Toa3002 getToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa3002.class);
        return (Toa3002) xs.fromXML(xml);
    }
}
