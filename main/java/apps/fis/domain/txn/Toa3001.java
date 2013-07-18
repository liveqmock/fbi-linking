package apps.fis.domain.txn;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import apps.fis.domain.base.Toa;
import apps.fis.domain.base.ToaFisHeader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  �ɿ���Ӧ����Ϣ����
 */

@XStreamAlias("ROOT")
public class Toa3001 extends Toa {
    public ToaFisHeader HEAD = new ToaFisHeader();
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
            public String WTDWZGBMBM = "";                   // 9 ί�е�λ���ܲ��ű���	C42	YES
            public String WTDWZGBMMC = "";                   // 10 ί�е�λ���ܲ�������	C100	YES
            public String WTZSBZ = "";                       // 11 ί�����ձ�־	C3	NO
            public String STZSDWBM = "";                     // 12 ����ִ�յ�λ����	C42	NO
            public String STZSDWMC = "";                     // 13 ����ִ�յ�λ����	C100	NO
            public String STDWZGBMBM = "";                   // 14 ���е�λ���ܲ��ű���	C42	YES
            public String STDWZGBMMC = "";                   // 15 ���е�λ���ܲ�������	C100	YES
            public String ZSDWZZJG = "";                     // 16 ִ�յ�λ��֯����	C20	YES
            public String FKRMC = "";                        // 17 ����������	C80	NO
            public String FKRKHH = "";                       // 18 �����˿�����	C80	YES
            public String FKRZH = "";                        // 19 �������˺�	C40	YES
            public String SKRMC = "";                        // 20 �տ�������	C80	NO
            public String SKRKHH = "";                       // 21 �տ��˿�����	C80	YES
            public String SKRZH = "";                        // 22 �տ����˺�	C40	YES
            public String ZJE = "";                          // 23 �ܽ��	N20,2	YES
            public String BZ = "";                           // 24 ��ע	C255	YES
            public String SGPBZ = "";                        // 25 �ֹ�Ʊ��־	C3	NO
            public String FMYY = "";                         // 26 ��ûԭ��	C80	YES
            public String FMLY = "";                         // 27 ��û����	C80	YES   ��ûƱ��ר�У�����Ϊ�գ�
            public String DSFY = "";                         // 28 ���շ�Ժ	C80	YES		��Ժר��Ʊ��ר�У�����Ϊ�գ�
            public String BGRMC = "";                        // 29 ������	C80	YES		��Ժר��Ʊ��ר�У�����Ϊ�գ�	BGRMC
            public String AY = "";                           // 30 ����	C80	YES		��Ժר��Ʊ��ר�У�����Ϊ�գ�
            public String AH = "";                           // 31 ����	C20	YES		��Ժר��Ʊ��ר�У�����Ϊ�գ�
            public String ZDJH = "";                         // 32 �ֵڼ���	C20	YES		��Ժר��Ʊ��ר�У�����Ϊ�գ�
            public String BDE = "";                          // 33 ��Ķ�	  C20	YES		��Ժר��Ʊ��ר�У�����Ϊ�գ�
            public String XZSPDTWYM = "";                    // 34 ������������Ψһ��	C50	YES		������������ר�ã��ֹ�Ʊʱ��Ҫ���и���Ʊ������Ϣ����¼�룩
            public String BYZD1 = "";                        // 35 �����ֶ�1	C100	YES		�ɿ�������Ϣ�����ֶ�
            public String BYZD2 = "";                        // 36 �����ֶ�2	C100	YES
            public String BYZD3 = "";                        // 37 �����ֶ�3	C100	YES

            @XStreamImplicit
            public List<Xmmx> xmmxes = new ArrayList<Xmmx>();

            @XStreamAlias("XMMX")
            public static class Xmmx implements Serializable {

                public String XMSX = "";                            // 38 ��Ŀ˳��	N1	NO		���ڱ�ʶ��Ŀ�ڽɿ����еĴ�ӡ�Ⱥ�˳��ֵ�ֱ�Ϊ1��2��3��4��5��6
                public String SRXMBM = "";                          // 39 ������Ŀ����	C42	NO	YES	����ɿ������ֹ�Ʊ����Ŀ����ĺ���λΪ����λ��Ŀ����У���롱
                public String SRXMMC = "";                          // 40 ������Ŀ����	C100	NO
                public String ZJXZBM = "";                          // 41 �ʽ����ʱ���	C42	YES
                public String ZJXZMC = "";                          // 42 �ʽ���������	C100	YES
                public String YSKMBM = "";                          // 43 Ԥ���Ŀ����	C20	YES
                public String YSKMMC = "";                          // 44 Ԥ���Ŀ����	C100	YES
                public String SJBZ = "";                            // 45 �սɱ�׼	C100	YES
                public String CFJDSBH = "";                         // 46 �������������	C30	YES  ��ûƱ��ר�У�����Ϊ�գ�
                public String FZXMMC = "";                          // 47 ������Ŀ����  C100	YES	���ܾ���ר��Ʊ�ݺ�����ǽ����ϻ���ר��Ʊ��ר�У�����Ϊ�գ�
                public String JNSHENGGK = "";                       // 48 ����ʡ����	N20,2 ��Ժר��Ʊ��ר�У�����Ϊ�գ�
                public String JNSHIGK = "";                         // 49 �����й���	N20,2 ��Ժר��Ʊ��ר�У�����Ϊ�գ�
                public String JZMJ = "";                            // 50 �������	N20,2 ����ǽ����ϻ���ר��Ʊ��ר�У�����Ϊ�գ�
                public String JLDW = "";                            // 51 ������λ	C40	YES
                public String SL = "";                              // 52 ����	N12,2	NO
                public String JE = "";                              // 53 ���	N12,2	NO
                public String BYZD1 = "";                           // 54 �����ֶ�1	C100	YES
                public String BYZD2 = "";                           // 55 �����ֶ�2	C100	YES
                public String BYZD3 = "";                           // 56 �����ֶ�3	C100	YES

            }
        }
    }

    @Override
    public String toString() {
        this.HEAD.CODE = "3001";
        XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("$", "_");
        HierarchicalStreamDriver hierarchicalStreamDriver = new XppDriver(replacer);
        XStream xs = new XStream(hierarchicalStreamDriver);
        xs.processAnnotations(Toa3001.class);
        return xs.toXML(this);
    }

    @Override
    public Toa3001 getToa(String xml) {
        XStream xs = new XStream(new DomDriver());
        xs.processAnnotations(Toa3001.class);
        return (Toa3001) xs.fromXML(xml);
    }
}
