package co.digdaya.kindis.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import co.digdaya.kindis.R;
import co.digdaya.kindis.model.TransactionHistoryModel;

/**
 * Created by DELL on 6/2/2017.
 */

public class AdapterTransactionHistory extends BaseAdapter {
    TransactionHistoryModel transactionHistoryModel;
    Context context;
    LayoutInflater inflater;

    public AdapterTransactionHistory(TransactionHistoryModel transactionHistoryModel, Context context) {
        this.transactionHistoryModel = transactionHistoryModel;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return transactionHistoryModel.result.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_history_transaction, null);

        TextView date, title, status;

        date = (TextView) convertView.findViewById(R.id.date);
        title = (TextView) convertView.findViewById(R.id.title);
        status = (TextView) convertView.findViewById(R.id.status);

        date.setText(transactionHistoryModel.result.get(position).date);
        title.setText(transactionHistoryModel.result.get(position).title + " |");
        status.setText(transactionHistoryModel.result.get(position).status);

        return convertView;
    }
}
