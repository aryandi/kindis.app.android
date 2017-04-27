package co.digdaya.kindis.view.adapter.item;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.digdaya.kindis.PlayerService;
import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.PlayerActionHelper;
import co.digdaya.kindis.helper.PlayerSessionHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.model.DataSingle;
import co.digdaya.kindis.view.dialog.DialogGetPremium;
import co.digdaya.kindis.view.holder.Item;

public class AdapterSongHorizontal extends RecyclerView.Adapter<Item> {
    Activity context;
    Dialog dialogPremium;
    DialogGetPremium dialogGetPremium;
    DataSingle dataSingle;
    int from;
    List<DataSingle.Data> datas;

    public AdapterSongHorizontal(Activity context, DataSingle dataSingle, int from){
        this.context = context;
        this.dataSingle = dataSingle;
        this.from = from;

        dialogGetPremium = new DialogGetPremium(context, dialogPremium);
        if (from == 0){
            datas = dataSingle.data;
        }else if (from == 1){
            datas = dataSingle.single;
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
        ImageView badgePremium = holder.badgePremium;
        TextView title = holder.title;
        TextView subTitle = holder.subtitle;
        RelativeLayout click = holder.click;

        title.setText(datas.get(position).title);
        subTitle.setText(datas.get(position).artist+" | "+datas.get(position).album_name);
        Glide.with(context)
                .load(ApiHelper.BASE_URL_IMAGE+datas.get(position).image)
                .thumbnail( 0.1f )
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);

        if (getItemViewType(position) == 1){
            badgePremium.setVisibility(View.VISIBLE);
        }


        final String uid = datas.get(position).uid;
        final int isAccountPremium = Integer.parseInt(new SessionHelper().getPreferences(context, "is_premium"));
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAccountPremium == getItemViewType(position) || isAccountPremium == 1){
                    Toast.makeText(context, "Loading . . . ", Toast.LENGTH_LONG).show();
                    new PlayerSessionHelper().setPreferences(context, "index", "1");
                    Intent intent = new Intent(context, PlayerService.class);
                    intent.setAction(PlayerActionHelper.UPDATE_RESOURCE);
                    intent.putExtra("single_id", uid);
                    context.startService(intent);
                }else {
                    dialogGetPremium.showDialog();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        int isPremium = Integer.parseInt(datas.get(position).is_premium);
        return isPremium;
    }
}