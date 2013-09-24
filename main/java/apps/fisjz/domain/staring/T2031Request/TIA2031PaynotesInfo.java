package apps.fisjz.domain.staring.T2031Request;


import common.dataformat.annotation.DataField;
import common.dataformat.annotation.SeperatedTextMessage;

@SeperatedTextMessage(separator = "\\|")
public class TIA2031PaynotesInfo {
    @DataField(seq = 3)
    private String banknum;  //交易流水
    @DataField(seq = 4)
    private String billid;  //序列
    @DataField(seq = 5)
    private String refundapplycode; //退付申请编号
    @DataField(seq = 6)
    private String paynotescode; //原缴款书单号
    @DataField(seq = 7)
    private String notescode; //票据编号
    @DataField(seq = 8)
    private String fromnotescode; //原票据编号
    @DataField(seq = 9)
    private String amt; //总金额      16位整数2小数
    @DataField(seq = 10)
    private String noteskindcode; //票据信息编码
    @DataField(seq = 11)
    private String noteskinkname; //票据信息名称
    @DataField(seq = 12)
    private String agentbankcode;  //代收银行编码
    @DataField(seq = 13)
    private String agentbankname;  //代收银行名称
    @DataField(seq = 14)
    private String bankrecdate;  //手工票：银行收款日期  yyyymmdd
    @DataField(seq = 15)
    private String bankacctdate;   //手工票：银行记账日期  yyyymmdd
    @DataField(seq = 16)
    private String ispreaudit;     //手工票：预审标志  0 未审核；1已审核
    @DataField(seq = 17)
    private String recfeeflag;    //手工票：到账标志  0 未到账；1已到账

    //=============

    public String getBanknum() {
        return banknum;
    }

    public void setBanknum(String banknum) {
        this.banknum = banknum;
    }

    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public String getRefundapplycode() {
        return refundapplycode;
    }

    public void setRefundapplycode(String refundapplycode) {
        this.refundapplycode = refundapplycode;
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

    public String getFromnotescode() {
        return fromnotescode;
    }

    public void setFromnotescode(String fromnotescode) {
        this.fromnotescode = fromnotescode;
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

    public String getBankrecdate() {
        return bankrecdate;
    }

    public void setBankrecdate(String bankrecdate) {
        this.bankrecdate = bankrecdate;
    }

    public String getBankacctdate() {
        return bankacctdate;
    }

    public void setBankacctdate(String bankacctdate) {
        this.bankacctdate = bankacctdate;
    }

    public String getIspreaudit() {
        return ispreaudit;
    }

    public void setIspreaudit(String ispreaudit) {
        this.ispreaudit = ispreaudit;
    }

    public String getRecfeeflag() {
        return recfeeflag;
    }

    public void setRecfeeflag(String recfeeflag) {
        this.recfeeflag = recfeeflag;
    }

    @Override
    public String toString() {
        return "TIA2031PaynotesInfo{" +
                "banknum='" + banknum + '\'' +
                ", billid='" + billid + '\'' +
                ", refundapplycode='" + refundapplycode + '\'' +
                ", paynotescode='" + paynotescode + '\'' +
                ", notescode='" + notescode + '\'' +
                ", fromnotescode='" + fromnotescode + '\'' +
                ", amt='" + amt + '\'' +
                ", noteskindcode='" + noteskindcode + '\'' +
                ", noteskinkname='" + noteskinkname + '\'' +
                ", agentbankcode='" + agentbankcode + '\'' +
                ", agentbankname='" + agentbankname + '\'' +
                ", bankrecdate='" + bankrecdate + '\'' +
                ", bankacctdate='" + bankacctdate + '\'' +
                ", ispreaudit='" + ispreaudit + '\'' +
                ", recfeeflag='" + recfeeflag + '\'' +
                '}';
    }
}
