package apps.fiskfq.gateway.domain.txn;

import apps.fiskfq.gateway.domain.base.Toa;
import apps.fiskfq.gateway.domain.base.xml.Signs;
import apps.fiskfq.gateway.domain.base.xml.ToaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Serializable;

/**
 * 手工票录入回执
 */

@XStreamAlias("Root")
public class Toa1457 extends Toa {
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
   chr_id	缴款书ID
billtype_code	缴款书样式编码
billtype_name	缴款书样式名称
bill_no	票号
verify_no	缴款书全票面校验码
makedate	开票日期
ien_code	执收单位业务码
ien_name	执收单位名称
set_year	年度
succ_code	OK


     */
    public static class Record implements Serializable {

        public String chr_id = "";
        public String billtype_code = "";
        public String billtype_name = "";
        public String bill_no = "";
        public String verify_no = "";
        public String makedate = "";
        public String ien_code = "";
        public String ien_name = "";
        public String set_year = "";
        public String succ_code = "";
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa1457.class);
        return (Toa1457) xs.fromXML(xml);
    }

}
