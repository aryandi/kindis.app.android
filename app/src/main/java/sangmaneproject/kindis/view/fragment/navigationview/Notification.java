package sangmaneproject.kindis.view.fragment.navigationview;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.VolleyHelper;
import sangmaneproject.kindis.view.adapter.AdapterNotification;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notification extends Fragment implements View.OnClickListener {
    ArrayList<HashMap<String, String>> listNotif = new ArrayList<>();
    AdapterNotification adapterNotification;
    DrawerLayout drawer;
    ImageButton btnDrawer;
    ListView listView;

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

        btnDrawer.setOnClickListener(this);

        getListNotification();
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
        new VolleyHelper().get(ApiHelper.NOTIFICATION, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
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
}
