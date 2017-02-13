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
import sangmaneproject.kindis.view.activity.DetailArtist;
import sangmaneproject.kindis.view.holder.Item;

/**
 * Created by vincenttp on 2/9/2017.
 */

public class AdapterArtist extends RecyclerView.Adapter<Item> {
    Context context;
    ArrayList<HashMap<String, String>> listArtist = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> dataArtist;

    public AdapterArtist (Context context, ArrayList<HashMap<String, String>> listArtist){
        this.context = context;
        this.listArtist = listArtist;
    }


    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        Item item= new Item(view);
        return item;
    }

    @Override
    public void onBindViewHolder(Item holder, int position) {
        ImageView imageView = holder.imageView;
        TextView title = holder.title;
        TextView subTitle = holder.subtitle;
        LinearLayout click = holder.click;

        dataArtist = listArtist.get(position);

        final String uid = dataArtist.get("uid");

        Glide.with(context)
                .load(ApiHelper.BASE_URL_IMAGE+dataArtist.get("image"))
                .thumbnail( 0.1f )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);
        title.setText(dataArtist.get("name"));
        subTitle.setText("Artist");

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        return listArtist.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
