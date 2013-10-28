package apps.fisjz.domain.staring.T2082Response;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.OneToManySeperatedTextMessage;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 */
@OneToManySeperatedTextMessage(separator = ",")
public class TOA2082Detail {
    @DataField(seq = 1)
    private String bankacctdate; //银行记账日期

    @DataField(seq = 2)
    private String performdeptname; //执收单位名称

    private String nontaxprogramcode;

    @DataField(seq = 3)
    private String nontaxprogramname;   //非税项目名称

    @DataField(seq = 4)
    private String performdeptcode;

    @DataField(seq = 5)
    private String amt; //收入金额

    @DataField(seq = 6)
    private String payfeemethodname;//缴款方式

    @DataField(seq = 7)
    private String payer;  //缴款人

    @DataField(seq = 8)
    private String notescode;//票据编号

    @DataField(seq = 9)
    private String operbankid; // 缴款网点

    @DataField(seq = 10)
    private String latefee; //滞纳金


    public String getPerformdeptname() {
        return performdeptname;
    }

    public void setPerformdeptname(String performdeptname) {
        this.performdeptname = performdeptname;
    }

    public String getNontaxprogramname() {
        return nontaxprogramname;
    }

    public void setNontaxprogramname(String nontaxprogramname) {
        this.nontaxprogramname = nontaxprogramname;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getLatefee() {
        return latefee;
    }

    public void setLatefee(String latefee) {
        this.latefee = latefee;
    }

    public String getPerformdeptcode() {
        return performdeptcode;
    }

    public void setPerformdeptcode(String performdeptcode) {
        this.performdeptcode = performdeptcode;
    }

    public String getNontaxprogramcode() {
        return nontaxprogramcode;
    }

    public void setNontaxprogramcode(String nontaxprogramcode) {
        this.nontaxprogramcode = nontaxprogramcode;
    }

    public String getBankacctdate() {
        return bankacctdate;
    }

    public void setBankacctdate(String bankacctdate) {
        this.bankacctdate = bankacctdate;
    }

    public String getPayfeemethodname() {
        return payfeemethodname;
    }

    public void setPayfeemethodname(String payfeemethodname) {
        this.payfeemethodname = payfeemethodname;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getNotescode() {
        return notescode;
    }

    public void setNotescode(String notescode) {
        this.notescode = notescode;
    }

    public String getOperbankid() {
        return operbankid;
    }

    public void setOperbankid(String operbankid) {
        this.operbankid = operbankid;
    }
}
