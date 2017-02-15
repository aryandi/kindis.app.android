package sangmaneproject.kindis.view.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.view.holder.Item;

public class AdapterSongVertical extends RecyclerView.Adapter<Item> {
    Context context;
    ArrayList<HashMap<String, String>> listSong = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> dataSong;

    public AdapterSongVertical(Context context, ArrayList<HashMap<String, String>> listSong){
        this.context = context;
        this.listSong = listSong;
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
        LinearLayout click = holder.click;
        dataSong = listSong.get(position);

        Glide.with(context)
                .load(ApiHelper.BASE_URL_IMAGE+dataSong.get("image"))
                .thumbnail( 0.1f )
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);
        title.setText(dataSong.get("title"));
        subTitle.setText(dataSong.get("description"));
    }

    @Override
    public int getItemCount() {
        return listSong.size();
    }
}