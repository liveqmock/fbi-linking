package apps.fisjz.domain.financebureau;

import java.io.Serializable;
import java.util.List;

/**
 * �ֹ�Ʊ
 */
public class FbPaynotesInfo4Manual implements Serializable{
    private String banknum;    //�ֹ�Ʊ��������ˮ  ���н�����ˮ�� ���ֹ�Ʊʱ��Ϊ�գ�
    private String notescode; //Ʊ�ݱ��
    private String checkcode;  //��֤��     һ��ɿ��գ��ֹ���ҵ���Ϊ��
    private String amt; //�ܽ��      16λ����2С��
    private String noteskindcode; //Ʊ����Ϣ����
    private String noteskinkname; //Ʊ����Ϣ����
    private String performdeptcode; //ִ�յ�λ����
    private String performdeptname; //ִ�յ�λ����
    private String printdate;  //��Ʊ����
    private String agentbankcode;  //�������б���
    private String agentbankname;  //������������
    private String payfeemethodcode;  //�ɿʽ����      1�ֽ�2ת�ˣ�
    private String payfeemethodname;  //�ɿʽ����
    private String paymethodcode;  //�ɿⷽʽ����     0 ֱ�ӽɿ⣻1 ���л�ɿ⣻
    private String paymethodname;  //�ɿⷽʽ����
    private String payer;  //�ɿ���
    private String payerbank;  //�ɿ��˿�����
    private String payerbankacct; //�ɿ����˺�
    private String remark;   //��ע
    private String createtime;    //�Ƶ����� yyyymmdd
    private String billtype;  //��������/ҵ������
    private String latefee;    //���ɽ� LATEFEE 16λ����2С��
    private String creater;    //�ֹ�Ʊ���Ƶ���  ���ֹ�Ʊʱ��Ϊ�գ�
    private String bankrecdate;  //�ֹ�Ʊ�������տ�����  yyyymmdd
    private String bankacctdate;   //�ֹ�Ʊ�����м�������  yyyymmdd
    private String ispreaudit;     //�ֹ�Ʊ��Ԥ���־  0 δ��ˣ�1�����
    private String recfeeflag;    //�ֹ�Ʊ�����˱�־  0 δ���ˣ�1�ѵ���

    private List<FbPaynotesItem> details;
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

    public List<FbPaynotesItem> getDetails() {
        return details;
    }

    public void setDetails(List<FbPaynotesItem> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "FbPaynotesInfo4Manual{" +
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
                ", billtype='" + billtype + '\'' +
                ", latefee='" + latefee + '\'' +
                ", creater='" + creater + '\'' +
                ", bankrecdate='" + bankrecdate + '\'' +
                ", bankacctdate='" + bankacctdate + '\'' +
                ", ispreaudit='" + ispreaudit + '\'' +
                ", recfeeflag='" + recfeeflag + '\'' +
                ", details=" + details +
                '}';
    }
}
