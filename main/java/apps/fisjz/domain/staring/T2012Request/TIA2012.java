package apps.fisjz.domain.staring.T2012Request;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.Link;
import common.dataformat.annotation.SeperatedTextMessage;

/**
 * User: zhanrui
 * Date: 13-9-22
 * Time: 下午4:19
 */
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class TIA2012 {
    @DataField(seq = 1)
    private String year;    //年度

    @DataField(seq = 2)
    private String areacode;  //财政编码

    @Link
    private TIA2012PaynotesInfo paynotesInfo;

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

    public TIA2012PaynotesInfo getPaynotesInfo() {
        return paynotesInfo;
    }

    public void setPaynotesInfo(TIA2012PaynotesInfo paynotesInfo) {
        this.paynotesInfo = paynotesInfo;
    }

    @Override
    public String toString() {
        return "TIA2012{" +
                "year='" + year + '\'' +
                ", areacode='" + areacode + '\'' +
                ", paynotesInfo=" + paynotesInfo +
                '}';
    }
}
