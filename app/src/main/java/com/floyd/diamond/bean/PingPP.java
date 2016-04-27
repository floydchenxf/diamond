package com.floyd.diamond.bean;

import java.util.List;

/**
 * Created by hy on 2016/3/29.
 */
public class PingPP {


    /**
     * id : ch_XPGC8OrjbHuDLubnrLrTmLOC
     * object : charge
     * created : 1459149931
     * livemode : true
     * paid : false
     * refunded : false
     * app : app_WbvfHKrHS8mP1e1C
     * channel : alipay
     * orderNo : 1234567
     * clientIp : 127.0.0.1
     * amount : 100
     * amountSettle : 100
     * currency : cny
     * subject : 全民模特充值测试
     * body : 金额[1.0]元
     * timePaid : null
     * timeExpire : 1459236331
     * timeSettle : null
     * transactionNo : null
     * refunds : {"object":"list","url":"/v1/charges/ch_XPGC8OrjbHuDLubnrLrTmLOC/refunds","hasMore":false,"data":[]}
     * amountRefunded : 0
     * failureCode : null
     * failureMsg : null
     * metadata : {}
     * credential : {"object":"credential","alipay":{"orderInfo":"service=\"mobile.securitypay.pay\"&_input_charset=\"utf-8\"&notify_url=\"https%3A%2F%2Fapi.pingxx.com%2Fnotify%2Fcharges%2Fch_XPGC8OrjbHuDLubnrLrTmLOC\"&partner=\"2088121298875911\"&out_trade_no=\"1234567\"&subject=\"全民模特充值测试\"&body=\"金额[1.0]元\"&total_fee=\"1.00\"&payment_type=\"1\"&seller_id=\"2088121298875911\"&it_b_pay=\"2016-03-29 15:25:31\"&sign=\"vsR429Ovor1Hfk%2BqyLM88%2FAiXdB1Wn6wsIXVb%2FF9IBMJQBtlhZHLPlHXaNUnv0MMS7yXgPDhhnuz%2BCCCS2lQi8XzrC1YB9NQ%2BSyO%2FpQJ7RSzISHBP2hFgqGC%2FC8JTn0h1DJdxthpRsaTkLzlzeRMDh0GQpxPCwPfn9sL5DhXOyg%3D\"&sign_type=\"RSA\""}}
     * extra : {}
     * description : null
     */

    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private String id;
        private String object;
        private int created;
        private boolean livemode;
        private boolean paid;
        private boolean refunded;
        private String app;
        private String channel;
        private String orderNo;
        private String clientIp;
        private int amount;
        private int amountSettle;
        private String currency;
        private String subject;
        private String body;
        private Object timePaid;
        private int timeExpire;
        private Object timeSettle;
        private Object transactionNo;
        /**
         * object : list
         * url : /v1/charges/ch_XPGC8OrjbHuDLubnrLrTmLOC/refunds
         * hasMore : false
         * data : []
         */

        private RefundsEntity refunds;
        private int amountRefunded;
        private Object failureCode;
        private Object failureMsg;
        private MetadataEntity metadata;
        /**
         * object : credential
         * alipay : {"orderInfo":"service=\"mobile.securitypay.pay\"&_input_charset=\"utf-8\"&notify_url=\"https%3A%2F%2Fapi.pingxx.com%2Fnotify%2Fcharges%2Fch_XPGC8OrjbHuDLubnrLrTmLOC\"&partner=\"2088121298875911\"&out_trade_no=\"1234567\"&subject=\"全民模特充值测试\"&body=\"金额[1.0]元\"&total_fee=\"1.00\"&payment_type=\"1\"&seller_id=\"2088121298875911\"&it_b_pay=\"2016-03-29 15:25:31\"&sign=\"vsR429Ovor1Hfk%2BqyLM88%2FAiXdB1Wn6wsIXVb%2FF9IBMJQBtlhZHLPlHXaNUnv0MMS7yXgPDhhnuz%2BCCCS2lQi8XzrC1YB9NQ%2BSyO%2FpQJ7RSzISHBP2hFgqGC%2FC8JTn0h1DJdxthpRsaTkLzlzeRMDh0GQpxPCwPfn9sL5DhXOyg%3D\"&sign_type=\"RSA\""}
         */

        private CredentialEntity credential;
        private ExtraEntity extra;
        private Object description;

        public void setId(String id) {
            this.id = id;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public void setCreated(int created) {
            this.created = created;
        }

        public void setLivemode(boolean livemode) {
            this.livemode = livemode;
        }

        public void setPaid(boolean paid) {
            this.paid = paid;
        }

        public void setRefunded(boolean refunded) {
            this.refunded = refunded;
        }

        public void setApp(String app) {
            this.app = app;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public void setClientIp(String clientIp) {
            this.clientIp = clientIp;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public void setAmountSettle(int amountSettle) {
            this.amountSettle = amountSettle;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public void setTimePaid(Object timePaid) {
            this.timePaid = timePaid;
        }

        public void setTimeExpire(int timeExpire) {
            this.timeExpire = timeExpire;
        }

        public void setTimeSettle(Object timeSettle) {
            this.timeSettle = timeSettle;
        }

        public void setTransactionNo(Object transactionNo) {
            this.transactionNo = transactionNo;
        }

        public void setRefunds(RefundsEntity refunds) {
            this.refunds = refunds;
        }

        public void setAmountRefunded(int amountRefunded) {
            this.amountRefunded = amountRefunded;
        }

        public void setFailureCode(Object failureCode) {
            this.failureCode = failureCode;
        }

        public void setFailureMsg(Object failureMsg) {
            this.failureMsg = failureMsg;
        }

        public void setMetadata(MetadataEntity metadata) {
            this.metadata = metadata;
        }

        public void setCredential(CredentialEntity credential) {
            this.credential = credential;
        }

        public void setExtra(ExtraEntity extra) {
            this.extra = extra;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public String getObject() {
            return object;
        }

        public int getCreated() {
            return created;
        }

        public boolean isLivemode() {
            return livemode;
        }

        public boolean isPaid() {
            return paid;
        }

        public boolean isRefunded() {
            return refunded;
        }

        public String getApp() {
            return app;
        }

        public String getChannel() {
            return channel;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public String getClientIp() {
            return clientIp;
        }

        public int getAmount() {
            return amount;
        }

        public int getAmountSettle() {
            return amountSettle;
        }

        public String getCurrency() {
            return currency;
        }

        public String getSubject() {
            return subject;
        }

        public String getBody() {
            return body;
        }

        public Object getTimePaid() {
            return timePaid;
        }

        public int getTimeExpire() {
            return timeExpire;
        }

        public Object getTimeSettle() {
            return timeSettle;
        }

        public Object getTransactionNo() {
            return transactionNo;
        }

        public RefundsEntity getRefunds() {
            return refunds;
        }

        public int getAmountRefunded() {
            return amountRefunded;
        }

        public Object getFailureCode() {
            return failureCode;
        }

        public Object getFailureMsg() {
            return failureMsg;
        }

        public MetadataEntity getMetadata() {
            return metadata;
        }

        public CredentialEntity getCredential() {
            return credential;
        }

        public ExtraEntity getExtra() {
            return extra;
        }

        public Object getDescription() {
            return description;
        }

        public static class RefundsEntity {
            private String object;
            private String url;
            private boolean hasMore;
            private List<?> data;

            public void setObject(String object) {
                this.object = object;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public void setHasMore(boolean hasMore) {
                this.hasMore = hasMore;
            }

            public void setData(List<?> data) {
                this.data = data;
            }

            public String getObject() {
                return object;
            }

            public String getUrl() {
                return url;
            }

            public boolean isHasMore() {
                return hasMore;
            }

            public List<?> getData() {
                return data;
            }

            @Override
            public String toString() {
                return super.toString();
            }
        }

        public static class MetadataEntity {
        }

        public static class CredentialEntity {
            /**
             * orderInfo : service="mobile.securitypay.pay"&_input_charset="utf-8"&notify_url="https%3A%2F%2Fapi.pingxx.com%2Fnotify%2Fcharges%2Fch_XPGC8OrjbHuDLubnrLrTmLOC"&partner="2088121298875911"&out_trade_no="1234567"&subject="全民模特充值测试"&body="金额[1.0]元"&total_fee="1.00"&payment_type="1"&seller_id="2088121298875911"&it_b_pay="2016-03-29 15:25:31"&sign="vsR429Ovor1Hfk%2BqyLM88%2FAiXdB1Wn6wsIXVb%2FF9IBMJQBtlhZHLPlHXaNUnv0MMS7yXgPDhhnuz%2BCCCS2lQi8XzrC1YB9NQ%2BSyO%2FpQJ7RSzISHBP2hFgqGC%2FC8JTn0h1DJdxthpRsaTkLzlzeRMDh0GQpxPCwPfn9sL5DhXOyg%3D"&sign_type="RSA"
             */

            private AlipayEntity alipay;

            public void setAlipay(AlipayEntity alipay) {
                this.alipay = alipay;
            }

            public AlipayEntity getAlipay() {
                return alipay;
            }

            @Override
            public String toString() {
                return super.toString();
            }

            public static class AlipayEntity {
            }
        }

        public static class ExtraEntity {
        }

        @Override
        public String toString() {
            return "{" +
                    "id='" + id + '\'' +
                    ", object='" + object + '\'' +
                    ", created=" + created +
                    ", livemode=" + livemode +
                    ", paid=" + paid +
                    ", refunded=" + refunded +
                    ", app='" + app + '\'' +
                    ", channel='" + channel + '\'' +
                    ", orderNo='" + orderNo + '\'' +
                    ", clientIp='" + clientIp + '\'' +
                    ", amount=" + amount +
                    ", amountSettle=" + amountSettle +
                    ", currency='" + currency + '\'' +
                    ", subject='" + subject + '\'' +
                    ", body='" + body + '\'' +
                    ", timePaid=" + timePaid +
                    ", timeExpire=" + timeExpire +
                    ", timeSettle=" + timeSettle +
                    ", transactionNo=" + transactionNo +
                    ", refunds=" + refunds +
                    ", amountRefunded=" + amountRefunded +
                    ", failureCode=" + failureCode +
                    ", failureMsg=" + failureMsg +
                    ", metadata=" + metadata +
                    ", credential=" + credential +
                    ", extra=" + extra +
                    ", description=" + description +
                    '}';
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
