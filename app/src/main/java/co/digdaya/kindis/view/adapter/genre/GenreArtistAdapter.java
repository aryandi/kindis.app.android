package co.digdaya.kindis.view.adapter.genre;

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

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.model.genre.GenreArtistModel;
import co.digdaya.kindis.view.activity.Detail.DetailArtist;
import co.digdaya.kindis.view.holder.Item;

/**
 * Created by DELL on 5/28/2017.
 */

public class GenreArtistAdapter extends RecyclerView.Adapter<Item> {
    Activity activity;
    GenreArtistModel genreArtistModel;
    int type;

    public GenreArtistAdapter(Activity activity, GenreArtistModel genreArtistModel, int type) {
        this.activity = activity;
        this.genreArtistModel = genreArtistModel;
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

        final String uid = genreArtistModel.data.get(position).uid;

        Glide.with(activity)
                .load(ApiHelper.BASE_URL_IMAGE+genreArtistModel.data.get(position).image)
                .thumbnail( 0.1f )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);
        title.setText(genreArtistModel.data.get(position).name);
        subTitle.setText("Artist");

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity.getApplicationContext(), DetailArtist.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uid", uid);
                intent.putExtra("type", "artist");
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return genreArtistModel.data.size();
    }
}
