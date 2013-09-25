package apps.fisjz.domain.staring.T2060Request;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.OneToMany;
import common.dataformat.annotation.SeperatedTextMessage;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 */
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class TIA2060 {
    @DataField(seq = 1)
    private String year;    //年度

    @DataField(seq = 2)
    private String areacode;  //财政编码

    @DataField(seq = 3)
    private String totalamt; //总金额

    @DataField(seq = 4)
    private String startdate; //开始日期

    @DataField(seq = 5)
    private String enddate; //结束日期

    @DataField(seq = 6)
    private String  itemNum;

    @DataField(seq = 7)
    @OneToMany(mappedTo = "apps.fisjz.domain.staring.T2060Request.TIA2060Detail", totalNumberField = "itemNum")
    private List<TIA2060Detail> details;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAreacode() {
        return areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getTotalamt() {
        return totalamt;
    }

    public void setTotalamt(String totalamt) {
        this.totalamt = totalamt;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getItemNum() {
        return itemNum;
    }

    public void setItemNum(String itemNum) {
        this.itemNum = itemNum;
    }

    public List<TIA2060Detail> getDetails() {
        return details;
    }

    public void setDetails(List<TIA2060Detail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "TIA2060{" +
                "year='" + year + '\'' +
                ", areacode='" + areacode + '\'' +
                ", totalamt='" + totalamt + '\'' +
                ", startdate='" + startdate + '\'' +
                ", enddate='" + enddate + '\'' +
                ", itemNum='" + itemNum + '\'' +
                ", details=" + details +
                '}';
    }
}
