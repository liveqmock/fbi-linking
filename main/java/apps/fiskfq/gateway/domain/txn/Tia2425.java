package apps.fiskfq.gateway.domain.txn;

import apps.fiskfq.gateway.domain.base.Tia;
import apps.fiskfq.gateway.domain.base.xml.Signs;
import apps.fiskfq.gateway.domain.base.xml.TiaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 对账请求
 */

@XStreamAlias("Root")
public class Tia2425 extends Tia {
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
    begin_date	收款确认起始日期
end_date	收款确认结束日期
receiveraccount	收款人账号
consign_ien_code	收入单位
chr_id	缴款书ID
billtype_code	缴款书样式编码
bill_no	票号
bankmoney	银行方总金额
is_getcollectinfo	是否获取缴款书明细
set_year	年度
route_user_code	路由用户编码
license	授权序列号
business_id	交易流水号
     */
    public static class Record implements Serializable {

        public String begin_date = "";
        public String end_date = "";
        public String receiveraccount = "";
        public String consign_ien_code = "";
        public String chr_id = "";
        public String billtype_code = "";
        public String bill_no = "";
        public String bankmoney = "";
        public String is_getcollectinfo = "";
        public String set_year = "";
        public String route_user_code = "";
        public String license = "";
        public String business_id = "";
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
        xs.processAnnotations(Tia2425.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }
}
