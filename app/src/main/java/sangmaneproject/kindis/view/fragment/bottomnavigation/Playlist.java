package sangmaneproject.kindis.view.fragment.bottomnavigation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sangmaneproject.kindis.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Playlist extends Fragment {


    public Playlist() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

}
