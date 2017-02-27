package sangmaneproject.kindis.view.fragment.detail;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sangmaneproject.kindis.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailAbout extends Fragment {
    String about;
    TextView txtAbout;

    public DetailAbout(String about) {
        this.about = about;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtAbout = (TextView) view.findViewById(R.id.txt_about);
        txtAbout.setText(about);
    }
}
