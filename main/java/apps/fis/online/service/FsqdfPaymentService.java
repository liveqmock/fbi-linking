package apps.fis.online.service;

import apps.fis.enums.PendingVchFlag;
import apps.fis.repository.dao.FsQdfPaymentInfoMapper;
import apps.fis.repository.dao.FsQdfPaymentItemMapper;
import apps.fis.repository.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 13-6-4
 * Time: ����5:19
 * To change this template use File | Settings | File Templates.
 */
@Service
public class FsqdfPaymentService {
    private static final Logger logger = LoggerFactory.getLogger(FsqdfPaymentService.class);
    @Autowired
    private FsQdfPaymentInfoMapper paymentInfoMapper;
    @Autowired
    private FsQdfPaymentItemMapper paymentItemMapper;
    @Autowired
    private FsqdfPendingVchinfoService pendingVchinfoService;

    private boolean isExistPaymentInfo(String voucherType, String voucherNo) {
        FsQdfPaymentInfo info = qryPaymentInfo(voucherType, voucherNo);
        return (info == null || "2".equals(info.getPendingFlag())) ? false : true;
    }

    private boolean isExistPaymentItem(String voucherType, String voucherNo, String xmsx) {
        FsQdfPaymentItemExample example = new FsQdfPaymentItemExample();
        example.createCriteria().andPjzlEqualTo(voucherType).andJksbhEqualTo(voucherNo).andXmsxEqualTo(xmsx);
        return paymentItemMapper.selectByExample(example).size() > 0 ? true : false;
    }

    public FsQdfPaymentInfo qryPaymentInfo(String voucherType, String voucherNo) {
        FsQdfPaymentInfoExample example = new FsQdfPaymentInfoExample();
        example.createCriteria().andPjzlEqualTo(voucherType).andJksbhEqualTo(voucherNo).andHostBookFlagNotEqualTo("2");
        List<FsQdfPaymentInfo> infos = paymentInfoMapper.selectByExample(example);
        return infos.size() > 0 ? infos.get(0) : null;
    }

    // 8λ����, ��ѯ������������
    public List<FsQdfPaymentInfo> qryPaymentInfos(String areaCode, String date) {
        FsQdfPaymentInfoExample example = new FsQdfPaymentInfoExample();
        example.createCriteria().andXzqhEqualTo(areaCode).andQdfBookFlagEqualTo("1")
                .andHostChkFlagEqualTo("0").andChkActDtEqualTo(date).andPendingFlagNotEqualTo("2");
        return paymentInfoMapper.selectByExample(example);
    }

    // ��ѯPOS��ϸ
    public List<FsQdfPaymentInfo> qryPosPaymentInfos(String areaCode, String date) {
        FsQdfPaymentInfoExample example = new FsQdfPaymentInfoExample();
        example.createCriteria().andXzqhEqualTo(areaCode).andQdfBookFlagEqualTo("1")
                .andYhskrqEqualTo(date).andHostBookFlagEqualTo("0")
                .andPendingFlagEqualTo("0").andJkfsEqualTo("06");
        example.setOrderByClause("BYZD3,FKRZH");
        return paymentInfoMapper.selectByExample(example);
    }

    public List<FsQdfPaymentItem> qryPaymentItems(String voucherType, String voucherNo) {
        FsQdfPaymentItemExample example = new FsQdfPaymentItemExample();
        example.createCriteria().andPjzlEqualTo(voucherType).andJksbhEqualTo(voucherNo);
        example.setOrderByClause("xmsx");
        return paymentItemMapper.selectByExample(example);
    }

    // ����ɿ�����Ϣ����ϸ
    public int insertPaymentInfoAndItems(FsQdfPaymentInfo info, List<FsQdfPaymentItem> items) {
        int cnt = insertPaymentInfo(info);
        for (FsQdfPaymentItem item : items) {
            insertPaymentItem(item);
            cnt++;
        }
        return cnt;
    }

    public int insertPaymentItems(List<FsQdfPaymentItem> items) {
        int cnt = 0;
        for (FsQdfPaymentItem item : items) {
            cnt += insertPaymentItem(item);
        }
        return cnt;
    }

    // �������Ʊ���ֹ�Ʊ
    private int insertPaymentInfo(FsQdfPaymentInfo info) {
        if (isExistPaymentInfo(info.getPjzl(), info.getJksbh()))
            return 1;
        // �Ǵ���Ʊ
        info.setPendingFlag("0");
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String time = new SimpleDateFormat("HHmmss").format(new Date());
        info.setOperDate(date);
        info.setOperTime(time);
        return paymentInfoMapper.insert(info);
    }

    // �������Ʊ
    private int insertPendingPaymentInfo(FsQdfPaymentInfo info) {
        // �������˱�־ 0δ���� 1�Ѽ���
        info.setHostBookFlag("0");
        // �������˱�־ 0δ���� 1�Ѷ���
        info.setHostChkFlag("0");
        // �в������˱�־ 0δ���� 1�Ѽ���
        info.setQdfBookFlag("1");
        // �в������˱�־ 0δ���� 1�Ѷ���
        info.setQdfChkFlag("0");
        // ��Ʊ
        info.setPendingFlag("1");
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String time = new SimpleDateFormat("HHmmss").format(new Date());
        info.setOperDate(date);
        info.setOperTime(time);
        return paymentInfoMapper.insert(info);
    }

    public int insertPendingPaymentInfos(List<FsQdfPaymentInfo> infos) {
        int cnt = 0;
        for (FsQdfPaymentInfo info : infos) {
            if (!isExistPaymentInfo(info.getPjzl(), info.getJksbh())) {
                cnt += insertPendingPaymentInfo(info);
            } else {
                cnt++;
            }
        }
        return cnt;
    }

    private int insertPaymentItem(FsQdfPaymentItem item) {
        if (isExistPaymentItem(item.getPjzl(), item.getJksbh(), item.getXmsx()))
            return 1;
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String time = new SimpleDateFormat("HHmmss").format(new Date());
        item.setOperDate(date);
        item.setOperTime(time);
        return paymentItemMapper.insert(item);
    }

    public int updatePaymentInfoAndItems(FsQdfPaymentInfo info, List<FsQdfPaymentItem> items) {
        int cnt = updatePaymentInfo(info);
        for (FsQdfPaymentItem item : items) {
            updatePaymentItem(item);
            cnt++;
        }
        return cnt;
    }

    @Transactional
    public int deletePaymentInfo(String dbpywxh) {
        FsQdfPaymentInfoExample example = new FsQdfPaymentInfoExample();
        example.createCriteria().andDbpywxhEqualTo(dbpywxh).andPendingFlagNotEqualTo("2");
        List<FsQdfPaymentInfo> infos = paymentInfoMapper.selectByExample(example);
        for (FsQdfPaymentInfo info : infos) {
            info.setPendingFlag("2");
            updatePaymentInfo(info);
        }
        return infos.size();
    }

    // ����Ʊ��Ϊ�Ѷ���
    // ���Ʊ��Ϊ��Ʊ������´���Ʊ��ϢΪ�Ѷ��ˣ����ڱ���
    public int updatePaymentInfosToChkact(List<FsQdfPaymentInfo> infos, String date8) {
        int cnt = 0;
        for (FsQdfPaymentInfo info : infos) {
            info.setQdfChkFlag("1");
            if ("1".equals(info.getPendingFlag())) {
                FsQdfPendingVchInfo pendingVchInfo = pendingVchinfoService.qryVchInfo(info.getXzqh(), info.getDbpywxh());
                if (pendingVchInfo != null) {
                    pendingVchInfo.setChkFlag("1");
                    pendingVchInfo.setChkDate(date8);
                    pendingVchinfoService.update(pendingVchInfo);
                }
            }
            cnt += updatePaymentInfo(info);
        }
        return cnt;
    }

    public int updatePaymentInfo(FsQdfPaymentInfo info) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String time = new SimpleDateFormat("HHmmss").format(new Date());
        info.setOperDate(date);
        info.setOperTime(time);
        return paymentInfoMapper.updateByPrimaryKey(info);
    }

    public int updatePaymentItem(FsQdfPaymentItem item) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String time = new SimpleDateFormat("HHmmss").format(new Date());
        item.setOperDate(date);
        item.setOperTime(time);
        return paymentItemMapper.updateByPrimaryKey(item);
    }
}
