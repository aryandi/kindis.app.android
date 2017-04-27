package co.digdaya.kindis.view.adapter.item;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.model.InfaqModel;
import co.digdaya.kindis.view.activity.Detail.DetailInfaq;
import co.digdaya.kindis.view.holder.ItemInfaq;

public class AdapterInfaq extends RecyclerView.Adapter<ItemInfaq> {
    Context context;
    InfaqModel infaqModel;

    public AdapterInfaq(InfaqModel infaqModel, Context context) {
        this.infaqModel = infaqModel;
        this.context = context;
    }

    @Override
    public ItemInfaq onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_infaq, parent, false);
        ItemInfaq itemInfaq = new ItemInfaq(view);
        return itemInfaq;
    }

    @Override
    public void onBindViewHolder(ItemInfaq holder, final int position) {
        final String uid = infaqModel.result.get(position).uid;
        final String titles = infaqModel.result.get(position).title;
        final String url = infaqModel.result.get(position).redirect_url;
        String imageUrl = ApiHelper.BASE_URL_IMAGE+infaqModel.result.get(position).main_image;

        TextView title = holder.labelNama;
        ImageView imageView = holder.imageView;

        title.setText(titles);
        Glide.with(context)
                .load(ApiHelper.BASE_URL_IMAGE+imageUrl)
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getItemViewType(position)==1){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }else {
                    Intent intent = new Intent(context, DetailInfaq.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("title", titles);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return infaqModel.result.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type = Integer.parseInt(infaqModel.result.get(position).is_url);
        return type;
    }
}
