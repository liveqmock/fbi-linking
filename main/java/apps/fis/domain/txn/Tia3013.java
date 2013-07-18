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
 * 3.4.7.1	ȡ���տ�����
 */

@XStreamAlias("ROOT")
public class Tia3013 extends Tia {
    public TiaFisHeader HEAD = new TiaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        public Data DATA = new Data();
    }

    public static class Data implements Serializable {
        /*
        3��	SKPJZL Ϊ�տ�Ʊ�����࣬��7�ֵ����нɿ��Ʊ�����ࣨ�ݶ��������������һ
        4��	SKPJBH Ϊ�տ�Ʊ�ݱ��
        */
        public String SKPJZL = "";
        public String SKPJBH = "";
    }

    @Override
    public String toString() {
        this.HEAD.CODE = "3013";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Tia3013.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + xs.toXML(this);
    }

    @Override
    public Tia3013 getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Tia3013.class);
        return (Tia3013) xs.fromXML(xml);
    }
}
