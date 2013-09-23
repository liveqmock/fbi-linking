package apps.fisjz.domain.staring.T2013Request;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.SeperatedTextMessage;

@SeperatedTextMessage(separator = "\\|")
public class TIA2013PaynotesInfo {
    @DataField(seq = 3)
    private String banknum;    //手工票：交易流水  银行交易流水号 （手工票时不为空）
    @DataField(seq = 4)
    private String notescode; //票据编号
    @DataField(seq = 5)
    private String checkcode;  //验证码     一般缴款收（手工）业务可为空
    @DataField(seq = 6)
    private String amt; //总金额      16位整数2小数
    @DataField(seq = 7)
    private String noteskindcode; //票据信息编码
    @DataField(seq = 8)
    private String noteskinkname; //票据信息名称
    @DataField(seq = 9)
    private String performdeptcode; //执收单位编码
    @DataField(seq = 10)
    private String performdeptname; //执收单位名称
    @DataField(seq = 11)
    private String printdate;  //打票日期
    @DataField(seq = 12)
    private String agentbankcode;  //代收银行编码
    @DataField(seq = 13)
    private String agentbankname;  //代收银行名称
    @DataField(seq = 14)
    private String payfeemethodcode;  //缴款方式编码      1现金；2转账；
    @DataField(seq = 15)
    private String payfeemethodname;  //缴款方式名称
    @DataField(seq = 16)
    private String paymethodcode;  //缴库方式编码     0 直接缴库；1 集中汇缴库；
    @DataField(seq = 17)
    private String paymethodname;  //缴库方式名称
    @DataField(seq = 18)
    private String payer;  //缴款人
    @DataField(seq = 19)
    private String payerbank;  //缴款人开户行
    @DataField(seq = 20)
    private String payerbankacct; //缴款人账号
    @DataField(seq = 21)
    private String remark;   //备注
    @DataField(seq = 22)
    private String createtime;    //制单日期 yyyymmdd
    @DataField(seq = 23)
    private String creater;    //手工票：制单人  （手工票时不为空）
    @DataField(seq = 24)
    private String bankrecdate;  //手工票：银行收款日期  yyyymmdd
    @DataField(seq = 25)
    private String bankacctdate;   //手工票：银行记账日期  yyyymmdd
    @DataField(seq = 26)
    private String ispreaudit;     //手工票：预审标志  0 未审核；1已审核
    @DataField(seq = 27)
    private String recfeeflag;    //手工票：到账标志  0 未到账；1已到账
    @DataField(seq = 28)
    private String billtype;  //单据类型/业务类型
    @DataField(seq = 29)
    private String latefee;    //滞纳金 LATEFEE 16位整数2小数

    //=======================================================================

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

    public String getPerformdeptcode() {
        return performdeptcode;
    }

    public void setPerformdeptcode(String performdeptcode) {
        this.performdeptcode = performdeptcode;
    }

    public String getPerformdeptname() {
        return performdeptname;
    }

    public void setPerformdeptname(String performdeptname) {
        this.performdeptname = performdeptname;
    }

    public String getPrintdate() {
        return printdate;
    }

    public void setPrintdate(String printdate) {
        this.printdate = printdate;
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

    public String getPayfeemethodcode() {
        return payfeemethodcode;
    }

    public void setPayfeemethodcode(String payfeemethodcode) {
        this.payfeemethodcode = payfeemethodcode;
    }

    public String getPayfeemethodname() {
        return payfeemethodname;
    }

    public void setPayfeemethodname(String payfeemethodname) {
        this.payfeemethodname = payfeemethodname;
    }

    public String getPaymethodcode() {
        return paymethodcode;
    }

    public void setPaymethodcode(String paymethodcode) {
        this.paymethodcode = paymethodcode;
    }

    public String getPaymethodname() {
        return paymethodname;
    }

    public void setPaymethodname(String paymethodname) {
        this.paymethodname = paymethodname;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getPayerbank() {
        return payerbank;
    }

    public void setPayerbank(String payerbank) {
        this.payerbank = payerbank;
    }

    public String getPayerbankacct() {
        return payerbankacct;
    }

    public void setPayerbankacct(String payerbankacct) {
        this.payerbankacct = payerbankacct;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
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

    @Override
    public String toString() {
        return "TIA2013PaynotesInfo{" +
                "banknum='" + banknum + '\'' +
                ", notescode='" + notescode + '\'' +
                ", checkcode='" + checkcode + '\'' +
                ", amt='" + amt + '\'' +
                ", noteskindcode='" + noteskindcode + '\'' +
                ", noteskinkname='" + noteskinkname + '\'' +
                ", performdeptcode='" + performdeptcode + '\'' +
                ", performdeptname='" + performdeptname + '\'' +
                ", printdate='" + printdate + '\'' +
                ", agentbankcode='" + agentbankcode + '\'' +
                ", agentbankname='" + agentbankname + '\'' +
                ", payfeemethodcode='" + payfeemethodcode + '\'' +
                ", payfeemethodname='" + payfeemethodname + '\'' +
                ", paymethodcode='" + paymethodcode + '\'' +
                ", paymethodname='" + paymethodname + '\'' +
                ", payer='" + payer + '\'' +
                ", payerbank='" + payerbank + '\'' +
                ", payerbankacct='" + payerbankacct + '\'' +
                ", remark='" + remark + '\'' +
                ", createtime='" + createtime + '\'' +
                ", creater='" + creater + '\'' +
                ", bankrecdate='" + bankrecdate + '\'' +
                ", bankacctdate='" + bankacctdate + '\'' +
                ", ispreaudit='" + ispreaudit + '\'' +
                ", recfeeflag='" + recfeeflag + '\'' +
                ", billtype='" + billtype + '\'' +
                ", latefee='" + latefee + '\'' +
                '}';
    }
}
