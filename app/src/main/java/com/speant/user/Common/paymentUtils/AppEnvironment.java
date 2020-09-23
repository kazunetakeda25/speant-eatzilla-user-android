package com.speant.user.Common.paymentUtils;


/**
 * Created by Rahul Hooda on 14/7/17.
 */
public enum AppEnvironment {

    SANDBOX {
        @Override
        public String merchant_Key() {
            return "BucIiJPg";
        }

        @Override
        public String merchant_ID() {
            return "6585523";
        }

        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return "cKdGMuJUOl";
        }

        @Override
        public boolean debug() {
            return true;
        }
    },
    PRODUCTION {
        @Override
        public String merchant_Key() {
            return "BucIiJPg";
        }
        @Override
        public String merchant_ID() {
            return "6585523";
        }
        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return "cKdGMuJUOl";
        }

        @Override
        public boolean debug() {
            return false;
        }
    };

    public abstract String merchant_Key();

    public abstract String merchant_ID();

    public abstract String furl();

    public abstract String surl();

    public abstract String salt();

    public abstract boolean debug();


}
