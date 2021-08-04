package com.bebo.manga;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bebo.manga.Common.Common;
import com.bebo.manga.adapter.MyComicAdapter;
import com.bebo.manga.model.Comic;
import com.bebo.manga.service.CheckInternetConnection;
import com.bebo.manga.service.InternetDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class ExploreFragment extends Fragment {

    BottomNavigationView bottomNavigationView;
    RecyclerView recycler_filter_search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_explore, container, false);

        recycler_filter_search = (RecyclerView)view.findViewById(R.id.recycler_explore);
        recycler_filter_search.setHasFixedSize(true);
        recycler_filter_search.setLayoutManager(new GridLayoutManager(getContext(), 1));


        bottomNavigationView = (BottomNavigationView)view.findViewById(R.id.bottom_nav);
        bottomNavigationView.inflateMenu(R.menu.main_menu);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_filter:
                            CheckInternetConnection.check(getContext());
                            if(Common.online)
                                showFilterDialog();
                            else
                                InternetDialog.show(getContext(), getActivity());
                            break;
                        case R.id.action_search:
                            CheckInternetConnection.check(getContext());
                            if(Common.online)
                                showSearchDialog();
                            else
                                InternetDialog.show(getActivity(), getActivity());
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
        return view;
    }



    private void showSearchDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Search");

        LayoutInflater inflater = this.getLayoutInflater();
        View search_layout = inflater.inflate(R.layout.dialog_search, null);
        EditText edt_search = (EditText)search_layout.findViewById(R.id.edt_search);
        alertDialog.setView(search_layout);
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fetchSearchComic(edt_search.getText().toString());
            }
        });
        alertDialog.show();
    }

    private void fetchSearchComic(String query) {
        List<Comic> comics = new ArrayList<>();
        for (Comic comic:Common.comicList)
        {
            if(comic.Name.contains(query))
            {
                comics.add(comic);
            }
        }
        recycler_filter_search.setAdapter(new MyComicAdapter(getContext(), comics, true, true));
    }

    private void showFilterDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Select Category");

        LayoutInflater inflater = this.getLayoutInflater();
        View filter_layout = inflater.inflate(R.layout.dialog_options, null);

        AutoCompleteTextView txt_category = (AutoCompleteTextView) filter_layout.findViewById(R.id.txt_category);
        ChipGroup chipGroup = (ChipGroup) filter_layout.findViewById(R.id.chipGroup);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, Common.categories);
        txt_category.setAdapter(adapter);
        txt_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txt_category.setText("");
                Chip chip = (Chip) inflater.inflate(R.layout.chip_item, null, false);
                chip.setText(((TextView) view).getText());
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chipGroup.removeView(view);
                    }
                });
                chipGroup.addView(chip);

            }
        });

        alertDialog.setView(filter_layout);
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("FILTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> filter_key = new ArrayList<>();
                StringBuilder filter_query = new StringBuilder("");
                for(int j = 0; j < chipGroup.getChildCount(); j++)
                {
                    Chip chip = (Chip)chipGroup.getChildAt(j);
                    filter_key.add(chip.getText().toString());
                }
                Collections.sort(filter_key);
                for(String key:filter_key)
                {
                    filter_query.append(key).append(",");
                }
                filter_query.setLength(filter_query.length() - 1);
                fitchFilterCategory(filter_query.toString());
            }
        });
        alertDialog.show();
    }

    private void fitchFilterCategory(String toString) {
        List<Comic> comics = new ArrayList<>();

        for(Comic comic:Common.comicList ){
            StringBuilder cat = new StringBuilder("");
            for(String category:comic.Category)
            {
                cat.append(category).append(",");
            }
            cat.setLength(cat.length() - 1);
            if(cat.toString().contains(toString)) {comics.add(comic);}
        }
        recycler_filter_search.setAdapter(new MyComicAdapter(getContext(), comics, true, true));
    }
}
