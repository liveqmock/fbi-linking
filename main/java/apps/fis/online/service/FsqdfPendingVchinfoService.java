package apps.fis.online.service;

import apps.fis.enums.PendingVchFlag;
import apps.fis.repository.dao.FsQdfPendingTxnMapper;
import apps.fis.repository.dao.FsQdfPendingVchInfoMapper;
import apps.fis.repository.model.FsQdfPendingTxn;
import apps.fis.repository.model.FsQdfPendingVchInfo;
import apps.fis.repository.model.FsQdfPendingVchInfoExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
 * ดýฒนฦฑ
*/
@Service
public class FsqdfPendingVchinfoService {
    private static final Logger logger = LoggerFactory.getLogger(FsqdfPendingVchinfoService.class);
    @Autowired
    private FsQdfPendingVchInfoMapper fsQdfPendingVchInfoMapper;

    public int insert(FsQdfPendingVchInfo vchInfo) {
        vchInfo.setQdfCfmFlag(PendingVchFlag.NOT_CONFIRM.getCode());
        vchInfo.setQdfCfmMsg(PendingVchFlag.NOT_CONFIRM.getTitle());
        vchInfo.setChkFlag("0");
        return fsQdfPendingVchInfoMapper.insert(vchInfo);
    }

    public List<FsQdfPendingVchInfo> qryPendingVchInfosByDate(String date8) {
        FsQdfPendingVchInfoExample example = new FsQdfPendingVchInfoExample();
        example.createCriteria().andOperDateEqualTo(date8);
        return fsQdfPendingVchInfoMapper.selectByExample(example);
    }

    public List<FsQdfPendingVchInfo> qryPendingVchInfos(PendingVchFlag flag) {
        FsQdfPendingVchInfoExample example = new FsQdfPendingVchInfoExample();
        example.createCriteria().andQdfCfmFlagEqualTo(flag.getCode());
        return fsQdfPendingVchInfoMapper.selectByExample(example);
    }

    public FsQdfPendingVchInfo qryVchInfo(String xzqh, String ywxh) {
        FsQdfPendingVchInfoExample example = new FsQdfPendingVchInfoExample();
        example.createCriteria().andDbpywxhEqualTo(ywxh).andXzqhEqualTo(xzqh);
        List<FsQdfPendingVchInfo> vchs = fsQdfPendingVchInfoMapper.selectByExample(example);
        return vchs.isEmpty() ? null : vchs.get(0);
    }

    public int update(FsQdfPendingVchInfo vchInfo) {
        return fsQdfPendingVchInfoMapper.updateByPrimaryKey(vchInfo);
    }
}
