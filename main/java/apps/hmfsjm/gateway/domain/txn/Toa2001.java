package apps.hmfsjm.gateway.domain.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
import apps.hmfsjm.gateway.domain.base.Toa;
import apps.hmfsjm.gateway.domain.base.xml.ToaHeader;

import java.io.Serializable;

/**
 * ά���ʽ�-�ɿ�ȷ��
 */

@XStreamAlias("HMROOT")
public class Toa2001 extends Toa {

    public ToaHeader INFO = new ToaHeader();
    public Body BODY = new Body();

    public static class Body  implements Serializable {
        /*
          PAY_BILLNO	�ɴ�֪ͨ����
          BILL_STS_CODE	�ɿ�������
          BILL_STS_TITLE	�ɿ���˵��
          RESERVE	������
         */
            public String PAY_BILLNO = "";
            public String BILL_STS_CODE = "";
            public String BILL_STS_TITLE = "";
            public String RESERVE = "";
    }



    @Override
    public Toa toToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa2001.class);
        return (Toa2001) xs.fromXML(xml);
    }
}