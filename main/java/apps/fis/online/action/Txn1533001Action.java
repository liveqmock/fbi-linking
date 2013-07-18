package apps.fis.online.action;

import apps.fis.enums.TxnRtnCode;
import apps.fis.online.service.CommonService;
import apps.fis.online.service.DataExchangeService;
import apps.fis.domain.txn.Tia3001;
import apps.fis.domain.txn.Toa3001;
import apps.fis.online.service.FsqdfPaymentService;
import apps.fis.repository.model.FsQdfPaymentInfo;
import apps.fis.repository.model.FsQdfPaymentItem;
import common.utils.ObjectFieldsCopier;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//	应收信息申请
@Component
public class Txn1533001Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1533001Action.class);
    @Autowired
    private FsqdfPaymentService fsqdfPaymentService;
    @Autowired
    private DataExchangeService dataExchangeService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {

        // 解析报文体
        String[] fieldArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(new String(msg.msgBody), "|");
        // 票据种类
        String voucherType = fieldArray[0];
        // 缴款书编号
        String voucherNo = fieldArray[1];

        logger.info("[1533001缴款书信息查询][网点号]" + msg.branchID + "[柜员号]" + msg.tellerID
                + "[票据种类] " + voucherType + "  [缴款书编号] " + voucherNo);
        // 查询本地数据库的缴款单
        FsQdfPaymentInfo info = fsqdfPaymentService.qryPaymentInfo(voucherType, voucherNo);
        List<FsQdfPaymentItem> items = fsqdfPaymentService.qryPaymentItems(voucherType, voucherNo);

        if (info != null && items.size() > 0) {
            String rtnMsg = assembleStr(info, items);
            msg.msgBody = rtnMsg.getBytes();
            if ("1".equals(info.getHostBookFlag()) || "1".equals(info.getQdfBookFlag())) {
                msg.rtnCode = "0001";
            }
        } else {
            // 市财政 3001 交易
            Tia3001 tia3001 = new Tia3001();
            tia3001.BODY.DATA.PJZL = voucherType;
            tia3001.BODY.DATA.JKSBH = voucherNo;
            // 发送请求报文到财政局、获取响应
            Toa3001 toa3001 = (Toa3001) dataExchangeService.process(tia3001);
            // 查询到缴款单信息
            if ("0".equals(toa3001.HEAD.STATUS)) {
                // 保存
                info = new FsQdfPaymentInfo();
                ObjectFieldsCopier.copyFields(toa3001.BODY.DATA, info);
                info.setYhwdbm(msg.branchID);
                info.setOperid(msg.tellerID);
                // 主机记账标志 0未记账 1已记账
                info.setHostBookFlag("0");
                // 主机对账标志 0未对账 1已对账
                info.setHostChkFlag("0");
                // 市财政记账标志 0未记账 1已记账
                info.setQdfBookFlag("0");
                // 市财政对账标志 0未对账 1已对账
                info.setQdfChkFlag("0");
                items = new ArrayList<FsQdfPaymentItem>();
                for (Toa3001.Body.Data.Xmmx xmmx : toa3001.BODY.DATA.xmmxes) {
                    FsQdfPaymentItem item = new FsQdfPaymentItem();
                    ObjectFieldsCopier.copyFields(xmmx, item);
                    item.setPjzl(toa3001.BODY.DATA.PJZL);
                    item.setJksbh(toa3001.BODY.DATA.JKSBH);
                    items.add(item);
                }
                fsqdfPaymentService.insertPaymentInfoAndItems(info, items);
                String rtnMsg = assembleStr(info, items);
                msg.msgBody = rtnMsg.getBytes();
            } else if ("2".equals(toa3001.HEAD.STATUS)) {
                msg.msgBody = ("|||||||||||||||||||||||||||||||||||||00|2|0|" + toa3001.HEAD.MESSAGE + "|").getBytes();
            } else {
                msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
                msg.msgBody = toa3001.HEAD.MESSAGE.getBytes();
            }
        }
        return msg;
    }

    private String assembleStr(FsQdfPaymentInfo info, List<FsQdfPaymentItem> items) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(info.getPjzl()).append("|")                            // 票据种类
                .append(info.getJksbh()).append("|")                             // 缴款书编号
                .append(nullToEmpty(info.getXzqh())).append("|")                 // 行政区划
                .append(nullToEmpty(info.getZsfs())).append("|")                 // 征收方式
                .append(nullToEmpty(info.getJkfs())).append("|")                 // 缴款方式
                .append(castToDate8(info.getTzrq())).append("|")                 // 填制日期
                .append(nullToEmpty(info.getWtzsdwbm())).append("|")             // 委托执收单位编码
                .append(nullToEmpty(info.getWtzsdwmc())).append("|")             // 委托执收单位名称
                .append(nullToEmpty(info.getWtdwzgbmbm())).append("|")           // 委托主管部门单位编码
                .append(nullToEmpty(info.getWtdwzgbmmc())).append("|")           // 委托主管部门单位名称
                .append(nullToEmpty(info.getWtzsbz())).append("|")                // 委托征收标志
                .append(nullToEmpty(info.getStzsdwbm())).append("|")              // 受托执收单位编码
                .append(nullToEmpty(info.getStzsdwmc())).append("|")              // 受托执收单位名称
                .append(nullToEmpty(info.getStdwzgbmbm())).append("|")            // 受托单位主管部门编码
                .append(nullToEmpty(info.getStdwzgbmmc())).append("|")            // 受托单位主管部门名称
                .append(nullToEmpty(info.getZsdwzzjg())).append("|")              // 执收单位组织结构
                .append(nullToEmpty(info.getFkrmc())).append("|")                 // 付款人名称
                .append(nullToEmpty(info.getFkrkhh())).append("|")               // 付款人开户行
                .append(nullToEmpty(info.getFkrzh())).append("|")                // 付款人账号
                .append(nullToEmpty(info.getSkrmc())).append("|")                // 收款人名称
                .append(nullToEmpty(info.getSkrkhh())).append("|")               // 收款人开户行
                .append(nullToEmpty(info.getSkrzh())).append("|")                // 收款人账号
                .append(info.getZje().toString()).append("|")                    // 总金额
                .append(nullToEmpty(info.getBz())).append("|")                    // 备注
//                .append(nullToEmpty(info.getLxskbz())).append("|")               // 离线收款标志
                .append(nullToEmpty(info.getSgpbz())).append("|")                // 手工票标志
                .append(nullToEmpty(info.getFmyy())).append("|")                // 罚没原因
                .append(nullToEmpty(info.getFmly())).append("|")                // 罚没理由
                .append(nullToEmpty(info.getDsfy())).append("|")                // 代收法院
                .append(nullToEmpty(info.getBgrmc())).append("|")               // 被告人
                .append(nullToEmpty(info.getAy())).append("|")                 // 案由
                .append(nullToEmpty(info.getAh())).append("|")                 // 案号
                .append(nullToEmpty(info.getZdjh())).append("|")               // 字第几号
                .append(nullToEmpty(info.getBde())).append("|")                // 标的额
                .append(nullToEmpty(info.getXzspdtwym())).append("|")          // 行政审批大厅唯一码
                .append(nullToEmpty(info.getByzd1())).append("|")          // 备用字段1
                .append(nullToEmpty(info.getByzd2())).append("|")          // 备用字段2
                .append(nullToEmpty(info.getByzd3())).append("|");          // 备用字段3
        if ("1".equals(info.getHostBookFlag()) || "1".equals(info.getQdfBookFlag())) {
            strBuilder.append("10").append("|");
        } else {
            strBuilder.append("00").append("|");                     // 缴款标志
        }
        strBuilder.append("0").append("|")                           // status
                .append(String.valueOf(items.size())).append("|");   // 记录数
        // 明细
        for (FsQdfPaymentItem item : items) {
            strBuilder.append(nullToEmpty(item.getXmsx())).append(",")
                    .append(nullToEmpty(item.getSrxmbm())).append(",")
                    .append(nullToEmpty(item.getSrxmmc())).append(",")
                    .append(nullToEmpty(item.getZjxzbm())).append(",")
                    .append(nullToEmpty(item.getZjxzmc())).append(",")
                    .append(nullToEmpty(item.getYskmbm())).append(",")
                    .append(nullToEmpty(item.getYskmmc())).append(",")
                    .append(nullToEmpty(item.getSjbz())).append(",")
                    .append(nullToEmpty(item.getCfjdsbh())).append(",")
                    .append(nullToEmpty(item.getFzxmmc())).append(",")
                    .append(item.getJnshenggk() == null ? "" : String.valueOf(item.getJnshenggk())).append(",")
                    .append(item.getJnshigk() == null ? "" : String.valueOf(item.getJnshigk())).append(",")
                    .append(item.getJzmj() == null ? "" : String.valueOf(item.getJzmj())).append(",")
                    .append(nullToEmpty(item.getJldw())).append(",")
                    .append(item.getSl() == null ? "" : String.valueOf(item.getSl())).append(",")
                    .append(item.getJe() == null ? "" : String.valueOf(item.getJe())).append(",")
                    .append(nullToEmpty(item.getByzd1())).append(",")
                    .append(nullToEmpty(item.getByzd2())).append(",")
                    .append(nullToEmpty(item.getByzd3())).append("|");
        }
        return strBuilder.toString();
    }

    private String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    private String castToDate8(String srcDate) {
        if (srcDate == null) {
            return "        ";
        } else if (srcDate.length() > 8) {
            return srcDate.substring(0, 4) + srcDate.substring(5, 7) + srcDate.substring(8, 10);
        } else {
            return srcDate;
        }
    }
}
