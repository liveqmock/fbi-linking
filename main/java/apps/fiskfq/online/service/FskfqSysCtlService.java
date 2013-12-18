package apps.fiskfq.online.service;

import apps.fiskfq.repository.MybatisManager;
import apps.fiskfq.repository.dao.FsKfqSysCtlMapper;
import apps.fiskfq.repository.model.FsKfqSysCtl;
import apps.fiskfq.repository.model.FsKfqSysCtlExample;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 开发区非税系统参数控制表
 */
public class FskfqSysCtlService {
    private static final Logger logger = LoggerFactory.getLogger(FskfqSysCtlService.class);
    MybatisManager manager = new MybatisManager();

    public FsKfqSysCtl getSysCtl(String bankId) {

        SqlSession session = manager.getSessionFactory().openSession();
        FsKfqSysCtlMapper mapper = session.getMapper(FsKfqSysCtlMapper.class);
        FsKfqSysCtlExample example = new FsKfqSysCtlExample();
        example.createCriteria().andBankIdEqualTo(bankId);
        return mapper.selectByExample(example).get(0);

    }

}
