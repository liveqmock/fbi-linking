package apps.fisjz.domain.staring.T2030Response;


import common.dataformat.annotation.DataField;
import common.dataformat.annotation.SeperatedTextMessage;

@SeperatedTextMessage(separator = "\\|")
public class TOA2030PaynotesInfo {
    @DataField(seq = 1)
    private String billid;  //序列
    @DataField(seq = 2)
    private String refundapplycode; //退付申请编号
    @DataField(seq = 3)
    private String paynotescode; //原缴款书单号
    @DataField(seq = 4)
    private String notescode; //票据编号
    @DataField(seq = 5)
    private String fromnotescode; //原票据编号
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
    private String recusername;   //领款人名称
    @DataField(seq = 15)
    private String recuserbank;   //领款人开户行
    @DataField(seq = 16)
    private String recuserbankaccount;   //领款人账号
    @DataField(seq = 17)
    private String refundreason;   //退付原因
    @DataField(seq = 18)
    private String remark;   //备注
    @DataField(seq = 19)
    private String createtime;    //制单日期 yyyymmdd
    @DataField(seq = 20)
    private String creater;    //手工票：制单人  （手工票时不为空）
    @DataField(seq = 21)
    private String bankrecdate;  //手工票：银行收款日期  yyyymmdd
    @DataField(seq = 22)
    private String bankacctdate;   //手工票：银行记账日期  yyyymmdd
    @DataField(seq = 23)
    private String ispreaudit;     //手工票：预审标志  0 未审核；1已审核
    @DataField(seq = 24)
    private String recfeeflag;    //手工票：到账标志  0 未到账；1已到账

    //=======================================================================

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

    public String getRecusername() {
        return recusername;
    }

    public void setRecusername(String recusername) {
        this.recusername = recusername;
    }

    public String getRecuserbank() {
        return recuserbank;
    }

    public void setRecuserbank(String recuserbank) {
        this.recuserbank = recuserbank;
    }

    public String getRecuserbankaccount() {
        return recuserbankaccount;
    }

    public void setRecuserbankaccount(String recuserbankaccount) {
        this.recuserbankaccount = recuserbankaccount;
    }

    public String getRefundreason() {
        return refundreason;
    }

    public void setRefundreason(String refundreason) {
        this.refundreason = refundreason;
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

    @Override
    public String toString() {
        return "TOA2030PaynotesInfo{" +
                "billid='" + billid + '\'' +
                ", refundapplycode='" + refundapplycode + '\'' +
                ", paynotescode='" + paynotescode + '\'' +
                ", notescode='" + notescode + '\'' +
                ", fromnotescode='" + fromnotescode + '\'' +
                ", amt='" + amt + '\'' +
                ", noteskindcode='" + noteskindcode + '\'' +
                ", noteskinkname='" + noteskinkname + '\'' +
                ", performdeptcode='" + performdeptcode + '\'' +
                ", performdeptname='" + performdeptname + '\'' +
                ", printdate='" + printdate + '\'' +
                ", agentbankcode='" + agentbankcode + '\'' +
                ", agentbankname='" + agentbankname + '\'' +
                ", recusername='" + recusername + '\'' +
                ", recuserbank='" + recuserbank + '\'' +
                ", recuserbankaccount='" + recuserbankaccount + '\'' +
                ", refundreason='" + refundreason + '\'' +
                ", remark='" + remark + '\'' +
                ", createtime='" + createtime + '\'' +
                ", creater='" + creater + '\'' +
                ", bankrecdate='" + bankrecdate + '\'' +
                ", bankacctdate='" + bankacctdate + '\'' +
                ", ispreaudit='" + ispreaudit + '\'' +
                ", recfeeflag='" + recfeeflag + '\'' +
                '}';
    }
}

