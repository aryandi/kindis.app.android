package sangmaneproject.kindis.view.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.view.holder.Item;
import sangmaneproject.kindis.view.holder.ItemFAQ;

public class AdapterFAQ extends RecyclerView.Adapter<ItemFAQ> {
    Context context;
    ArrayList<HashMap<String, String>> listFAQ;
    HashMap<String, String> dataFAQ;

    public AdapterFAQ(Context context, ArrayList<HashMap<String, String>> listFAQ) {
        this.context = context;
        this.listFAQ = listFAQ;
    }

    @Override
    public ItemFAQ onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_faq, parent, false);
        ItemFAQ item= new ItemFAQ(view);
        return item;
    }

    @Override
    public void onBindViewHolder(ItemFAQ holder, int position) {
        TextView title = holder.title;
        TextView subTitle = holder.subTitle;

        dataFAQ = listFAQ.get(position);

        title.setText(dataFAQ.get("title")+" ?");
        subTitle.setText(dataFAQ.get("subtitle"));
    }

    @Override
    public int getItemCount() {
        return listFAQ.size();
    }
}
