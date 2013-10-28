package apps.fisjz.domain.staring.T2081Response;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.OneToMany;
import common.dataformat.annotation.SeperatedTextMessage;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lin Yong
 * Date: 13-10-14
 * Time: ÉÏÎç11:04
 */
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class TOA2081 {
    @DataField(seq = 1)
    private String  itemNum;

    @DataField(seq = 2)
    @OneToMany(mappedTo = "apps.fisjz.domain.staring.T2081Response.TOA2081Detail", totalNumberField = "itemNum")
    private List<TOA2081Detail> details;

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public List<TOA2081Detail> getDetails() {
        return details;
    }

    public void setDetails(List<TOA2081Detail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "TOA2081{" +
                "itemNum='" + itemNum + '\'' +
                ", details=" + details +
                '}';
    }
}
