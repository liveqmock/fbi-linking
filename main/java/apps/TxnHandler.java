package apps;

import gateway.domain.LFixedLengthProtocol;

/**
 * Ӧ�ñ���̳д���
 */
public abstract class TxnHandler {

    public LFixedLengthProtocol handle(LFixedLengthProtocol tia) throws Exception{
         return execute(tia);
    }

    public abstract LFixedLengthProtocol execute(LFixedLengthProtocol tia) throws Exception;
}
