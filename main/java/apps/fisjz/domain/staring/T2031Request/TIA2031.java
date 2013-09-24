package apps.fisjz.domain.staring.T2031Request;

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
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class TIA2031 {
    @DataField(seq = 1)
    private String year;    //年度

    @DataField(seq = 2)
    private String areacode;  //财政编码

    @Link
    private TIA2031PaynotesInfo paynotesInfo;

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

    public TIA2031PaynotesInfo getPaynotesInfo() {
        return paynotesInfo;
    }

    public void setPaynotesInfo(TIA2031PaynotesInfo paynotesInfo) {
        this.paynotesInfo = paynotesInfo;
    }

    @Override
    public String toString() {
        return "TIA2011{" +
                "year='" + year + '\'' +
                ", areacode='" + areacode + '\'' +
                ", paynotesInfo=" + paynotesInfo +
                '}';
    }
}
