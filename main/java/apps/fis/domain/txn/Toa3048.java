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
 * 3.4.3.2	电子对账反馈
 */

@XStreamAlias("ROOT")
public class Toa3048 extends Toa {
    public ToaFisHeader HEAD = new ToaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        public Data DATA = new Data();
    }

    public static class Data implements Serializable {
        /*
        DZSBWJM 代表财政反馈的对账失败明细信息文件名称，文件名命名规则为，30480120100608134530(功能号+银行类别+日期YYYYMMDD +时间HHMMSS)，文件名不能重复。
        */
        public String DZSBWJM = "";
    }

    @Override
    public String toString() {
        this.HEAD.CODE = "3048";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa3048.class);
        return xs.toXML(this);
    }

    @Override
    public Toa3048 getToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa3048.class);
        return (Toa3048) xs.fromXML(xml);
    }
}
