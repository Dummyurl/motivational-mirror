package com.motivationselfie.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.motivationselfie.R;
import com.motivationselfie.adapters.QuoteListAdapter;
import com.motivationselfie.interfaces.AddQuoteInterface;
import com.motivationselfie.interfaces.InterfaceContainer;
import com.motivationselfie.modals.QuotesAssests;
import com.motivationselfie.utils.Container;
import com.motivationselfie.utils.Preference;
import com.motivationselfie.utils.Utilities;

import java.util.ArrayList;


@SuppressWarnings("ALL")
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
                if (!Preference.getInAppFromPref(getActivity())) {

                    Utilities.showInAppDialog(getActivity(), getResources().getString(R.string.message_InAppQuotes));

                } else {

                    Utilities.showAddQuoteDialog(getActivity());
                }
            }
        });

        lv_quotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Preference.setQuoteIdFromPref(getActivity(), list_fav_New.get(position).getId());
                Preference.setBackStackFromPref(getActivity(), true);
                getActivity().onBackPressed();
            }
        });

        // Get Array List From SharePrefrence...
        list_Fav_Id = new ArrayList<Integer>();
        if (Preference.getfavArrayListPref(getActivity()) != null) {
            list_Fav_Id = Preference.getfavArrayListPref(getActivity());
        }

        // Get Self ArrayList
        lisFavSelf = new ArrayList<QuotesAssests>();
        if (Preference.getfavSelfArrayListPref(getActivity()) != null && Preference.getfavSelfArrayListPref(getActivity()).size() > 0) {
            lisFavSelf = Preference.getfavSelfArrayListPref(getActivity());
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
        if (Preference.getfavSelfArrayListPref(getActivity()) != null && Preference.getfavSelfArrayListPref(getActivity()).size() > 0) {

            lisFavSelf = Preference.getfavSelfArrayListPref(getActivity());
            QuotesAssests assest = new QuotesAssests();
            assest.setQuotes(quote);
            assest.setId((lisFavSelf.get(lisFavSelf.size() - 1).getId() + 1));
            assest.setAuthor("");

            if (list_fav_New.size() < 21) {
                lisFavSelf.add(assest);
                list_fav_New.add(assest);
                Preference.insertSelfArrayListPref(getActivity(), lisFavSelf);
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
                    Preference.insertSelfArrayListPref(getActivity(), lisFavSelf);
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
                Preference.insertSelfArrayListPref(getActivity(), lisFavSelf);

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
    }

    // Here to check the success true and reload the current fragment
    @Override
    public void InAppSuccess(Boolean isSuccess) {
        if (isSuccess) {
            reloadFragmenmt(this);
        }
    }
}
