package apps.fiskfq.gateway.domain.txn;

import apps.fiskfq.gateway.domain.base.Toa;
import apps.fiskfq.gateway.domain.base.xml.Signs;
import apps.fiskfq.gateway.domain.base.xml.ToaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Serializable;

/**
 * 收款确认回执
 */

@XStreamAlias("Root")
public class Toa1402 extends Toa {
    public ToaHeader Head = new ToaHeader();
    public Body Body = new Body();
    public Signs Signs = new Signs();


    public static class Body implements Serializable {

        public Object Object = new Object();
    }

    public static class Object implements Serializable {

        public Record Record = new Record();
    }


    /*
    chr_id	等于请求缴款书ID
billtype_code	等于请求缴款书样式编码
bill_no	等于请求票号
set_year	等于请求年度
succ_code	OK

     */
    public static class Record implements Serializable {

        public String chr_id = "";
        public String billtype_code = "";
        public String bill_no = "";
        public String set_year = "";
        public String succ_code = "";
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa1402.class);
        return (Toa1402) xs.fromXML(xml);
    }

}
