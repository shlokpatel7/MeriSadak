package com.example.abhinav.merisadak2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyAccountFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my_account,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            TextView nameText,mailText;
            nameText=view.findViewById(R.id.name_text);
            mailText=view.findViewById(R.id.email_text);
            nameText.setText("Name : "+user.getDisplayName());
            mailText.setText("Email : "+user.getEmail());
        }
    }
}
