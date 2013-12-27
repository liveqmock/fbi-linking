package apps.fiskfq;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * ��������˰����
 */
public class Client1534011Test {

    public static void main(String[] args) {

         /*
         chr_id
        billtype_code	�ɿ�����ʽ����	String	[1,20]	��ӦƱ����ʽ����
bill_no	Ʊ��	String	[1,42]	�ɿ����ӡˢƱ��
bill_money	�տ���	Currency		��λ��Ԫ
bank_indate	�����տ�ʱ��	DateTime
incomestatus	�տ�״̬	NString	1	3��֧Ʊ�Ǽ�
5��ʵ��
pm_code	�ɿʽ����	NString	[1,42]
cheque_no	�����	String	[1,20]
payerbank	�ɿ����˻�������	GBString	[1,100]
payeraccount	�ɿ����˺�	String	[1,42]
set_year	���	Integer	4	ҵ�����
route_user_code	·���û�����	String	8	·���û����룺agent+��λ�鼯�б���
license	��Ȩ���к�	String		��Ȩ���к��ɷ�����˾ͳһ�ṩ
business_id	������ˮ��	String	12	������ˮ�������к���ҵ��ϵͳ��������6λ���ڣ�YYMMDD�� �� 6λ��Ա�ţ�����λǰ���㣩

         */
        String chrid = "";
        String billtype_code = "";
        String billno = "";
        String billMoney = "";
        String bank_indate = "";
        String incomestatus = "";
        String pm_code = "";
        String cheque_no = "";
        String payerbank = "";
        String payeraccount = "";
        String set_year = "";
        String route_user_code = "";
        String license = "";
        String business_id = "";
        String str = "1.017631             99991534011371986106371986106021fis154FISKFQ201312251109103B77D28F5AE5184F                " +
                chrid + "|" +
                billtype_code + "|" +
                billno + "|" +
                billMoney + "|" +
                bank_indate + "|" +
                incomestatus + "|" +
                pm_code + "|" +
                cheque_no + "|" +
                payerbank + "|" +
                payeraccount + "|" +
                set_year + "|" +
                route_user_code + "|" +
                license + "|" +
                business_id + "|";
        System.out.println(str.length() + 6);
//        test4011(str);

    }

    public static void test4011(String str) {
        try {
            Socket socket = new Socket("127.0.0.1", 60005);
            socket.setSoTimeout(10000);
            OutputStream os = socket.getOutputStream();
            byte[] reqBytes = str.getBytes();
            os.write(reqBytes);
            os.flush();
            InputStream is = socket.getInputStream();
            byte[] lengthBytes = new byte[6];
            is.read(lengthBytes);
            System.out.println("�����ձ����ܳ��ȣ�" + new String(lengthBytes));
            int toReadlength = Integer.parseInt(new String(lengthBytes).trim()) - 6;
            byte[] dataBytes = new byte[toReadlength];
            int readlen = is.read(dataBytes);
            System.out.println(new String(dataBytes));

            if (readlen != toReadlength) {

                throw new RuntimeException("���ĳ��ȶ�ȡʧ��");
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
