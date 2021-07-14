package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;


    Button addButton;
    EditText etitem;
    RecyclerView rvItems;
    itemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById (R.id.addButton);
        etitem = findViewById (R.id.etitem);
        rvItems = findViewById (R.id.rvItems);


        loadItems();

        itemsAdapter.OnLongClickListener onLongClickListener= new itemsAdapter.OnLongClickListener()
        {
            @Override
            public void onItemLongClicked(int position) {

                //delete the item from the model
                items.remove(position);
                //notify the adapter when and where we deleted an item
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item Removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };


        itemsAdapter.onClickListener onClickListener = new itemsAdapter.onClickListener() {
            @Override
            public void onItemClicked(int position) {
                //Create a new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                //Pass in data
                i.putExtra (KEY_ITEM_TEXT, items.get(position));
                i.putExtra (KEY_ITEM_POSITION, position);
                //display the result
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        itemsAdapter = new itemsAdapter (items, onLongClickListener, onClickListener);
        rvItems.setAdapter (itemsAdapter);
        rvItems.setLayoutManager (new LinearLayoutManager (this));

        addButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                String todoItem = etitem.getText().toString();
                //add item to model and notify adapter that an item has been added
                items.add(todoItem);
                itemsAdapter.notifyItemInserted(items.size() - 1);
                etitem.setText("");
                Toast.makeText(getApplicationContext(), "Item Added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }


    private File getDataFile ()
    {
        return new File (getFilesDir(), "data.txt");
    }
    //loads items by rerading every line of a data file
    private void loadItems () {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error Reading Items", e);
            items = new ArrayList<>();
        }

    }


    //this function will save items to the file
    private void saveItems ()
    {
        try
        {
            FileUtils.writeLines (getDataFile(), items);
        }
        catch (IOException e)
        {
            Log.e("MainActivity", "Error Saving Items", e);
        }
    }


    //handle the result of the edit activity
    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            //retrieve the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);

            //get original position of edited item from key position
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //update model, notify adapter, apply changes
            items.set(position, itemText);
            itemsAdapter.notifyItemChanged(position);
            saveItems();
            Toast.makeText(getApplicationContext(), "Item Updated", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown Call To onActivityResult");
        }
    }
}























