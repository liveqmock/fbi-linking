package apps.hmfsjm.repository.dao.common;

import apps.hmfsjm.repository.model.VoucherBill;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * Õ®”√
 */
public interface CommonMapper {

    @Select("select v.vch_num as vchnum, v.vch_sts as vchsts, b.billno as billno, " +
            " b.oper_date as txndate, b.txn_amt as txnamt from hmfs_jm_bill b " +
            " left join hmfs_jm_voucher v " +
            " on b.billno = v.billno " +
            " where b.oper_date = #{date8} and v.vch_sts != '1' " +
            " order by b.billno, v.vch_sts, v.vch_num desc")
    List<VoucherBill> qryVoucherBills(@Param("date8") String date8);

    @Select("select v.vch_num as vchnum, v.vch_sts as vchsts, b.billno as billno, " +
            " b.oper_date as txndate, b.txn_amt as txnamt from hmfs_jm_bill b  " +
            " left join hmfs_jm_voucher v" +
            " on b.billno = v.billno " +
            " where b.billno = #{billNo}")
    List<VoucherBill> qryVoucher(@Param("billNo") String billNo);

    @Select("select  count(v.vch_num) from hmfs_jm_voucher v " +
            " where v.txn_date = #{date8} and v.vch_sts = #{vchsts} ")
    String qryVchCnt(@Param("date8") String date8, @Param("vchsts") String vchsts);
}