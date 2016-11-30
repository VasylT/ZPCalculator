package com.fromitt.zpcalculator.storage;

import com.fromitt.zpcalculator.SalaryViewItem;

import java.math.BigDecimal;

/**
 *
 * Created by Tkachov Vasyl on 23.11.2016.
 */

public class Calculator {

    private static final int mCurrencyDecimalsCount = 4;
    private static final int mDecimalsCount = 2;

    public static SalaryViewItem calculateSalaryValues(SalaryDataItem item) {
        float exchangeRateStart = (item.getExchangeBuy1() + item.getExchangeSale1()) / 2;
        float exchangeRateEnd = (item.getExchangeBuy2() + item.getExchangeSale2()) / 2;

        float cardCurrencyStart = 0;
        if (exchangeRateStart != 0) {
            cardCurrencyStart = item.getMoneyOnCard1() / exchangeRateStart;
        }
        float cardCurrencyEnd = 0;
        if (exchangeRateEnd != 0) {
            cardCurrencyEnd = item.getMoneyOnCard2() / exchangeRateEnd;
        }

        float moneyOnCard = item.getMoneyOnCard1() + item.getMoneyOnCard2();
        float moneyOnCardCurrency = cardCurrencyStart + cardCurrencyEnd;

        float cashCurrency = item.getSalary() - moneyOnCardCurrency;
        float cash = cashCurrency * exchangeRateEnd;

        float total = moneyOnCard + cash;

        exchangeRateStart = round(exchangeRateStart, mCurrencyDecimalsCount);
        exchangeRateEnd = round(exchangeRateEnd, mCurrencyDecimalsCount);
        moneyOnCard = round(moneyOnCard, mDecimalsCount);
        cash = round(cash, mDecimalsCount);
        total = round(total, mDecimalsCount);

        return new SalaryViewItem(exchangeRateStart, exchangeRateEnd, moneyOnCard, cash, total);
    }

    /**
     * Round to certain number of decimals
     *
     * @param d            number to be rounded.
     * @param decimalPlace number of decimals desired.
     *
     * @return number with decimals rounded.
     */
    private static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_DOWN);
        return bd.floatValue();
    }
}
