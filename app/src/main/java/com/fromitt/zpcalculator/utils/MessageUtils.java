package com.fromitt.zpcalculator.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

/**
 *
 * Created by Tkachov Vasyl on 09.11.2016.
 */

public class MessageUtils {

    public static final String IS_NEW          = "is_new";
    public static final String DATE            = "date";
    public static final String SALARY          = "salary";
    public static final String EXCHANGE_BUY1   = "exchange_buy1";
    public static final String EXCHANGE_SALE1  = "exchange_sale1";
    public static final String MONEY_ON_CARD1  = "money_on_card1";
    public static final String EXCHANGE_BUY2   = "exchange_buy2";
    public static final String EXCHANGE_SALE2  = "exchange_sale2";
    public static final String MONEY_ON_CARD2  = "money_on_card2";

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("LLL\nyyyy");

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DATE_FORMAT_TOOLBAR = new SimpleDateFormat("LLLL yyyy");
}
