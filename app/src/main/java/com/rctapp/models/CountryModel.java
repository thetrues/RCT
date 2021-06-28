package com.rctapp.models;

public class CountryModel {
    String code, name, dial, currency_name, currency_symbol, currency_code;

    public CountryModel() {
    }

    public CountryModel(String code, String name, String dial, String currency_name, String currency_symbol, String currency_code) {
        this.code = code;
        this.name = name;
        this.dial = dial;
        this.currency_name = currency_name;
        this.currency_symbol = currency_symbol;
        this.currency_code = currency_code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDial() {
        return dial;
    }

    public void setDial(String dial) {
        this.dial = dial;
    }

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    @Override
    public String toString() {
        return "CountryModel{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", dial='" + dial + '\'' +
                ", currency_name='" + currency_name + '\'' +
                ", currency_symbol='" + currency_symbol + '\'' +
                ", currency_code='" + currency_code + '\'' +
                '}';
    }
}
