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
 *3.4.10.1	����Ʊ��Ʊ��Ϣ����
 */

@XStreamAlias("ROOT")
public class Tia3027 extends Tia {
    public TiaFisHeader HEAD = new TiaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        public Data DATA = new Data();
    }

    public static class Data implements Serializable {
        public String XZQH = "";                         // ��������	C6	NO	YES	�����������룬�����ұ�׼����
        /*
        6��	SQRQ��DBPYWXH����ѡһ��ȫѡ��������ȫΪ�գ����磺ѡ���������ڣ�����������Ӧ���ڵ����в�Ʊ��Ϣ��ѡ��ҵ����ţ�����������Ӧҵ����ŵĲ�Ʊ��Ϣ��
         */
        public String SQRQ = "";                         // ��������
        public String DBPYWXH = "";                      // ҵ�����	C50	NO	YES	����Ʊ��ΨһID
    }

    @Override
    public String toString() {
        this.HEAD.CODE = "3027";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Tia3027.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + xs.toXML(this);
    }

    @Override
    public Tia3027 getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Tia3027.class);
        return (Tia3027) xs.fromXML(xml);
    }
}
