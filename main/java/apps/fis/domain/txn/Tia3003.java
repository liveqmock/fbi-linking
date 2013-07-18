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
 * 3.4.3.1	���Ӷ�������
 */

@XStreamAlias("ROOT")
public class Tia3003 extends Tia {
    public TiaFisHeader HEAD = new TiaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        public Data DATA = new Data();
    }

    public static class Data implements Serializable {
        /*
        3��	DZWJMΪFTP�������ϴ�Ҫ�����ļ����ļ����������淶Ϊ��30030121000020090224131426(���ܺ�+�������+��������+����YYYYMMDD +ʱ��HHMMSS)���ļ��������ظ���
         */
        public String DZWJM = "";
    }

    @Override
    public String toString() {
        this.HEAD.CODE = "3003";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Tia3003.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + xs.toXML(this);
    }

    @Override
    public Tia getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Tia3003.class);
        return (Tia3003) xs.fromXML(xml);
    }
}
