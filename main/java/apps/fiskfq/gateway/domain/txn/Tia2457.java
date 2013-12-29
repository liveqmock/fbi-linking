package apps.fiskfq.gateway.domain.txn;

import apps.fiskfq.gateway.domain.base.Tia;
import apps.fiskfq.gateway.domain.base.xml.Signs;
import apps.fiskfq.gateway.domain.base.xml.TiaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 手工票录入请求
 */

@XStreamAlias("Root")
public class Tia2457 extends Tia {
    public TiaHeader Head = new TiaHeader();
    public Body Body = new Body();
    public Signs Signs = new Signs();

    public static class Body implements Serializable {

        public BodyObject Object = new BodyObject();
    }

    public static class BodyObject implements Serializable {

        public BodyRecord Record = new BodyRecord();
    }


    public static class BodyRecord implements Serializable {

        /*
        rg_code	区划码
billtype_code	缴款书样式编码
bill_no	票号
verify_no	全票面校验码
pm_code	缴款方式编码
ien_code	执收单位业务码
ien_name	执收单位名称
consign_ien_code	委托单位编码
consign_ien_name	委托单位名称
bill_money	收款金额
set_year	年度
bank_user	创建人
Bank_no	银行编码
payer	缴款人
payerbank	缴款人开户行
payeraccount	缴款人账号
receiver	收款人全称
receiverbank	收款人账户开户行
receiveraccount	收款人账号
is_consign	是否委托
remark	备注

         */
        public String rg_code = "";
        public String billtype_code = "";
        public String bill_no = "";
        public String verify_no = "";
        public String pm_code = "";
        public String ien_code = "";
        public String ien_name = "";
        public String consign_ien_code = "";
        public String consign_ien_name = "";
        public String bill_money = "";
        public String set_year = "";
        public String bank_user = "";
        public String bank_no = "";
        public String payer = "";
        public String payerbank = "";
        public String payeraccount = "";
        public String receiver = "";
        public String receiverbank = "";
        public String receiveraccount = "";
        public String is_consign = "";
        public String remark = "";

        public List<DetailRecord> Object
                = new ArrayList<DetailRecord>();

        @XStreamAlias("Record")
        public static class DetailRecord {
            /*
            in_bis_code	收入项目业务码
in_bis_name	收入项目名称
chargenum	收入数量
chargemoney	收入金额
             */
            public String in_bis_code = "";
            public String in_bis_name = "";
            public String chargenum = "";
            public String chargemoney = "";

        }
    }

    @Override
    public String toString() {

        if (StringUtils.isEmpty(Head.msgId)) {
            Head.msgId = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date());
        }
        Head.msgRef = Head.msgId;
        if ("".equals(Head.workDate)) {
            Head.workDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        Head.dataType = "2457";

        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Tia2457.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }
}
