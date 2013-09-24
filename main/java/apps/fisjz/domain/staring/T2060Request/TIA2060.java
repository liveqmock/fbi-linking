package apps.fisjz.domain.staring.T2060Request;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.SeperatedTextMessage;

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
    private String finorg;  //财政编码

    @DataField(seq = 3)
    private String totalNum; //记录数

    @DataField(seq = 4)
    private String totalAmt; //总金额


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

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }
}
