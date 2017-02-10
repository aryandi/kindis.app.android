package sangmaneproject.kindis.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;
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

        dataArtist = listArtist.get(position);

        Glide.with(context)
                .load(ApiHelper.BASE_URL_IMAGE+dataArtist.get("image"))
                .thumbnail( 0.1f )
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);
        title.setText(dataArtist.get("name"));
        subTitle.setText("Artist");
    }

    @Override
    public int getItemCount() {
        return listArtist.size();
    }
}
