package apps.fisjz.domain.staring.T2010Response;


import common.dataformat.annotation.DataField;
import common.dataformat.annotation.Link;
import common.dataformat.annotation.OneToMany;
import common.dataformat.annotation.SeperatedTextMessage;

import java.util.List;

@SeperatedTextMessage(separator = "\\|")
public class TOA2010 {
    @Link
    private  PaynotesInfo paynotesInfo;

    //add
    @DataField(seq = 24)
    private String  itemNum;

    @DataField(seq = 25)
    @OneToMany(mappedTo = "apps.fisjz.domain.staring.T1532010.PaynotesItem", totalNumberField = "itemNum")
    private List<PaynotesItem> paynotesItems;


    public PaynotesInfo getPaynotesInfo() {
        return paynotesInfo;
    }

    public void setPaynotesInfo(PaynotesInfo paynotesInfo) {
        this.paynotesInfo = paynotesInfo;
    }

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public List<PaynotesItem> getPaynotesItems() {
        return paynotesItems;
    }

    public void setPaynotesItems(List<PaynotesItem> paynotesItems) {
        this.paynotesItems = paynotesItems;
    }
}
