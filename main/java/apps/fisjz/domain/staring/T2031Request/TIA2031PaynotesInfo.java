package apps.fisjz.domain.staring.T2031Request;


import common.dataformat.annotation.DataField;
import common.dataformat.annotation.SeperatedTextMessage;

@SeperatedTextMessage(separator = "\\|")
public class TIA2031PaynotesInfo {
    @DataField(seq = 3)
    private String banknum;  //������ˮ
    @DataField(seq = 4)
    private String billid;  //����
    @DataField(seq = 5)
    private String refundapplycode; //�˸�������
    @DataField(seq = 6)
    private String paynotescode; //ԭ�ɿ��鵥��
    @DataField(seq = 7)
    private String notescode; //Ʊ�ݱ��
    @DataField(seq = 8)
    private String fromnotescode; //ԭƱ�ݱ��
    @DataField(seq = 9)
    private String amt; //�ܽ��      16λ����2С��
    @DataField(seq = 10)
    private String noteskindcode; //Ʊ����Ϣ����
    @DataField(seq = 11)
    private String noteskinkname; //Ʊ����Ϣ����
    @DataField(seq = 12)
    private String agentbankcode;  //�������б���
    @DataField(seq = 13)
    private String agentbankname;  //������������
    @DataField(seq = 14)
    private String bankrecdate;  //�ֹ�Ʊ�������տ�����  yyyymmdd
    @DataField(seq = 15)
    private String bankacctdate;   //�ֹ�Ʊ�����м�������  yyyymmdd
    @DataField(seq = 16)
    private String ispreaudit;     //�ֹ�Ʊ��Ԥ���־  0 δ��ˣ�1�����
    @DataField(seq = 17)
    private String recfeeflag;    //�ֹ�Ʊ�����˱�־  0 δ���ˣ�1�ѵ���

    //=============

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
        return "TIA2031PaynotesInfo{" +
                "banknum='" + banknum + '\'' +
                ", billid='" + billid + '\'' +
                ", refundapplycode='" + refundapplycode + '\'' +
                ", paynotescode='" + paynotescode + '\'' +
                ", notescode='" + notescode + '\'' +
                ", fromnotescode='" + fromnotescode + '\'' +
                ", amt='" + amt + '\'' +
                ", noteskindcode='" + noteskindcode + '\'' +
                ", noteskinkname='" + noteskinkname + '\'' +
                ", agentbankcode='" + agentbankcode + '\'' +
                ", agentbankname='" + agentbankname + '\'' +
                ", bankrecdate='" + bankrecdate + '\'' +
                ", bankacctdate='" + bankacctdate + '\'' +
                ", ispreaudit='" + ispreaudit + '\'' +
                ", recfeeflag='" + recfeeflag + '\'' +
                '}';
    }
}
