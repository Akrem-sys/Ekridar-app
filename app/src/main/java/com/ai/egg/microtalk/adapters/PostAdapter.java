package com.ai.egg.microtalk.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.egg.microtalk.BackendApp;
import com.ai.egg.microtalk.House;
import com.ai.egg.microtalk.activities.PostPage;
import com.ai.egg.microtalk.R;
import com.ai.egg.microtalk.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<House> HouseList;
    private Context context;

    public PostAdapter(List<House> houseList, Context context) {
        this.HouseList = houseList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        House Item = HouseList.get(position);
        String[] data = Utils.getDate(Item.getDate());
        holder.Date.setText(data[0]);
        String image=Item.getImgURL().substring(0, Item.getImgURL().indexOf("+"));
        holder.Username.setText(Item.getOwner());
        holder.Localisation.setText(Item.getRegion()+","+Item.getGov()+", "+Item.getCity());
        Picasso.with(context).load(BackendApp.URLFORIMAGE+image).into(holder.Image);
        Picasso.with(context).load(Item.getAvatarURL()).into(holder.Avatar);
        Log.d("Test Avatar",image);
        holder.Price.setText(Item.getPrice()+"DT");
        holder.Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackendApp.DESCRIPTION=Item.getDescription();
                BackendApp.IMAGEURL=Item.getImgURL();
                BackendApp.AVATARURL=Item.getAvatarURL();
                BackendApp.Item=Item;
                context.startActivity(new Intent(context, PostPage.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return HouseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Username,Localisation,Price,Date;
        public ImageView Avatar,Image;
        public LinearLayout Layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Date = (TextView) itemView.findViewById(R.id.date);
            Price = (TextView) itemView.findViewById(R.id.price);
            Avatar = (ImageView) itemView.findViewById(R.id.avatar);
            Username = (TextView) itemView.findViewById(R.id.post_owner);
            Localisation = (TextView) itemView.findViewById(R.id.house_localisation);
            Image = (ImageView) itemView.findViewById(R.id.house_image);
            Layout = (LinearLayout) itemView.findViewById(R.id.post_layout);
        }
    }
}
