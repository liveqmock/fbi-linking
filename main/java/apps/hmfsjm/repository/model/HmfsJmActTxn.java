package apps.hmfsjm.repository.model;

import java.math.BigDecimal;

public class HmfsJmActTxn {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column FIS.HMFS_JM_ACT_TXN.PKID
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    private String pkid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column FIS.HMFS_JM_ACT_TXN.TXN_AMT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    private BigDecimal txnAmt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column FIS.HMFS_JM_ACT_TXN.AREA_ACCOUNT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    private String areaAccount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column FIS.HMFS_JM_ACT_TXN.HOUSE_ACCOUNT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    private String houseAccount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column FIS.HMFS_JM_ACT_TXN.OWNER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    private String owner;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column FIS.HMFS_JM_ACT_TXN.RESERVE
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    private String reserve;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column FIS.HMFS_JM_ACT_TXN.OPER_DATE
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    private String operDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column FIS.HMFS_JM_ACT_TXN.OPER_TIME
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    private String operTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column FIS.HMFS_JM_ACT_TXN.OPER_ID
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    private String operId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column FIS.HMFS_JM_ACT_TXN.REMARK
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column FIS.HMFS_JM_ACT_TXN.TXN_CODE
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    private String txnCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column FIS.HMFS_JM_ACT_TXN.ACT_SERIAL_NO
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    private String actSerialNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column FIS.HMFS_JM_ACT_TXN.BOOK_TYPE
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    private String bookType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column FIS.HMFS_JM_ACT_TXN.BILLNO
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    private String billno;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column FIS.HMFS_JM_ACT_TXN.DEPT_ID
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    private String deptId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column FIS.HMFS_JM_ACT_TXN.PKID
     *
     * @return the value of FIS.HMFS_JM_ACT_TXN.PKID
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public String getPkid() {
        return pkid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column FIS.HMFS_JM_ACT_TXN.PKID
     *
     * @param pkid the value for FIS.HMFS_JM_ACT_TXN.PKID
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public void setPkid(String pkid) {
        this.pkid = pkid == null ? null : pkid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column FIS.HMFS_JM_ACT_TXN.TXN_AMT
     *
     * @return the value of FIS.HMFS_JM_ACT_TXN.TXN_AMT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public BigDecimal getTxnAmt() {
        return txnAmt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column FIS.HMFS_JM_ACT_TXN.TXN_AMT
     *
     * @param txnAmt the value for FIS.HMFS_JM_ACT_TXN.TXN_AMT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public void setTxnAmt(BigDecimal txnAmt) {
        this.txnAmt = txnAmt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column FIS.HMFS_JM_ACT_TXN.AREA_ACCOUNT
     *
     * @return the value of FIS.HMFS_JM_ACT_TXN.AREA_ACCOUNT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public String getAreaAccount() {
        return areaAccount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column FIS.HMFS_JM_ACT_TXN.AREA_ACCOUNT
     *
     * @param areaAccount the value for FIS.HMFS_JM_ACT_TXN.AREA_ACCOUNT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public void setAreaAccount(String areaAccount) {
        this.areaAccount = areaAccount == null ? null : areaAccount.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column FIS.HMFS_JM_ACT_TXN.HOUSE_ACCOUNT
     *
     * @return the value of FIS.HMFS_JM_ACT_TXN.HOUSE_ACCOUNT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public String getHouseAccount() {
        return houseAccount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column FIS.HMFS_JM_ACT_TXN.HOUSE_ACCOUNT
     *
     * @param houseAccount the value for FIS.HMFS_JM_ACT_TXN.HOUSE_ACCOUNT
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public void setHouseAccount(String houseAccount) {
        this.houseAccount = houseAccount == null ? null : houseAccount.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column FIS.HMFS_JM_ACT_TXN.OWNER
     *
     * @return the value of FIS.HMFS_JM_ACT_TXN.OWNER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public String getOwner() {
        return owner;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column FIS.HMFS_JM_ACT_TXN.OWNER
     *
     * @param owner the value for FIS.HMFS_JM_ACT_TXN.OWNER
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column FIS.HMFS_JM_ACT_TXN.RESERVE
     *
     * @return the value of FIS.HMFS_JM_ACT_TXN.RESERVE
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public String getReserve() {
        return reserve;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column FIS.HMFS_JM_ACT_TXN.RESERVE
     *
     * @param reserve the value for FIS.HMFS_JM_ACT_TXN.RESERVE
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public void setReserve(String reserve) {
        this.reserve = reserve == null ? null : reserve.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column FIS.HMFS_JM_ACT_TXN.OPER_DATE
     *
     * @return the value of FIS.HMFS_JM_ACT_TXN.OPER_DATE
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public String getOperDate() {
        return operDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column FIS.HMFS_JM_ACT_TXN.OPER_DATE
     *
     * @param operDate the value for FIS.HMFS_JM_ACT_TXN.OPER_DATE
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public void setOperDate(String operDate) {
        this.operDate = operDate == null ? null : operDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column FIS.HMFS_JM_ACT_TXN.OPER_TIME
     *
     * @return the value of FIS.HMFS_JM_ACT_TXN.OPER_TIME
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public String getOperTime() {
        return operTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column FIS.HMFS_JM_ACT_TXN.OPER_TIME
     *
     * @param operTime the value for FIS.HMFS_JM_ACT_TXN.OPER_TIME
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public void setOperTime(String operTime) {
        this.operTime = operTime == null ? null : operTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column FIS.HMFS_JM_ACT_TXN.OPER_ID
     *
     * @return the value of FIS.HMFS_JM_ACT_TXN.OPER_ID
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public String getOperId() {
        return operId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column FIS.HMFS_JM_ACT_TXN.OPER_ID
     *
     * @param operId the value for FIS.HMFS_JM_ACT_TXN.OPER_ID
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public void setOperId(String operId) {
        this.operId = operId == null ? null : operId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column FIS.HMFS_JM_ACT_TXN.REMARK
     *
     * @return the value of FIS.HMFS_JM_ACT_TXN.REMARK
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column FIS.HMFS_JM_ACT_TXN.REMARK
     *
     * @param remark the value for FIS.HMFS_JM_ACT_TXN.REMARK
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column FIS.HMFS_JM_ACT_TXN.TXN_CODE
     *
     * @return the value of FIS.HMFS_JM_ACT_TXN.TXN_CODE
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public String getTxnCode() {
        return txnCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column FIS.HMFS_JM_ACT_TXN.TXN_CODE
     *
     * @param txnCode the value for FIS.HMFS_JM_ACT_TXN.TXN_CODE
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public void setTxnCode(String txnCode) {
        this.txnCode = txnCode == null ? null : txnCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column FIS.HMFS_JM_ACT_TXN.ACT_SERIAL_NO
     *
     * @return the value of FIS.HMFS_JM_ACT_TXN.ACT_SERIAL_NO
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public String getActSerialNo() {
        return actSerialNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column FIS.HMFS_JM_ACT_TXN.ACT_SERIAL_NO
     *
     * @param actSerialNo the value for FIS.HMFS_JM_ACT_TXN.ACT_SERIAL_NO
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public void setActSerialNo(String actSerialNo) {
        this.actSerialNo = actSerialNo == null ? null : actSerialNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column FIS.HMFS_JM_ACT_TXN.BOOK_TYPE
     *
     * @return the value of FIS.HMFS_JM_ACT_TXN.BOOK_TYPE
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public String getBookType() {
        return bookType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column FIS.HMFS_JM_ACT_TXN.BOOK_TYPE
     *
     * @param bookType the value for FIS.HMFS_JM_ACT_TXN.BOOK_TYPE
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public void setBookType(String bookType) {
        this.bookType = bookType == null ? null : bookType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column FIS.HMFS_JM_ACT_TXN.BILLNO
     *
     * @return the value of FIS.HMFS_JM_ACT_TXN.BILLNO
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public String getBillno() {
        return billno;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column FIS.HMFS_JM_ACT_TXN.BILLNO
     *
     * @param billno the value for FIS.HMFS_JM_ACT_TXN.BILLNO
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public void setBillno(String billno) {
        this.billno = billno == null ? null : billno.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column FIS.HMFS_JM_ACT_TXN.DEPT_ID
     *
     * @return the value of FIS.HMFS_JM_ACT_TXN.DEPT_ID
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column FIS.HMFS_JM_ACT_TXN.DEPT_ID
     *
     * @param deptId the value for FIS.HMFS_JM_ACT_TXN.DEPT_ID
     *
     * @mbggenerated Wed Nov 06 15:07:04 CST 2013
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId == null ? null : deptId.trim();
    }
}