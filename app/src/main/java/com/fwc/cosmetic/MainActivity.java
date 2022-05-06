package com.fwc.cosmetic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.fwc.cosmetic.fragment.CartFragment;
import com.fwc.cosmetic.fragment.HomeFragment;
import com.fwc.cosmetic.fragment.ProfileFragment;
import com.fwc.cosmetic.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    Fragment homeFragment = new HomeFragment();
    Fragment cartFragment = new CartFragment();
    Fragment searchFragment = new SearchFragment();
    Fragment profileFragment = new ProfileFragment();

    FirebaseAuth mAuth;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.navigation);
        mAuth = FirebaseAuth.getInstance();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_main:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homeFragment).commit();
                    return true;
                case R.id.nav_search:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, searchFragment).commit();
                    return true;
                case R.id.nav_cart:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, cartFragment).commit();
                    return true;
                case R.id.nav_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, profileFragment).commit();
                    return true;
            }

            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_main);
    }
}