package cs.dal.food4fit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by graceliu on 2017-11-20.
 */

public class DirectionsFragment extends Fragment {
    private static final String TAG = "DirectionsFragment";

    private Button btnTEST2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_directions,container,false);
        btnTEST2 = (Button) view.findViewById(R.id.btnTEST2);

        btnTEST2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TESTING BUTTON CLICK 2",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
