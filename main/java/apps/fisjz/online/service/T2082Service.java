package apps.fisjz.online.service;

import apps.fisjz.domain.financebureau.FbPaynotesItemExport;
import apps.fisjz.domain.financebureau.FbPaynotesItemExport;
import apps.fisjz.domain.staring.T2082Response.TOA2082;
import apps.fisjz.domain.staring.T2082Response.TOA2082Detail;
import apps.fisjz.domain.staring.T2082Request.TIA2082;
import apps.fisjz.domain.staring.T2082Response.TOA2082;
import apps.fisjz.domain.staring.T2082Response.TOA2082Detail;
import apps.fisjz.enums.TxnRtnCode;
import apps.fisjz.repository.dao.common.ActChkMapper;
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
public class T2082Service {
    private static final Logger logger = LoggerFactory.getLogger(T2082Service.class);

    @Autowired
    ActChkMapper actChkMapper;


    //本地已有信息，不再去财政局查询，直接读取本地数据
    @SuppressWarnings("unchecked")
    @Transactional
    public void processTxn(Map paramMap) {
        TIA2082 tia = (TIA2082) paramMap.get("tia");
        String areaCode = tia.getAreaCode();
        String startDate = tia.getStartDate();
        String endDate = tia.getEndDate();

        List<FbPaynotesItemExport> itemExportList = actChkMapper.selectItemTotalExportForStaring(areaCode,startDate,endDate);
        TOA2082 toa = new TOA2082();
        List<TOA2082Detail> items =  new ArrayList<TOA2082Detail>();
        int infoNum = 0;
        infoNum = itemExportList.size();
        int itemNum = 0;
        for(FbPaynotesItemExport infoExport:itemExportList) {
            TOA2082Detail info= new TOA2082Detail();
            try {
//                BeanUtils.populate(item, (Map)itemExport);
                BeanUtils.copyProperties(info, infoExport);
            } catch (Exception e) {
                throw new RuntimeException("报文处理错误.", e);
            }

            String performDeptCode = infoExport.getPerformdeptcode();
            List<FbPaynotesItemExport> itemList = actChkMapper.selectItemExportForStaring(performDeptCode,areaCode,startDate,endDate);
            itemNum = itemNum +itemList.size();
            for(FbPaynotesItemExport itemExport:itemList){
                TOA2082Detail item= new TOA2082Detail();
                try {
//                    BeanUtils.populate(item, (Map)itemExport);
                    BeanUtils.copyProperties(item, itemExport);
                } catch (Exception e) {
                    throw new RuntimeException("报文处理错误.", e);
                }
                items.add(item);
            }
            items.add(info);
        }
        toa.setItemNum(""+(infoNum+itemNum));
        toa.setDetails(items);

        //组特色平台响应报文
        Map<String, Object> modelObjectsMap = new HashMap<String, Object>();
        modelObjectsMap.put(toa.getClass().getName(), toa);
        SeperatedTextDataFormat dataFormat = new SeperatedTextDataFormat("apps.fisjz.domain.staring.T2082Response");
        String result = null;
        try {
            result = (String) dataFormat.toMessage(modelObjectsMap);
        } catch (Exception e) {
            throw new RuntimeException("响应报文处理错误", e);
        }

        paramMap.put("rtnCode", TxnRtnCode.TXN_EXECUTE_SECCESS.getCode());
        paramMap.put("rtnMsg", result);
    }

    public int selectItemExportForStaring(Map paramMap){
        TIA2082 tia = (TIA2082) paramMap.get("tia");
        String areaCode = tia.getAreaCode();
        String startDate = tia.getStartDate();
        String endDate = tia.getEndDate();
        List<FbPaynotesItemExport> itemExportList = actChkMapper.selectItemTotalExportForStaring(areaCode, startDate, endDate);
        return itemExportList.size();
    }
}
