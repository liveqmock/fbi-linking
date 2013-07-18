package apps.fis.domain.base;

import apps.fis.SystemParameter;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

public class TiaFisHeader implements Serializable {
    @XStreamAsAttribute
    public String TYPE = "0";                                    //	TYPE=”0” 代表数据为请求类型
    @XStreamAsAttribute
    public String CODE = "";                                     // 交易码
    public String YHLB = SystemParameter.YHLB;                   // 银行类别
    public String USERNAME = SystemParameter.USERNAME;           // 用户名
    public String PASSWORD = SystemParameter.PASSWORD;           // 密码
}
