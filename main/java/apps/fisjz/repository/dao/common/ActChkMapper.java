package apps.fisjz.repository.dao.common;

import apps.fisjz.domain.financebureau.FbPaynotesInfo4ChkAct;
import apps.fisjz.domain.financebureau.FbPaynotesInfoExport;
import apps.fisjz.domain.financebureau.FbPaynotesItemExport;
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

    @Select("update fs_jzf_payment_info a set a.fb_chk_flag = '1' " +
            " where a.area_code = #{areaCode}" +
            "   and a.bankacctdate = #{txnDate} " +
            "   and a.archive_flag = '0'" +
            "   and a.recfeeflag = '1'")
    void updateThirdPartyChkSuccessFlag(@Param("areaCode")String areaCode, @Param("txnDate")String txnDate);

    @Select(" select c.performdeptcode,c.performdeptname, '合计' nontaxprogramname," +
            " count(*) amount,sum(nvl(c.amt,0)) amt, " +
            " sum(nvl(c.latefee,0)) latefee,(sum(nvl(c.amt,0))+sum(nvl(c.latefee,0))) total " +
            " from ( " +
            " select a.nontaxprogramcode,a.nontaxprogramname,a.amt,a.amount, " +
            " b.performdeptcode,b.performdeptname,b.latefee " +
            " from FS_JZF_PAYMENT_ITEM a " +
            " left outer join FS_JZF_PAYMENT_INFO b on a.pkid_maininfo = b.pkid " +
            " where b.bankacctdate>= #{startDate} and b.bankacctdate<= #{endDate} " +
            " and b.area_code= #{areaCode} "+
            " ) c " +
            " group by c.performdeptcode,c.performdeptname")
    List<FbPaynotesInfoExport> selectTotalExportForStaring(@Param("areaCode")String areaCode,
                                   @Param("startDate")String startDate, @Param("endDate")String endDate);

    @Select(" select '' performdeptname,a.nontaxprogramname,count(*)amount, " +
            " sum(nvl(a.amt,0)) amt,'' latefee,sum(nvl(a.amt,0)) total  " +
            " from FS_JZF_PAYMENT_ITEM a " +
            " where a.pkid_maininfo in " +
            " (select b.pkid from FS_JZF_PAYMENT_INFO b  " +
            " where b.performdeptcode= #{performdeptcode}" +
            " and b.bankacctdate>= #{startDate} and b.bankacctdate<= #{endDate} " +
            " and b.area_code= #{areaCode} )  " +
            " group by a.nontaxprogramname " )
    List<FbPaynotesInfoExport> selectInfoExportForStaring(@Param("performdeptcode")String performdeptcode,
                                                          @Param("areaCode")String areaCode,
                                                          @Param("startDate")String startDate,
                                                          @Param("endDate")String endDate);

    @Select(" select c.performdeptcode,'合计' performdeptname, sum(nvl(c.amt,0)) amt," +
            "sum(nvl(c.latefee, 0)) latefee " +
            " from " +
            " (select b.bankacctdate,b.performdeptcode,b.performdeptname," +
            " a.nontaxprogramcode,a.nontaxprogramname,nvl(a.amt,0) amt,b.payfeemethodname," +
            " b.payer,b.notescode,b.oper_bankid operbankid,nvl(b.latefee,0) latefee" +
            " from FS_JZF_PAYMENT_ITEM a" +
            " left outer join FS_JZF_PAYMENT_INFO b on a.pkid_maininfo = b.pkid" +
            " where b.bankacctdate>= #{startDate} and b.bankacctdate<= #{endDate} " +
            " and b.area_code= #{areaCode} ) c" +
            " group by c.performdeptcode" )
    List<FbPaynotesItemExport> selectItemTotalExportForStaring(@Param("areaCode")String areaCode,
                                                          @Param("startDate")String startDate,
                                                          @Param("endDate")String endDate);

    @Select(" select b.bankacctdate,b.performdeptcode,b.performdeptname," +
            " a.nontaxprogramcode,a.nontaxprogramname,nvl(a.amt,0) amt,b.payfeemethodname," +
            " b.payer,b.notescode,b.oper_bankid operbankid,nvl(b.latefee,0) latefee" +
            " from FS_JZF_PAYMENT_ITEM a" +
            " left outer join FS_JZF_PAYMENT_INFO b on a.pkid_maininfo = b.pkid" +
            " where b.bankacctdate>= #{startDate} and b.bankacctdate<= #{endDate} " +
            " and b.area_code= #{areaCode} and b.performdeptcode= #{performdeptcode}" )
    List<FbPaynotesItemExport> selectItemExportForStaring(@Param("performdeptcode")String performdeptcode,
                                                          @Param("areaCode")String areaCode,
                                                          @Param("startDate")String startDate,
                                                          @Param("endDate")String endDate);
}
