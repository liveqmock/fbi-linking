package apps.fis.domain.base;

import apps.fis.enums.TxnRtnCode;

import java.io.Serializable;

public abstract class Tia implements Serializable {

     public Tia toBean(String str) {
         try {
         return getTia(str);
         }catch (Exception e) {
             throw new RuntimeException(TxnRtnCode.MSG_PARSE_FAILED.toRtnMsg());
         }
     }

    public abstract Tia getTia(String str);
}
