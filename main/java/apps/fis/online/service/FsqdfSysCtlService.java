package apps.fis.online.service;

import apps.fis.SystemParameter;
import apps.fis.repository.dao.FsQdfSysCtlMapper;
import apps.fis.repository.model.FsQdfSysCtl;
import apps.fis.repository.model.FsQdfSysCtlExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
 * 市财政非税系统控制
*/
@Service
public class FsqdfSysCtlService {
    private static final Logger logger = LoggerFactory.getLogger(FsqdfSysCtlService.class);
    @Autowired
    private FsQdfSysCtlMapper fsQdfSysCtlMapper;

    public FsQdfSysCtl getFsQdfSysCtl(String sysSeqno) {
        FsQdfSysCtlExample example = new FsQdfSysCtlExample();
        example.createCriteria().andSctSeqnoEqualTo(sysSeqno);
        return fsQdfSysCtlMapper.selectByExample(example).get(0);
    }

    // 不明款业务序号
    public String getBmkTxnSeq() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssss");
        return SystemParameter.YHLB + "bmk" + sdf.format(new Date());
    }

    // 待补票业务序号
    public String getDbpTxnSeq() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssss");
        return SystemParameter.YHLB + "dbp" + sdf.format(new Date());
    }

    private String getTxnSeq(String sysSeqno) {
        FsQdfSysCtlExample example = new FsQdfSysCtlExample();
        example.createCriteria().andSctSeqnoEqualTo(sysSeqno);
        FsQdfSysCtl sysCtl = fsQdfSysCtlMapper.selectByExample(example).get(0);
        int txnseq = sysCtl.getTxnSeq();
        sysCtl.setTxnSeq(txnseq + 1);
        fsQdfSysCtlMapper.updateByExample(sysCtl, example);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date()) + txnseq;
    }

    public int update(FsQdfSysCtl sysCtl, String seqno) {
        FsQdfSysCtlExample example = new FsQdfSysCtlExample();
        example.createCriteria().andSctSeqnoEqualTo(seqno);
        return fsQdfSysCtlMapper.updateByExample(sysCtl, example);
    }
}
