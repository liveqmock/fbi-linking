package apps.fis.domain.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import apps.fis.domain.base.Toa;
import apps.fis.domain.base.ToaFisHeader;

import java.io.Serializable;

/**
 * 3.4.3.2	���Ӷ��˷���
 */

@XStreamAlias("ROOT")
public class Toa3048 extends Toa {
    public ToaFisHeader HEAD = new ToaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        public Data DATA = new Data();
    }

    public static class Data implements Serializable {
        /*
        DZSBWJM ������������Ķ���ʧ����ϸ��Ϣ�ļ����ƣ��ļ�����������Ϊ��30480120100608134530(���ܺ�+�������+����YYYYMMDD +ʱ��HHMMSS)���ļ��������ظ���
        */
        public String DZSBWJM = "";
    }

    @Override
    public String toString() {
        this.HEAD.CODE = "3048";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa3048.class);
        return xs.toXML(this);
    }

    @Override
    public Toa3048 getToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa3048.class);
        return (Toa3048) xs.fromXML(xml);
    }
}
