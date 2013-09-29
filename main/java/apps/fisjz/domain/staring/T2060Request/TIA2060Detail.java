package apps.fisjz.domain.staring.T2060Request;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.OneToManySeperatedTextMessage;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 */
@OneToManySeperatedTextMessage(separator = ",")
public class TIA2060Detail {
    @DataField(seq = 1)
    private String banknum;    //银行流水

    @DataField(seq = 2)
    private String notescode;  //票据编号

    @DataField(seq = 3)
    private String amt; //总金额

    public String getBanknum() {
        return banknum;
    }

    public void setBanknum(String banknum) {
        this.banknum = banknum;
    }

    public String getNotescode() {
        return notescode;
    }

    public void setNotescode(String notescode) {
        this.notescode = notescode;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    @Override
    public String toString() {
        return "TIA2060Detail{" +
                "banknum='" + banknum + '\'' +
                ", notescode='" + notescode + '\'' +
                ", amt='" + amt + '\'' +
                '}';
    }
}
