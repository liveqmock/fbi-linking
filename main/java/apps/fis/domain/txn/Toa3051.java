package apps.fis.domain.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import apps.fis.domain.base.Toa;
import apps.fis.domain.base.ToaFisHeader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 3.4.10.2	����Ʊ��Ʊ��Ϣ����
 */

@XStreamAlias("ROOT")
public class Toa3051 extends Toa {
    public ToaFisHeader HEAD = new ToaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {
        @XStreamImplicit
        public List<Data> dataList = new ArrayList<Data>();

        @XStreamAlias("DATA")
        public static class Data implements Serializable {
            public String DBPYWXH = "";                 // ����Ʊҵ�����
            public String PJZL = "";                    // �²�Ʊ��Ʊ������
            public String XBPH = "";                    // XBPHΪ�²�Ʊ�ţ�����ƱƱ�����Ϻ����²�Ʊ��Ʊ�ݱ�š�������ƱƱ�����Ϻ�δ���²�Ʊ�����²�Ʊ��Ϊ�գ�
        }
    }



    @Override
    public String toString() {
        this.HEAD.CODE = "3051";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa3051.class);
        return xs.toXML(this);
    }

    @Override
    public Toa3051 getToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa3051.class);
        return (Toa3051) xs.fromXML(xml);
    }
}
