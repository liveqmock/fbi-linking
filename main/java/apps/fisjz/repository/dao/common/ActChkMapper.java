package apps.fisjz.repository.dao.common;

import apps.fisjz.domain.financebureau.FbPaynotesInfo4ChkAct;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * User: zhanrui
 * Date: 13-9-27
 */
@Component
public interface ActChkMapper {

    @Select("select count(*) from fs_jzf_payment_info " +
            " where " +
            "   bankacctdate = #{txnDate} " +
            "   and recfeeflag = '1' " +       //已到账
            "   and archive_flag = '0' " +
            "   and area_code = #{areaCode}")
    int selectHostChkTotalCount(@Param("areaCode")String areaCode, @Param("txnDate")String txnDate);

    @Select("select " +
            " sum(case billtype when '2' then -to_number(amt) when '3' then -to_number(amt) else to_number(amt) end) as amt " +
            " from fs_jzf_payment_info " +
            " where " +
            "   bankacctdate = #{txnDate} " +
            "   and recfeeflag = '1' " +       //已到账
            "   and archive_flag = '0' " +
            "   and area_code = #{areaCode}")
    BigDecimal selectHostChkTotalAmt(@Param("areaCode")String areaCode, @Param("txnDate")String txnDate);


/*
    @Select("select * " +
            " where " +
            "   bankacctdate = #{txnDate} " +
            "   recfeeflag = '1' " +       //已到账
            "   archive_flag = '0' " +
            "   and area_code = #{areaCode}")
    List<FsJzfPaymentInfo> selectPaymentinfoList_HostActChk(String areaCode, String txnDate);
*/


    @Select("select a.*, " +
            "   (case a.billtype when '2' then '-'||b.amt when '3' then '-'||b.amt else b.amt end) as bankamt, " +
            "   b.nontaxprogramcode," +
            "   b.nontaxprogramname" +
            "  from fs_jzf_payment_info a" +
            "  left join fs_jzf_payment_item b" +
            "    on a.pkid = b.pkid_maininfo "+
            " where a.area_code = #{areaCode}" +
            "   and a.bankacctdate = #{txnDate} " +
            "   and a.archive_flag = '0'" +
            "   and a.recfeeflag = '1'")
    List<FbPaynotesInfo4ChkAct> selectDetailsForThirdParty(@Param("areaCode")String areaCode, @Param("txnDate")String txnDate);

    @Select("update fs_jzf_payment_info a set a.host_chk_flag = '1' " +
            " where a.area_code = #{areaCode}" +
            "   and a.bankacctdate = #{txnDate} " +
            "   and a.archive_flag = '0'" +
            "   and a.recfeeflag = '1'")
    void updateHostChkSuccessFlag(@Param("areaCode")String areaCode, @Param("txnDate")String txnDate);
}
