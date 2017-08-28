package co.digdaya.kindis.live.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.live.R;

public class AdapterNotification extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> listNotif;
    HashMap<String, String> data = new HashMap<String, String>();
    TextView title, subtitle;

    public AdapterNotification(Context context, ArrayList<HashMap<String, String>> listNotif){
        this.context = context;
        this.listNotif = listNotif;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listNotif.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.adapter_notification, null);
        data = listNotif.get(i);

        title = (TextView) view.findViewById(R.id.title);
        subtitle = (TextView) view.findViewById(R.id.subtitle);

        title.setText(data.get("title"));
        subtitle.setText(data.get("content"));
        return view;
    }
}
