
package com.example.if_dose;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import timber.log.Timber;

public class GlucoseActivity extends AppCompatActivity {

    private ImageView imageViewBlood;
    private TextInputEditText name_edit_text, name_edit_text1, name_edit_text2, name_edit_text3, name_edit_text4;
    private Button buttonLogin;
    private BottomNavigationView bottomNavigationViewtop;
    private SharedPreferences spGlycemies;
    private SharedPreferences.Editor edit;
    private Context context;

    public void affect_glyc(final String GlycAp, String unitInject, String glucoAvantRepas,
                            String confirmDose, final String comment) {
        Float r1 = spGlycemies.getFloat(GlycAp, 0);
        if (r1 != null) name_edit_text1.setText(r1.toString());

        Float r2 = spGlycemies.getFloat(glucoAvantRepas, 0);
        if (r2 != null) name_edit_text.setText(r2.toString());

        Float r3 = spGlycemies.getFloat(unitInject, 0);
        if (r3 != null) name_edit_text2.setText(r3.toString());

        Float r4 = spGlycemies.getFloat(confirmDose, 0);
        if (r4 != null) name_edit_text3.setText(r4.toString());

        String r5 = spGlycemies.getString(comment, "");
        if (r5 != null) name_edit_text4.setText(r5);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.putString(comment, name_edit_text4.getText().toString());
                edit.apply();
            }
        });

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.glucosemanagement);
        Toolbar bar = findViewById(R.id.toolbar);
        setSupportActionBar(bar);
        bar.setLogo(R.drawable.shield);
        name_edit_text2 = findViewById(R.id.name_edit_text2g);
        name_edit_text3 = findViewById(R.id.name_edit_text3g);
        name_edit_text4 = findViewById(R.id.name_edit_text4g);
        name_edit_text1 = findViewById(R.id.name_edit_text1g);
        imageViewBlood = findViewById(R.id.imageViewBlood);
        name_edit_text = findViewById(R.id.name_edit_textg);
        buttonLogin = findViewById(R.id.buttonLogin);
        context = getApplicationContext();
        spGlycemies = context.getSharedPreferences("glycemies", Context.MODE_PRIVATE);
        edit = spGlycemies.edit();
        bottomNavigationViewtop = findViewById(R.id.bottom_navigation);
        //recuperer lindex de repas
        Intent i = getIntent();
        int idx = i.getIntExtra("idrep", 0);

        Menu men = bottomNavigationViewtop.getMenu();
        /*  MenuItem mit = men.getItem(0);
        mit.setChecked(true);*/
        switch (idx) {
            //iftar
            case 0:
                bottomNavigationViewtop.setSelectedItemId(R.id.djcof);
                affect_glyc("GlycAp0", "unitInject0", "glucoAvantRepas0"
                        , "confirmDose0", "comment1");//
                MenuItem mit0 = men.getItem(1);
                mit0.setChecked(true);
                break;
            //ghada2
            case 1:
                bottomNavigationViewtop.setSelectedItemId(R.id.dinch);
                affect_glyc("GlycAp1", "unitInject1", "glucoAvantRepas1"
                        , "confirmDose1", "comment2");//
                MenuItem mit1 = men.getItem(3);
                mit1.setChecked(true);
                break;
            //wjba khafifa
            case 2:
                bottomNavigationViewtop.setSelectedItemId(R.id.pjhamb);
                affect_glyc("GlycAp2", "unitInject2", "glucoAvantRepas2"
                        , "confirmDose2", "comment3");//
                MenuItem mit2 = men.getItem(0);
                mit2.setChecked(true);
                break;
            //3acha2
            case 3:
                bottomNavigationViewtop.setSelectedItemId(R.id.colrice);
                affect_glyc("GlycAp3", "unitInject3", "glucoAvantRepas3"
                        , "confirmDose3", "comment4");
                MenuItem mit3 = men.getItem(2);
                mit3.setChecked(true);
                break;
        }

        bottomNavigationViewtop.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.djcof:
                    affect_glyc("GlycAp0", "unitInject0", "glucoAvantRepas0"
                            , "confirmDose0", "comment1");//
                    break;
                    case R.id.dinch:
                        affect_glyc("GlycAp1", "unitInject1", "glucoAvantRepas1"
                                , "confirmDose1", "comment2");//
                        break;
                    case R.id.pjhamb:
                        affect_glyc("GlycAp2", "unitInject2", "glucoAvantRepas2"
                                , "confirmDose2", "comment3");//
                        break;
                    case R.id.colrice:
                        affect_glyc("GlycAp3", "unitInject3", "glucoAvantRepas3"
                                , "confirmDose3", "comment4");
                        break;
                }
                return false;
            }
        });
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationViewG);
        bottomNavigationView.setSelectedItemId(R.id.nav_manage);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_camera:
                        Intent intent = new Intent(GlucoseActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_gallery:
                        Intent intent1 = new Intent(GlucoseActivity.this, ProfileActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.nav_slideshow:
                        Intent intent2 = new Intent(GlucoseActivity.this, ReportActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.nav_manage:
                        Intent intent3 = new Intent(GlucoseActivity.this, GlucoseActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.nav_camera2:
                        Intent intent4 = new Intent(GlucoseActivity.this, MealActivity.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });


    }

}

