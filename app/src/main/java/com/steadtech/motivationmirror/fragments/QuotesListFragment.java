package com.steadtech.motivationmirror.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.steadtech.motivationmirror.R;
import com.steadtech.motivationmirror.adapters.QuoteListAdapter;
import com.steadtech.motivationmirror.interfaces.AddQuoteInterface;
import com.steadtech.motivationmirror.interfaces.InterfaceContainer;
import com.steadtech.motivationmirror.modals.QuotesAssests;
import com.steadtech.motivationmirror.utils.Container;
import com.steadtech.motivationmirror.utils.Prefrence;
import com.steadtech.motivationmirror.utils.Utilities;

import java.util.ArrayList;


public class QuotesListFragment extends BaseFragment implements AddQuoteInterface {
    public static final String TAG = QuotesListFragment.class.getSimpleName();
    private ListView lv_quotes;
    private LinearLayout linear_add_quote;
    private QuoteListAdapter adapter;
    private ArrayList<Integer> list_Fav_Id;
    private ArrayList<QuotesAssests> list_fav_New;
    private ArrayList<QuotesAssests> lisFavSelf;

    public static Fragment newInstance() {
        Fragment fragment = new QuotesListFragment();
        return fragment;
    }

    public QuotesListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_quotes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InterfaceContainer.quto = this;
        lv_quotes = (ListView) view.findViewById(R.id.lv_quotes);
        linear_add_quote = (LinearLayout) view.findViewById(R.id.linear_add_quote);

        linear_add_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Prefrence.getInAppFromPref(getActivity())) {

                    Utilities.showInAppDialog(getActivity(), getResources().getString(R.string.message_InAppQuotes));

                } else {

                    Utilities.showAddQuoteDialog(getActivity());
                }
            }
        });

        lv_quotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Prefrence.setQuoteIdFromPref(getActivity(), list_fav_New.get(position).getId());
                Prefrence.setBackStackFromPref(getActivity(), true);
                getActivity().onBackPressed();
            }
        });

        // Get Array List From SharePrefrence...
        list_Fav_Id = new ArrayList<Integer>();
        if (Prefrence.getfavArrayListPref(getActivity()) != null) {
            list_Fav_Id = Prefrence.getfavArrayListPref(getActivity());
        }

        // Get Self ArrayList
        lisFavSelf = new ArrayList<QuotesAssests>();
        if (Prefrence.getfavSelfArrayListPref(getActivity()) != null && Prefrence.getfavSelfArrayListPref(getActivity()).size() > 0) {
            lisFavSelf = Prefrence.getfavSelfArrayListPref(getActivity());
        }

        // Get Favourite Quotes To Compare Original Quote List to New id Favourite quotes...
        list_fav_New = new ArrayList<QuotesAssests>();
        if (list_Fav_Id.size() > 0) {
            for (int j = 0; j < list_Fav_Id.size(); j++) {
                for (int i = 0; i < Container.getQuote_list().size(); i++) {
                    if (Container.getQuote_list().get(i).getId().equals(list_Fav_Id.get(j))) {
                        QuotesAssests quote = new QuotesAssests();
                        quote.setId(Container.getQuote_list().get(i).getId());
                        quote.setQuotes(Container.getQuote_list().get(i).getQuotes());
                        quote.setAuthor(Container.getQuote_list().get(i).getAuthor());
                        list_fav_New.add(quote);
                    }
                }
            }
        }

        // Get Self List Quotes...
        if (lisFavSelf.size() > 0) {

            for (int i = 0; i < lisFavSelf.size(); i++) {
                QuotesAssests quote = new QuotesAssests();
                quote.setId(lisFavSelf.get(i).getId());
                quote.setQuotes(lisFavSelf.get(i).getQuotes());
                quote.setAuthor("");
                list_fav_New.add(quote);
            }
        }

        if (list_fav_New != null && list_fav_New.size() > 0) {
            setAdapter();
        }
    }

    private void setAdapter() {
        adapter = new QuoteListAdapter(getActivity(), list_fav_New);
        lv_quotes.setAdapter(adapter);
    }

    @Override
    public void addNewQuote(String quote) {
        if (Prefrence.getfavSelfArrayListPref(getActivity()) != null && Prefrence.getfavSelfArrayListPref(getActivity()).size() > 0) {

            lisFavSelf = Prefrence.getfavSelfArrayListPref(getActivity());
            QuotesAssests assest = new QuotesAssests();
            assest.setQuotes(quote);
            assest.setId((lisFavSelf.get(lisFavSelf.size() - 1).getId() + 1));
            assest.setAuthor("");

            if (list_fav_New.size() < 21) {
                lisFavSelf.add(assest);
                list_fav_New.add(assest);
                Prefrence.insertSelfArrayListPref(getActivity(), lisFavSelf);
            } else {

                if (list_fav_New.size() > 0) {
                    for (int i = 0; i < lisFavSelf.size(); i++) {
                        if (list_fav_New.get(0).getId().equals(lisFavSelf.get(i).getId())) {
                            lisFavSelf.remove(i);
                        }
                    }
                    list_fav_New.remove(0);
                    lisFavSelf.add(assest);
                    list_fav_New.add(assest);
                    Prefrence.insertSelfArrayListPref(getActivity(), lisFavSelf);
                }
            }

            if (adapter == null) {
                setAdapter();
            } else {
                adapter.notifyDataSetChanged();
            }

        } else {
            if (Container.getQuote_list() != null && Container.getQuote_list().size() > 0) {
                QuotesAssests assest = new QuotesAssests();
                assest.setQuotes(quote);
                assest.setId((Container.getQuote_list().size() + 1));
                assest.setAuthor("");
                lisFavSelf.add(assest);
                list_fav_New.add(assest);
                Prefrence.insertSelfArrayListPref(getActivity(), lisFavSelf);

                if (adapter == null) {
                    setAdapter();
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void okButtonClick() {
        mHelper.launchPurchaseFlow(getActivity(), ITEM_SKU, 10001,
                mPurchaseFinishedListener, "mypurchasetoken");
    }


    // Here to check the success true and reload the current fragment
    @Override
    public void InAppSuccess(Boolean isSuccess) {
        if (isSuccess) {
            reloadFragmenmt(this);
        }
    }
}
