package co.digdaya.kindis.view.adapter.item;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.digdaya.kindis.R;
import co.digdaya.kindis.model.DetailInfaqModel;
import co.digdaya.kindis.view.holder.Item;

/**
 * Created by DELL on 4/27/2017.
 */

public class AdapterGalleryInfaq extends RecyclerView.Adapter<Item> {
    Context context;
    DetailInfaqModel.Gallery gallery;

    public AdapterGalleryInfaq(Context context, DetailInfaqModel.Gallery gallery) {
        this.context = context;
        this.gallery = gallery;
    }

    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more, parent, false);
        Item item= new Item(view);
        return item;
    }

    @Override
    public void onBindViewHolder(Item holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
