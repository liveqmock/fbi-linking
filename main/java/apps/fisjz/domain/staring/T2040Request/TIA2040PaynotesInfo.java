package apps.fisjz.domain.staring.T2040Request;


import common.dataformat.annotation.DataField;
import common.dataformat.annotation.SeperatedTextMessage;

@SeperatedTextMessage(separator = "\\|")
public class TIA2040PaynotesInfo {

    @DataField(seq = 3)
    private String banknum;    //手工票：交易流水  银行交易流水号 （手工票时不为空）
    @DataField(seq = 4)
    private String paynotescode; //缴款书单号
    @DataField(seq = 5)
    private String notescode; //票据编号
    @DataField(seq = 6)
    private String amt; //总金额      16位整数2小数
    @DataField(seq = 7)
    private String noteskindcode; //票据信息编码
    @DataField(seq = 8)
    private String noteskinkname; //票据信息名称
    @DataField(seq = 9)
    private String agentbankcode;  //代收银行编码
    @DataField(seq = 10)
    private String agentbankname;  //代收银行名称
    @DataField(seq = 11)
    private String canceldate;  //银行冲销日期
    @DataField(seq = 12)
    private String billtype;  //单据类型/业务类型    3

    //=======================================================================

    public String getBanknum() {
        return banknum;
    }

    public void setBanknum(String banknum) {
        this.banknum = banknum;
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

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getNoteskindcode() {
        return noteskindcode;
    }

    public void setNoteskindcode(String noteskindcode) {
        this.noteskindcode = noteskindcode;
    }

    public String getNoteskinkname() {
        return noteskinkname;
    }

    public void setNoteskinkname(String noteskinkname) {
        this.noteskinkname = noteskinkname;
    }

    public String getAgentbankcode() {
        return agentbankcode;
    }

    public void setAgentbankcode(String agentbankcode) {
        this.agentbankcode = agentbankcode;
    }

    public String getAgentbankname() {
        return agentbankname;
    }

    public void setAgentbankname(String agentbankname) {
        this.agentbankname = agentbankname;
    }

    public String getCanceldate() {
        return canceldate;
    }

    public void setCanceldate(String canceldate) {
        this.canceldate = canceldate;
    }

    public String getBilltype() {
        return billtype;
    }

    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }

    @Override
    public String toString() {
        return "TIA2040PaynotesInfo{" +
                "banknum='" + banknum + '\'' +
                ", paynotescode='" + paynotescode + '\'' +
                ", notescode='" + notescode + '\'' +
                ", amt='" + amt + '\'' +
                ", noteskindcode='" + noteskindcode + '\'' +
                ", noteskinkname='" + noteskinkname + '\'' +
                ", agentbankcode='" + agentbankcode + '\'' +
                ", agentbankname='" + agentbankname + '\'' +
                ", canceldate='" + canceldate + '\'' +
                ", billtype='" + billtype + '\'' +
                '}';
    }
}
