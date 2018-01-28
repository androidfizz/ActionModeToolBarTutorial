package com.androidfizz.contextualtoolbar;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.androidfizz.contextualtoolbar.adapter.RecyclerViewAdapter;
import com.androidfizz.contextualtoolbar.divider.MyDividerItemDecoration;
import com.androidfizz.contextualtoolbar.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter
        .OnItemClickListener {

    private Context context;
    private RecyclerViewAdapter adapter;
    private List<Item> list;
    private ActionMode actionMode;
    private int statusBarColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //hold current color of status bar
            statusBarColor = getWindow().getStatusBarColor();
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this));
        list = prepareList();
        adapter = new RecyclerViewAdapter(list);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private List<Item> prepareList() {
        List<Item> list = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            list.add(new Item(String.format(Locale.US, "%d. %s", i, "Item")));
        }
        return list;
    }

    @Override
    public void onItemClick(int position) {
        if (adapter.getSelectedItemCount() > 0) {
            select(position);
        }
    }

    @Override
    public void onItemLongPress(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(new ActionModeCallBack());
        }
        select(position);
    }

    private void select(int position) {
        adapter.select(position);
        int count = adapter.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
            actionMode = null;
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }


    public void flagSelected() {
        int arr[] = adapter.getSelectedItemsPosition();
        for (int position : arr) {
            Item item = list.get(position);
            item.setFlag(!item.isFlag());
            item.setSelected(false);
        }
        adapter.clearSelections();
        actionMode.finish();
        actionMode = null;
    }


    public void deleteSelected() {
        int arr[] = adapter.getSelectedItemsPosition();
        adapter.clearSelections();
        int i = arr.length - 1;
        while (i >= 0) {
            list.remove(arr[i]);
            i--;
        }

        adapter.notifyDataSetChanged();
        actionMode.finish();
        actionMode = null;
    }

    private class ActionModeCallBack implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_more, menu);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(
                        ContextCompat.getColor(context, R.color.colorPrimary));
            }
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_flag:
                    flagSelected();
                    return true;

                case R.id.action_delete:
                    deleteSelected();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelections();
            actionMode = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(statusBarColor);
            }
        }
    }
}
