package apps.fis.domain.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import apps.fis.domain.base.Tia;
import apps.fis.domain.base.TiaFisHeader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  3.4.2.1	ʵ�տ�����
 */

@XStreamAlias("ROOT")
public class Tia3002 extends Tia {
    public TiaFisHeader HEAD = new TiaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {

        public Data DATA = new Data();

        public static class Data implements Serializable {
            public String PJZL = "";                         // 1 Ʊ������	C10	NO	YES	��7�ֵ����нɿ��Ʊ�����ࣨ���������һ��
            public String JKSBH = "";                        // 2 �ɿ�����	C20	NO	YES		JKSBH
            public String XZQH = "";                         // 3 ��������	C6	NO		��������������λ	XZQH
            public String ZSFS = "";                         // 4 ���շ�ʽ	C3	NO 1 ֱ�ӽɿ� 2 ���л�� ����ʱû�м��л��ҵ��
            public String JKFS = "";                         // 5 �ɿʽ	C3	YES		Ϊ�գ�������ʵ���տʽΪ׼
            public String TZRQ = "";                         // 6 ��������	DATE	NO		��Ʊ����
            public String WTZSDWBM = "";                     // 7 ί��ִ�յ�λ����	C42	NO
            public String WTZSDWMC = "";                     // 8 ί��ִ�յ�λ����	C100	NO
            public String WTZSBZ = "";                       // 9 ί�����ձ�־	C3	NO
            public String STZSDWBM = "";                     // 10 ����ִ�յ�λ����	C42	NO
            public String STZSDWMC = "";                     // 11 ����ִ�յ�λ����	C100	NO
            public String ZSDWZZJG = "";                     // 12 ִ�յ�λ��֯����	C20	YES
            public String FKRMC = "";                        // 13 ����������	C80	NO
            public String FKRKHH = "";                       // 14 �����˿�����	C80	YES
            public String FKRZH = "";                        // 15 �������˺�	C40	YES
            public String SKRMC = "";                        // 16 �տ�������	C80	NO
            public String SKRKHH = "";                       // 17 �տ��˿����� C80	YES
            public String SKRZH = "";                        // 18 �տ����˺�	C40	YES
            public String YHWDBM = "";                       // 19 ����������� C20	YES
            public String ZJE = "";                          // 20 �ܽ��	N20,2 YES
            public String BZ = "";                           // 21 ��ע	C255 YES
            public String JYM = "";                          // 22 У����	C4	YES	ȫƱ����ϢУ���룬�ڽɿ���Ϊ����Ʊʱ�и�У����
            public String LXSKBZ = "";                       // 23 �����տ��־ C3	NO �����տ��־ 0������ 1����
            public String SGPBZ = "";                        // 24 �ֹ�Ʊ��־	C3	NO
            public String YHSKRQ = "";                       // 25 �����տ�����	DATE	YES
            public String FMYY = "";                         // 26 ��ûԭ��	C80	YES
            public String FMLY = "";                         // 27 ��û����	C80	YES   ��ûƱ��ר�У�����Ϊ�գ�
            public String DSFY = "";                         // 28 ���շ�Ժ	C80	YES		��Ժר��Ʊ��ר�У�����Ϊ�գ�
            public String BGRMC = "";                        // 29 ������	C80	YES		��Ժר��Ʊ��ר�У�����Ϊ�գ�	BGRMC
            public String AY = "";                           // 30 ����	C80	YES		��Ժר��Ʊ��ר�У�����Ϊ�գ�
            public String AH = "";                           // 31 ����	C20	YES		��Ժר��Ʊ��ר�У�����Ϊ�գ�
            public String ZDJH = "";                         // 32 �ֵڼ���	C20	YES		��Ժר��Ʊ��ר�У�����Ϊ�գ�
            public String BDE = "";                          // 33 ��Ķ�	  C20	YES		��Ժר��Ʊ��ר�У�����Ϊ�գ�
            public String XZSPDTWYM = "";                    // 34 ������������Ψһ��	C50	YES		������������ר�ã��ֹ�Ʊʱ��Ҫ���и���Ʊ������Ϣ����¼�룩
            public String BMKYWXH = "";                      // 35 ������ҵ�����	C50	YES		����˽ɿ���Ϊ֮ǰ������ת��ɵ�����ʱ�����ֶζ�ӦΪ֮ǰ�������ҵ����š�
            public String BYZD1 = "";                        // 36 �����ֶ�1	C100	YES		�ɿ�������Ϣ�����ֶ�
            public String BYZD2 = "";                        // 37 �����ֶ�2	C100	YES
            public String BYZD3 = "";                        // 38 �����ֶ�3	C100	YES

            @XStreamImplicit
            public List<Xmmx> xmmxes = new ArrayList<Xmmx>();

            @XStreamAlias("XMMX")
            public static class Xmmx implements Serializable {

                public String XMSX = "";                            // 39 ��Ŀ˳��	N1	NO		���ڱ�ʶ��Ŀ�ڽɿ����еĴ�ӡ�Ⱥ�˳��ֵ�ֱ�Ϊ1��2��3��4��5��6
                public String SRXMBM = "";                          // 40 ������Ŀ����	C42	NO	YES	����ɿ������ֹ�Ʊ����Ŀ����ĺ���λΪ����λ��Ŀ����У���롱
                public String SRXMMC = "";                          // 41 ������Ŀ����	C100	NO
                public String SJBZ = "";                            // 42 �սɱ�׼	C100	YES
                public String CFJDSBH = "";                         // 43 �������������	C30	YES  ��ûƱ��ר�У�����Ϊ�գ�
                public String FZXMMC = "";                          // 44 ������Ŀ����  C100	YES	���ܾ���ר��Ʊ�ݺ�����ǽ����ϻ���ר��Ʊ��ר�У�����Ϊ�գ�
                public String JNSHENGGK = "";                       // 45 ����ʡ����	N20,2 ��Ժר��Ʊ��ר�У�����Ϊ�գ�
                public String JNSHIGK = "";                         // 46 �����й���	N20,2 ��Ժר��Ʊ��ר�У�����Ϊ�գ�
                public String JZMJ = "";                            // 47 �������	N20,2 ����ǽ����ϻ���ר��Ʊ��ר�У�����Ϊ�գ�
                public String JLDW = "";                            // 48 ������λ	C40	YES
                public String SL = "";                              // 49 ����	N12,2	NO
                public String JE = "";                              // 50 ���	N12,2	NO
                public String BYZD1 = "";                           // 51 �����ֶ�1	C100	YES
                public String BYZD2 = "";                           // 52 �����ֶ�2	C100	YES
                public String BYZD3 = "";                           // 53 �����ֶ�3	C100	YES
            }
        }
    }


    @Override
    public String toString() {
        this.HEAD.CODE = "3002";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Tia3002.class);
        return "<?xml version=\"1.0\" encoding=\"GBK\"?>" + xs.toXML(this);
    }

    @Override
    public Tia3002 getTia(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Tia3002.class);
        return (Tia3002) xs.fromXML(xml);
    }
}
