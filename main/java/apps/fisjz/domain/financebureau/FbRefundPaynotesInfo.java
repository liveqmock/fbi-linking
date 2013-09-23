package apps.fisjz.domain.financebureau;

import java.io.Serializable;

/**
 * 退付票
 */
public class FbRefundPaynotesInfo implements Serializable {
    private String billid;  //序列
    private String refundapplycode; //申请书单号
    private String paynotescode; //原缴款书单号
    private String notescode; //票据编号
    private String fromnotescode;   //退付票：原票据编号 (退付票时不为空)
    private String amt; //总金额      16位整数2小数
    private String noteskindcode; //票据信息编码
    private String noteskinkname; //票据信息名称
    private String performdeptcode; //执收单位编码
    private String performdeptname; //执收单位名称
    private String printdate;  //打票日期
    private String agentbankcode;  //代收银行编码
    private String agentbankname;  //代收银行名称

    private String recusername;   //领款人名称
    private String recuserbank;   //领款人开户行
    private String recuserbankaccount;   //领款人账号
    private String refundreason;   //退付原因

    private String remark;   //备注
    private String createtime;    //制单日期 yyyymmdd
    private String creater;    //手工票：制单人  （手工票时不为空）
    private String bankrecdate;  //手工票：银行收款日期  yyyymmdd
    private String bankacctdate;   //手工票：银行记账日期  yyyymmdd
    private String ispreaudit;     //手工票：预审标志  0 未审核；1已审核
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
        return "FbRefundPaynotesInfo{" +
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
