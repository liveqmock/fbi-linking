package apps.fisjz.online.service;

import apps.fisjz.repository.dao.FsJzfBaseBankMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ������Ϣҵ����
 * User: zhanrui
 * Date: 13-9-23
 * Time: ����1:46
 */
@Service
public class BaseInfoService {
    private static final Logger logger = LoggerFactory.getLogger(BaseInfoService.class);
    @Autowired
    FsJzfBaseBankMapper baseBankMapper;
}
