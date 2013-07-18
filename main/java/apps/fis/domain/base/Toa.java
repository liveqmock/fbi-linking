package apps.fis.domain.base;

import apps.fis.enums.TxnRtnCode;

import java.io.Serializable;

public abstract class Toa implements Serializable {
    public Toa toBean(String str) {
        try {
            return getToa(str);
        }catch (Exception e) {
            throw new RuntimeException(TxnRtnCode.MSG_PARSE_FAILED.toRtnMsg());
        }
    }

    public abstract Toa getToa(String str);
}
