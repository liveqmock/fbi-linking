package apps.fisjz.gateway.financebureau;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-9-22
 * Time: ����1:24
 */
public interface NontaxBankService {
    //��ѯ��������
    List queryAllElementCode(String applicationid, String elementcode, int year);

    //�ɿ�����Ϣ��ѯ�ӿ�
    List queryNontaxPayment(String applicationid, String bank, String year, String finorg, String notescode, String checkcode, String billtype);

    //�ɿ����տ�ӿ�
    List updateNontaxPayment(String applicationid, String bank, String year, String finorg, List paramList);

    //�ɿ��鵽�˽ӿ�
    List accountNontaxPayment(String applicationid, String bank, String year, String finorg, List paramList);

    //(�ֹ���һ��ɿ���
    List insertNontaxPayment(String applicationid, String bank, String year, String finorg, List paramList);

    //�˸��ɿ�����Ϣ��ѯ
    List queryRefundNontaxPayment(String applicationid, String bank, String year, String finorg, String refundapplycode, String notescode);

    //�˸��ɿ���ȷ��
    List updateRefundNontaxPayment(String applicationid, String bank, String year, String finorg, List paramList);

    //�ɿ������
    List cancelNontaxPayment(String applicationid, String bank, String year, String finorg, List paramList);

    //���ɿ�����Ϣ��ѯ
    List queryExtraNontaxPayment(String applicationid, String bank, String year, String finorg, String anumber);

    //�ɿ�����ϸ��Ϣ�ϴ�(����)
    List insertBankNontaxPayment(String applicationid, String bank, String year, String finorg, List paramList);

    //δ�������ϴ��ӿ�(����)
    List insertBankUnknowData(String applicationid, String bank, String year, String finorg, List paramList);
}
