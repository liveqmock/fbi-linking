package apps.fisjz.domain.staring.T2011Request;


import common.dataformat.annotation.DataField;
import common.dataformat.annotation.SeperatedTextMessage;

@SeperatedTextMessage(separator = "\\|")
public class TIA2011PaynotesInfo {
    @DataField(seq = 3)
    private String banknum;    //手工票：交易流水  银行交易流水号 （手工票时不为空）

    @DataField(seq = 4)
    private String billid;  //序列

    @DataField(seq = 5)
    private String paynotescode; //缴款书单号

    @DataField(seq = 6)
    private String notescode; //票据编号

    @DataField(seq = 7)
    private String checkcode;  //验证码     一般缴款收（手工）业务可为空

    @DataField(seq = 8)
    private String amt; //总金额      16位整数2小数

    @DataField(seq = 9)
    private String noteskindcode; //票据信息编码

    @DataField(seq = 10)
    private String noteskinkname; //票据信息名称

    @DataField(seq = 11)
    private String agentbankcode;  //代收银行编码

    @DataField(seq = 12)
    private String agentbankname;  //代收银行名称

    @DataField(seq = 17)
    private String billtype;  //单据类型/业务类型

    @DataField(seq = 18)
    private String latefee;    //滞纳金 LATEFEE 16位整数2小数

    @DataField(seq = 13)
    private String bankrecdate;  //手工票：银行收款日期  yyyymmdd

    @DataField(seq = 14)
    private String bankacctdate;   //手工票：银行记账日期  yyyymmdd

    @DataField(seq = 15)
    private String ispreaudit;     //手工票：预审标志  0 未审核；1已审核

    @DataField(seq = 16)
    private String recfeeflag;    //手工票：到账标志  0 未到账；1已到账

    
    @DataField(seq = 19)
    private String banknotescode;   //银行代开票号  (宁夏业务)如果业务类型为 3 缴款通知书，不可为空

    //=======================================================================

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

    public String getBilltype() {
        return billtype;
    }

    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }

    public String getLatefee() {
        return latefee;
    }

    public void setLatefee(String latefee) {
        this.latefee = latefee;
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

    public String getBanknotescode() {
        return banknotescode;
    }

    public void setBanknotescode(String banknotescode) {
        this.banknotescode = banknotescode;
    }

    @Override
    public String toString() {
        return "TIA2011PaynotesInfo{" +
                "banknum='" + banknum + '\'' +
                ", billid='" + billid + '\'' +
                ", paynotescode='" + paynotescode + '\'' +
                ", notescode='" + notescode + '\'' +
                ", checkcode='" + checkcode + '\'' +
                ", amt='" + amt + '\'' +
                ", noteskindcode='" + noteskindcode + '\'' +
                ", noteskinkname='" + noteskinkname + '\'' +
                ", agentbankcode='" + agentbankcode + '\'' +
                ", agentbankname='" + agentbankname + '\'' +
                ", billtype='" + billtype + '\'' +
                ", latefee='" + latefee + '\'' +
                ", bankrecdate='" + bankrecdate + '\'' +
                ", bankacctdate='" + bankacctdate + '\'' +
                ", ispreaudit='" + ispreaudit + '\'' +
                ", recfeeflag='" + recfeeflag + '\'' +
                ", banknotescode='" + banknotescode + '\'' +
                '}';
    }
}
