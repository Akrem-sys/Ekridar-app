package com.ai.egg.microtalk.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;

import com.ai.egg.microtalk.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SlideAdapter extends SliderViewAdapter<SlideAdapter.MyViewHolder> {
    final int REQUEST_EXTERNAL_STORAGE = 100;
    Context context;
    private List<Uri> Items;

    public SlideAdapter(Context context, List<Uri> items) {
        this.context = context;
        Items = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        if (Items.get(position)!=null){
            viewHolder.img.setBackground(null);
        }
        Picasso.with(context).load(Items.get(position)).resize(6000,2000).into(viewHolder.img);
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Items.remove(position);
                if (Items.size()==0){
                    Items.add(null);
                    viewHolder.img.setBackground(context.getResources().getDrawable(R.drawable.ic_darkre));
                }
                notifyDataSetChanged();
            }
        });
        viewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(((Activity)context), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
//                    return;
                } else {
                    launchGalleryIntent();
                }

            }
        });
    }
    @Override
    public int getCount() {
        return Items.size();
    }

    class MyViewHolder extends  SliderViewAdapter.ViewHolder
    {
        ImageView img;
        ImageButton btnDelete,btnAdd;
        public MyViewHolder(View itemView){
            super(itemView);
            btnAdd = itemView.findViewById(R.id.add_images_btn);
            btnDelete = itemView.findViewById(R.id.delete_image_btn);
            img = itemView.findViewById(R.id.img_for_slider);
        }
    }
    public void launchGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        ((Activity) context).startActivityForResult(intent, REQUEST_EXTERNAL_STORAGE);
    }




}
