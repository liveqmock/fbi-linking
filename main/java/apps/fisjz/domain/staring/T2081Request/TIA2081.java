package apps.fisjz.domain.staring.T2081Request;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.SeperatedTextMessage;

/**
 * Created with IntelliJ IDEA.
 * User: Lin Yong
 * Date: 13-10-14
 * Time: ����10:50
 */
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class TIA2081 {
    @DataField(seq = 1)
    private String exportType;    //�嵥����

    @DataField(seq = 2)
    private String areaCode;    //��������

    @DataField(seq = 3)
    private String startDate;   //��ʼ����

    @DataField(seq = 4)
    private String endDate;     //��ֹ����

    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "TIA2081{" +
                "exportType='" + exportType + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
