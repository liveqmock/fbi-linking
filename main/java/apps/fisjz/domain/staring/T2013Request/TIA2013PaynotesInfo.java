package apps.fisjz.domain.staring.T2013Request;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.SeperatedTextMessage;

@SeperatedTextMessage(separator = "\\|")
public class TIA2013PaynotesInfo {
    @DataField(seq = 3)
    private String banknum;    //�ֹ�Ʊ��������ˮ  ���н�����ˮ�� ���ֹ�Ʊʱ��Ϊ�գ�
    @DataField(seq = 4)
    private String notescode; //Ʊ�ݱ��
    @DataField(seq = 5)
    private String checkcode;  //��֤��     һ��ɿ��գ��ֹ���ҵ���Ϊ��
    @DataField(seq = 6)
    private String amt; //�ܽ��      16λ����2С��
    @DataField(seq = 7)
    private String noteskindcode; //Ʊ����Ϣ����
    @DataField(seq = 8)
    private String noteskinkname; //Ʊ����Ϣ����
    @DataField(seq = 9)
    private String performdeptcode; //ִ�յ�λ����
    @DataField(seq = 10)
    private String performdeptname; //ִ�յ�λ����
    @DataField(seq = 11)
    private String printdate;  //��Ʊ����
    @DataField(seq = 12)
    private String agentbankcode;  //�������б���
    @DataField(seq = 13)
    private String agentbankname;  //������������
    @DataField(seq = 14)
    private String payfeemethodcode;  //�ɿʽ����      1�ֽ�2ת�ˣ�
    @DataField(seq = 15)
    private String payfeemethodname;  //�ɿʽ����
    @DataField(seq = 16)
    private String paymethodcode;  //�ɿⷽʽ����     0 ֱ�ӽɿ⣻1 ���л�ɿ⣻
    @DataField(seq = 17)
    private String paymethodname;  //�ɿⷽʽ����
    @DataField(seq = 18)
    private String payer;  //�ɿ���
    @DataField(seq = 19)
    private String payerbank;  //�ɿ��˿�����
    @DataField(seq = 20)
    private String payerbankacct; //�ɿ����˺�
    @DataField(seq = 21)
    private String remark;   //��ע
    @DataField(seq = 22)
    private String createtime;    //�Ƶ����� yyyymmdd
    @DataField(seq = 23)
    private String creater;    //�ֹ�Ʊ���Ƶ���  ���ֹ�Ʊʱ��Ϊ�գ�
    @DataField(seq = 24)
    private String bankrecdate;  //�ֹ�Ʊ�������տ�����  yyyymmdd
    @DataField(seq = 25)
    private String bankacctdate;   //�ֹ�Ʊ�����м�������  yyyymmdd
    @DataField(seq = 26)
    private String ispreaudit;     //�ֹ�Ʊ��Ԥ���־  0 δ��ˣ�1�����
    @DataField(seq = 27)
    private String recfeeflag;    //�ֹ�Ʊ�����˱�־  0 δ���ˣ�1�ѵ���
    @DataField(seq = 28)
    private String billtype;  //��������/ҵ������
    @DataField(seq = 29)
    private String latefee;    //���ɽ� LATEFEE 16λ����2С��

    //=======================================================================

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

    public String getPayfeemethodcode() {
        return payfeemethodcode;
    }

    public void setPayfeemethodcode(String payfeemethodcode) {
        this.payfeemethodcode = payfeemethodcode;
    }

    public String getPayfeemethodname() {
        return payfeemethodname;
    }

    public void setPayfeemethodname(String payfeemethodname) {
        this.payfeemethodname = payfeemethodname;
    }

    public String getPaymethodcode() {
        return paymethodcode;
    }

    public void setPaymethodcode(String paymethodcode) {
        this.paymethodcode = paymethodcode;
    }

    public String getPaymethodname() {
        return paymethodname;
    }

    public void setPaymethodname(String paymethodname) {
        this.paymethodname = paymethodname;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getPayerbank() {
        return payerbank;
    }

    public void setPayerbank(String payerbank) {
        this.payerbank = payerbank;
    }

    public String getPayerbankacct() {
        return payerbankacct;
    }

    public void setPayerbankacct(String payerbankacct) {
        this.payerbankacct = payerbankacct;
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

    @Override
    public String toString() {
        return "TIA2013PaynotesInfo{" +
                "banknum='" + banknum + '\'' +
                ", notescode='" + notescode + '\'' +
                ", checkcode='" + checkcode + '\'' +
                ", amt='" + amt + '\'' +
                ", noteskindcode='" + noteskindcode + '\'' +
                ", noteskinkname='" + noteskinkname + '\'' +
                ", performdeptcode='" + performdeptcode + '\'' +
                ", performdeptname='" + performdeptname + '\'' +
                ", printdate='" + printdate + '\'' +
                ", agentbankcode='" + agentbankcode + '\'' +
                ", agentbankname='" + agentbankname + '\'' +
                ", payfeemethodcode='" + payfeemethodcode + '\'' +
                ", payfeemethodname='" + payfeemethodname + '\'' +
                ", paymethodcode='" + paymethodcode + '\'' +
                ", paymethodname='" + paymethodname + '\'' +
                ", payer='" + payer + '\'' +
                ", payerbank='" + payerbank + '\'' +
                ", payerbankacct='" + payerbankacct + '\'' +
                ", remark='" + remark + '\'' +
                ", createtime='" + createtime + '\'' +
                ", creater='" + creater + '\'' +
                ", bankrecdate='" + bankrecdate + '\'' +
                ", bankacctdate='" + bankacctdate + '\'' +
                ", ispreaudit='" + ispreaudit + '\'' +
                ", recfeeflag='" + recfeeflag + '\'' +
                ", billtype='" + billtype + '\'' +
                ", latefee='" + latefee + '\'' +
                '}';
    }
}
