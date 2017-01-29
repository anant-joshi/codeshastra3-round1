package com.noddyandfriends.disastermanager;


import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.noddyandfriends.disastermanager.model.User;
import com.noddyandfriends.disastermanager.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.noddyandfriends.disastermanager.util.Constants.USER_ADDRESS;
import static com.noddyandfriends.disastermanager.util.Constants.USER_DATE_OF_BIRTH;
import static com.noddyandfriends.disastermanager.util.Constants.USER_EMERGENCY_NAME;
import static com.noddyandfriends.disastermanager.util.Constants.USER_EMERGENCY_TEL;
import static com.noddyandfriends.disastermanager.util.Constants.USER_NAME;
import static com.noddyandfriends.disastermanager.util.Constants.USER_PREFS;
import static com.noddyandfriends.disastermanager.util.Constants.USER_SEX;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnterDetailsFragment extends Fragment {
    private static String LOG_TAG = EnterDetailsFragment.class.getName();
    TextInputEditText nameEditText;
    TextInputEditText addressEditText;
    TextInputEditText dateOfBirthEditText;
    TextInputEditText emergencyContactName;
    TextInputEditText emergencyContactNumber;
    AppCompatSpinner foodPreferenceSpinner;
    Button sendButton;
    private View.OnClickListener onSendDetails;

    public EnterDetailsFragment() {
        onSendDetails = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(USER_PREFS, 0).edit();
                editor.putString(USER_NAME, nameEditText.getText().toString());
                editor.putString(USER_ADDRESS, addressEditText.getText().toString());
                editor.putString(USER_EMERGENCY_NAME, emergencyContactName.getText().toString());
                editor.putString(USER_EMERGENCY_TEL, emergencyContactNumber.getText().toString());
                String spinnerValue = foodPreferenceSpinner.getSelectedItem().toString();
                if (spinnerValue.equalsIgnoreCase(getResources().getStringArray(R.array.spinner_gender)[0])) {
                    editor.putBoolean(USER_SEX, true);
                } else {
                    editor.putBoolean(USER_SEX, false);
                }
                Date date = null;
                try {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(dateOfBirthEditText.getText().toString());
                    editor.putLong(USER_DATE_OF_BIRTH, date.getTime());
                } catch (ParseException pe) {
                    Log.e(LOG_TAG, pe.getMessage());
                }
                editor.apply();

                User user = new User(
                        nameEditText.getText().toString(),
                        addressEditText.getText().toString(),
                        emergencyContactName.getText().toString(),
                        emergencyContactNumber.getText().toString(),
                        spinnerValue.equalsIgnoreCase(getResources().getStringArray(R.array.spinner_gender)[0]),
                        date);

                Uri uri = Constants.HOST_URI;
                //TODO: Manipulate host uri

                JsonObjectRequest jorLagakeHaisha = new JsonObjectRequest(
                        Request.Method.POST,
                        uri.toString(),
                        User.userToJson(user, 0, 0, null),
                        null,
                        null
                );
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_enter_details, container, false);
        nameEditText = (TextInputEditText) rootView.findViewById(R.id.enter_details_input_name);
        addressEditText = (TextInputEditText) rootView.findViewById(R.id.enter_details_input_address);
        dateOfBirthEditText = (TextInputEditText) rootView.findViewById(R.id.enter_details_input_dob);
        emergencyContactName = (TextInputEditText) rootView.findViewById(R.id.enter_details_input_emergency_contact_name);
        emergencyContactNumber = (TextInputEditText) rootView.findViewById(R.id.enter_details_input_emergency_number);
        foodPreferenceSpinner = (AppCompatSpinner) rootView.findViewById(R.id.enter_details_input_food_preference);
        sendButton = (Button) rootView.findViewById(R.id.enter_details_send_details);
        sendButton.setOnClickListener(onSendDetails);
        return rootView;
    }

}
