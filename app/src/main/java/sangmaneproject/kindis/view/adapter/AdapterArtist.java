package sangmaneproject.kindis.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.view.holder.Item;

/**
 * Created by vincenttp on 2/9/2017.
 */

public class AdapterArtist extends RecyclerView.Adapter<Item> {
    private int[] img = new int[]{
            R.drawable.bg_sign_in, R.drawable.bg_sign_in, R.drawable.bg_sign_in
    };

    public AdapterArtist (){}


    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        Item item= new Item(view);
        return item;
    }

    @Override
    public void onBindViewHolder(Item holder, int position) {
        ImageView imageView = holder.imageView;
        imageView.setImageResource(img[position]);
    }

    @Override
    public int getItemCount() {
        return img.length;
    }
}
