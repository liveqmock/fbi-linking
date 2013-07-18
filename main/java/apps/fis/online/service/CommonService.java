package apps.fis.online.service;

import apps.fis.SystemParameter;
import apps.fis.repository.dao.FsQdfSysCtlMapper;
import apps.fis.repository.dao.common.CommonMapper;
import apps.fis.repository.model.FsQdfSysCtl;
import apps.fis.repository.model.FsQdfSysCtlExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * 市财政非税
*/
@Service
public class CommonService {
    private static final Logger logger = LoggerFactory.getLogger(CommonService.class);
    @Autowired
    private CommonMapper commonMapper;

    public BigDecimal qryTotalAmtByDate(String chkact) {
        return commonMapper.qryTotalAmtByDate(chkact);
    }

    public int qryCbsTxnCnt(String chkact) {
        return commonMapper.qryCbsTxnCnt(chkact);
    }
}
