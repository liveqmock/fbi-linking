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
 *  3.4.2.1	实收款申请
 */

@XStreamAlias("ROOT")
public class Tia3002 extends Tia {
    public TiaFisHeader HEAD = new TiaFisHeader();
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
            public String WTZSBZ = "";                       // 9 委托征收标志	C3	NO
            public String STZSDWBM = "";                     // 10 受托执收单位编码	C42	NO
            public String STZSDWMC = "";                     // 11 受托执收单位名称	C100	NO
            public String ZSDWZZJG = "";                     // 12 执收单位组织机构	C20	YES
            public String FKRMC = "";                        // 13 付款人名称	C80	NO
            public String FKRKHH = "";                       // 14 付款人开户行	C80	YES
            public String FKRZH = "";                        // 15 付款人账号	C40	YES
            public String SKRMC = "";                        // 16 收款人名称	C80	NO
            public String SKRKHH = "";                       // 17 收款人开户行 C80	YES
            public String SKRZH = "";                        // 18 收款人账号	C40	YES
            public String YHWDBM = "";                       // 19 银行网点编码 C20	YES
            public String ZJE = "";                          // 20 总金额	N20,2 YES
            public String BZ = "";                           // 21 备注	C255 YES
            public String JYM = "";                          // 22 校验码	C4	YES	全票面信息校验码，在缴款书为机打票时有该校验码
            public String LXSKBZ = "";                       // 23 离线收款标志 C3	NO 离线收款标志 0，不是 1，是
            public String SGPBZ = "";                        // 24 手工票标志	C3	NO
            public String YHSKRQ = "";                       // 25 银行收款日期	DATE	YES
            public String FMYY = "";                         // 26 罚没原因	C80	YES
            public String FMLY = "";                         // 27 罚没理由	C80	YES   罚没票据专有（不能为空）
            public String DSFY = "";                         // 28 代收法院	C80	YES		法院专用票据专有（不能为空）
            public String BGRMC = "";                        // 29 被告人	C80	YES		法院专用票据专有（不能为空）	BGRMC
            public String AY = "";                           // 30 案由	C80	YES		法院专用票据专有（不能为空）
            public String AH = "";                           // 31 案号	C20	YES		法院专用票据专有（不能为空）
            public String ZDJH = "";                         // 32 字第几号	C20	YES		法院专用票据专有（不能为空）
            public String BDE = "";                          // 33 标的额	  C20	YES		法院专用票据专有（不能为空）
            public String XZSPDTWYM = "";                    // 34 行政审批大厅唯一码	C50	YES		行政审批大厅专用（手工票时需要银行根据票据上信息自行录入）
            public String BMKYWXH = "";                      // 35 不明款业务序号	C50	YES		如果此缴款书为之前不明款转变成的收入时，此字段对应为之前不明款的业务序号。
            public String BYZD1 = "";                        // 36 备用字段1	C100	YES		缴款书主信息备用字段
            public String BYZD2 = "";                        // 37 备用字段2	C100	YES
            public String BYZD3 = "";                        // 38 备用字段3	C100	YES

            @XStreamImplicit
            public List<Xmmx> xmmxes = new ArrayList<Xmmx>();

            @XStreamAlias("XMMX")
            public static class Xmmx implements Serializable {

                public String XMSX = "";                            // 39 项目顺序	N1	NO		用于标识项目在缴款书中的打印先后顺序，值分别为1、2、3、4、5、6
                public String SRXMBM = "";                          // 40 收入项目编码	C42	NO	YES	如果缴款书是手工票，项目编码的后两位为“单位项目关联校验码”
                public String SRXMMC = "";                          // 41 收入项目名称	C100	NO
                public String SJBZ = "";                            // 42 收缴标准	C100	YES
                public String CFJDSBH = "";                         // 43 处罚决定书编码	C30	YES  罚没票据专有（不能为空）
                public String FZXMMC = "";                          // 44 辅助项目名称  C100	YES	接受捐赠专用票据和新型墙体材料基金专用票据专有（不能为空）
                public String JNSHENGGK = "";                       // 45 缴纳省国库	N20,2 法院专用票据专有（不能为空）
                public String JNSHIGK = "";                         // 46 缴纳市国库	N20,2 法院专用票据专有（不能为空）
                public String JZMJ = "";                            // 47 建筑面积	N20,2 新型墙体材料基金专用票据专有（不能为空）
                public String JLDW = "";                            // 48 计量单位	C40	YES
                public String SL = "";                              // 49 数量	N12,2	NO
                public String JE = "";                              // 50 金额	N12,2	NO
                public String BYZD1 = "";                           // 51 备用字段1	C100	YES
                public String BYZD2 = "";                           // 52 备用字段2	C100	YES
                public String BYZD3 = "";                           // 53 备用字段3	C100	YES
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
