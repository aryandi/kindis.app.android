package sangmaneproject.kindis.view.fragment.bottomnavigation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.SessionHelper;
import sangmaneproject.kindis.helper.VolleyHelper;

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPlaylist();
    }

    private void getPlaylist(){
        Map<String, String> param = new HashMap<String, String>();
        param.put("token", new SessionHelper().getPreferences(getContext(), "token"));

        Log.d("tokenn", new SessionHelper().getPreferences(getContext(), "token"));

        new VolleyHelper().post(ApiHelper.PLAYLIST, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    Log.d("playlistresponse", response);
                }else {

                }
            }
        });
    }
}
