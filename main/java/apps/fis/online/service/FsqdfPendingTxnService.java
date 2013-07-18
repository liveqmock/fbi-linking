package apps.fis.online.service;

import apps.fis.repository.dao.FsQdfPendingTxnMapper;
import apps.fis.repository.dao.FsQdfShareInfoMapper;
import apps.fis.repository.model.FsQdfPendingTxn;
import apps.fis.repository.model.FsQdfPendingTxnExample;
import apps.fis.repository.model.FsQdfShareInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
 * ²»Ã÷¿î
*/
@Service
public class FsqdfPendingTxnService {
    private static final Logger logger = LoggerFactory.getLogger(FsqdfPendingTxnService.class);
    @Autowired
    private FsQdfPendingTxnMapper fsqdfPendingTxnMapper;

    public int insert(FsQdfPendingTxn txn) {
        return fsqdfPendingTxnMapper.insert(txn);
    }

    public boolean isExist(String bmkywxh) {
        FsQdfPendingTxnExample example = new FsQdfPendingTxnExample();
        example.createCriteria().andBmkywxhEqualTo(bmkywxh);
        return fsqdfPendingTxnMapper.countByExample(example) > 0;
    }
}
