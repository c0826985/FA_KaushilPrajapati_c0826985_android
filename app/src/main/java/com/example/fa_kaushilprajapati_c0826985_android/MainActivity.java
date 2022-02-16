package com.example.fa_kaushilprajapati_c0826985_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.OnSwipe;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView favList;
    Button button,mark;
    RecyclerView recyclerView;
    TextView textView;
    ArrayList<PlacesModel> placesModelArrayList = new ArrayList<>();

    static ArrayAdapter adapter;
    static ArrayList<String> arrayList = new ArrayList<String>();
    static ArrayList<LatLng> location = new ArrayList<LatLng>();

    public String getAddress;
    public int counter;

    private PlacesListAdapter placesListAdapter;

    static ArrayList<String> getData = new ArrayList<>();

    //Custom customAdapter = new Custom();
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper = new DBHelper(MainActivity.this);

    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // favList = findViewById(R.id.favplacess);
        Toast.makeText(MainActivity.this,"Dont forget to add places to display in the list",Toast.LENGTH_LONG);
        recyclerView = findViewById(R.id.favplacess);
        button = findViewById(R.id.button);
        mark = findViewById(R.id.mark);
        textView = findViewById(R.id.textView);
        getGGData();
        dbHelper = new DBHelper(MainActivity.this);
        arrayList.add("Touch me to add your favourite place");
        location.add(new LatLng(0, 0));

        placesModelArrayList = dbHelper.getAll();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        placesListAdapter = new PlacesListAdapter(placesModelArrayList,dbHelper);

        recyclerView.setAdapter(placesListAdapter);

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, getData);
        //favList.setAdapter(adapter);

       // favList.setAdapter(customAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setVisibility(View.VISIBLE);
            }
        });

    }
    private void getGGData() {
        cursor = dbHelper.getData();
        if (cursor.getCount() == 0) {
            Toast.makeText(MainActivity.this, "No data", Toast.LENGTH_SHORT);
        }
//        if(cursor.moveToFirst()){
//            do{
//                getAddress = cursor.getString(0);
//            }while (cursor.moveToNext());
//        }

        while (cursor.moveToNext()) {
            getAddress = cursor.getString(0);
            getData.add(cursor.getString(0));
            counter += 1;
        }

    }

    String deleteData;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            deleteData = placesModelArrayList.get(position).getAddress();
            dbHelper.deletePlaces(deleteData);
            placesModelArrayList.remove(position);
            Toast.makeText(MainActivity.this, "Favourite Place Deleted", Toast.LENGTH_SHORT).show();
               // placesModelArrayList = dbHelper.getAll();
            placesListAdapter.notifyDataSetChanged();

         Snackbar.make(recyclerView,deleteData + "Deleted",Snackbar.LENGTH_LONG).show(); //. setAction("undo", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dbHelper.insertPlaces(deleteData);
//                    if(position == 0) {
//                        placesModelArrayList.get(position).setAddress(deleteData);
//                    }
//                    else {
//                        placesModelArrayList.get(position-1).setAddress(deleteData);
//                    }
//                    placesListAdapter.notifyDataSetChanged();
//                }
//            }).show();
        }
    };





}



