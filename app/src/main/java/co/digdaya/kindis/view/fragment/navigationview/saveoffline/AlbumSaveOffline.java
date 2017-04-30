package co.digdaya.kindis.view.fragment.navigationview.saveoffline;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.digdaya.kindis.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumSaveOffline extends Fragment {


    public AlbumSaveOffline() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album_save_offline, container, false);
    }

}
