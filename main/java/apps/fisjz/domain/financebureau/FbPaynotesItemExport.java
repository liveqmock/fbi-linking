package apps.fisjz.domain.financebureau;

import java.io.Serializable;

public class FbPaynotesItemExport implements Serializable {
    private String nontaxprogramcode;  //非税项目编码
    private String nontaxprogramname;   //非税项目名称
    private String amt;    //金额 16位整数2小数
    private String notescode; //票据编号
    private String performdeptcode; //执收单位编码
    private String performdeptname; //执收单位名称
    private String payfeemethodname;  //缴款方式名称
    private String payer;  //缴款人
    private String latefee;    //滞纳金 LATEFEE 16位整数2小数
    private String bankacctdate;   //手工票：银行记账日期  yyyymmdd
    private String operbankid;//银行网点


    public String getNontaxprogramcode() {
        return nontaxprogramcode;
    }

    
    public void setNontaxprogramcode(String nontaxprogramcode) {
        this.nontaxprogramcode = nontaxprogramcode == null ? null : nontaxprogramcode.trim();
    }

    
    public String getNontaxprogramname() {
        return nontaxprogramname;
    }

    
    public void setNontaxprogramname(String nontaxprogramname) {
        this.nontaxprogramname = nontaxprogramname == null ? null : nontaxprogramname.trim();
    }

    public String getAmt() {
        return amt;
    }

    
    public void setAmt(String amt) {
        this.amt = amt == null ? null : amt.trim();
    }

    public String getNotescode() {
        return notescode;
    }

    public void setNotescode(String notescode) {
        this.notescode = notescode;
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

    public String getLatefee() {
        return latefee;
    }

    public void setLatefee(String latefee) {
        this.latefee = latefee;
    }

    public String getBankacctdate() {
        return bankacctdate;
    }

    public void setBankacctdate(String bankacctdate) {
        this.bankacctdate = bankacctdate;
    }

    public String getOperbankid() {
        return operbankid;
    }

    public void setOperbankid(String operbankid) {
        this.operbankid = operbankid;
    }
}