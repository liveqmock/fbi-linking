package apps.fis.online.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/*
 * 市财政非税
*/
@Service
public class OdsbService {
    private static final Logger logger = LoggerFactory.getLogger(OdsbService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 从ODSB取POS明细
    public List<Map<String, Object>> qryPosTxns(String date10) {
        String sql = "select POS_REF_NO as posrefno, CR_POS_TX_SQ_NO as arllogno, CR_TX_DT as txdt," +
                " CR_TX_AMT as tranamt from BF_EVT_CHN_POS_TRAD where EC_FLG = '0' " +
                " and CR_TX_DT = '" + date10 + "'" +
                " order by CR_POS_TX_SQ_NO ";
        return jdbcTemplate.queryForList(sql);
    }
}
