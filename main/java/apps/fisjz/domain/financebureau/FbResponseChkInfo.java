package apps.fisjz.domain.financebureau;

import java.io.Serializable;

public class FbResponseChkInfo implements Serializable {
    private String billid;  //序列
    private String paynotescode; //缴款书单号
    private String notescode; //票据编号

    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public String getPaynotescode() {
        return paynotescode;
    }

    public void setPaynotescode(String paynotescode) {
        this.paynotescode = paynotescode;
    }

    public String getNotescode() {
        return notescode;
    }

    public void setNotescode(String notescode) {
        this.notescode = notescode;
    }

    @Override
    public String toString() {
        return "FbResponseChkInfo{" +
                "billid='" + billid + '\'' +
                ", paynotescode='" + paynotescode + '\'' +
                ", notescode='" + notescode + '\'' +
                '}';
    }
}