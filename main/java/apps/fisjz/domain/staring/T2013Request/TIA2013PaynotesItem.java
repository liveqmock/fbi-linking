package apps.fisjz.domain.staring.T2013Request;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.OneToManySeperatedTextMessage;

@OneToManySeperatedTextMessage(separator = ",")
public class TIA2013PaynotesItem {
    @DataField(seq = 1)
    private String banknum;

    @DataField(seq = 2)
    private String nontaxprogramcode;  //��˰��Ŀ����

    @DataField(seq = 3)
    private String nontaxprogramname;   //��˰��Ŀ����

    @DataField(seq = 4)
    private String units;      //������λ    16λ����4С��

    @DataField(seq = 5)
    private String amount;    //���� 16λ���� 4С��

    @DataField(seq = 6)
    private String standardkindcode;   //�շѱ�׼����

    @DataField(seq = 7)
    private String standardkindname;  //�շѱ�׼����

    @DataField(seq = 8)
    private String amt;    //��� 16λ����2С��


    
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

    
    public String getUnits() {
        return units;
    }

    
    public void setUnits(String units) {
        this.units = units == null ? null : units.trim();
    }

    
    public String getAmount() {
        return amount;
    }

    
    public void setAmount(String amount) {
        this.amount = amount == null ? null : amount.trim();
    }

    
    public String getStandardkindcode() {
        return standardkindcode;
    }

    
    public void setStandardkindcode(String standardkindcode) {
        this.standardkindcode = standardkindcode == null ? null : standardkindcode.trim();
    }

    
    public String getStandardkindname() {
        return standardkindname;
    }

    
    public void setStandardkindname(String standardkindname) {
        this.standardkindname = standardkindname == null ? null : standardkindname.trim();
    }

    
    public String getAmt() {
        return amt;
    }

    
    public void setAmt(String amt) {
        this.amt = amt == null ? null : amt.trim();
    }

    
    public String getBanknum() {
        return banknum;
    }

    
    public void setBanknum(String banknum) {
        this.banknum = banknum == null ? null : banknum.trim();
    }

    @Override
    public String toString() {
        return "TIA2013PaynotesItem{" +
                "banknum='" + banknum + '\'' +
                ", nontaxprogramcode='" + nontaxprogramcode + '\'' +
                ", nontaxprogramname='" + nontaxprogramname + '\'' +
                ", units='" + units + '\'' +
                ", amount='" + amount + '\'' +
                ", standardkindcode='" + standardkindcode + '\'' +
                ", standardkindname='" + standardkindname + '\'' +
                ", amt='" + amt + '\'' +
                '}';
    }
}