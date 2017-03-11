package sangmaneproject.kindis.view.adapter.item;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.PlayerService;
import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.PlayerActionHelper;
import sangmaneproject.kindis.helper.PlayerSessionHelper;
import sangmaneproject.kindis.helper.SessionHelper;
import sangmaneproject.kindis.view.dialog.GetPremium;
import sangmaneproject.kindis.view.holder.Item;

public class AdapterSongHorizontal extends RecyclerView.Adapter<Item> {
    Activity context;
    Dialog dialogPremium;
    GetPremium getPremium;
    ArrayList<HashMap<String, String>> listSong = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> dataSong;

    public AdapterSongHorizontal(Activity context, ArrayList<HashMap<String, String>> listSong){
        this.context = context;
        this.listSong = listSong;
        getPremium = new GetPremium(context, dialogPremium);
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

        final String uid = dataSong.get("uid");

        Glide.with(context)
                .load(ApiHelper.BASE_URL_IMAGE+dataSong.get("image"))
                .thumbnail( 0.1f )
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);
        title.setText(dataSong.get("title"));
        subTitle.setText(dataSong.get("subtitle"));

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("kontollll", new SessionHelper().getPreferences(context, "is_premium")+" = "+dataSong.get("is_premium"));
                if (new SessionHelper().getPreferences(context, "is_premium").equals(dataSong.get("is_premium")) || new SessionHelper().getPreferences(context, "is_premium").equals("1")){
                    Toast.makeText(context, "Loading . . . ", Toast.LENGTH_LONG).show();
                    new PlayerSessionHelper().setPreferences(context, "index", "1");
                    Intent intent = new Intent(context, PlayerService.class);
                    intent.setAction(PlayerActionHelper.UPDATE_RESOURCE);
                    intent.putExtra("single_id", uid);
                    context.startService(intent);
                }else {
                    getPremium.showDialog();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSong.size();
    }
}