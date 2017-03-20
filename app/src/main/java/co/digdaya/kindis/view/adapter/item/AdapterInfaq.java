package co.digdaya.kindis.view.adapter.item;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import co.digdaya.kindis.R;
import co.digdaya.kindis.model.InfaqModel;
import co.digdaya.kindis.view.activity.Detail.DetailInfaq;
import co.digdaya.kindis.view.holder.ItemInfaq;

public class AdapterInfaq extends RecyclerView.Adapter<ItemInfaq> {
    ArrayList<InfaqModel> list;
    Context context;
    InfaqModel infaqModel;

    public AdapterInfaq(ArrayList<InfaqModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ItemInfaq onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_infaq, parent, false);
        ItemInfaq itemInfaq = new ItemInfaq(view);
        return itemInfaq;
    }

    @Override
    public void onBindViewHolder(ItemInfaq holder, int position) {
        infaqModel = list.get(position);
        final String uid = infaqModel.getUid();
        final String titles = infaqModel.getTitle();

        TextView title = holder.labelNama;
        ImageView imageView = holder.imageView;

        title.setText(titles);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailInfaq.class);
                intent.putExtra("uid", uid);
                intent.putExtra("title", titles);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
