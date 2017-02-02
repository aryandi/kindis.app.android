package sangmaneproject.kindis.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.R;
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
        title.setText(dataGenre.get("title"));
    }

    @Override
    public int getItemCount() {
        return listGenre.size();
    }
    /*Context context;
    ArrayList<HashMap<String, String>> listGenre;
    HashMap<String, String> dataGenre;
    LayoutInflater inflater;
    ImageView icon;
    TextView title;

    public AdapterGenre(Context context, ArrayList<HashMap<String, String>> listGenre) {
        this.context = context;
        this.listGenre = listGenre;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listGenre.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.adapter_genre, viewGroup, false);
        icon = (ImageView) view.findViewById(R.id.ic_genre);
        title = (TextView) view.findViewById(R.id.title_genre);

        dataGenre = listGenre.get(i);

        title.setText(dataGenre.get("title"));

        return view;
    }*/
}