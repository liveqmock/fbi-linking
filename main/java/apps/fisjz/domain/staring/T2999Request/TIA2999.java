package apps.fisjz.domain.staring.T2999Request;

import common.dataformat.annotation.DataField;
import common.dataformat.annotation.SeperatedTextMessage;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-29
 */
@SeperatedTextMessage(separator = "\\|", mainClass = true)
public class TIA2999 {
    @DataField(seq = 1)
    private String banknum;    //Á÷Ë®ºÅ

    public String getBanknum() {
        return banknum;
    }

    public void setBanknum(String banknum) {
        this.banknum = banknum;
    }
}
