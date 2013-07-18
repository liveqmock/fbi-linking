package apps.fis.online.service;

import apps.fis.repository.dao.FsQdfChkTxnMapper;
import apps.fis.repository.dao.common.CommonMapper;
import apps.fis.repository.model.FsQdfChkTxn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/*
 * 市财政非税主机对账明细表
*/
@Service
public class FsqdfChkTxnService {
    private static final Logger logger = LoggerFactory.getLogger(FsqdfChkTxnService.class);
    @Autowired
    private FsQdfChkTxnMapper chkTxnMapper;

    public int insert(FsQdfChkTxn record) {
        return chkTxnMapper.insert(record);
    }

    public int insert(List<FsQdfChkTxn> records) {
        int cnt = 0;
        for(FsQdfChkTxn record : records) {
            cnt += insert(record);
        }
        return cnt;
    }
}
