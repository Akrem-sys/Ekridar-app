package com.ai.egg.microtalk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ai.egg.microtalk.BackendApp;
import com.ai.egg.microtalk.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostPageSliderAdapter extends SliderViewAdapter<PostPageSliderAdapter.MyViewHolder> {
    Context context;
    private List<String> Items;

    public PostPageSliderAdapter(Context context, List<String> items) {
        this.context = context;
        Items = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_page_slider_adapter,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        Picasso.with(context).load(BackendApp.URLFORIMAGE+Items.get(position)).resize(1920,1080).into(viewHolder.img);
    }
    @Override
    public int getCount() {
        return Items.size();
    }

    class MyViewHolder extends  SliderViewAdapter.ViewHolder
    {
        ImageView img;
        public MyViewHolder(View itemView){
            super(itemView);
            img = itemView.findViewById(R.id.img_for_slider);
        }
    }

}
