package co.digdaya.kindis.view.fragment.navigationview.saveoffline;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import co.digdaya.kindis.R;
import co.digdaya.kindis.databse.KindisDBHelper;
import co.digdaya.kindis.databse.KindisDBname;

/**
 * Created by DELL on 5/8/2017.
 */

public class SingleSaveOffline extends Fragment {
    TextView title, subtitle;
    Button btnRefresh;

    KindisDBHelper kindisDBHelper;
    SQLiteDatabase db;

    public SingleSaveOffline() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_empty_state, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        kindisDBHelper = new KindisDBHelper(getContext());
        db = kindisDBHelper.getWritableDatabase();
        initEmptyState(view);
        getData();
    }

    private void initEmptyState(View view){
        title = (TextView) view.findViewById(R.id.title);
        subtitle = (TextView) view.findViewById(R.id.subtitle);
        btnRefresh = (Button) view.findViewById(R.id.btn_refresh);

        subtitle.setVisibility(View.GONE);
        btnRefresh.setVisibility(View.GONE);
        title.setText("No offline single");
    }

    private void getData(){
        Cursor cursor = db.rawQuery("select * from "+KindisDBname.TABLE_NAME+" ORDER BY "+KindisDBname.COLUMN_ID+" DESC",null);
        if (cursor.moveToFirst()){
            while (cursor.isAfterLast()==false){
                System.out.println("DBData: "+cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_TITLE)));
                cursor.moveToNext();
            }
        }
    }

    private Cursor getAllData(){
        return db.query(KindisDBname.TABLE_NAME, null, null, null,null,null,null);
    }
}
