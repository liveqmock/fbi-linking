package apps.fisjz.online.service;

import apps.fisjz.domain.financebureau.FbPaynotesInfoExport;
import apps.fisjz.domain.staring.T2010Response.TOA2010;
import apps.fisjz.domain.staring.T2010Response.TOA2010PaynotesInfo;
import apps.fisjz.domain.staring.T2010Response.TOA2010PaynotesItem;
import apps.fisjz.domain.staring.T2081Request.TIA2081;
import apps.fisjz.domain.staring.T2081Response.TOA2081;
import apps.fisjz.domain.staring.T2081Response.TOA2081Detail;
import apps.fisjz.enums.TxnRtnCode;
import apps.fisjz.repository.dao.FsJzfPaymentInfoMapper;
import apps.fisjz.repository.dao.FsJzfPaymentItemMapper;
import apps.fisjz.repository.dao.common.ActChkMapper;
import apps.fisjz.repository.model.FsJzfPaymentItem;
import common.dataformat.SeperatedTextDataFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缴款书信息查询处理
 * User: zhanrui
 * Date: 13-9-23
 */
@Service
public class T2081Service {
    private static final Logger logger = LoggerFactory.getLogger(T2081Service.class);

    @Autowired
    ActChkMapper actChkMapper;


    //本地已有信息，不再去财政局查询，直接读取本地数据
    @SuppressWarnings("unchecked")
    @Transactional
    public void processTxn(Map paramMap) {
        TIA2081 tia = (TIA2081) paramMap.get("tia");
        String areaCode = tia.getAreaCode();
        String startDate = tia.getStartDate();
        String endDate = tia.getEndDate();

        List<FbPaynotesInfoExport> infoExportList = actChkMapper.selectTotalExportForStaring(areaCode,startDate,endDate);
        TOA2081 toa = new TOA2081();
        List<TOA2081Detail> items =  new ArrayList<TOA2081Detail>();
        int infoNum = 0;
        infoNum = infoExportList.size();
        int itemNum = 0;
        for(FbPaynotesInfoExport infoExport:infoExportList) {
            TOA2081Detail info= new TOA2081Detail();
            try {
//                BeanUtils.populate(info, (Map)infoExport);
                BeanUtils.copyProperties(info, infoExport);
            } catch (Exception e) {
                throw new RuntimeException("报文处理错误.", e);
            }
            items.add(info);
            String performDeptCode = infoExport.getPerformdeptcode();
            List<FbPaynotesInfoExport> itemList = actChkMapper.selectInfoExportForStaring(performDeptCode,areaCode,startDate,endDate);
            itemNum = itemNum +itemList.size();
            for(FbPaynotesInfoExport itemExport:itemList){
                TOA2081Detail item= new TOA2081Detail();
                try {
//                    BeanUtils.populate(item, (Map)itemExport);
                    BeanUtils.copyProperties(item, itemExport);
                } catch (Exception e) {
                    throw new RuntimeException("报文处理错误.", e);
                }
                items.add(item);
            }
        }
        toa.setItemNum(""+(infoNum+itemNum));
        toa.setDetails(items);

        //组特色平台响应报文
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(toa.getClass().getName(), toa);
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2081Response");
        String result = null;
        try {
            result = (String) dataFormat.toMessage(modelObjectsMap);
        } catch (Exception e) {
            throw new RuntimeException("响应报文处理错误", e);
        }

        paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
        paramMap.put("rtnMsg", result);
    }

    public int selectTotalExportForStaring(Map paramMap){
        TIA2081 tia = (TIA2081) paramMap.get("tia");
        String areaCode = tia.getAreaCode();
        String startDate = tia.getStartDate();
        String endDate = tia.getEndDate();
        List<FbPaynotesInfoExport> infoExportList = actChkMapper.selectTotalExportForStaring(areaCode,startDate,endDate);
        return infoExportList.size();
    }
}
