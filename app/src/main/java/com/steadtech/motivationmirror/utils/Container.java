package com.steadtech.motivationmirror.utils;

import android.graphics.Bitmap;

import com.steadtech.motivationmirror.modals.QuotesAssests;

import java.util.ArrayList;


public class Container {
    public static Bitmap bitmap;
    public static ArrayList<QuotesAssests> quote_list;

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
