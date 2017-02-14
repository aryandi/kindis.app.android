package sangmaneproject.kindis.view.adapter;

import android.content.Context;
import android.content.Intent;
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
import sangmaneproject.kindis.view.activity.Detail;
import sangmaneproject.kindis.view.holder.Item;

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
        Item item= new Item(view);
        return item;
    }

    @Override
    public void onBindViewHolder(Item holder, int position) {
        ImageView imageView = holder.imageView;
        TextView title = holder.title;
        TextView subTitle = holder.subtitle;
        LinearLayout click = holder.click;
        dataSinggle = listAlbum.get(position);

        Glide.with(context)
                .load(ApiHelper.BASE_URL_IMAGE+dataSinggle.get("image"))
                .thumbnail( 0.1f )
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);
        title.setText(dataSinggle.get("title"));
        subTitle.setText(dataSinggle.get("description"));

        final String uid = dataSinggle.get("uid");
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detail.class);
                intent.putExtra("uid", uid);
                intent.putExtra("type", "album");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listAlbum.size();
    }
}
