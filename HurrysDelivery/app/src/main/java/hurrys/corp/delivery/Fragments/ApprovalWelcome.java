package hurrys.corp.delivery.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import hurrys.corp.delivery.Configurations.Session;
import hurrys.corp.delivery.R;


public class ApprovalWelcome extends Fragment {


    public ApprovalWelcome() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_approval_welcome, container, false);
        Button go=v.findViewById(R.id.go);

        Session session=new Session(getActivity());


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getActivity()!=null) {
                    Fragment fragment = new OfflineFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).commitAllowingStateLoss();
                }

            }
        });

        return v;
    }
}
