package com.ruiqin.refreshdemo.module.trading.entiy;

import java.util.List;

/**
 * Created by ruiqin.shen
 * 类说明：响应交易明细
 */
public class RespTradingRecord {

    /**
     * PageNow : 1
     * PageSize : 10
     * Records : [{"Amount":2.78,"BillId":"36adc9c1-16f0-44e6-af17-a74a00108009","Status":"活动奖励","Time":"2017-04-03 01:00:04","TopCategoryId":1020,"Type":1}]
     * TotalCount : 1
     */

    private int PageNow;
    private int PageSize;
    private int TotalCount;
    private List<RecordsBean> Records;

    public int getPageNow() {
        return PageNow;
    }

    public void setPageNow(int PageNow) {
        this.PageNow = PageNow;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int PageSize) {
        this.PageSize = PageSize;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int TotalCount) {
        this.TotalCount = TotalCount;
    }

    public List<RecordsBean> getRecords() {
        return Records;
    }

    public void setRecords(List<RecordsBean> Records) {
        this.Records = Records;
    }

    public static class RecordsBean {
        /**
         * Amount : 2.78
         * BillId : 36adc9c1-16f0-44e6-af17-a74a00108009
         * Status : 活动奖励
         * Time : 2017-04-03 01:00:04
         * TopCategoryId : 1020
         * Type : 1
         */

        private double Amount;
        private String BillId;
        private String Status;
        private String Time;
        private int TopCategoryId;
        private int Type;

        public double getAmount() {
            return Amount;
        }

        public void setAmount(double Amount) {
            this.Amount = Amount;
        }

        public String getBillId() {
            return BillId;
        }

        public void setBillId(String BillId) {
            this.BillId = BillId;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getTime() {
            return Time.substring(0, Time.indexOf(" ")).replace('-', '年').replace('-', '月') + "日";
        }

        public void setTime(String Time) {
            this.Time = Time;
        }

        public int getTopCategoryId() {
            return TopCategoryId;
        }

        public void setTopCategoryId(int TopCategoryId) {
            this.TopCategoryId = TopCategoryId;
        }

        public int getType() {
            return Type;
        }

        public void setType(int Type) {
            this.Type = Type;
        }
    }
}
