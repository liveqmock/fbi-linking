package apps.fisjz.domain.staring.T2082Response;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.OneToManySeperatedTextMessage;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 */
@OneToManySeperatedTextMessage(separator = ",")
public class TOA2082Detail {
    @DataField(seq = 1)
    private String bankacctdate; //���м�������

    @DataField(seq = 2)
    private String performdeptname; //ִ�յ�λ����

    private String nontaxprogramcode;

    @DataField(seq = 3)
    private String nontaxprogramname;   //��˰��Ŀ����

    @DataField(seq = 4)
    private String performdeptcode;

    @DataField(seq = 5)
    private String amt; //������

    @DataField(seq = 6)
    private String payfeemethodname;//�ɿʽ

    @DataField(seq = 7)
    private String payer;  //�ɿ���

    @DataField(seq = 8)
    private String notescode;//Ʊ�ݱ��

    @DataField(seq = 9)
    private String operbankid; // �ɿ�����

    @DataField(seq = 10)
    private String latefee; //���ɽ�


    public String getPerformdeptname() {
        return performdeptname;
    }

    public void setPerformdeptname(String performdeptname) {
        this.performdeptname = performdeptname;
    }

    public String getNontaxprogramname() {
        return nontaxprogramname;
    }

    public void setNontaxprogramname(String nontaxprogramname) {
        this.nontaxprogramname = nontaxprogramname;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getLatefee() {
        return latefee;
    }

    public void setLatefee(String latefee) {
        this.latefee = latefee;
    }

    public String getPerformdeptcode() {
        return performdeptcode;
    }

    public void setPerformdeptcode(String performdeptcode) {
        this.performdeptcode = performdeptcode;
    }

    public String getNontaxprogramcode() {
        return nontaxprogramcode;
    }

    public void setNontaxprogramcode(String nontaxprogramcode) {
        this.nontaxprogramcode = nontaxprogramcode;
    }

    public String getBankacctdate() {
        return bankacctdate;
    }

    public void setBankacctdate(String bankacctdate) {
        this.bankacctdate = bankacctdate;
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

    public String getNotescode() {
        return notescode;
    }

    public void setNotescode(String notescode) {
        this.notescode = notescode;
    }

    public String getOperbankid() {
        return operbankid;
    }

    public void setOperbankid(String operbankid) {
        this.operbankid = operbankid;
    }
}
