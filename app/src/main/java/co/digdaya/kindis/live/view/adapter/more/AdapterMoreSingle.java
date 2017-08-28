package co.digdaya.kindis.live.view.adapter.more;

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

import co.digdaya.kindis.live.service.PlayerService;
import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.PlayerActionHelper;
import co.digdaya.kindis.live.helper.PlayerSessionHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.model.MoreModel;
import co.digdaya.kindis.live.view.activity.Premium;
import co.digdaya.kindis.live.view.dialog.DialogGetPremium;
import co.digdaya.kindis.live.view.holder.Item;

/**
 * Created by DELL on 4/3/2017.
 */

public class AdapterMoreSingle extends RecyclerView.Adapter<Item> {
    Activity context;
    MoreModel.SinggleMore singgleMore;
    DialogGetPremium dialogGetPremium;
    Dialog dialogPremium;

    public AdapterMoreSingle(Activity context, MoreModel.SinggleMore singgleMore) {
        this.context = context;
        this.singgleMore = singgleMore;
        dialogGetPremium = new DialogGetPremium(context, dialogPremium);
    }

    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_grid, parent, false);
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

        title.setText(singgleMore.result.get(position).title);
        subTitle.setText(singgleMore.result.get(position).artist);
        Glide.with(context)
                .load(ApiHelper.BASE_URL_IMAGE+singgleMore.result.get(position).image)
                .thumbnail( 0.1f )
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);

        if (getItemViewType(position) == 1){
            badgePremium.setVisibility(View.VISIBLE);
        }

        final int isAccountPremium = Integer.parseInt(new SessionHelper().getPreferences(context, "is_premium"));
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAccountPremium == getItemViewType(position) || isAccountPremium == 1){
                    Toast.makeText(context, "Loading . . . ", Toast.LENGTH_LONG).show();
                    new PlayerSessionHelper().setPreferences(context, "index", "1");
                    Intent intent = new Intent(context, PlayerService.class);
                    intent.setAction(PlayerActionHelper.UPDATE_RESOURCE);
                    intent.putExtra("single_id", singgleMore.result.get(position).uid);
                    context.startService(intent);
                }else {
                    Intent intent = new Intent(context, Premium.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return singgleMore.result.size();
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.parseInt(singgleMore.result.get(position).is_premium);
    }
}
