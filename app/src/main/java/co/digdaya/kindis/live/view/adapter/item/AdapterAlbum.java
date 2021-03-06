package co.digdaya.kindis.live.view.adapter.item;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.Constanta;
import co.digdaya.kindis.live.view.activity.Detail.Detail;
import co.digdaya.kindis.live.view.holder.Item;

/**
 * Created by vincenttp on 1/27/2017.
 */

public class AdapterAlbum extends RecyclerView.Adapter<Item> {
    Context context;
    ArrayList<HashMap<String, String>> listAlbum = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> dataSinggle;

    public AdapterAlbum(Context context, ArrayList<HashMap<String, String>> listAlbum){
        this.context = context;
        this.listAlbum = listAlbum;
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

        dataSinggle = listAlbum.get(position);

        Glide.with(context)
                .load(ApiHelper.BASE_URL_IMAGE+dataSinggle.get("image"))
                .thumbnail( 0.1f )
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);
        Log.d("imageadapteralbum", ApiHelper.BASE_URL_IMAGE+dataSinggle.get("image"));
        title.setText(dataSinggle.get("title"));
        subTitle.setText(dataSinggle.get("year"));

        final String uid = dataSinggle.get("uid");

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uid", uid);
                intent.putExtra(Constanta.INTENT_EXTRA_TYPE, "album");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listAlbum.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
