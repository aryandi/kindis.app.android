package co.digdaya.kindis.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.view.holder.ItemFAQ;

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
        final WebView subTitle = holder.subTitle;
        String style = "<style type=\"text/css\">\n" +
                "@font-face {\n" +
                "    font-family: MyFont;\n" +
                "    src: url(\"file:///android_asset/CitrixSans-Regular.ttf\")\n" +
                "}\n" +
                "body {\n" +
                "    font-family: MyFont;\n" +
                "    font-size: 14px;\n" +
                "}\n" +
                "</style>";
        subTitle.setBackgroundColor(0x00000000);

        dataFAQ = listFAQ.get(position);
        String html = style+"<font color='white'>"+dataFAQ.get("subtitle")+"</font>";

        title.setText(dataFAQ.get("title")+" ?");
        subTitle.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
    }

    @Override
    public int getItemCount() {
        return listFAQ.size();
    }
}
