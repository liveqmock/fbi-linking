package apps.fisjz.repository.dao;

import apps.fisjz.repository.model.FsJzfPaymentInfo;
import apps.fisjz.repository.model.FsJzfPaymentInfoExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FsJzfPaymentInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_JZF_PAYMENT_INFO
     *
     * @mbggenerated Sun Sep 29 12:59:02 CST 2013
     */
    int countByExample(FsJzfPaymentInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_JZF_PAYMENT_INFO
     *
     * @mbggenerated Sun Sep 29 12:59:02 CST 2013
     */
    int deleteByExample(FsJzfPaymentInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_JZF_PAYMENT_INFO
     *
     * @mbggenerated Sun Sep 29 12:59:02 CST 2013
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_JZF_PAYMENT_INFO
     *
     * @mbggenerated Sun Sep 29 12:59:02 CST 2013
     */
    int insert(FsJzfPaymentInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_JZF_PAYMENT_INFO
     *
     * @mbggenerated Sun Sep 29 12:59:02 CST 2013
     */
    int insertSelective(FsJzfPaymentInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_JZF_PAYMENT_INFO
     *
     * @mbggenerated Sun Sep 29 12:59:02 CST 2013
     */
    List<FsJzfPaymentInfo> selectByExample(FsJzfPaymentInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_JZF_PAYMENT_INFO
     *
     * @mbggenerated Sun Sep 29 12:59:02 CST 2013
     */
    FsJzfPaymentInfo selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_JZF_PAYMENT_INFO
     *
     * @mbggenerated Sun Sep 29 12:59:02 CST 2013
     */
    int updateByExampleSelective(@Param("record") FsJzfPaymentInfo record, @Param("example") FsJzfPaymentInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_JZF_PAYMENT_INFO
     *
     * @mbggenerated Sun Sep 29 12:59:02 CST 2013
     */
    int updateByExample(@Param("record") FsJzfPaymentInfo record, @Param("example") FsJzfPaymentInfoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_JZF_PAYMENT_INFO
     *
     * @mbggenerated Sun Sep 29 12:59:02 CST 2013
     */
    int updateByPrimaryKeySelective(FsJzfPaymentInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table FIS.FS_JZF_PAYMENT_INFO
     *
     * @mbggenerated Sun Sep 29 12:59:02 CST 2013
     */
    int updateByPrimaryKey(FsJzfPaymentInfo record);
}