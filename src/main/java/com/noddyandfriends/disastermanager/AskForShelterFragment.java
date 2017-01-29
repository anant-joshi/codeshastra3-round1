package com.noddyandfriends.disastermanager;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.noddyandfriends.disastermanager.util.Utilities;


/**
 * A simple {@link Fragment} subclass.
 */
public class AskForShelterFragment extends Fragment {


    public AskForShelterFragment() {
        // Required empty public constructor
    }
    private int shelterSeekerCount;
    private TextInputEditText[] names, ages;
    private AppCompatSpinner[] sexes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ask_for_shelter, container, false);
        TextInputEditText editText = (TextInputEditText) rootView.findViewById(R.id.request_shelter_number_of_people);
        shelterSeekerCount = 0;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String string = charSequence.toString();
                shelterSeekerCount = Integer.parseInt(string);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.request_shelter_layout_space);
        inflateViewsInto(linearLayout, shelterSeekerCount, getContext());
        Button sendRequest = (Button) rootView.findViewById(R.id.request_shelter_send_button);

        return rootView;
    }

    private void inflateViewsInto(ViewGroup container, int numViews, Context context){
        if(numViews>0){
            names = new TextInputEditText[numViews];
            ages = new TextInputEditText[numViews];
            sexes = new AppCompatSpinner[numViews];


            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int margins = Utilities.convertDpToPx(context, 8);
            params.setMargins(margins, margins, margins, margins);


            for(int i = 0; i<numViews; i++){
                names[i] = new TextInputEditText(context);
                names[i].setLayoutParams(params);
                names[i].setInputType(InputType.TYPE_CLASS_TEXT);

                ages[i] = new TextInputEditText(context);
                ages[i].setLayoutParams(params);
                ages[i].setInputType(InputType.TYPE_CLASS_NUMBER);

                sexes[i] = new AppCompatSpinner(context);
                params.setMargins(margins, margins, margins, 2*margins);
                sexes[i].setLayoutParams(params);
                sexes[i].setAdapter(new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, new String[]{"Female","Male"}));
                container.addView(names[i]);
                container.addView(ages[i]);
                container.addView(sexes[i]);
            }
        }
    }

}
