package com.example.tejidospulido_app.Vista;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tejidospulido_app.Model.Classes.Category;
import com.example.tejidospulido_app.Model.Classes.Page;
import com.example.tejidospulido_app.Presentador.Adapter.AdapterCategories;
import com.example.tejidospulido_app.R;
import com.example.tejidospulido_app.Model.Classes.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuFragment extends Fragment {

    private static final String TAG = "Menu";

    private User user;
    private String categoria;
    private ExpandableListView exp_list_view;
    private SearchView buscador;
    private ArrayList<Category> list_categories;
    private HashMap<String, ArrayList<Page>> list_pages;
    private AdapterCategories adapter;

    public MenuFragment(User user){
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViews(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        handleChargeCategoryList();
    }

    private void setViews(View view) {
        list_categories = new ArrayList<>();
        list_pages = new HashMap<>();
        exp_list_view = (ExpandableListView) view.findViewById(R.id.exp_list_view);
        buscador = (SearchView) view.findViewById(R.id.search_view);

        handleChargeCategoryList();

        buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                handleSearchCategory(s);
                return true;
            }
        });
    }

    public void handleSearchCategory(String s) {
        ArrayList<Category> filteredList = new ArrayList<>();
        for (Category c : list_categories){
            if (c.getNombre().toLowerCase().contains(s.toLowerCase())){
                filteredList.add(c);
                list_pages.put(c.getNombre(), c.getListOfPaginas());
                adapter = new AdapterCategories(getContext(), filteredList, list_pages);
                exp_list_view.setAdapter(adapter);
            }
        }
        adapter = new AdapterCategories(getContext(), filteredList, list_pages);
        exp_list_view.setAdapter(adapter);
    }

    public void handleChargeCategoryList() {
        FirebaseDatabase.getInstance().getReference().child("categorias").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list_categories.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Category c = snapshot.getValue(Category.class);
                    list_categories.add(c);
                    list_pages.put(c.getNombre(), c.getListOfPaginas());
                    adapter = new AdapterCategories(getContext(), list_categories, list_pages);
                    exp_list_view.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
