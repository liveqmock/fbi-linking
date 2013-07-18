package apps.fis.online.action;

import apps.fis.domain.txn.Tia3028;
import apps.fis.domain.txn.Toa3028;
import apps.fis.enums.TxnRtnCode;
import apps.fis.online.service.DataExchangeService;
import apps.fis.online.service.FsqdfShareInfoService;
import apps.fis.repository.model.FsQdfShareInfo;
import gateway.domain.LFixedLengthProtocol;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// 查询分成信息
@Component
public class Txn1533028Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txn1533028Action.class);
    @Autowired
    private DataExchangeService dataExchangeService;
    @Autowired
    private FsqdfShareInfoService fsqdfShareInfoService;

    @Override
    public LFixedLengthProtocol process(LFixedLengthProtocol msg) throws Exception {
        // 解析报文体
        String applyDate = new String(msg.msgBody);
        if (applyDate.length() == 8) {
            applyDate = applyDate.substring(0, 4) + "-" + applyDate.substring(4, 6) + "-" + applyDate.substring(6, 8);
        }
        logger.info("[3028查询分成信息] [网点号]" + msg.branchID + "[柜员号]" + msg.tellerID + "[查询日期]" + applyDate);

        Tia3028 tia3028 = new Tia3028();
        tia3028.BODY.DATA.SQRQ = applyDate;

        Toa3028 toa3028 = (Toa3028) dataExchangeService.process(tia3028);
        if ("0".equals(toa3028.HEAD.STATUS)) {
            logger.info("[分成文件]" + toa3028.BODY.DATA.BPXXWJM);
            String fileData = dataExchangeService.getFtpfileData(toa3028.BODY.DATA.BPXXWJM);
            List<FsQdfShareInfo> infos = assembleShareInfos(applyDate, fileData);
            fsqdfShareInfoService.insert(infos);
            // 文件\0 结束，去掉\0
            fileData.substring(0, fileData.length() - 1);
            fileData.replace("|||", ",");
            fileData.replace("###\n", "|");
            String rtninfos = String.valueOf(infos.size()) + "|" + fileData.substring(13);
            msg.msgBody = rtninfos.getBytes();
        } else if ("EGOV-0003".equals(toa3028.HEAD.ERRCODE)) {
            msg.rtnCode = "0000";
            msg.msgBody = toa3028.HEAD.MESSAGE.getBytes();
        } else {
            msg.rtnCode = TxnRtnCode.TXN_EXECUTE_FAILED.getCode();
            msg.msgBody = toa3028.HEAD.MESSAGE.getBytes();
        }
        return msg;
    }

    private List<FsQdfShareInfo> assembleShareInfos(String date, String fileData) {
        if (StringUtils.isEmpty(fileData)) {
            return null;
        } else {
            fileData = new StringBuffer(fileData).delete((fileData.length() - 3), fileData.length()).toString();
            fileData = fileData.substring(13);
            String[] lines = StringUtils.splitByWholeSeparatorPreserveAllTokens(fileData, "###");
            List<FsQdfShareInfo> infos = new ArrayList<FsQdfShareInfo>();
            for (String line : lines) {
                String[] fields = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, "|||");
                FsQdfShareInfo info = new FsQdfShareInfo();
                info.setApplyDate(date);
                info.setPjzl(fields[0]);
                info.setJksbh(fields[1]);
                info.setHcxzqh(fields[2]);
                info.setHczgbmbm(fields[3]);
                info.setHczgbmmc(fields[4]);
                info.setHcdwbm(fields[5]);
                info.setHcdwmc(fields[6]);
                info.setHcxmbm(fields[7]);
                info.setHcxmmc(fields[8]);
                info.setHczjxzbm(fields[9]);
                info.setHczjxzmc(fields[10]);
                info.setYskmbm(fields[11]);
                info.setYskmmc(fields[12]);
                info.setHczhmc(fields[13]);
                info.setHczh(fields[14]);
                info.setHckhhmc(fields[15]);
                info.setHcyhlb(fields[16]);
                info.setHrxzqh(fields[17]);
                info.setFcje(StringUtils.isEmpty(fields[18]) ? null : new BigDecimal(fields[18]));
                infos.add(info);
            }
            return infos;
        }
    }

}
