package apps.fisjz.domain.financebureau;

import java.io.Serializable;

public class FbPaynotesItemExport implements Serializable {
    private String nontaxprogramcode;  //��˰��Ŀ����
    private String nontaxprogramname;   //��˰��Ŀ����
    private String amt;    //��� 16λ����2С��
    private String notescode; //Ʊ�ݱ��
    private String performdeptcode; //ִ�յ�λ����
    private String performdeptname; //ִ�յ�λ����
    private String payfeemethodname;  //�ɿʽ����
    private String payer;  //�ɿ���
    private String latefee;    //���ɽ� LATEFEE 16λ����2С��
    private String bankacctdate;   //�ֹ�Ʊ�����м�������  yyyymmdd
    private String operbankid;//��������


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