package co.digdaya.kindis.view.fragment.navigationview.saveoffline;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.digdaya.kindis.R;
import co.digdaya.kindis.databse.KindisDBHelper;
import co.digdaya.kindis.databse.KindisDBname;
import co.digdaya.kindis.model.DataSingleOffline;
import co.digdaya.kindis.util.SpacingItem.SpacingItemGenre;
import co.digdaya.kindis.view.activity.Player.ListSongPlayer;
import co.digdaya.kindis.view.adapter.item.AdapterSongOffline;

/**
 * Created by DELL on 5/8/2017.
 */

public class SingleSaveOffline extends Fragment {
    RecyclerView recyclerView;
    List<DataSingleOffline> dataSingleOfflines = new ArrayList<>();
    AdapterSongOffline adapterSongOffline;

    public SingleSaveOffline() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getData()){
            return inflater.inflate(R.layout.fragment_save_offline_list, container, false);
        }else {
            return inflater.inflate(R.layout.layout_empty_state, container, false);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getData()){
            initView(view);
        }else {
            initEmptyState(view);
        }
    }

    private boolean getData(){
        dataSingleOfflines.clear();
        KindisDBHelper kindisDBHelper = new KindisDBHelper(getContext());
        SQLiteDatabase db = kindisDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+KindisDBname.TABLE_NAME+" ORDER BY "+KindisDBname.COLUMN_ID+" DESC",null);
        if (cursor.moveToFirst()){
            while (cursor.isAfterLast()==false){
                System.out.println("DBData: "+cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_TITLE)));
                System.out.println("DBData: "+cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_PATH)));
                DataSingleOffline dataSingleOffline = new DataSingleOffline();
                dataSingleOffline.setUid(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ID)));
                dataSingleOffline.setTitle(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_TITLE)));
                dataSingleOffline.setPath(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_PATH)));
                dataSingleOffline.setImage(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_IMAGE)));
                dataSingleOffline.setAlbum(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ALBUM)));
                dataSingleOffline.setArtist(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ARTIST)));
                dataSingleOffline.setArtist_id(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ARTIST_ID)));
                dataSingleOfflines.add(dataSingleOffline);
                cursor.moveToNext();
            }
            return true;
        }else {
            return false;
        }
    }

    private void initEmptyState(View view){
        TextView title, subtitle;
        Button btnRefresh;

        title = (TextView) view.findViewById(R.id.title);
        subtitle = (TextView) view.findViewById(R.id.subtitle);
        btnRefresh = (Button) view.findViewById(R.id.btn_refresh);

        subtitle.setVisibility(View.GONE);
        btnRefresh.setVisibility(View.GONE);
        title.setText("No offline single");
    }

    private void initView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.list_save_offline);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.addItemDecoration(new SpacingItemGenre(getContext(), "more"));

        adapterSongOffline = new AdapterSongOffline(getActivity(), dataSingleOfflines);
        recyclerView.setAdapter(adapterSongOffline);
    }
}
