package com.noddyandfriends.disastermanager.util;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.noddyandfriends.disastermanager.model.RequestSeekerGroup;

/**
 * Created by anant on 29/1/17.
 */

public class OnAskForShelterButtonPressed implements View.OnClickListener {

    private Context context;
    private RequestSeekerGroup rsg;
    public OnAskForShelterButtonPressed(Context context, RequestSeekerGroup rsg){
        this.context = context;
        this.rsg = rsg;
    }

    @Override
    public void onClick(View view) {
        Uri base = Constants.HOST_URI;

        //TODO: manipulate base Uri


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                base.toString(),
                RequestSeekerGroup.getJsonFromSeekerGroup(rsg),
                null,
                null
        );
        //Request created


        AppController.getInstance().addToRequestQueue(request);
        //Request sent
    }
}
