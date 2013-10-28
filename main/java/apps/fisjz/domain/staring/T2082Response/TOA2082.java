package apps.fisjz.domain.staring.T2082Response;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.OneToMany;
import common.dataformat.annotation.SeperatedTextMessage;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lin Yong
 * Date: 13-10-14
 * Time: ÉÏÎç11:10
 */
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class TOA2082 {
    @DataField(seq = 1)
    private String  itemNum;

    @DataField(seq = 2)
    @OneToMany(mappedTo = "apps.fisjz.domain.staring.T2082Response.TOA2082Detail", totalNumberField = "itemNum")
    private List<TOA2082Detail> details;

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public List<TOA2082Detail> getDetails() {
        return details;
    }

    public void setDetails(List<TOA2082Detail> details) {
        this.details = details;
    }
}
