package apps.fisjz.domain.staring.T2013Request;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.Link;
import common.dataformat.annotation.OneToMany;
import common.dataformat.annotation.SeperatedTextMessage;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 * Time: 下午4:19
 * To change this template use File | Settings | File Templates.
 */
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class TIA2013 {
    @DataField(seq = 1)
    private String year;    //年度

    @DataField(seq = 2)
    private String finorg;  //财政编码

    @Link
    private TIA2013PaynotesInfo paynotesInfo;

    @DataField(seq = 30)
    private String  itemNum;

    @DataField(seq = 31)
    @OneToMany(mappedTo = "apps.fisjz.domain.staring.T2013Request.TIA2013PaynotesItem", totalNumberField = "itemNum")
    private List<TIA2013PaynotesItem> paynotesItems;


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFinorg() {
        return finorg;
    }

    public void setFinorg(String finorg) {
        this.finorg = finorg;
    }

    public TIA2013PaynotesInfo getPaynotesInfo() {
        return paynotesInfo;
    }

    public void setPaynotesInfo(TIA2013PaynotesInfo paynotesInfo) {
        this.paynotesInfo = paynotesInfo;
    }

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public List<TIA2013PaynotesItem> getPaynotesItems() {
        return paynotesItems;
    }

    public void setPaynotesItems(List<TIA2013PaynotesItem> paynotesItems) {
        this.paynotesItems = paynotesItems;
    }

    @Override
    public String toString() {
        return "TIA2013{" +
                "year='" + year + '\'' +
                ", finorg='" + finorg + '\'' +
                ", paynotesInfo=" + paynotesInfo +
                ", itemNum='" + itemNum + '\'' +
                ", paynotesItems=" + paynotesItems +
                '}';
    }
}
