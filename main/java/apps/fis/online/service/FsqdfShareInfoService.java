package apps.fis.online.service;

import apps.fis.repository.dao.FsQdfShareInfoMapper;
import apps.fis.repository.model.FsQdfShareInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
 * 市财政非税业务处理
*/
@Service
public class FsqdfShareInfoService {
    private static final Logger logger = LoggerFactory.getLogger(FsqdfShareInfoService.class);
    @Autowired
    private FsQdfShareInfoMapper fsQdfShareInfoMapper;

    public int insert(List<FsQdfShareInfo> infos) {
        int cnt = 0;
        for(FsQdfShareInfo record : infos) {
            cnt += insert(record);
        }
        return cnt;
    }

    public int insert(FsQdfShareInfo record) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String time = new SimpleDateFormat("HHmmss").format(new Date());
        record.setOperDate(date);
        record.setOperTime(time);
        record.setShareStatus("0");
        return fsQdfShareInfoMapper.insert(record);
    }

}
