package co.digdaya.kindis.live.view.adapter.item;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.AnalyticHelper;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.model.InfaqModel;
import co.digdaya.kindis.live.view.activity.Detail.DetailInfaq;
import co.digdaya.kindis.live.view.holder.ItemInfaq;

public class AdapterInfaq extends RecyclerView.Adapter<ItemInfaq> {
    Context context;
    private AnalyticHelper analyticHelper;
    InfaqModel infaqModel;

    public AdapterInfaq(InfaqModel infaqModel, Context context, AnalyticHelper analyticHelper) {
        this.infaqModel = infaqModel;
        this.context = context;
        this.analyticHelper = analyticHelper;
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
        final String imageUrl = ApiHelper.BASE_URL_IMAGE+infaqModel.result.get(position).main_image;

        TextView title = holder.labelNama;
        ImageView imageView = holder.imageView;

        title.setText(titles);
        Glide.with(context)
                .load(imageUrl)
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
                    analyticHelper.contentClick("infaq", uid, "infaq", titles, url, "infaq");
                }else {
                    Intent intent = new Intent(context, DetailInfaq.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("title", titles);
                    intent.putExtra("image", imageUrl);
                    context.startActivity(intent);
                    analyticHelper.contentClick("infaq", uid, "infaq", titles, "null", "infaq");
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
