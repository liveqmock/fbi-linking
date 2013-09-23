package apps.fisjz.domain.staring.T2010Response;


import common.dataformat.annotation.DataField;
import common.dataformat.annotation.Link;
import common.dataformat.annotation.OneToMany;
import common.dataformat.annotation.SeperatedTextMessage;

import java.util.List;

@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class TOA2010 {
    @Link
    private TOA2010PaynotesInfo paynotesInfo;

    //add
    @DataField(seq = 24)
    private String  itemNum;

    @DataField(seq = 25)
    @OneToMany(mappedTo = "apps.fisjz.domain.staring.T1532010.TOA2010PaynotesItem", totalNumberField = "itemNum")
    private List<TOA2010PaynotesItem> paynotesItems;


    public TOA2010PaynotesInfo getPaynotesInfo() {
        return paynotesInfo;
    }

    public void setPaynotesInfo(TOA2010PaynotesInfo paynotesInfo) {
        this.paynotesInfo = paynotesInfo;
    }

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public List<TOA2010PaynotesItem> getPaynotesItems() {
        return paynotesItems;
    }

    public void setPaynotesItems(List<TOA2010PaynotesItem> paynotesItems) {
        this.paynotesItems = paynotesItems;
    }
}
