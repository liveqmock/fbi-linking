package apps.fiskfq.gateway.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-12-23
 * Time: ÉÏÎç10:31
 * To change this template use File | Settings | File Templates.
 */
@XStreamAlias("attribute")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"content"})
public class Attribute {
    @XStreamAsAttribute
    public String name = new String();
    @XStreamAsAttribute
    public String description = new String();
    public String content = "";

    public Attribute(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
