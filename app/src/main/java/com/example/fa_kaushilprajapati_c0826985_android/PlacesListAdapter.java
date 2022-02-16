package com.example.fa_kaushilprajapati_c0826985_android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.OnSwipe;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.ViewHolder> {

    Context context;
    public PlacesListAdapter(ArrayList<PlacesModel> placesModelArrayList, DBHelper dbHelper) {
        this.placesModelArrayList = placesModelArrayList;
        this.dbHelper = dbHelper;
    }

    DBHelper dbHelper;
    PlacesListAdapter adapter;
    ArrayList<PlacesModel> placesModelArrayList;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.fav_places_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlacesModel places = placesModelArrayList.get(position);
        TextView addresstv;
        ImageView statimage,edit;
        CheckBox checkBox;

        addresstv =  holder.itemView.findViewById(R.id.address_text_view);
        statimage = holder.itemView.findViewById(R.id.static_image_location);
        edit = holder.itemView.findViewById(R.id.edit_fav_place);
        checkBox = holder.itemView.findViewById(R.id.checkbox);


        addresstv.setText(placesModelArrayList.get(position).getAddress());
        statimage.setImageResource(R.drawable.ic_baseline_place_24);
        edit.setImageResource(R.drawable.ic_baseline_edit_24);

//        checkBox.setChecked(placesModelArrayList.get(position).getChecked());

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked())
                {
                    holder.itemView.setBackgroundColor(Color.rgb(47, 115, 107));
                    placesModelArrayList.get(holder.getAdapterPosition()).setChecked(true);
                }
                else {
                    holder.itemView.setBackgroundColor(Color.WHITE);
                }
            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context ,MapsActivity.class);
                intent.putExtra("place", places);
                context.startActivity(intent);
               // String updatestring =  MainActivity.arrayList.get(holder.getAdapterPosition()+1);
                //dbHelper.updatePlaces(addresstv.getText().toString(), updatestring);

            }
        });

    }

    @Override
    public int getItemCount() {
        return placesModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }


}
