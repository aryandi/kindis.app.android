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
import co.digdaya.kindis.model.DataPlaylistOffline;
import co.digdaya.kindis.util.SpacingItem.SpacingItemGenre;
import co.digdaya.kindis.view.adapter.offline.AdapterPlaylistOffline;

/**
 * Created by DELL on 5/19/2017.
 */

public class PlaylistSaveOffline extends Fragment {
    TextView title, subtitle;
    Button btnRefresh;
    RecyclerView recyclerView;
    List<DataPlaylistOffline> dataPlaylistOfflines = new ArrayList<>();
    AdapterPlaylistOffline adapterPlaylistOffline;

    public PlaylistSaveOffline() {
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

    private void initEmptyState(View view){
        title = (TextView) view.findViewById(R.id.title);
        subtitle = (TextView) view.findViewById(R.id.subtitle);
        btnRefresh = (Button) view.findViewById(R.id.btn_refresh);

        subtitle.setVisibility(View.GONE);
        btnRefresh.setVisibility(View.GONE);
        title.setText("No offline playlist");
    }

    private void initView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.list_save_offline);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.addItemDecoration(new SpacingItemGenre(getContext(), "more"));

        adapterPlaylistOffline = new AdapterPlaylistOffline(getActivity(), dataPlaylistOfflines);
        recyclerView.setAdapter(adapterPlaylistOffline);
    }

    private boolean getData(){
        dataPlaylistOfflines.clear();
        KindisDBHelper kindisDBHelper = new KindisDBHelper(getContext());
        SQLiteDatabase db = kindisDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ KindisDBname.TABLE_PLAYLIST +" ORDER BY "+KindisDBname.COLUMN_PLAYLIST_ID+" DESC",null);
        if (cursor.moveToFirst()){
            while (cursor.isAfterLast()==false){
                DataPlaylistOffline dataAlbumOffline = new DataPlaylistOffline();
                dataAlbumOffline.setPlaylist_id(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_PLAYLIST_ID)));
                dataAlbumOffline.setPlaylist(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_PLAYLIST)));
                dataAlbumOffline.setImage(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_IMAGE)));
                dataAlbumOffline.setBanner(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_BANNER_IMAGE)));
                dataPlaylistOfflines.add(dataAlbumOffline);
                cursor.moveToNext();
            }
            return true;
        }else {
            return false;
        }
    }
}