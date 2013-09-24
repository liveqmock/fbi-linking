package apps.fisjz.domain.staring.T2030Request;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.SeperatedTextMessage;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 */
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class TIA2030 {
    @DataField(seq = 1)
    private String year;    //���

    @DataField(seq = 2)
    private String areacode;  //��������

    @DataField(seq = 3)
    private String refundapplycode; //�˸��������

    @DataField(seq = 4)
    private String notescode; //Ʊ�ݱ���

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

    public String getRefundapplycode() {
        return refundapplycode;
    }

    public void setRefundapplycode(String refundapplycode) {
        this.refundapplycode = refundapplycode;
    }

    public String getNotescode() {
        return notescode;
    }

    public void setNotescode(String notescode) {
        this.notescode = notescode;
    }
}
