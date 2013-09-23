package apps.fisjz.domain.financebureau;

import java.io.Serializable;

/**
 * �˸�Ʊ
 */
public class FbRefundPaynotesInfo implements Serializable {
    private String billid;  //����
    private String refundapplycode; //�����鵥��
    private String paynotescode; //ԭ�ɿ��鵥��
    private String notescode; //Ʊ�ݱ��
    private String fromnotescode;   //�˸�Ʊ��ԭƱ�ݱ�� (�˸�Ʊʱ��Ϊ��)
    private String amt; //�ܽ��      16λ����2С��
    private String noteskindcode; //Ʊ����Ϣ����
    private String noteskinkname; //Ʊ����Ϣ����
    private String performdeptcode; //ִ�յ�λ����
    private String performdeptname; //ִ�յ�λ����
    private String printdate;  //��Ʊ����
    private String agentbankcode;  //�������б���
    private String agentbankname;  //������������

    private String recusername;   //���������
    private String recuserbank;   //����˿�����
    private String recuserbankaccount;   //������˺�
    private String refundreason;   //�˸�ԭ��

    private String remark;   //��ע
    private String createtime;    //�Ƶ����� yyyymmdd
    private String creater;    //�ֹ�Ʊ���Ƶ���  ���ֹ�Ʊʱ��Ϊ�գ�
    private String bankrecdate;  //�ֹ�Ʊ�������տ�����  yyyymmdd
    private String bankacctdate;   //�ֹ�Ʊ�����м�������  yyyymmdd
    private String ispreaudit;     //�ֹ�Ʊ��Ԥ���־  0 δ��ˣ�1�����
    private String recfeeflag;    //�ֹ�Ʊ�����˱�־  0 δ���ˣ�1�ѵ���

    //=======================================================================

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

    @Override
    public String toString() {
        return "FbRefundPaynotesInfo{" +
                "billid='" + billid + '\'' +
                ", refundapplycode='" + refundapplycode + '\'' +
                ", paynotescode='" + paynotescode + '\'' +
                ", notescode='" + notescode + '\'' +
                ", fromnotescode='" + fromnotescode + '\'' +
                ", amt='" + amt + '\'' +
                ", noteskindcode='" + noteskindcode + '\'' +
                ", noteskinkname='" + noteskinkname + '\'' +
                ", performdeptcode='" + performdeptcode + '\'' +
                ", performdeptname='" + performdeptname + '\'' +
                ", printdate='" + printdate + '\'' +
                ", agentbankcode='" + agentbankcode + '\'' +
                ", agentbankname='" + agentbankname + '\'' +
                ", recusername='" + recusername + '\'' +
                ", recuserbank='" + recuserbank + '\'' +
                ", recuserbankaccount='" + recuserbankaccount + '\'' +
                ", refundreason='" + refundreason + '\'' +
                ", remark='" + remark + '\'' +
                ", createtime='" + createtime + '\'' +
                ", creater='" + creater + '\'' +
                ", bankrecdate='" + bankrecdate + '\'' +
                ", bankacctdate='" + bankacctdate + '\'' +
                ", ispreaudit='" + ispreaudit + '\'' +
                ", recfeeflag='" + recfeeflag + '\'' +
                '}';
    }
}
