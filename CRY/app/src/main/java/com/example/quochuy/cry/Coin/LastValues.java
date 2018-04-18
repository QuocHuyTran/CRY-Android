package com.example.quochuy.cry.Coin;

/**
 * Created by Quoc Huy on 4/5/2018.
 */

public class LastValues {
        private String timeStamp;
        private String price;
        private String marketcap;
        private String volume24;
        private String change_1h;
        private String change_24h;
        private String date;

        public LastValues(String timeStamp, String price, String marketcap, String volume24, String change_1h, String change_24h) {
            this.timeStamp = timeStamp;
            this.price = price;
            this.marketcap = marketcap;
            this.volume24 = volume24;
            this.change_1h = change_1h;
            this.change_24h = change_24h;
        }

        public LastValues(String timeStamp, String price, String marketcap, String volume24, String change_24h) {
            this.timeStamp = timeStamp;
            this.price = price;
            this.marketcap = marketcap;
            this.volume24 = volume24;
            this.change_24h = change_24h;
        }

        public LastValues() {

        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMarketcap() {
            return marketcap;
        }

        public void setMarketcap(String marketcap) {
            this.marketcap = marketcap;
        }

        public String getVolume24() {
            return volume24;
        }

        public void setVolume24(String volume24) {
            this.volume24 = volume24;
        }

        public String getChange_1h() {
            return change_1h;
        }

        public void setChange_1h(String change_1h) {
            this.change_1h = change_1h;
        }

        public String getChange_24h() {
            return change_24h;
        }

        public void setChange_24h(String getChange_24h) {
            this.change_24h = getChange_24h;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
}
