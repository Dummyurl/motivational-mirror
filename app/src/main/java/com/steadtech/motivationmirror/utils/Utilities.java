package com.steadtech.motivationmirror.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.steadtech.motivationmirror.R;
import com.steadtech.motivationmirror.interfaces.InterfaceContainer;
import com.steadtech.motivationmirror.modals.QuotesAssests;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class Utilities {
    private static String quote = "";

    // Get random quote using list
    public static String getRandomQuote(Context ctx, ArrayList<QuotesAssests> list) {
//        Log.e("Size", "Size?? " + Prefrence.getfavArrayListPref(ctx).size());
//        Log.e("Size2", "Size2?? " + list.size());
//
//        // Here To compare favourite array list with main quote list and remove favourite ids in main array list and then place random quote in it.
//        if (Prefrence.getfavArrayListPref(ctx) != null && Prefrence.getfavArrayListPref(ctx).size() > 0) {
//            for (int i = 0; i < Prefrence.getfavArrayListPref(ctx).size(); i++) {
//                for (int j = 0; j < list.size(); j++) {
//                    if (Prefrence.getfavArrayListPref(ctx).get(i).equals(list.get(j).getId())) {
//                        list.remove(j);
//                    }
//                }
//            }
//        }
//        Log.e("Size3", "Size3?? " + list.size());
        Random random = new Random();
        int index = random.nextInt(list.size());
        Prefrence.setQuoteIdFromPref(ctx, list.get(index).getId());
        return list.get(index).getQuotes() + "\n" + list.get(index).getAuthor();
    }

    // Get quotes using id
    public static String getQuoteUsingId(ArrayList<QuotesAssests> list, ArrayList<QuotesAssests> list_self, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id)) {
                quote = list.get(i).getQuotes() + "\n" + list.get(i).getAuthor();
            }
        }

        if (list_self != null && list_self.size() > 0) {
            for (int i = 0; i < list_self.size(); i++) {
                if (list_self.get(i).getId().equals(id)) {
                    quote = list_self.get(i).getQuotes();
                }
            }
        }
        return quote;
    }

    // Get Current Date...
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        return sdf.format(new Date());
    }

    public static void showInAppDialog(final Context ctx, String text) {
        // custom dialog
        final Dialog dialog = new Dialog(ctx, R.style.NewDialog);
        dialog.setContentView(R.layout.custom);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setTitle("");

        // set the custom dialog components - text, image and button
        TextView textView = (TextView) dialog.findViewById(R.id.textView);
        textView.setText(text);

        Button dialogButton = (Button) dialog.findViewById(R.id.button_Ok);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InterfaceContainer.quto.okButtonClick();
                dialog.dismiss();
            }
        });

        Button dialogCancel = (Button) dialog.findViewById(R.id.button_Cancel);
        // if button is clicked, close the custom dialog
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showNetworkDialog(Context ctx, String text, String text_error) {
        // custom dialog
        final Dialog dialog = new Dialog(ctx, R.style.NewDialog);
        dialog.setContentView(R.layout.custom);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setTitle("");

        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        tv_title.setText(text);

        // set the custom dialog components - text, image and button
        TextView textView = (TextView) dialog.findViewById(R.id.textView);
        textView.setText(text_error);

        Button dialogButton = (Button) dialog.findViewById(R.id.button_Ok);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button dialogCancel = (Button) dialog.findViewById(R.id.button_Cancel);
        dialogCancel.setText(ctx.getResources().getString(R.string.action_settings));
        // if button is clicked, close the custom dialog
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showAddQuoteDialog(final Context ctx) {
        // custom dialog
        final Dialog dialog = new Dialog(ctx, R.style.NewDialog);
        dialog.setContentView(R.layout.addquote_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setTitle("");

        // set the custom dialog components - text, image and button
        final EditText edt_add_quotes = (EditText) dialog.findViewById(R.id.edt_add_quotes);
        edt_add_quotes.setText("");

        Button button_Add = (Button) dialog.findViewById(R.id.button_Add);
        // if button is clicked, close the custom dialog
        button_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_add_quotes.getText().length() > 0) {
                    InterfaceContainer.quto.addNewQuote(edt_add_quotes.getText().toString());
                    dialog.dismiss();
                } else {
                    showToastLong(ctx, ctx.getResources().getString(R.string.please_add_quotes));
                }
            }
        });

        dialog.show();
    }

    public static void showToastLong(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
    }

    public static void showToastShort(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }

    public static void shareBitmap(Context ctx, Bitmap bitmap, String fileName) {
        try {
            File file = new File(ctx.getCacheDir(), fileName + ".png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            ctx.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}


