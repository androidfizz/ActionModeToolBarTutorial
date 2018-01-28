package com.androidfizz.contextualtoolbar.model;

/**
 * Created by jitendra.singh on 1/7/2018
 * for ContextualToolbar
 */

public class Item {
    private String text;
    private boolean flag;
    private boolean selected;

    public Item(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
