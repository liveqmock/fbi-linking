package apps.fiskfq.gateway.domain.base.xml;

import apps.fiskfq.PropertyManager;

import java.io.Serializable;

public class TiaHeader implements Serializable {
    public String src = PropertyManager.getProperty("src.code");                   // 发送方编码
    public String des = PropertyManager.getProperty("des.code");                   // 接收方编码
    public String dataType = "";
    public String msgId = "";                 // 报文标识号
    public String msgRef = "";                // 报文参考号  发起请求报文时报文参考号同报文标识号
    public String workDate = "";              // 工作日期
}
