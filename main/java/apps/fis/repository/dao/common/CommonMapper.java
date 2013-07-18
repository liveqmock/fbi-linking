package apps.fis.repository.dao.common;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 */
@Component
public interface CommonMapper {

    @Select("select sum(t.zje) from FS_QDF_PAYMENT_INFO t" +
            " where t.chk_act_dt = #{chkact}" +
            " and t.host_book_flag = '1'")
    BigDecimal qryTotalAmtByDate(@Param("chkact") String chkact);

    @Select("select count(*) from FS_QDF_PAYMENT_INFO t" +
            " where t.chk_act_dt = #{chkact}" +
            " and t.host_book_flag = '1'")
    Integer qryCbsTxnCnt(@Param("chkact") String chkact);
}