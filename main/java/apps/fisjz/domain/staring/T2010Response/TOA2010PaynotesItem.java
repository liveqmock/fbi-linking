package apps.fisjz.domain.staring.T2010Response;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.OneToManySeperatedTextMessage;

@OneToManySeperatedTextMessage(separator = ",")
public class TOA2010PaynotesItem {
    
    private String pkid;

    @DataField(seq = 1)
    private String billid;    //序列

    @DataField(seq = 2)
    private String mainid;    //主单ID/主单序列

    @DataField(seq = 3)
    private String nontaxprogramcode;  //非税项目编码

    @DataField(seq = 4)
    private String nontaxprogramname;   //非税项目名称

    @DataField(seq = 5)
    private String units;      //计量单位    16位整数4小数

    @DataField(seq = 6)
    private String amount;    //数量 16位整数 4小数

    @DataField(seq = 7)
    private String standardkindcode;   //收费标准编码

    @DataField(seq = 8)
    private String standardkindname;  //收费标准名称

    @DataField(seq = 9)
    private String amt;    //金额 16位整数2小数

    
    private String banknum;

    
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

    
    public String getBanknum() {
        return banknum;
    }

    
    public void setBanknum(String banknum) {
        this.banknum = banknum == null ? null : banknum.trim();
    }
}