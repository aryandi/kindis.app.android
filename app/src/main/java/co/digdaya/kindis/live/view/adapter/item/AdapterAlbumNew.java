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

import java.util.List;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.model.DataAlbum;
import co.digdaya.kindis.live.view.activity.Detail.Detail;
import co.digdaya.kindis.live.view.holder.Item;

/**
 * Created by DELL on 3/31/2017.
 */

public class AdapterAlbumNew extends RecyclerView.Adapter<Item> {
    Activity context;
    int from;
    private RowClickListener rowClickListener;
    DataAlbum dataAlbum;
    List<DataAlbum.Data> datas;

    public AdapterAlbumNew(Activity context, DataAlbum dataAlbum, int from, RowClickListener rowClickListener){
        this.context = context;
        this.dataAlbum = dataAlbum;
        this.from = from;
        this.rowClickListener = rowClickListener;

        if (from == 0){
            datas = dataAlbum.data;
        }else if (from == 1){
            datas = dataAlbum.albums;
        }else if (from == 2){
            datas = dataAlbum.album;
        }
    }

    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_album, parent, false);
        Item item= new Item(view);
        return item;
    }

    @Override
    public void onBindViewHolder(Item holder, final int position) {
        ImageView imageView = holder.imageView;
        TextView title = holder.title;
        TextView subTitle = holder.subtitle;
        RelativeLayout click = holder.click;

        title.setText(datas.get(position).title);
        subTitle.setText(datas.get(position).year+" | "+datas.get(position).artist);
        Glide.with(context)
                .load(ApiHelper.BASE_URL_IMAGE+datas.get(position).image)
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);

        final String uid = datas.get(position).uid;

        final int pos = position;

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickListener.onRowClick(pos);
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
        return datas.size();
    }

    public interface RowClickListener {
        void onRowClick(int position);
    }
}
