package apps.fiskfq.gateway.domain.txn;

import apps.fiskfq.gateway.domain.base.Toa;
import apps.fiskfq.gateway.domain.base.xml.ToaHeader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.Serializable;

/**
 * 通用收妥回执
 */

@XStreamAlias("Root")
public class Toa9000 extends Toa {
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
        ori_datatype	原数据类型	NString	4		M
        ori_send_orgcode	原发起方编码	NString	[1,15]		M
        ori_entrust_date	原委托日期	Date		请求发起日期	M
        Result	公共处理结果	String	4		M
        add_word	附言	GBString	[1,60]		O
         */
        public String ori_datatype = "";
        public String ori_send_orgcode = "";
        public String ori_entrust_date = "";
        public String Result = "";
        public String add_word = "";
    }

    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa9000.class);
        return (Toa9000) xs.fromXML(xml);
    }
}
