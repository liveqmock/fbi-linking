package apps.fisjz.domain.staring.T2010Request;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.SeperatedTextMessage;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 * Time: ����4:19
 * To change this template use File | Settings | File Templates.
 */
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class TIA2010 {
    @DataField(seq = 1)
    private String year;    //���

    @DataField(seq = 2)
    private String areacode;  //��������

    @DataField(seq = 3)
    private String notescode; //Ʊ�ݱ��

    @DataField(seq = 4)
    private String checkcode; //��֤��

    @DataField(seq = 5)
    private String billtype;  //��������/ҵ������

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

    public String getNotescode() {
        return notescode;
    }

    public void setNotescode(String notescode) {
        this.notescode = notescode;
    }

    public String getCheckcode() {
        return checkcode;
    }

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

    public String getBilltype() {
        return billtype;
    }

    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }
}
