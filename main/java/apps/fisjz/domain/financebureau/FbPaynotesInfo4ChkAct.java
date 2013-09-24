package apps.fisjz.domain.financebureau;

import java.io.Serializable;

public class FbPaynotesInfo4ChkAct implements Serializable {
    private String billid;  //����
    private String banknum;    //�ֹ�Ʊ��������ˮ  ���н�����ˮ�� ���ֹ�Ʊʱ��Ϊ�գ�
    private String notescode; //Ʊ�ݱ��
    private String nontaxprogramcode; //��˰��Ŀ����
    private String nontaxprogramname; //��˰��Ŀ����
    private String bankamt; //���      16λ����2С��
    private String latefee;    //���ɽ� LATEFEE 16λ����2С��
    private String noteskindcode; //Ʊ����Ϣ����
    private String noteskinkname; //Ʊ����Ϣ����
    private String performdeptcode; //ִ�յ�λ����
    private String performdeptname; //ִ�յ�λ����
    private String agentbankcode;  //�������б���
    private String agentbankname;  //������������
    private String payer;  //�ɿ���
    private String text1;   //Ԥ���ֶ�
    private String text2;   //Ԥ���ֶ�
    private String text3;   //Ԥ���ֶ�
    private String startdate;   //��ʼ����
    private String enddate;   //��������
    //=======================================================================

    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

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

    public String getNontaxprogramcode() {
        return nontaxprogramcode;
    }

    public void setNontaxprogramcode(String nontaxprogramcode) {
        this.nontaxprogramcode = nontaxprogramcode;
    }

    public String getNontaxprogramname() {
        return nontaxprogramname;
    }

    public void setNontaxprogramname(String nontaxprogramname) {
        this.nontaxprogramname = nontaxprogramname;
    }

    public String getBankamt() {
        return bankamt;
    }

    public void setBankamt(String bankamt) {
        this.bankamt = bankamt;
    }

    public String getLatefee() {
        return latefee;
    }

    public void setLatefee(String latefee) {
        this.latefee = latefee;
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

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
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

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    @Override
    public String toString() {
        return "FbPaynotesInfo4ChkAct{" +
                "billid='" + billid + '\'' +
                ", banknum='" + banknum + '\'' +
                ", notescode='" + notescode + '\'' +
                ", nontaxprogramcode='" + nontaxprogramcode + '\'' +
                ", nontaxprogramname='" + nontaxprogramname + '\'' +
                ", bankamt='" + bankamt + '\'' +
                ", latefee='" + latefee + '\'' +
                ", noteskindcode='" + noteskindcode + '\'' +
                ", noteskinkname='" + noteskinkname + '\'' +
                ", performdeptcode='" + performdeptcode + '\'' +
                ", performdeptname='" + performdeptname + '\'' +
                ", agentbankcode='" + agentbankcode + '\'' +
                ", agentbankname='" + agentbankname + '\'' +
                ", payer='" + payer + '\'' +
                ", text1='" + text1 + '\'' +
                ", text2='" + text2 + '\'' +
                ", text3='" + text3 + '\'' +
                ", startdate='" + startdate + '\'' +
                ", enddate='" + enddate + '\'' +
                '}';
    }
}
