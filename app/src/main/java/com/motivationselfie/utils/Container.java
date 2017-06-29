package com.motivationselfie.utils;

import android.graphics.Bitmap;

import com.motivationselfie.modals.QuotesAssests;

import java.util.ArrayList;


public class Container {
    private static Bitmap bitmap;
    private static ArrayList<QuotesAssests> quote_list;

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static void setBitmap(Bitmap bitmap) {
        Container.bitmap = bitmap;
    }


    public static ArrayList<QuotesAssests> getQuote_list() {
        return quote_list;
    }

    public static void setQuote_list(ArrayList<QuotesAssests> quote_list) {
        Container.quote_list = quote_list;
    }
}