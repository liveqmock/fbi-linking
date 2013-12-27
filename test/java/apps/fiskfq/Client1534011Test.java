package apps.fiskfq;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 开发区非税测试
 */
public class Client1534011Test {

    public static void main(String[] args) {

         /*
         chr_id
        billtype_code	缴款书样式编码	String	[1,20]	对应票据样式编码
bill_no	票号	String	[1,42]	缴款书的印刷票号
bill_money	收款金额	Currency		单位：元
bank_indate	银行收款时间	DateTime
incomestatus	收款状态	NString	1	3：支票登记
5：实收
pm_code	缴款方式编码	NString	[1,42]
cheque_no	结算号	String	[1,20]
payerbank	缴款人账户开户行	GBString	[1,100]
payeraccount	缴款人账号	String	[1,42]
set_year	年度	Integer	4	业务年度
route_user_code	路由用户编码	String	8	路由用户编码：agent+三位归集行编码
license	授权序列号	String		授权序列号由方正公司统一提供
business_id	交易流水号	String	12	交易流水号由银行核心业务系统产生，由6位日期（YYMMDD） ＋ 6位柜员号（不足位前补零）

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
            System.out.println("待接收报文总长度：" + new String(lengthBytes));
            int toReadlength = Integer.parseInt(new String(lengthBytes).trim()) - 6;
            byte[] dataBytes = new byte[toReadlength];
            int readlen = is.read(dataBytes);
            System.out.println(new String(dataBytes));

            if (readlen != toReadlength) {

                throw new RuntimeException("报文长度读取失败");
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
