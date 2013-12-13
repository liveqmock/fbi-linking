package apps.fiskfq.gateway.domain.txn;

import apps.fiskfq.gateway.domain.base.Toa;
import apps.fiskfq.gateway.domain.base.xml.ToaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Serializable;

/**
 * 退出回执
 */

@XStreamAlias("Root")
public class Toa9908 extends Toa {
    public ToaHeader Head = new ToaHeader();
    public Body Body = new Body();


    public static class Body implements Serializable {

        public Object Object = new Object();
    }

    public static class Object implements Serializable {

        public Record Record = new Record();
    }


    public static class Record implements Serializable {

        /*
        login_result	结果
        add_word	附言
         */
        public String login_result = "";
        public String add_word = "";
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa9908.class);
        return (Toa9908) xs.fromXML(xml);
    }
}
