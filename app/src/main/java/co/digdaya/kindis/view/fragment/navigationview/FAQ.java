package co.digdaya.kindis.view.fragment.navigationview;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.VolleyHelper;
import co.digdaya.kindis.util.BaseBottomPlayer.BottomPlayerFragment;
import co.digdaya.kindis.util.BaseBottomPlayer.BottomPlayerStaticFragment;
import co.digdaya.kindis.view.adapter.AdapterFAQ;

/**
 * A simple {@link Fragment} subclass.
 */
public class FAQ extends BottomPlayerStaticFragment {
    DrawerLayout drawer;
    ImageButton btnDrawer;
    ArrayList<HashMap<String, String>> listFAQ = new ArrayList<>();
    RecyclerView viewFAQ;
    AdapterFAQ adapter;

    public FAQ(DrawerLayout drawer) {
        this.drawer = drawer;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faq, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnDrawer = (ImageButton) view.findViewById(R.id.btn_drawer);
        viewFAQ = (RecyclerView) view.findViewById(R.id.list_faq);

        btnDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        getListFAQ();
    }

    private void getListFAQ(){
        listFAQ.clear();
        new VolleyHelper().get(ApiHelper.FAQ, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray result = object.getJSONArray("result");
                        for (int i=0; i<result.length(); i++){
                            JSONObject data = result.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("title", data.optString("question"));
                            map.put("subtitle", data.optString("answer"));
                            listFAQ.add(map);
                        }

                        adapter = new AdapterFAQ(getContext(), listFAQ);
                        viewFAQ.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        viewFAQ.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
