package com.davidmihola.hexgridlayout;

import android.support.annotation.NonNull;

/**
 * Created by dmta on 20.05.16.
 */
public final class Item {

    @NonNull
    public final String title;

    public Item(String title) {
        this.title = title;
    }

    @Override
    public String
    toString() {
        return "Item{" +
                "title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return title.equals(item.title);

    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}
