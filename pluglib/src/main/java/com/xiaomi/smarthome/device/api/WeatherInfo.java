
package com.xiaomi.smarthome.device.api;

public class WeatherInfo {

    public static class Aqi {
        public String city;
        public String city_id;
        public String pub_time;
        public String aqi;
        public String pm25;
        public String pm10;
        public String so2;
        public String no2;
        public String src;
        public String spot;
    }

    public Aqi aqi = new Aqi();

}
