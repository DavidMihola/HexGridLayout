package com.davidmihola.hexgridlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.pcollections.TreePVector;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private HexGridLayout hexGridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.plant(new Timber.DebugTree());

        setContentView(R.layout.activity_main);

        hexGridLayout = (HexGridLayout) findViewById(R.id.hex_grid_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();

        HexGridData hexGridData = new HexGridData(
                new Item("Center"),
                TreePVector.<Item>empty()
                        .plus(new Item("1"))
                        .plus(new Item("2"))
                        .plus(new Item("3"))
                        .plus(new Item("4"))
                        .plus(new Item("5"))
                        .plus(new Item("6"))
        );

        hexGridLayout.setData(hexGridData);

        hexGridLayout.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClicked(Item item) {
                Toast.makeText(
                        MainActivity.this,
                        String.format("Item clicked: %s", item.toString()),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}
