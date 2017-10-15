package com.example.ruben.myapplication.vocabularycard;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ruben.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardFragment extends Fragment {


    public CardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.vocabulary_card,null);
        TextView myTextView = (TextView)v.findViewById(R.id.textView);

        myTextView.setText(getArguments().getString("frgText"));
        // inflate the layout corresponding to this fragment.
        return v;
    }

}
