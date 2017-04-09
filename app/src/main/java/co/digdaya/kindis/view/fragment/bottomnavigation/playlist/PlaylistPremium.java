package co.digdaya.kindis.view.fragment.bottomnavigation.playlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;

/**
 * Created by DELL on 4/9/2017.
 */

public class PlaylistPremium extends Fragment{
    VolleyHelper volleyHelper;
    SessionHelper sessionHelper;
    public PlaylistPremium() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist_premium, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        volleyHelper = new VolleyHelper();
        sessionHelper = new SessionHelper();

        getList();
    }

    private void getList(){
        volleyHelper.get(ApiHelper.LIST_PLAYLIST_PREMIUM + sessionHelper.getPreferences(getContext(), "user_id")+"&token_access=" + sessionHelper.getPreferences(getContext(), "token_access"), new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    System.out.println("listpremium : "+ApiHelper.LIST_PLAYLIST_PREMIUM + sessionHelper.getPreferences(getContext(), "user_id")+"&token_access=" + sessionHelper.getPreferences(getContext(), "token_access"));
                    System.out.println("listpremium : "+response);
                }
            }
        });
    }
}
