package co.digdaya.kindis.live.view.fragment.navigationview;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.CheckConnection;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.util.BaseBottomPlayer.BottomPlayerFragment;
import co.digdaya.kindis.live.view.adapter.AdapterNotification;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notification extends BottomPlayerFragment implements View.OnClickListener {
    ArrayList<HashMap<String, String>> listNotif = new ArrayList<>();
    AdapterNotification adapterNotification;
    DrawerLayout drawer;
    ImageButton btnDrawer;
    ListView listView;

    LinearLayout contEmptyState;
    Button refresh;
    ProgressDialog loading;

    public Notification(DrawerLayout drawer) {
        this.drawer = drawer;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnDrawer = (ImageButton) view.findViewById(R.id.btn_drawer);
        listView = (ListView) view.findViewById(R.id.listview);

        contEmptyState = (LinearLayout) view.findViewById(R.id.empty_state);
        refresh = (Button) view.findViewById(R.id.btn_refresh);
        loading = new ProgressDialog(getActivity(), R.style.MyTheme);
        loading.setProgressStyle(android.R.style.Widget_Material_Light_ProgressBar_Large_Inverse);
        loading.setCancelable(false);

        btnDrawer.setOnClickListener(this);

        if (listNotif.isEmpty()){
            setLayout();
        }else {
            adapterNotification = new AdapterNotification(getContext(), listNotif);
            listView.setAdapter(adapterNotification);
        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLayout();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_drawer:
                drawer.openDrawer(GravityCompat.START);
                break;
        }
    }

    private void getListNotification(){
        loading.show();
        new VolleyHelper().get(ApiHelper.NOTIFICATION, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismiss();
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray result = object.getJSONArray("result");
                        for (int i=0; i<result.length(); i++){
                            JSONObject data = result.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("uid", data.optString("uid"));
                            map.put("title", data.optString("title"));
                            map.put("content", data.optString("content"));
                            listNotif.add(map);
                        }
                        adapterNotification = new AdapterNotification(getContext(), listNotif);
                        listView.setAdapter(adapterNotification);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setLayout(){
        if (new CheckConnection().isInternetAvailable(getContext())){
            getListNotification();
            listView.setVisibility(View.VISIBLE);
            contEmptyState.setVisibility(View.GONE);
        }else {
            listView.setVisibility(View.GONE);
            contEmptyState.setVisibility(View.VISIBLE);
        }
    }
}
