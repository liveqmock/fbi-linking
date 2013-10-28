package apps.fisjz.domain.staring.T2081Response;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.OneToManySeperatedTextMessage;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 */
@OneToManySeperatedTextMessage(separator = ",")
public class TOA2081Detail {
    @DataField(seq = 1)
    private String performdeptname; //执收单位名称

    @DataField(seq = 2)
    private String nontaxprogramname;   //非税项目名称

    @DataField(seq = 3)
    private String amount; //收入项目数量

    @DataField(seq = 4)
    private String amt; //收入金额

    @DataField(seq = 5)
    private String latefee; //滞纳金

    @DataField(seq = 6)
    private String total; //合计

    private String performdeptcode;

    private String nontaxprogramcode;
    public String getPerformdeptname() {
        return performdeptname;
    }

    public void setPerformdeptname(String performdeptname) {
        this.performdeptname = performdeptname;
    }

    public String getNontaxprogramname() {
        return nontaxprogramname;
    }

    public void setNontaxprogramname(String nontaxprogramname) {
        this.nontaxprogramname = nontaxprogramname;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getLatefee() {
        return latefee;
    }

    public void setLatefee(String latefee) {
        this.latefee = latefee;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPerformdeptcode() {
        return performdeptcode;
    }

    public void setPerformdeptcode(String performdeptcode) {
        this.performdeptcode = performdeptcode;
    }

    public String getNontaxprogramcode() {
        return nontaxprogramcode;
    }

    public void setNontaxprogramcode(String nontaxprogramcode) {
        this.nontaxprogramcode = nontaxprogramcode;
    }

    @Override
    public String toString() {
        return "TOA2081Detail{" +
                "performdeptname='" + performdeptname + '\'' +
                ", nontaxprogramname='" + nontaxprogramname + '\'' +
                ", amount='" + amount + '\'' +
                ", amt='" + amt + '\'' +
                ", latefee='" + latefee + '\'' +
                ", total='" + total + '\'' +
                '}';
    }
}
