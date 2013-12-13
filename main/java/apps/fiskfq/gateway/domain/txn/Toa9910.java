package apps.fiskfq.gateway.domain.txn;

import apps.fiskfq.gateway.domain.base.Toa;
import apps.fiskfq.gateway.domain.base.xml.ToaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Serializable;

/**
 * 通用技术应答
 */

@XStreamAlias("Root")
public class Toa9910 extends Toa {
    public ToaHeader Head = new ToaHeader();
    public Body Body = new Body();


    public static class Body implements Serializable {

        public Object Object = new Object();
    }

    public static class Object implements Serializable {

        public Record Record = new Record();
    }


    public static class Record implements Serializable {

        public String ori_datatype = "";
        public String result = "";
        public String add_word = "";
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa9910.class);
        return (Toa9910) xs.fromXML(xml);
    }

}
