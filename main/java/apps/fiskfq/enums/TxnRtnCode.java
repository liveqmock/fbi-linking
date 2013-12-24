package apps.fiskfq.enums;

import java.util.Hashtable;

/**
 * ҵ���׷�����
 */
public enum TxnRtnCode implements EnumApp {

    TXN_EXECUTE_SECCESS("0000", "�������"),
    TXN_EXECUTED_ALREADY("0001", "��Ʊ���ѽɿ�"),
    TXN_EXECUTE_FAILED("1100", "����ʧ��"),

    MSG_AUTHENTICATE_ILLEGAL("2000", "������֤ʧ��"),
    MSG_PARSE_FAILED("2001", "���Ľ�������"),

    MSG_AMT_ERROR("4001", "������"),

    MSG_SEND_TIMEOUT("3000", "ͨ�ų�ʱ"),
    MSG_SEND_ERROR("3001", "ͨ���쳣"),

    UNKNOWN_EXCEPTION("9000", "����δ֪�쳣");

    private String code = null;
    private String title = null;
    private static Hashtable<String, TxnRtnCode> aliasEnums;

    TxnRtnCode(String code, String title) {
        this.init(code, title);
    }

    @SuppressWarnings("unchecked")
    private void init(String code, String title) {
        this.code = code;
        this.title = title;
        synchronized (this.getClass()) {
            if (aliasEnums == null) {
                aliasEnums = new Hashtable();
            }
        }
        aliasEnums.put(code, this);
        aliasEnums.put(title, this);
    }

    public static TxnRtnCode valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }

    public String toRtnMsg() {
        return this.code + "|" + this.title;
    }
}