package com.davidmihola.hexgridlayout;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by dmta on 20.05.16.
 */
public final class HexGridData {

    @NonNull public final Item centerItem;
    // The list may be empty, but not null!
    @NonNull public final List<Item> otherItems;

    public HexGridData(Item centerItem, List<Item> otherItems) {
        this.centerItem = centerItem;
        this.otherItems = otherItems;
    }

    @Override
    public String toString() {
        return "HexGridData{" +
                "centerItem=" + centerItem +
                ", otherItems=" + otherItems +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HexGridData that = (HexGridData) o;

        if (!centerItem.equals(that.centerItem)) return false;
        return otherItems.equals(that.otherItems);

    }

    @Override
    public int hashCode() {
        int result = centerItem.hashCode();
        result = 31 * result + otherItems.hashCode();
        return result;
    }
}
