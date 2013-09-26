package apps.fisjz.domain.staring.T2040Request;


import common.dataformat.annotation.DataField;
import common.dataformat.annotation.SeperatedTextMessage;

@SeperatedTextMessage(separator = "\\|")
public class TIA2040PaynotesInfo {

    @DataField(seq = 3)
    private String banknum;    //�ֹ�Ʊ��������ˮ  ���н�����ˮ�� ���ֹ�Ʊʱ��Ϊ�գ�
    @DataField(seq = 4)
    private String paynotescode; //�ɿ��鵥��
    @DataField(seq = 5)
    private String notescode; //Ʊ�ݱ��
    @DataField(seq = 6)
    private String amt; //�ܽ��      16λ����2С��
    @DataField(seq = 7)
    private String noteskindcode; //Ʊ����Ϣ����
    @DataField(seq = 8)
    private String noteskinkname; //Ʊ����Ϣ����
    @DataField(seq = 9)
    private String agentbankcode;  //�������б���
    @DataField(seq = 10)
    private String agentbankname;  //������������
    @DataField(seq = 11)
    private String canceldate;  //���г�������
    @DataField(seq = 12)
    private String billtype;  //��������/ҵ������    3

    //=======================================================================

    public String getBanknum() {
        return banknum;
    }

    public void setBanknum(String banknum) {
        this.banknum = banknum;
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

    public String getCanceldate() {
        return canceldate;
    }

    public void setCanceldate(String canceldate) {
        this.canceldate = canceldate;
    }

    public String getBilltype() {
        return billtype;
    }

    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }

    @Override
    public String toString() {
        return "TIA2040PaynotesInfo{" +
                "banknum='" + banknum + '\'' +
                ", paynotescode='" + paynotescode + '\'' +
                ", notescode='" + notescode + '\'' +
                ", amt='" + amt + '\'' +
                ", noteskindcode='" + noteskindcode + '\'' +
                ", noteskinkname='" + noteskinkname + '\'' +
                ", agentbankcode='" + agentbankcode + '\'' +
                ", agentbankname='" + agentbankname + '\'' +
                ", canceldate='" + canceldate + '\'' +
                ", billtype='" + billtype + '\'' +
                '}';
    }
}
