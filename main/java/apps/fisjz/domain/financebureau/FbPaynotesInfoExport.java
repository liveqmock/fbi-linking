package apps.fisjz.domain.financebureau;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Lin Yong
 * Date: 13-10-23
 * Time: 上午10:23
 */
public class FbPaynotesInfoExport implements Serializable {
    private String performdeptcode; //执收单位编码
    private String performdeptname; //执收单位名称
    private String nontaxprogramcode;  //非税项目编码
    private String nontaxprogramname;   //非税项目名称
    private String amount;    //数量 16位整数 4小数
    private String amt;    //金额 16位整数2小数
    private String latefee;    //滞纳金 LATEFEE 16位整数2小数
    private String total;    //合计  16位整数2小数

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount == null ? null : amount.trim();
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt == null ? null : amt.trim();
    }

    public String getLatefee() {
        return latefee;
    }

    public void setLatefee(String latefee) {
        this.latefee = latefee == null ? null : latefee.trim();
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total == null ? null : total.trim();
    }

    @Override
    public String toString() {
        return "FbPaynotesInfoExport{" +
                "performdeptcode='" + performdeptcode + '\'' +
                ", performdeptname='" + performdeptname + '\'' +
                ", nontaxprogramcode='" + nontaxprogramcode + '\'' +
                ", nontaxprogramname='" + nontaxprogramname + '\'' +
                ", amount='" + amount + '\'' +
                ", amt='" + amt + '\'' +
                ", latefee='" + latefee + '\'' +
                ", total='" + total + '\'' +
                '}';
    }
}
