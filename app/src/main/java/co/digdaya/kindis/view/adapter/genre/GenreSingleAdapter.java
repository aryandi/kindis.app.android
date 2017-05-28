package co.digdaya.kindis.view.adapter.genre;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.PlayerActionHelper;
import co.digdaya.kindis.helper.PlayerSessionHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.model.genre.GenreSingleModel;
import co.digdaya.kindis.service.PlayerService;
import co.digdaya.kindis.view.activity.Premium;
import co.digdaya.kindis.view.dialog.DialogGetPremium;
import co.digdaya.kindis.view.holder.Item;

/**
 * Created by DELL on 5/28/2017.
 */

public class GenreSingleAdapter extends RecyclerView.Adapter<Item> {
    Activity activity;
    GenreSingleModel genreSingleModel;
    DialogGetPremium dialogGetPremium;
    Dialog dialogPremium;

    public GenreSingleAdapter(Activity activity, GenreSingleModel genreSingleModel) {
        this.activity = activity;
        this.genreSingleModel = genreSingleModel;
        dialogGetPremium = new DialogGetPremium(activity, dialogPremium);
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
        ImageView badgePremium = holder.badgePremium;
        TextView title = holder.title;
        TextView subTitle = holder.subtitle;
        RelativeLayout click = holder.click;

        title.setText(genreSingleModel.data.get(position).title);
        subTitle.setText(genreSingleModel.data.get(position).artist+" | "+genreSingleModel.data.get(position).album_name);
        Glide.with(activity)
                .load(ApiHelper.BASE_URL_IMAGE+genreSingleModel.data.get(position).image)
                .thumbnail( 0.1f )
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);

        if (getItemViewType(position) == 1){
            badgePremium.setVisibility(View.VISIBLE);
        }


        final String uid = genreSingleModel.data.get(position).uid;
        final int isAccountPremium = Integer.parseInt(new SessionHelper().getPreferences(activity, "is_premium"));
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAccountPremium == getItemViewType(position) || isAccountPremium == 1){
                    Toast.makeText(activity, "Loading . . . ", Toast.LENGTH_LONG).show();
                    new PlayerSessionHelper().setPreferences(activity, "index", "1");
                    Intent intent = new Intent(activity, PlayerService.class);
                    intent.setAction(PlayerActionHelper.UPDATE_RESOURCE);
                    intent.putExtra("single_id", uid);
                    activity.startService(intent);
                }else {
                    Intent intent = new Intent(activity, Premium.class);
                    activity.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return genreSingleModel.data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        int isPremium = 0;
        if (genreSingleModel.data.get(position).is_premium!=null){
            isPremium = Integer.parseInt(genreSingleModel.data.get(position).is_premium);
        }
        return isPremium;
    }
}
