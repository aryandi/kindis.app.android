package co.digdaya.kindis.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.digdaya.kindis.R;
import co.digdaya.kindis.model.TabModel;
import co.digdaya.kindis.view.holder.Item;
import co.digdaya.kindis.view.holder.ItemListTab;

/**
 * Created by DELL on 3/31/2017.
 */

public class AdapterListTab extends RecyclerView.Adapter<ItemListTab> {
    Context context;
    TabModel tabModel;

    public AdapterListTab(Context context, TabModel tabModel) {
        this.context = context;
        this.tabModel = tabModel;
    }

    @Override
    public ItemListTab onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        ItemListTab item= new ItemListTab(view);
        return item;
    }

    @Override
    public void onBindViewHolder(ItemListTab holder, int position) {
        TextView title = holder.title;
        TextView btnMore = holder.btnMore;
        RecyclerView recyclerView = holder.list;

        title.setText(tabModel.tab1.get(position).name);

        if (tabModel.tab1.get(position).type_id.equals("1")){

        }
    }

    @Override
    public int getItemCount() {
        return tabModel.tab1.size();
    }
}
