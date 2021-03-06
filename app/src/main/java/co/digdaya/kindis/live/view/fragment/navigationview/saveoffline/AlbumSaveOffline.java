package co.digdaya.kindis.live.view.fragment.navigationview.saveoffline;


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

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.database.KindisDBHelper;
import co.digdaya.kindis.live.database.KindisDBname;
import co.digdaya.kindis.live.helper.AnalyticHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.model.DataAlbumOffline;
import co.digdaya.kindis.live.util.SpacingItem.SpacingItemGenre;
import co.digdaya.kindis.live.view.adapter.offline.AdapterAlbumOffline;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumSaveOffline extends Fragment {
    private final AnalyticHelper analyticHelper;
    TextView title, subtitle;
    Button btnRefresh;
    RecyclerView recyclerView;
    List<DataAlbumOffline> dataSingleOfflines = new ArrayList<>();
    AdapterAlbumOffline adapterAlbumOffline;


    public AlbumSaveOffline() {
        // Required empty public constructor
        analyticHelper = new AnalyticHelper(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getData() && Integer.parseInt(new SessionHelper().getPreferences(getActivity(), "is_premium"))==1){
            return inflater.inflate(R.layout.fragment_save_offline_list, container, false);
        }else {
            return inflater.inflate(R.layout.layout_empty_state, container, false);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getData() && Integer.parseInt(new SessionHelper().getPreferences(getActivity(), "is_premium"))==1){
            initView(view);
        }else {
            initEmptyState(view);
        }
        analyticHelper.swipeOffline("Album");
    }

    private void initEmptyState(View view){
        title = view.findViewById(R.id.title);
        subtitle = view.findViewById(R.id.subtitle);
        btnRefresh = view.findViewById(R.id.btn_refresh);

        subtitle.setVisibility(View.GONE);
        btnRefresh.setVisibility(View.GONE);
        title.setText("No offline album");
    }

    private void initView(View view){
        recyclerView = view.findViewById(R.id.list_save_offline);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.addItemDecoration(new SpacingItemGenre(getActivity(), "more"));

        adapterAlbumOffline = new AdapterAlbumOffline(getActivity(), dataSingleOfflines);
        recyclerView.setAdapter(adapterAlbumOffline);
    }

    private boolean getData(){
        dataSingleOfflines.clear();
        KindisDBHelper kindisDBHelper = new KindisDBHelper(getContext());
        SQLiteDatabase db = kindisDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ KindisDBname.TABLE_ALBUM +" ORDER BY "+KindisDBname.COLUMN_ALBUM_ID+" DESC",null);
        if (cursor.moveToFirst()){
            while (cursor.isAfterLast()==false){
                DataAlbumOffline dataAlbumOffline = new DataAlbumOffline();
                dataAlbumOffline.setAlbum_id(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ALBUM_ID)));
                dataAlbumOffline.setAlbum(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ALBUM)));
                dataAlbumOffline.setArtist(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ARTIST)));
                dataAlbumOffline.setDesc(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ALBUM_DESC)));
                dataAlbumOffline.setImage(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_IMAGE)));
                dataAlbumOffline.setBanner(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_BANNER_IMAGE)));
                dataSingleOfflines.add(dataAlbumOffline);
                cursor.moveToNext();
            }
            return true;
        }else {
            return false;
        }
    }
}
