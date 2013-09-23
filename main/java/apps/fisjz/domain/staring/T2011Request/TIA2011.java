package apps.fisjz.domain.staring.T2011Request;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.Link;
import common.dataformat.annotation.SeperatedTextMessage;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 * Time: 下午4:19
 * To change this template use File | Settings | File Templates.
 */
@SeperatedTextMessage(separator = "\\|")
public class TIA2011 {
    @DataField(seq = 1)
    private String year;    //年度

    @DataField(seq = 2)
    private String finorg;  //财政编码

    @Link
    private  PaynotesInfo paynotesInfo;

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

    public PaynotesInfo getPaynotesInfo() {
        return paynotesInfo;
    }

    public void setPaynotesInfo(PaynotesInfo paynotesInfo) {
        this.paynotesInfo = paynotesInfo;
    }
}
