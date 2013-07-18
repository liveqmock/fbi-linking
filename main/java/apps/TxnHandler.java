package apps;

import gateway.domain.LFixedLengthProtocol;

/**
 * 应用必须继承此类
 */
public abstract class TxnHandler {

    public LFixedLengthProtocol handle(LFixedLengthProtocol tia) throws Exception{
         return execute(tia);
    }

    public abstract LFixedLengthProtocol execute(LFixedLengthProtocol tia) throws Exception;
}
