package com.example.icamargo.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //declaring stateful obje cts here' these will be null before onCreate is called
    //^look up what an arrayAdapter is!!!!
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    //class in Android.widget package (video)
    ListView lvItems;
    //instance of the list view itself


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize above in order...
        lvItems = (ListView) findViewById(R.id.lvItems);


        //arrayadapter requires three arguments: one reference to the main activity (disk), one type of item
        //that will be adapted, and the third is the item list that we just created
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        lvItems.setAdapter(itemsAdapter);
        //changed above after we created readItems!!!

        //connecting to layout- dont create a new instance of layview, use id that we assigned to the listview
        //in the last video!! findviewbyid accepts an integer use the R class then cast the output into the listview
        /*TODO: LOOK UP WHAT AN R CLASS IS */

//        //mock data
//        items.add("First Item");
//        items.add("Second Item");
        setupListViewListener();
    }


    public void onAddItem(View v){
        //resolve the editText in the same way that we resolved listView
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();

        //now add the item text to our todo list
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        //^clears the newItem field so that user can enter next items without having to clear.

        writeItems();

        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
        //class in android.widget, brief notification displayed to user
        //takes in an application context, text to dispaly to user, duration of notification
    }


    //we are not making the program call this only we call this. at end of onCrewate
    private void setupListViewListener() {
        //use log class to debug and know when code is being called WONT IMPACT UI
        //two arguments: a tag  to aggregate messages (use class name) and then second argument is message itself
        Log.i("MainActivity", "Setting up listener on list view");

        //takes an instance of a long click listener which is in the interface of adapterview
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> paerent, View view, int position, long id){
                Log.i("MainActivity", "Item removed from list: " + position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
                //long click was consumed (TODO: look up what this means)
                // TODO: 6/21/17 go over the video again so you understand the itemsadapter and onitemlongclick stuff.
                //the content of the onitemlongclick will only be executed if given a long click
                //passing an instance of a listener into a control but logic wont be implemented
            }
        });

    }


    //below: methods to support persistence. can all be private.
    //first metnod:return a file that allows us to access the stored model

    private File getDataFile(){
        Log.i("MainActivity", "File created");
        return new File(getFilesDir(),"todo.txt");
        //what is getFilesDir doing??
    }

    private void readItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset())); //what is a charset
        } catch (IOException e) {
            //changed to logging since we are already using this
            Log.e("Main Activity", "Error reading file", e);
            //e means error so it will be in red in the android monitor. we passed the object into this
            items = new ArrayList<>();
        }
    }

    private void writeItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("Main Activity", "Error with writing file", e);
        }

    }
    //done with file system persistence!!! just make sure that you are calling read/write at right time
    //want to do at start and every time the model is changed --> need to call write items at a bunch of places
    //but only read at the initialization!!

}






















//hi