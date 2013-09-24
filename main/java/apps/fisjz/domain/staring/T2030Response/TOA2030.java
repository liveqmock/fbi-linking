package apps.fisjz.domain.staring.T2030Response;


import common.dataformat.annotation.DataField;
import common.dataformat.annotation.Link;
import common.dataformat.annotation.OneToMany;
import common.dataformat.annotation.SeperatedTextMessage;

import java.util.List;

@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class TOA2030 {
    @Link
    private TOA2030PaynotesInfo paynotesInfo;

    @DataField(seq = 25)
    private String  itemNum;

    @DataField(seq = 26)
    @OneToMany(mappedTo = "apps.fisjz.domain.staring.T1532030.TOA2030PaynotesItem", totalNumberField = "itemNum")
    private List<TOA2030PaynotesItem> paynotesItems;


    public TOA2030PaynotesInfo getPaynotesInfo() {
        return paynotesInfo;
    }

    public void setPaynotesInfo(TOA2030PaynotesInfo paynotesInfo) {
        this.paynotesInfo = paynotesInfo;
    }

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public List<TOA2030PaynotesItem> getPaynotesItems() {
        return paynotesItems;
    }

    public void setPaynotesItems(List<TOA2030PaynotesItem> paynotesItems) {
        this.paynotesItems = paynotesItems;
    }

    @Override
    public String toString() {
        return "TOA2030{" +
                "paynotesInfo=" + paynotesInfo +
                ", itemNum='" + itemNum + '\'' +
                ", paynotesItems=" + paynotesItems +
                '}';
    }
}
