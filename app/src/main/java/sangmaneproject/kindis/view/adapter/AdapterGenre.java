package sangmaneproject.kindis.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.view.activity.Detail;
import sangmaneproject.kindis.view.holder.Item;
import sangmaneproject.kindis.view.holder.ItemGenre;

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
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listGenre.size();
    }
}