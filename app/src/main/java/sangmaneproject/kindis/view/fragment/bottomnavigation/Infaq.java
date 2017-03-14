package sangmaneproject.kindis.view.fragment.bottomnavigation;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.VolleyHelper;
import sangmaneproject.kindis.model.InfaqModel;
import sangmaneproject.kindis.util.BackgroundProses.DialogLoading;
import sangmaneproject.kindis.util.SpacingItemInfaq;
import sangmaneproject.kindis.view.adapter.item.AdapterInfaq;

/**
 * A simple {@link Fragment} subclass.
 */
public class Infaq extends Fragment {
    DialogLoading loading;
    ArrayList<InfaqModel> listInfaq = new ArrayList<>();
    AdapterInfaq adapterInfaq;
    RecyclerView listViewInfaq;

    public Infaq() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_infaq, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loading = new DialogLoading(getActivity());

        listViewInfaq = (RecyclerView) view.findViewById(R.id.list_infaq);
        listViewInfaq.setLayoutManager(new GridLayoutManager(getContext(),2));

        getInfaq();
    }

    private void getInfaq(){
        loading.showLoading();
        new VolleyHelper().get(ApiHelper.LIST_INFAQ, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismisLoading();
                Log.d("infaqresponse", response);
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONArray result = object.getJSONArray("result");
                            for (int i=0; i<result.length(); i++){
                                JSONObject data = result.getJSONObject(i);
                                InfaqModel infaqModel = new InfaqModel();
                                infaqModel.setUid(data.getString("uid"));
                                infaqModel.setTitle(data.getString("title"));
                                infaqModel.setMain_image(data.getString("main_image"));
                                infaqModel.setDate_created(data.getString("date_created"));
                                listInfaq.add(infaqModel);
                            }

                            adapterInfaq = new AdapterInfaq(listInfaq, getContext());
                            listViewInfaq.setAdapter(adapterInfaq);
                            listViewInfaq.setNestedScrollingEnabled(true);
                            listViewInfaq.addItemDecoration(new SpacingItemInfaq(getContext()));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
