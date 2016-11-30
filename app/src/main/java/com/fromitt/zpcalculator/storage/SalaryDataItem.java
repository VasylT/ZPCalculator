package com.fromitt.zpcalculator.storage;

import java.util.Date;

/**
 *
 * Created by Tkachov Vasyl on 08.11.2016.
 */

public class SalaryDataItem {

    private final Date   date;
    private final float  salary;
    private final float  exchangeBuy1;
    private final float  exchangeSale1;
    private final float  moneyOnCard1;
    private final float  exchangeBuy2;
    private final float  exchangeSale2;
    private final float  moneyOnCard2;

    public SalaryDataItem(Date date, float salary, float exchangeBuy1, float exchangeSale1, float moneyOnCard1, float exchangeBuy2, float exchangeSale2, float moneyOnCard2) {
        this.date = date;
        this.salary = salary;
        this.exchangeBuy1 = exchangeBuy1;
        this.exchangeSale1 = exchangeSale1;
        this.moneyOnCard1 = moneyOnCard1;
        this.exchangeBuy2 = exchangeBuy2;
        this.exchangeSale2 = exchangeSale2;
        this.moneyOnCard2 = moneyOnCard2;
    }

    public Date getDate() {
        return date;
    }

    public float getSalary() {
        return salary;
    }

    public float getExchangeBuy1() {
        return exchangeBuy1;
    }

    public float getExchangeSale1() {
        return exchangeSale1;
    }

    public float getMoneyOnCard1() {
        return moneyOnCard1;
    }

    public float getExchangeBuy2() {
        return exchangeBuy2;
    }

    public float getExchangeSale2() {
        return exchangeSale2;
    }

    public float getMoneyOnCard2() {
        return moneyOnCard2;
    }
}
