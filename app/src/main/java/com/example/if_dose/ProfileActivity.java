package com.example.if_dose;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProfileActivity extends AppCompatActivity {
    private ImageView imageViewBlood;
    private TextInputEditText name_edit_text,name_edit_text2,name_edit_text3,name_edit_text4,name_edit_text5;
    private TextInputLayout name_text_input,name_text_input2,name_text_input3,name_text_input4,name_text_input5;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_info);
        Toolbar bar =findViewById(R.id.toolbar);
        setSupportActionBar(bar);
        bar.setLogo(R.drawable.shield);
        name_edit_text = findViewById(R.id.name_edit_text);
        name_edit_text2= findViewById(R.id.name_edit_text2);
        name_edit_text3= findViewById(R.id.name_edit_text3);
        name_edit_text4= findViewById(R.id.name_edit_text4);
        name_edit_text5= findViewById(R.id.name_edit_text5);
        name_text_input=findViewById(R.id.name_text_input);
        name_text_input2=findViewById(R.id.name_text_input2);
        name_text_input3=findViewById(R.id.name_text_input3);
        name_text_input4=findViewById(R.id.name_text_input4);
        name_text_input5=findViewById(R.id.name_text_input5);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationViewP);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.nav_gallery);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_camera:
                        Intent intent = new Intent(ProfileActivity.this, WelcomeActivity.class);
                        startActivity(intent);

                        break;

                    case R.id.nav_gallery:
                        Intent intent1 = new Intent(ProfileActivity.this, ProfileActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.nav_slideshow:
                        Intent intent2 = new Intent(ProfileActivity.this, ReportActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.nav_manage:
                        Intent intent3 = new Intent(ProfileActivity.this, GlucoseActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.nav_camera2:
                        Intent intent4 = new Intent(ProfileActivity.this, MealActivity.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });

        SharedPreferences sp;

        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        float rp = sp.getFloat("rp", 0);
        float rd = sp.getFloat("rd", 0);
        float rc = sp.getFloat("rc", 0);
        float rdi = sp.getFloat("rdi", 0);
        float is = sp.getFloat("is", 0);

        name_edit_text.setText(String.valueOf(rp));
        name_edit_text.setEnabled(false);
        name_edit_text2.setText(String.valueOf(rd));
        name_edit_text2.setEnabled(false);
        name_edit_text3.setText(String.valueOf(rc));
        name_edit_text3.setEnabled(false);
        name_edit_text4.setText(String.valueOf(rdi));
        name_edit_text4.setEnabled(false);
        name_edit_text5.setText(String.valueOf(is));
        name_edit_text5.setEnabled(false);

    }
}
