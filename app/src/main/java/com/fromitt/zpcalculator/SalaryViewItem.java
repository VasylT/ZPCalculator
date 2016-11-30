package com.fromitt.zpcalculator;

/**
 *
 * Created by Tkachov Vasyl on 23.11.2016.
 */

public class SalaryViewItem {

    private float exchangeRateStart;
    private float exchangeRateEnd;
    private float moneyOnCard;
    private float cash;
    private float total;

    public SalaryViewItem(float exchangeRateStart, float exchangeRateEnd, float moneyOnCard, float cash, float total) {
        this.exchangeRateStart = exchangeRateStart;
        this.exchangeRateEnd = exchangeRateEnd;
        this.moneyOnCard = moneyOnCard;
        this.cash = cash;
        this.total = total;
    }

    public float getExchangeRateStart() {
        return exchangeRateStart;
    }

    public float getExchangeRateEnd() {
        return exchangeRateEnd;
    }

    public float getMoneyOnCard() {
        return moneyOnCard;
    }

    public float getCash() {
        return cash;
    }

    public float getTotal() {
        return total;
    }
}
