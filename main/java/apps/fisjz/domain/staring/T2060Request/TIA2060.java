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
    private String year;    //���

    @DataField(seq = 2)
    private String areacode;  //��������

    @DataField(seq = 3)
    private String totalNum; //��¼��

    @DataField(seq = 4)
    private String totalAmt; //�ܽ��

    @DataField(seq = 5)
    private String startdate; //��ʼ����

    @DataField(seq = 6)
    private String enddate; //��������


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
}
