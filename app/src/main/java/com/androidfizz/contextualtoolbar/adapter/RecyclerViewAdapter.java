package com.androidfizz.contextualtoolbar.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidfizz.contextualtoolbar.R;
import com.androidfizz.contextualtoolbar.model.Item;

import java.util.List;

/**
 * Created by jitendra.singh on 1/7/2018
 * email: jitendra@live.in
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    //for handing long click and simple click on an item
    public interface OnItemClickListener {
        void onItemClick(int position);

        void onItemLongPress(int position);
    }

    private List<Item> list;
    private OnItemClickListener onItemClickListener;
    private SparseBooleanArray selectedItems;

    public RecyclerViewAdapter(List<Item> list) {
        this.list = list;
        selectedItems = new SparseBooleanArray();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void select(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            list.get(pos).setSelected(false);
        } else {
            selectedItems.put(pos, true);
            list.get(pos).setSelected(true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        for (int i = 0; i < selectedItems.size(); i++) {
            int position = selectedItems.keyAt(i);
            list.get(position).setSelected(false);
        }
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }


    public int[] getSelectedItemsPosition() {
        int[] arr = new int[selectedItems.size()];
        for (int i = 0; i < selectedItems.size(); i++) {
            arr[i] = selectedItems.keyAt(i);
        }
        return arr;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private TextView tvText;
        private ImageView ivFlag;
        private FrameLayout frameSelected;

        ViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tvText);
            ivFlag = (ImageView) itemView.findViewById(R.id.ivFlag);
            frameSelected = (FrameLayout) itemView.findViewById(R.id.frameSelected);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        void setData(Item item) {
            tvText.setText(item.getText());
            ivFlag.setVisibility(item.isFlag() ? View.VISIBLE : View.INVISIBLE);
            frameSelected.setVisibility(item.isSelected() ? View.VISIBLE : View.GONE);
        }

        @Override
        public boolean onLongClick(View view) {
            if (onItemClickListener != null)
                onItemClickListener.onItemLongPress(getAdapterPosition());
            return true;
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
