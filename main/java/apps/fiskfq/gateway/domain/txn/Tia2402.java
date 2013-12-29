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
import java.util.Date;

/**
 * Ӧ�������տ�ȷ��
 */

@XStreamAlias("Root")
public class Tia2402 extends Tia {
    public TiaHeader Head = new TiaHeader();
    public Body Body = new Body();
    public Signs Signs = new Signs();

    public static class Body implements Serializable {

        public BodyObject Object = new BodyObject();
    }

    public static class BodyObject implements Serializable {

        public BodyRecord Record = new BodyRecord();
    }


    /*

   chr_id	�ɿ���ID
billtype_code	�ɿ�����ʽ����
bill_no	Ʊ��
bill_money	�տ���
bank_indate	�����տ�ʱ��
incomestatus	�տ�״̬
pm_code	�ɿʽ����
cheque_no	�����
payerbank	�ɿ����˻�������
payeraccount	�ɿ����˺�
set_year	���
route_user_code	·���û�����
license	��Ȩ���к�
business_id	������ˮ��
     */
    public static class BodyRecord implements Serializable {

        public String chr_id = "";
        public String billtype_code = "";
        public String bill_no = "";
        public String bill_money = "";
        public String bank_indate = "";
        public String incomestatus = "";
        public String pm_code = "";
        public String cheque_no = "";
        public String payerbank = "";
        public String payeraccount = "";
        public String set_year = "";
        public String route_user_code = "";
        public String license = "";
        public String business_id = "";
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
        Head.dataType = "2402";

        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Tia2402.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + "\n" + xs.toXML(this);
    }
}
