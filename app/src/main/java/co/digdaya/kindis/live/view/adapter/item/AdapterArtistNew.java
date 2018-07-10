package co.digdaya.kindis.live.view.adapter.item;

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
import co.digdaya.kindis.live.model.DataArtist;
import co.digdaya.kindis.live.view.activity.Detail.DetailArtist;
import co.digdaya.kindis.live.view.holder.Item;

/**
 * Created by DELL on 3/31/2017.
 */

public class AdapterArtistNew extends RecyclerView.Adapter<Item> {
    Activity context;
    DataArtist dataArtist;
    String subtitles;
    private RowClickListener rowClickListener;

    public AdapterArtistNew(Activity context, DataArtist dataArtist, String subtitle, RowClickListener rowClickListener){
        this.context = context;
        this.dataArtist = dataArtist;
        this.subtitles = subtitle;
        this.rowClickListener = rowClickListener;
    }

    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_album, parent, false);
        return new Item(view);
    }

    @Override
    public void onBindViewHolder(Item holder, final int position) {
        ImageView imageView = holder.imageView;
        TextView title = holder.title;
        TextView subTitle = holder.subtitle;
        RelativeLayout click = holder.click;

        final String uid = dataArtist.data.get(position).uid;

        Glide.with(context)
                .load(ApiHelper.BASE_URL_IMAGE + dataArtist.data.get(position).image)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);
        title.setText(dataArtist.data.get(position).name);
        subTitle.setText(subtitles);

        final int pos = position;
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rowClickListener.onRowClick(pos);
                Intent intent = new Intent(context.getApplicationContext(), DetailArtist.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uid", uid);
                intent.putExtra("type", "artist");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArtist.data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface RowClickListener {
        void onRowClick(int position);
    }
}
