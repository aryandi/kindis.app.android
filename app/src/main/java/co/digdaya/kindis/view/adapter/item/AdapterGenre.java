package co.digdaya.kindis.view.adapter.item;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.view.activity.Detail.Detail;
import co.digdaya.kindis.view.holder.ItemGenre;

public class AdapterGenre extends RecyclerView.Adapter<ItemGenre> {
    Context context;
    ArrayList<HashMap<String, String>> listGenre;
    HashMap<String, String> dataGenre;

    public AdapterGenre(Context context, ArrayList<HashMap<String, String>> listGenre) {
        this.context = context;
        this.listGenre = listGenre;
    }

    @Override
    public ItemGenre onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_genre, parent, false);
        ItemGenre itemGenre = new ItemGenre(view);
        return itemGenre;
    }

    @Override
    public void onBindViewHolder(ItemGenre holder, int position) {
        dataGenre = listGenre.get(position);
        TextView title = holder.title;
        RelativeLayout click = holder.click;
        title.setText(dataGenre.get("title"));

        final String uid = dataGenre.get("uid");
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detail.class);
                intent.putExtra("uid", uid);
                intent.putExtra("type", "genre");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listGenre.size();
    }
}