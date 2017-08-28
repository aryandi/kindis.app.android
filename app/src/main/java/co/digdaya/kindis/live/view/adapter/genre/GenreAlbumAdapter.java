package co.digdaya.kindis.live.view.adapter.genre;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.model.genre.GenreAlbumModel;
import co.digdaya.kindis.live.view.activity.Detail.Detail;
import co.digdaya.kindis.live.view.holder.Item;

/**
 * Created by DELL on 5/28/2017.
 */

public class GenreAlbumAdapter extends RecyclerView.Adapter<Item> {
    Activity activity;
    GenreAlbumModel genreAlbumModel;
    int type;

    public GenreAlbumAdapter(Activity activity, GenreAlbumModel genreAlbumModel, int type) {
        this.activity = activity;
        this.genreAlbumModel = genreAlbumModel;
        this.type = type;
    }

    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (type==1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_album, parent, false);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_grid, parent, false);
        }
        Item item= new Item(view);
        return item;
    }

    @Override
    public void onBindViewHolder(Item holder, int position) {
        ImageView imageView = holder.imageView;
        TextView title = holder.title;
        TextView subTitle = holder.subtitle;
        RelativeLayout click = holder.click;

        title.setText(genreAlbumModel.data.get(position).title);
        subTitle.setText(genreAlbumModel.data.get(position).year+" | "+genreAlbumModel.data.get(position).artist);
        Glide.with(activity)
                .load(ApiHelper.BASE_URL_IMAGE+genreAlbumModel.data.get(position).image)
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);

        final String uid = genreAlbumModel.data.get(position).uid;

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Detail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uid", uid);
                intent.putExtra("type", "album");
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return genreAlbumModel.data.size();
    }
}
