package apps.fisjz.domain.staring.T2012Response;


import common.dataformat.annotation.DataField;
import common.dataformat.annotation.SeperatedTextMessage;

@SeperatedTextMessage(separator = "\\|",  mainClass = true)
public class TOA2012 {
    @DataField(seq = 1)
    private String  rtnCode;

    @DataField(seq = 2)
    private String  rtnMsg;

    //============================

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getRtnMsg() {
        return rtnMsg;
    }

    public void setRtnMsg(String rtnMsg) {
        this.rtnMsg = rtnMsg;
    }
}
