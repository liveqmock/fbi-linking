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
 *3.4.11.1	����ƱƱ��������Ϣ����
 */

@XStreamAlias("ROOT")
public class Tia3051 extends Tia {
    public TiaFisHeader HEAD = new TiaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        public Data DATA = new Data();
    }

    public static class Data implements Serializable {
        public String XZQH = "";                         // ��������	C6	NO	YES	�����������룬�����ұ�׼����
        /*
        SQRQ��DBPYWXH����ѡһ��ȫѡ��������ȫΪ�գ���ҵ����Ų�Ϊ�գ����������ҵ����Ŷ�Ӧ�Ĵ���ƱƱ�����������Ϣ��
        ��ҵ�����Ϊ�ա��������ڲ�Ϊ�գ������������Ӧ���ڵĴ���ƱƱ��������Ϣ��
        ��������ÿ�ն���ǰ�����ڻ�ȡ����Ʊ������Ϣ��Ȼ���ϵͳ�������ϵĲ�ƱƱ����Ϣ�������ϻ�ɾ����         */
        public String SQRQ = "";                         // ��������
        public String DBPYWXH = "";                      // ҵ�����	C50	NO	YES	����Ʊ��ΨһID
    }

    @Override
    public String toString() {
        this.HEAD.CODE = "3051";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Tia3051.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + xs.toXML(this);
    }

    @Override
    public Tia3051 getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Tia3051.class);
        return (Tia3051) xs.fromXML(xml);
    }
}
