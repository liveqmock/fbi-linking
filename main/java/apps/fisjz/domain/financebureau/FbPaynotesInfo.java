package apps.fisjz.domain.financebureau;


import java.io.Serializable;

public class FbPaynotesInfo implements Serializable {
    private String banknum;    //�ֹ�Ʊ��������ˮ  ���н�����ˮ�� ���ֹ�Ʊʱ��Ϊ�գ�
    private String billid;  //����
    private String paynotescode; //�ɿ��鵥��
    private String notescode; //Ʊ�ݱ��
    private String checkcode;  //��֤��     һ��ɿ��գ��ֹ���ҵ���Ϊ��
    private String amt; //�ܽ��      16λ����2С��
    private String noteskindcode; //Ʊ����Ϣ����
    private String noteskinkname; //Ʊ����Ϣ����
    //private String performdeptcode; //ִ�յ�λ����
    //private String performdeptname; //ִ�յ�λ����
    //private String printdate;  //��Ʊ����
    private String agentbankcode;  //�������б���
    private String agentbankname;  //������������
    //private String payfeemethodcode;  //�ɿʽ����      1�ֽ�2ת�ˣ�
    //private String payfeemethodname;  //�ɿʽ����
    //private String paymethodcode;  //�ɿⷽʽ����     0 ֱ�ӽɿ⣻1 ���л�ɿ⣻
    //private String paymethodname;  //�ɿⷽʽ����
    //private String payer;  //�ɿ���
    //private String payerbank;  //�ɿ��˿�����
    //private String payerbankacct; //�ɿ����˺�
    //private String remark;   //��ע
    //private String createtime;    //�Ƶ����� yyyymmdd
    private String billtype;  //��������/ҵ������
    private String latefee;    //���ɽ� LATEFEE 16λ����2С��
    //private String creater;    //�ֹ�Ʊ���Ƶ���  ���ֹ�Ʊʱ��Ϊ�գ�
    private String bankrecdate;  //�ֹ�Ʊ�������տ�����  yyyymmdd
    private String bankacctdate;   //�ֹ�Ʊ�����м�������  yyyymmdd
    private String ispreaudit;     //�ֹ�Ʊ��Ԥ���־  0 δ��ˣ�1�����
    private String recfeeflag;    //�ֹ�Ʊ�����˱�־  0 δ���ˣ�1�ѵ���
    //private String fromnotescode;   //�˸�Ʊ��ԭƱ�ݱ�� (�˸�Ʊʱ��Ϊ��)
    //private String recusername;   //���������
    //private String recuserbank;   //����˿�����
    //private String recuserbankaccount;   //������˺�
    //private String refundreason;   //�˸�ԭ��
    private String banknotescode;   //���д���Ʊ��  (����ҵ��)���ҵ������Ϊ 3 �ɿ�֪ͨ�飬����Ϊ��
    //private String refundapplycode;   //��������(�˸�)
    //private String text1;   //Ԥ���ֶ�
    //private String text2;   //Ԥ���ֶ�
    //private String text3;   //Ԥ���ֶ�


    //=======================================================================
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

    public String getBanknotescode() {
        return banknotescode;
    }

    public void setBanknotescode(String banknotescode) {
        this.banknotescode = banknotescode;
    }

    @Override
    public String toString() {
        return "FbPaynotesInfo{" +
                "banknum='" + banknum + '\'' +
                ", billid='" + billid + '\'' +
                ", paynotescode='" + paynotescode + '\'' +
                ", notescode='" + notescode + '\'' +
                ", checkcode='" + checkcode + '\'' +
                ", amt='" + amt + '\'' +
                ", noteskindcode='" + noteskindcode + '\'' +
                ", noteskinkname='" + noteskinkname + '\'' +
                ", agentbankcode='" + agentbankcode + '\'' +
                ", agentbankname='" + agentbankname + '\'' +
                ", billtype='" + billtype + '\'' +
                ", latefee='" + latefee + '\'' +
                ", bankrecdate='" + bankrecdate + '\'' +
                ", bankacctdate='" + bankacctdate + '\'' +
                ", ispreaudit='" + ispreaudit + '\'' +
                ", recfeeflag='" + recfeeflag + '\'' +
                ", banknotescode='" + banknotescode + '\'' +
                '}';
    }
}
