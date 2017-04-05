package co.digdaya.kindis.view.adapter.item;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.digdaya.kindis.R;
import co.digdaya.kindis.model.DataGenre;
import co.digdaya.kindis.view.activity.Detail.Detail;
import co.digdaya.kindis.view.holder.ItemGenre;

/**
 * Created by vincenttp on 4/2/2017.
 */

public class AdapterGenreNew extends RecyclerView.Adapter<ItemGenre>{
    Context context;
    DataGenre dataGenre;

    public AdapterGenreNew(Context context, DataGenre dataGenre) {
        this.context = context;
        this.dataGenre = dataGenre;
    }

    @Override
    public ItemGenre onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        ItemGenre itemGenre = new ItemGenre(view);
        return itemGenre;
    }

    @Override
    public void onBindViewHolder(ItemGenre holder, int position) {
        TextView title = holder.title;
        RelativeLayout click = holder.click;

        title.setText(dataGenre.data.get(position).title);

        final String uid = dataGenre.data.get(position).uid;
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
        return dataGenre.data.size();
    }
}
