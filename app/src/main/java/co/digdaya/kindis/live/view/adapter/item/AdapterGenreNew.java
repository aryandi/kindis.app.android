package co.digdaya.kindis.live.view.adapter.item;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.model.DataGenre;
import co.digdaya.kindis.live.view.activity.Detail.DetailGenre;
import co.digdaya.kindis.live.view.holder.ItemGenre;

/**
 * Created by vincenttp on 4/2/2017.
 */

public class AdapterGenreNew extends RecyclerView.Adapter<ItemGenre>{
    Context context;
    private DataGenre dataGenre;
    private RowClickListener rowClickListener;

    public AdapterGenreNew(Context context, DataGenre dataGenre, RowClickListener rowClickListener) {
        this.context = context;
        this.dataGenre = dataGenre;
        this.rowClickListener = rowClickListener;
    }

    @Override
    public ItemGenre onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new ItemGenre(view);
    }

    @Override
    public void onBindViewHolder(ItemGenre holder, int position) {
        TextView title = holder.title;
        RelativeLayout click = holder.click;

        title.setText(dataGenre.data.get(position).title);
        final String uid = dataGenre.data.get(position).uid;
        final int pos = position;
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowClickListener.onRowClick(pos);
                Intent intent = new Intent(context, DetailGenre.class);
                intent.putExtra("uid", uid);
                intent.putExtra("title", dataGenre.data.get(pos).title);
                intent.putExtra("image", dataGenre.data.get(pos).image);
                intent.putExtra("desc", dataGenre.data.get(pos).description);
                intent.putExtra("type", "genre");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataGenre.data.size();
    }

    public interface RowClickListener {
        void onRowClick(int position);
    }
}
