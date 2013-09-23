package apps.fisjz.domain.financebureau;

import java.io.Serializable;

public class FbPaynotesItem implements Serializable {
    private String billid;    //����
    private String mainid;    //����ID/��������
    private String nontaxprogramcode;  //��˰��Ŀ����
    private String nontaxprogramname;   //��˰��Ŀ����
    private String units;      //������λ    16λ����4С��
    private String amount;    //���� 16λ���� 4С��
    private String standardkindcode;   //�շѱ�׼����
    private String standardkindname;  //�շѱ�׼����
    private String amt;    //��� 16λ����2С��

    public String getBillid() {
        return billid;
    }

    
    public void setBillid(String billid) {
        this.billid = billid == null ? null : billid.trim();
    }

    
    public String getMainid() {
        return mainid;
    }

    
    public void setMainid(String mainid) {
        this.mainid = mainid == null ? null : mainid.trim();
    }

    
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

    @Override
    public String toString() {
        return "FbPaynotesItem{" +
                "billid='" + billid + '\'' +
                ", mainid='" + mainid + '\'' +
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