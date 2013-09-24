package apps.fisjz.domain.financebureau;


import java.io.Serializable;

public class FbPaynotesInfo4Cancel implements Serializable {
    private String banknum;    //手工票：交易流水  银行交易流水号 （手工票时不为空）
    private String paynotescode; //缴款书单号
    private String notescode; //票据编号
    private String checkcode;  //验证码     一般缴款收（手工）业务可为空
    private String amt; //总金额      16位整数2小数
    private String noteskindcode; //票据信息编码
    private String noteskinkname; //票据信息名称
    private String agentbankcode;  //代收银行编码
    private String agentbankname;  //代收银行名称
    private String canceldate;  //银行冲销日期
    private String billtype;  //单据类型/业务类型
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

    public String getCheckcode() {
        return checkcode;
    }

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
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
        return "FbPaynotesInfo4Cancel{" +
                "banknum='" + banknum + '\'' +
                ", paynotescode='" + paynotescode + '\'' +
                ", notescode='" + notescode + '\'' +
                ", checkcode='" + checkcode + '\'' +
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
