package com.example.andruinotemp.model;

public class TemperatureHumidityMeasure {
    private String temperature;
    private String humidity;
    private String measureTimeStamp;

    public TemperatureHumidityMeasure(String temperature, String humidity, String measureTimeStamp){
        this.temperature = temperature;
        this.humidity = humidity;
        this.measureTimeStamp = measureTimeStamp;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getMeasureTimeStamp() {
        return measureTimeStamp;
    }

    public void setMeasureTimeStamp(String measureTimeStamp) {
        this.measureTimeStamp = measureTimeStamp;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        return this.temperature+"|"+this.humidity+"|"+this.measureTimeStamp; //To change body of generated methods, choose Tools | Templates.
    }
}
