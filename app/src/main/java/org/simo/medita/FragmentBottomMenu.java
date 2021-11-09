package org.simo.medita;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentBottomMenu extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragmant_menu_bottom, container, false);

        ImageView menu1 = (ImageView) view.findViewById(R.id.id_menu1);
        ImageView menu2 = (ImageView) view.findViewById(R.id.id_menu2);
        ImageView menu3 = (ImageView) view.findViewById(R.id.id_menu3);
        ImageView menu4 = (ImageView) view.findViewById(R.id.id_menu4);

        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Home.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Favoritos.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Progreso.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        return view;
    }
}
