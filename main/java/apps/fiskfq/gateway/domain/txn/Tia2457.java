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
 * �ֹ�Ʊ¼������
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
        rg_code	������
billtype_code	�ɿ�����ʽ����
bill_no	Ʊ��
verify_no	ȫƱ��У����
pm_code	�ɿʽ����
ien_code	ִ�յ�λҵ����
ien_name	ִ�յ�λ����
consign_ien_code	ί�е�λ����
consign_ien_name	ί�е�λ����
bill_money	�տ���
set_year	���
bank_user	������
Bank_no	���б���
payer	�ɿ���
payerbank	�ɿ��˿�����
payeraccount	�ɿ����˺�
receiver	�տ���ȫ��
receiverbank	�տ����˻�������
receiveraccount	�տ����˺�
is_consign	�Ƿ�ί��
remark	��ע

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
            in_bis_code	������Ŀҵ����
in_bis_name	������Ŀ����
chargenum	��������
chargemoney	������
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
