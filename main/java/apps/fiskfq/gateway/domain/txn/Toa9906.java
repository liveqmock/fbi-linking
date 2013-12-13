package apps.fiskfq.gateway.domain.txn;

import apps.fiskfq.gateway.domain.base.Toa;
import apps.fiskfq.gateway.domain.base.xml.ToaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Serializable;

/**
 * µÇÂ¼»ØÖ´
 */

@XStreamAlias("Root")
public class Toa9906 extends Toa {
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
        login_result	µÇÂ¼½á¹û
        accredit_code	ÊÚÈ¨Âë
        add_word	¸½ÑÔ
         */
        public String login_result = "";
        public String accredit_code = "";
        public String add_word = "";
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa9906.class);
        return (Toa9906) xs.fromXML(xml);
    }
}
