package apps.fisjz.domain.staring.T2011Request;


import common.dataformat.annotation.DataField;
import common.dataformat.annotation.SeperatedTextMessage;

@SeperatedTextMessage(separator = "\\|")
public class TIA2011PaynotesInfo {

    private String pkid;

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

    private String performdeptcode; //执收单位编码
    private String performdeptname; //执收单位名称
    private String printdate;  //打票日期

    @DataField(seq = 11)
    private String agentbankcode;  //代收银行编码

    @DataField(seq = 12)
    private String agentbankname;  //代收银行名称

    private String payfeemethodcode;  //缴款方式编码      1现金；2转账；
    private String payfeemethodname;  //缴款方式名称
    private String paymethodcode;  //缴库方式编码     0 直接缴库；1 集中汇缴库；
    private String paymethodname;  //缴库方式名称
    private String payer;  //缴款人
    private String payerbank;  //缴款人开户行
    private String payerbankacct; //缴款人账号
    private String remark;   //备注
    private String createtime;    //制单日期 yyyymmdd

    @DataField(seq = 17)
    private String billtype;  //单据类型/业务类型

    @DataField(seq = 18)
    private String latefee;    //滞纳金 LATEFEE 16位整数2小数

    @DataField(seq = 3)
    private String banknum;    //手工票：交易流水  银行交易流水号 （手工票时不为空）

    private String creater;    //手工票：制单人  （手工票时不为空）

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

    private String fromnotescode;   //退付票：原票据编号 (退付票时不为空)
    private String recusername;   //领款人名称
    private String recuserbank;   //领款人开户行
    private String recuserbankaccount;   //领款人账号
    private String refundreason;   //退付原因
    private String refundapplycode;   //申请书编号(退付)
    private String text1;   //预留字段
    private String text2;   //预留字段
    private String text3;   //预留字段


    //=======================================================================
    public String getPkid() {
        return pkid;
    }

    
    public void setPkid(String pkid) {
        this.pkid = pkid == null ? null : pkid.trim();
    }

    
    public String getBillid() {
        return billid;
    }

    
    public void setBillid(String billid) {
        this.billid = billid == null ? null : billid.trim();
    }

    
    public String getPaynotescode() {
        return paynotescode;
    }

    
    public void setPaynotescode(String paynotescode) {
        this.paynotescode = paynotescode == null ? null : paynotescode.trim();
    }

    
    public String getNotescode() {
        return notescode;
    }

    
    public void setNotescode(String notescode) {
        this.notescode = notescode == null ? null : notescode.trim();
    }

    
    public String getCheckcode() {
        return checkcode;
    }

    
    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode == null ? null : checkcode.trim();
    }

    
    public String getAmt() {
        return amt;
    }

    
    public void setAmt(String amt) {
        this.amt = amt == null ? null : amt.trim();
    }

    
    public String getNoteskindcode() {
        return noteskindcode;
    }

    
    public void setNoteskindcode(String noteskindcode) {
        this.noteskindcode = noteskindcode == null ? null : noteskindcode.trim();
    }

    
    public String getNoteskinkname() {
        return noteskinkname;
    }

    
    public void setNoteskinkname(String noteskinkname) {
        this.noteskinkname = noteskinkname == null ? null : noteskinkname.trim();
    }

    
    public String getPerformdeptcode() {
        return performdeptcode;
    }

    
    public void setPerformdeptcode(String performdeptcode) {
        this.performdeptcode = performdeptcode == null ? null : performdeptcode.trim();
    }

    
    public String getPerformdeptname() {
        return performdeptname;
    }

    
    public void setPerformdeptname(String performdeptname) {
        this.performdeptname = performdeptname == null ? null : performdeptname.trim();
    }

    
    public String getPrintdate() {
        return printdate;
    }

    
    public void setPrintdate(String printdate) {
        this.printdate = printdate == null ? null : printdate.trim();
    }

    
    public String getAgentbankcode() {
        return agentbankcode;
    }

    
    public void setAgentbankcode(String agentbankcode) {
        this.agentbankcode = agentbankcode == null ? null : agentbankcode.trim();
    }

    
    public String getAgentbankname() {
        return agentbankname;
    }

    
    public void setAgentbankname(String agentbankname) {
        this.agentbankname = agentbankname == null ? null : agentbankname.trim();
    }

    
    public String getPayfeemethodcode() {
        return payfeemethodcode;
    }

    
    public void setPayfeemethodcode(String payfeemethodcode) {
        this.payfeemethodcode = payfeemethodcode == null ? null : payfeemethodcode.trim();
    }

    
    public String getPayfeemethodname() {
        return payfeemethodname;
    }

    
    public void setPayfeemethodname(String payfeemethodname) {
        this.payfeemethodname = payfeemethodname == null ? null : payfeemethodname.trim();
    }

    
    public String getPaymethodcode() {
        return paymethodcode;
    }

    
    public void setPaymethodcode(String paymethodcode) {
        this.paymethodcode = paymethodcode == null ? null : paymethodcode.trim();
    }

    
    public String getPaymethodname() {
        return paymethodname;
    }

    
    public void setPaymethodname(String paymethodname) {
        this.paymethodname = paymethodname == null ? null : paymethodname.trim();
    }

    
    public String getPayer() {
        return payer;
    }

    
    public void setPayer(String payer) {
        this.payer = payer == null ? null : payer.trim();
    }

    
    public String getPayerbank() {
        return payerbank;
    }

    
    public void setPayerbank(String payerbank) {
        this.payerbank = payerbank == null ? null : payerbank.trim();
    }

    
    public String getPayerbankacct() {
        return payerbankacct;
    }

    
    public void setPayerbankacct(String payerbankacct) {
        this.payerbankacct = payerbankacct == null ? null : payerbankacct.trim();
    }

    
    public String getRemark() {
        return remark;
    }

    
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    
    public String getCreatetime() {
        return createtime;
    }

    
    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    
    public String getBilltype() {
        return billtype;
    }

    
    public void setBilltype(String billtype) {
        this.billtype = billtype == null ? null : billtype.trim();
    }

    
    public String getLatefee() {
        return latefee;
    }

    
    public void setLatefee(String latefee) {
        this.latefee = latefee == null ? null : latefee.trim();
    }

    
    public String getBanknum() {
        return banknum;
    }

    
    public void setBanknum(String banknum) {
        this.banknum = banknum == null ? null : banknum.trim();
    }

    
    public String getCreater() {
        return creater;
    }

    
    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    
    public String getBankrecdate() {
        return bankrecdate;
    }

    
    public void setBankrecdate(String bankrecdate) {
        this.bankrecdate = bankrecdate == null ? null : bankrecdate.trim();
    }

    
    public String getBankacctdate() {
        return bankacctdate;
    }

    
    public void setBankacctdate(String bankacctdate) {
        this.bankacctdate = bankacctdate == null ? null : bankacctdate.trim();
    }

    
    public String getIspreaudit() {
        return ispreaudit;
    }

    
    public void setIspreaudit(String ispreaudit) {
        this.ispreaudit = ispreaudit == null ? null : ispreaudit.trim();
    }

    
    public String getRecfeeflag() {
        return recfeeflag;
    }

    
    public void setRecfeeflag(String recfeeflag) {
        this.recfeeflag = recfeeflag == null ? null : recfeeflag.trim();
    }

    
    public String getFromnotescode() {
        return fromnotescode;
    }

    
    public void setFromnotescode(String fromnotescode) {
        this.fromnotescode = fromnotescode == null ? null : fromnotescode.trim();
    }

    public String getBanknotescode() {
        return banknotescode;
    }

    public void setBanknotescode(String banknotescode) {
        this.banknotescode = banknotescode;
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

    public String getRefundapplycode() {
        return refundapplycode;
    }

    public void setRefundapplycode(String refundapplycode) {
        this.refundapplycode = refundapplycode;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }
}
