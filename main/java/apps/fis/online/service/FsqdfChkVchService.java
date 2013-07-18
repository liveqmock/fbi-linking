package apps.fis.online.service;

import apps.fis.repository.dao.FsQdfChkVchMapper;
import apps.fis.repository.dao.FsQdfSysCtlMapper;
import apps.fis.repository.model.FsQdfChkVch;
import apps.fis.repository.model.FsQdfChkVchExample;
import apps.fis.repository.model.FsQdfSysCtl;
import apps.fis.repository.model.FsQdfSysCtlExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * 市财政非税系统控制
*/
@Service
public class FsqdfChkVchService {
    private static final Logger logger = LoggerFactory.getLogger(FsqdfChkVchService.class);
    @Autowired
    private FsQdfChkVchMapper fsQdfChkVchMapper;

    public int insert(FsQdfChkVch vch) {
        return fsQdfChkVchMapper.insert(vch);
    }

    public int deleteVchsByChkDate(String chkDate) {
        FsQdfChkVchExample example = new FsQdfChkVchExample();
        example.createCriteria().andChkDateEqualTo(chkDate);
        return fsQdfChkVchMapper.deleteByExample(example);
    }
}
