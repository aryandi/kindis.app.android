package sangmaneproject.kindis.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.view.adapter.AdapterTopListened;


/**
 * A simple {@link Fragment} subclass.
 */
public class MostPlayed extends Fragment {
    AdapterTopListened adapterTopListened;
    RecyclerView recyclerViewTopListened;
    RecyclerView recyclerViewArtist;
    LinearLayoutManager horizontalLayoutManagaer;


    public MostPlayed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_most_played, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewTopListened = (RecyclerView) view.findViewById(R.id.rv_top_listened);
        recyclerViewArtist = (RecyclerView) view.findViewById(R.id.rv_artist);
        horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerViewTopListened.setLayoutManager(horizontalLayoutManagaer);
        recyclerViewArtist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        adapterTopListened = new AdapterTopListened();
        recyclerViewTopListened.setAdapter(adapterTopListened);
        recyclerViewTopListened.setNestedScrollingEnabled(false);

        recyclerViewArtist.setAdapter(adapterTopListened);
        recyclerViewArtist.setNestedScrollingEnabled(false);
    }
}
