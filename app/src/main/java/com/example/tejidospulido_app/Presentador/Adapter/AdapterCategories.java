package com.example.tejidospulido_app.Presentador.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

import com.example.tejidospulido_app.Model.Classes.Category;
import com.example.tejidospulido_app.Model.Classes.Page;
import com.example.tejidospulido_app.Vista.PageActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterCategories extends BaseExpandableListAdapter {

    private ArrayList<Category> list_categories;
    private HashMap<String, ArrayList<Page>> list_pages;
    private Context mcontext;

    public AdapterCategories(Context context, ArrayList<Category> categoryList, HashMap<String, ArrayList<Page>> list_pages){
        this.mcontext = context;
        this.list_categories = categoryList;
        this.list_pages = list_pages;
    }

    @Override
    public int getGroupCount() {
        return list_categories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list_pages.get(list_categories.get(groupPosition).getNombre()).size();
    }

    @Override
    public Category getGroup(int groupPosition) {
        return list_categories.get(groupPosition);
    }

    @Override
    public Page getChild(int groupPosition, int childPosition) {
        Log.d("MENU", list_pages.get(list_categories.get(groupPosition).getNombre()).get(childPosition).toString());
        return list_pages.get(list_categories.get(groupPosition).getNombre()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        TextView textView = convertView.findViewById(android.R.id.text1);
        //Initialize string
        String categoryName = getGroup(groupPosition).getNombre();
        textView.setText(categoryName);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(20);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Log.d("Manu", "Holaaa");
        convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_selectable_list_item, parent, false);
        TextView textView = convertView.findViewById(android.R.id.text1);
        //Initialize string
        String pageName = getChild(groupPosition, childPosition).getNombre();
        textView.setText(pageName);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(18);
        textView.setPadding(100,0,10,0);

        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent_up = new Intent(mcontext, PageActivity.class);
                intent_up.putExtra("category", getGroup(groupPosition).getNombre());
                intent_up.putExtra("page", getChild(groupPosition, childPosition));
                mcontext.startActivity(intent_up);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
