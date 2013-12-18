package apps.fiskfq.gateway.domain.txn;

import apps.fiskfq.gateway.domain.base.Tia;
import apps.fiskfq.gateway.domain.base.xml.TiaHeader;
import apps.fiskfq.gateway.domain.base.xml.Signs;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 应收数据查询请求
 */

@XStreamAlias("Root")
public class Tia2401 extends Tia {
    public TiaHeader Head = new TiaHeader();
    public Body Body = new Body();
    public Signs Signs = new Signs();

    public static class Body implements Serializable {

        public Object Object = new Object();
    }

    public static class Object implements Serializable {

        public Record Record = new Record();
    }


    /*
    billtype_code	缴款书样式编码
bill_no	票号
verify_no	全票面校验码
bill_money	收款金额
set_year	年度
     */
    public static class Record implements Serializable {

        public String billtype_code = "";
        public String bill_no = "";
        public String verify_no = "";
        public String bill_money = "";
        public String set_year = "";
    }

    @Override
    public String toString() {

        Head.msgId = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        Head.msgRef = Head.msgId;
        if ("".equals(Head.workDate)) {
            Head.workDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }

        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Tia2401.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }
}
