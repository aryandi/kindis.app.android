package co.digdaya.kindis.view.adapter.item;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.model.DataAlbum;
import co.digdaya.kindis.view.activity.Detail.Detail;
import co.digdaya.kindis.view.holder.Item;

/**
 * Created by DELL on 3/31/2017.
 */

public class AdapterAlbumNew extends RecyclerView.Adapter<Item> {
    Activity context;
    DataAlbum dataAlbum;

    public AdapterAlbumNew(Activity context, DataAlbum dataAlbum){
        this.context = context;
        this.dataAlbum = dataAlbum;

        Log.d("AdapterAlbumNew", "true");
    }


    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_album, parent, false);
        Item item= new Item(view);
        return item;
    }

    @Override
    public void onBindViewHolder(Item holder, int position) {
        ImageView imageView = holder.imageView;
        TextView title = holder.title;
        TextView subTitle = holder.subtitle;
        RelativeLayout click = holder.click;

        System.out.println("AdapterAlbumNew : "+dataAlbum.data.get(position).title);

        title.setText(dataAlbum.data.get(position).title);
        subTitle.setText(dataAlbum.data.get(position).year);
        Glide.with(context)
                .load(ApiHelper.BASE_URL_IMAGE+dataAlbum.data.get(position).image)
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);

        final String uid = dataAlbum.data.get(position).uid;

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uid", uid);
                intent.putExtra("type", "album");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataAlbum.data.size();
    }


}
