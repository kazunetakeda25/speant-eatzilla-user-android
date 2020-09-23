package com.speant.user.Models;

public class UrlResponse {
    private boolean status;
    private UrlData details;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public UrlData getDetails() {
        return details;
    }

    public void setDetails(UrlData details) {
        this.details = details;
    }

    public class UrlData {
        private String about_us;
        private String help;
        private String faq;

        public String getAbout_us() {
            return about_us;
        }

        public void setAbout_us(String about_us) {
            this.about_us = about_us;
        }

        public String getHelp() {
            return help;
        }

        public void setHelp(String help) {
            this.help = help;
        }

        public String getFaq() {
            return faq;
        }

        public void setFaq(String faq) {
            this.faq = faq;
        }
    }
}
