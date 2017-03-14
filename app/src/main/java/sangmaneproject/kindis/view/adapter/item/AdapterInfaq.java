package sangmaneproject.kindis.view.adapter.item;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.model.InfaqModel;
import sangmaneproject.kindis.view.holder.ItemGenre;
import sangmaneproject.kindis.view.holder.ItemInfaq;

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

        Log.d("kontollll", infaqModel.getTitle());
        TextView title = holder.labelMasjid;
        title.setText(infaqModel.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
