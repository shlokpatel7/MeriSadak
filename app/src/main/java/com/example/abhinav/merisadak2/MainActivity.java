package com.example.abhinav.merisadak2;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    Button button;
    FloatingActionButton floatingActionButton;
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                100);
        setContentView(R.layout.activity_main);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            //user signed in
            floatingActionButton=findViewById(R.id.fab_button);
            floatingActionButton.setClickable(true);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(MainActivity.this,AddFeedActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // No user is signed in
            sendToLogin();
        } else {
            //user signed in
            getSupportActionBar().setTitle("Welcome to MeriSadak");
            bottomNavigationView=findViewById(R.id.bottom_nav_view);
            bottomNavigationView.setOnNavigationItemSelectedListener(this);
            Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_frame,new FeedFragment()).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.top_items,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.sign_out){
            FirebaseAuth.getInstance().signOut();
            sendToLogin();
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_feed:
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_frame,new FeedFragment()).commit();
                break;
            case R.id.my_account:
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.main_frame,new MyAccountFragment()).commit();
                break;
        }
        item.setCheckable(true);
        return true;
    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

}
