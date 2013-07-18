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
 *3.4.12.1	��������Ϣ�ϴ�����
 */

@XStreamAlias("ROOT")
public class Tia3004 extends Tia {
    public TiaFisHeader HEAD = new TiaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        public Data DATA = new Data();
    }

    public static class Data implements Serializable {
        public String XZQH = "";                         // ��������	C6	NO	YES	�����������룬�����ұ�׼����
        public String BMKYWXH = "";                      // ������ҵ�����	C50	YES	����˴���ƱΪ֮ǰ������ת��ɵģ����ֶζ�ӦΪ֮ǰ�������ҵ����š�
        public String CZZHZH = "";                       // ����ר���˺�	C40	NO
        public String JKFS = "";                         // �ɿʽ	C3	NO		03���04ת��֧Ʊ05�ֽ�ͨ��
        public String YT = "";                           // ��;	C100	YES
        public String JYRQ = "";                         // ��������	DATE	NO
        public String JE = "";                           // ���	N12,4	NO
        public String BYZD1 = "";                        // �����ֶ�1	C100	YES
        public String BYZD2 = "";                        // �����ֶ�2	C100	YES
        public String BYZD3 = "";                        // �����ֶ�3	C100	YES
        public String BYZD4 = "";                        // �����ֶ�4	C100	YES
        public String BYZD5 = "";                        // �����ֶ�5	C100	YES
        public String BYZD6 = "";                        // �����ֶ�6	C100	YES
        public String BYZD7 = "";                        // �����ֶ�7	C100	YES

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
