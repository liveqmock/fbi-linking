package apps.fis.domain.base;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

public class ToaFisHeader implements Serializable {
    @XStreamAsAttribute
    public String TYPE = "1";                      // TYPE=”1” 代表交互数据为反馈类型
    @XStreamAsAttribute
    public String CODE = "";                       // 交易码
    public String STATUS = "";                     // 状态
    public String ERRCODE = "";                    // 错误码
    public String MESSAGE = "";                    // 信息
}
