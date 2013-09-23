package apps.fisjz.domain.staring.T2011Request;


import common.dataformat.annotation.DataField;
import common.dataformat.annotation.SeperatedTextMessage;

@SeperatedTextMessage(separator = "\\|")
public class TIA2011PaynotesInfo {

    private String pkid;

    @DataField(seq = 4)
    private String billid;  //����

    @DataField(seq = 5)
    private String paynotescode; //�ɿ��鵥��

    @DataField(seq = 6)
    private String notescode; //Ʊ�ݱ��

    @DataField(seq = 7)
    private String checkcode;  //��֤��     һ��ɿ��գ��ֹ���ҵ���Ϊ��

    @DataField(seq = 8)
    private String amt; //�ܽ��      16λ����2С��

    @DataField(seq = 9)
    private String noteskindcode; //Ʊ����Ϣ����

    @DataField(seq = 10)
    private String noteskinkname; //Ʊ����Ϣ����

    private String performdeptcode; //ִ�յ�λ����
    private String performdeptname; //ִ�յ�λ����
    private String printdate;  //��Ʊ����

    @DataField(seq = 11)
    private String agentbankcode;  //�������б���

    @DataField(seq = 12)
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

    @DataField(seq = 17)
    private String billtype;  //��������/ҵ������

    @DataField(seq = 18)
    private String latefee;    //���ɽ� LATEFEE 16λ����2С��

    @DataField(seq = 3)
    private String banknum;    //�ֹ�Ʊ��������ˮ  ���н�����ˮ�� ���ֹ�Ʊʱ��Ϊ�գ�

    private String creater;    //�ֹ�Ʊ���Ƶ���  ���ֹ�Ʊʱ��Ϊ�գ�

    @DataField(seq = 13)
    private String bankrecdate;  //�ֹ�Ʊ�������տ�����  yyyymmdd

    @DataField(seq = 14)
    private String bankacctdate;   //�ֹ�Ʊ�����м�������  yyyymmdd

    @DataField(seq = 15)
    private String ispreaudit;     //�ֹ�Ʊ��Ԥ���־  0 δ��ˣ�1�����

    @DataField(seq = 16)
    private String recfeeflag;    //�ֹ�Ʊ�����˱�־  0 δ���ˣ�1�ѵ���

    
    @DataField(seq = 19)
    private String banknotescode;   //���д���Ʊ��  (����ҵ��)���ҵ������Ϊ 3 �ɿ�֪ͨ�飬����Ϊ��

    private String fromnotescode;   //�˸�Ʊ��ԭƱ�ݱ�� (�˸�Ʊʱ��Ϊ��)
    private String recusername;   //���������
    private String recuserbank;   //����˿�����
    private String recuserbankaccount;   //������˺�
    private String refundreason;   //�˸�ԭ��
    private String refundapplycode;   //��������(�˸�)
    private String text1;   //Ԥ���ֶ�
    private String text2;   //Ԥ���ֶ�
    private String text3;   //Ԥ���ֶ�


    //=======================================================================
    public String getPkid() {
        return pkid;
    }

    
    public void setPkid(String pkid) {
        this.pkid = pkid == null ? null : pkid.trim();
    }

    
    public String getBillid() {
        return billid;
    }

    
    public void setBillid(String billid) {
        this.billid = billid == null ? null : billid.trim();
    }

    
    public String getPaynotescode() {
        return paynotescode;
    }

    
    public void setPaynotescode(String paynotescode) {
        this.paynotescode = paynotescode == null ? null : paynotescode.trim();
    }

    
    public String getNotescode() {
        return notescode;
    }

    
    public void setNotescode(String notescode) {
        this.notescode = notescode == null ? null : notescode.trim();
    }

    
    public String getCheckcode() {
        return checkcode;
    }

    
    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode == null ? null : checkcode.trim();
    }

    
    public String getAmt() {
        return amt;
    }

    
    public void setAmt(String amt) {
        this.amt = amt == null ? null : amt.trim();
    }

    
    public String getNoteskindcode() {
        return noteskindcode;
    }

    
    public void setNoteskindcode(String noteskindcode) {
        this.noteskindcode = noteskindcode == null ? null : noteskindcode.trim();
    }

    
    public String getNoteskinkname() {
        return noteskinkname;
    }

    
    public void setNoteskinkname(String noteskinkname) {
        this.noteskinkname = noteskinkname == null ? null : noteskinkname.trim();
    }

    
    public String getPerformdeptcode() {
        return performdeptcode;
    }

    
    public void setPerformdeptcode(String performdeptcode) {
        this.performdeptcode = performdeptcode == null ? null : performdeptcode.trim();
    }

    
    public String getPerformdeptname() {
        return performdeptname;
    }

    
    public void setPerformdeptname(String performdeptname) {
        this.performdeptname = performdeptname == null ? null : performdeptname.trim();
    }

    
    public String getPrintdate() {
        return printdate;
    }

    
    public void setPrintdate(String printdate) {
        this.printdate = printdate == null ? null : printdate.trim();
    }

    
    public String getAgentbankcode() {
        return agentbankcode;
    }

    
    public void setAgentbankcode(String agentbankcode) {
        this.agentbankcode = agentbankcode == null ? null : agentbankcode.trim();
    }

    
    public String getAgentbankname() {
        return agentbankname;
    }

    
    public void setAgentbankname(String agentbankname) {
        this.agentbankname = agentbankname == null ? null : agentbankname.trim();
    }

    
    public String getPayfeemethodcode() {
        return payfeemethodcode;
    }

    
    public void setPayfeemethodcode(String payfeemethodcode) {
        this.payfeemethodcode = payfeemethodcode == null ? null : payfeemethodcode.trim();
    }

    
    public String getPayfeemethodname() {
        return payfeemethodname;
    }

    
    public void setPayfeemethodname(String payfeemethodname) {
        this.payfeemethodname = payfeemethodname == null ? null : payfeemethodname.trim();
    }

    
    public String getPaymethodcode() {
        return paymethodcode;
    }

    
    public void setPaymethodcode(String paymethodcode) {
        this.paymethodcode = paymethodcode == null ? null : paymethodcode.trim();
    }

    
    public String getPaymethodname() {
        return paymethodname;
    }

    
    public void setPaymethodname(String paymethodname) {
        this.paymethodname = paymethodname == null ? null : paymethodname.trim();
    }

    
    public String getPayer() {
        return payer;
    }

    
    public void setPayer(String payer) {
        this.payer = payer == null ? null : payer.trim();
    }

    
    public String getPayerbank() {
        return payerbank;
    }

    
    public void setPayerbank(String payerbank) {
        this.payerbank = payerbank == null ? null : payerbank.trim();
    }

    
    public String getPayerbankacct() {
        return payerbankacct;
    }

    
    public void setPayerbankacct(String payerbankacct) {
        this.payerbankacct = payerbankacct == null ? null : payerbankacct.trim();
    }

    
    public String getRemark() {
        return remark;
    }

    
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    
    public String getCreatetime() {
        return createtime;
    }

    
    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    
    public String getBilltype() {
        return billtype;
    }

    
    public void setBilltype(String billtype) {
        this.billtype = billtype == null ? null : billtype.trim();
    }

    
    public String getLatefee() {
        return latefee;
    }

    
    public void setLatefee(String latefee) {
        this.latefee = latefee == null ? null : latefee.trim();
    }

    
    public String getBanknum() {
        return banknum;
    }

    
    public void setBanknum(String banknum) {
        this.banknum = banknum == null ? null : banknum.trim();
    }

    
    public String getCreater() {
        return creater;
    }

    
    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    
    public String getBankrecdate() {
        return bankrecdate;
    }

    
    public void setBankrecdate(String bankrecdate) {
        this.bankrecdate = bankrecdate == null ? null : bankrecdate.trim();
    }

    
    public String getBankacctdate() {
        return bankacctdate;
    }

    
    public void setBankacctdate(String bankacctdate) {
        this.bankacctdate = bankacctdate == null ? null : bankacctdate.trim();
    }

    
    public String getIspreaudit() {
        return ispreaudit;
    }

    
    public void setIspreaudit(String ispreaudit) {
        this.ispreaudit = ispreaudit == null ? null : ispreaudit.trim();
    }

    
    public String getRecfeeflag() {
        return recfeeflag;
    }

    
    public void setRecfeeflag(String recfeeflag) {
        this.recfeeflag = recfeeflag == null ? null : recfeeflag.trim();
    }

    
    public String getFromnotescode() {
        return fromnotescode;
    }

    
    public void setFromnotescode(String fromnotescode) {
        this.fromnotescode = fromnotescode == null ? null : fromnotescode.trim();
    }

    public String getBanknotescode() {
        return banknotescode;
    }

    public void setBanknotescode(String banknotescode) {
        this.banknotescode = banknotescode;
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

    public String getRefundapplycode() {
        return refundapplycode;
    }

    public void setRefundapplycode(String refundapplycode) {
        this.refundapplycode = refundapplycode;
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
}
