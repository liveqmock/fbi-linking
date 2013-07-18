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
 *  缴款书应收信息反馈
 */

@XStreamAlias("ROOT")
public class Toa3001 extends Toa {
    public ToaFisHeader HEAD = new ToaFisHeader();
    public Body BODY = new Body();

    public static class Body implements Serializable {

        public Data DATA = new Data();

        public static class Data implements Serializable {

            public String PJZL = "";                         // 1 票据种类	C10	NO	YES	共7种到银行缴款的票据种类（编码见附件一）
            public String JKSBH = "";                        // 2 缴款书编号	C20	NO	YES		JKSBH
            public String XZQH = "";                         // 3 行政区划	C6	NO		行政区划编码六位	XZQH
            public String ZSFS = "";                         // 4 征收方式	C3	NO 1 直接缴库 2 集中汇缴 （暂时没有集中汇缴业务）
            public String JKFS = "";                         // 5 缴款方式	C3	YES		为空，以银行实际收款方式为准
            public String TZRQ = "";                         // 6 填制日期	DATE	NO		开票日期
            public String WTZSDWBM = "";                     // 7 委托执收单位编码	C42	NO
            public String WTZSDWMC = "";                     // 8 委托执收单位名称	C100	NO
            public String WTDWZGBMBM = "";                   // 9 委托单位主管部门编码	C42	YES
            public String WTDWZGBMMC = "";                   // 10 委托单位主管部门名称	C100	YES
            public String WTZSBZ = "";                       // 11 委托征收标志	C3	NO
            public String STZSDWBM = "";                     // 12 受托执收单位编码	C42	NO
            public String STZSDWMC = "";                     // 13 受托执收单位名称	C100	NO
            public String STDWZGBMBM = "";                   // 14 受托单位主管部门编码	C42	YES
            public String STDWZGBMMC = "";                   // 15 受托单位主管部门名称	C100	YES
            public String ZSDWZZJG = "";                     // 16 执收单位组织机构	C20	YES
            public String FKRMC = "";                        // 17 付款人名称	C80	NO
            public String FKRKHH = "";                       // 18 付款人开户行	C80	YES
            public String FKRZH = "";                        // 19 付款人账号	C40	YES
            public String SKRMC = "";                        // 20 收款人名称	C80	NO
            public String SKRKHH = "";                       // 21 收款人开户行	C80	YES
            public String SKRZH = "";                        // 22 收款人账号	C40	YES
            public String ZJE = "";                          // 23 总金额	N20,2	YES
            public String BZ = "";                           // 24 备注	C255	YES
            public String SGPBZ = "";                        // 25 手工票标志	C3	NO
            public String FMYY = "";                         // 26 罚没原因	C80	YES
            public String FMLY = "";                         // 27 罚没理由	C80	YES   罚没票据专有（不能为空）
            public String DSFY = "";                         // 28 代收法院	C80	YES		法院专用票据专有（不能为空）
            public String BGRMC = "";                        // 29 被告人	C80	YES		法院专用票据专有（不能为空）	BGRMC
            public String AY = "";                           // 30 案由	C80	YES		法院专用票据专有（不能为空）
            public String AH = "";                           // 31 案号	C20	YES		法院专用票据专有（不能为空）
            public String ZDJH = "";                         // 32 字第几号	C20	YES		法院专用票据专有（不能为空）
            public String BDE = "";                          // 33 标的额	  C20	YES		法院专用票据专有（不能为空）
            public String XZSPDTWYM = "";                    // 34 行政审批大厅唯一码	C50	YES		行政审批大厅专用（手工票时需要银行根据票据上信息自行录入）
            public String BYZD1 = "";                        // 35 备用字段1	C100	YES		缴款书主信息备用字段
            public String BYZD2 = "";                        // 36 备用字段2	C100	YES
            public String BYZD3 = "";                        // 37 备用字段3	C100	YES

            @XStreamImplicit
            public List<Xmmx> xmmxes = new ArrayList<Xmmx>();

            @XStreamAlias("XMMX")
            public static class Xmmx implements Serializable {

                public String XMSX = "";                            // 38 项目顺序	N1	NO		用于标识项目在缴款书中的打印先后顺序，值分别为1、2、3、4、5、6
                public String SRXMBM = "";                          // 39 收入项目编码	C42	NO	YES	如果缴款书是手工票，项目编码的后两位为“单位项目关联校验码”
                public String SRXMMC = "";                          // 40 收入项目名称	C100	NO
                public String ZJXZBM = "";                          // 41 资金性质编码	C42	YES
                public String ZJXZMC = "";                          // 42 资金性质名称	C100	YES
                public String YSKMBM = "";                          // 43 预算科目编码	C20	YES
                public String YSKMMC = "";                          // 44 预算科目名称	C100	YES
                public String SJBZ = "";                            // 45 收缴标准	C100	YES
                public String CFJDSBH = "";                         // 46 处罚决定书编码	C30	YES  罚没票据专有（不能为空）
                public String FZXMMC = "";                          // 47 辅助项目名称  C100	YES	接受捐赠专用票据和新型墙体材料基金专用票据专有（不能为空）
                public String JNSHENGGK = "";                       // 48 缴纳省国库	N20,2 法院专用票据专有（不能为空）
                public String JNSHIGK = "";                         // 49 缴纳市国库	N20,2 法院专用票据专有（不能为空）
                public String JZMJ = "";                            // 50 建筑面积	N20,2 新型墙体材料基金专用票据专有（不能为空）
                public String JLDW = "";                            // 51 计量单位	C40	YES
                public String SL = "";                              // 52 数量	N12,2	NO
                public String JE = "";                              // 53 金额	N12,2	NO
                public String BYZD1 = "";                           // 54 备用字段1	C100	YES
                public String BYZD2 = "";                           // 55 备用字段2	C100	YES
                public String BYZD3 = "";                           // 56 备用字段3	C100	YES

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
