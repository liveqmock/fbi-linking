package common.dataformat.samples.staringmodel.T1000;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.Link;
import common.dataformat.annotation.OneToMany;
import common.dataformat.annotation.SeperatedTextMessage;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-7
 */
@SeperatedTextMessage(separator = "\\|")
public class TIA1000 {

    @Link
    private Header header;

    @DataField(seq = 3)
    private String  id;

    @DataField(seq = 4)
    private String  name;

    @DataField(seq = 5)
    private String  itemNum;

    @DataField(seq = 6)
    @OneToMany(mappedTo = "common.dataformat.samples.staringmodel.T1000.Item", totalNumberField = "itemNum")
    private List<Item> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }
}
