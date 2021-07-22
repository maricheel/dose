package com.example.if_dose;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import com.example.if_dose.Models.Aliment;
import com.example.if_dose.Models.AlimentsList;
import com.example.if_dose.Models.Calculator;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import timber.log.Timber;

public class ShowCalculActivity extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    final int REQUEST_CODE = 123;
    private final static String TAG = "ShowCalculActivity";
    protected Calculator c;
    protected double resultat = 0.0;
    private SharedPreferences spGlycemies;
    private EditText tConfirm;
    private SharedPreferences sp;
    private int idx;
    private double glucideAvantRepas;
    private double unitInject;
    private Context context;
    private SharedPreferences.Editor editGlyc;
    private String alimentsDuJour = "";
    private TableLayout mytable;
    private TableRow ro;
    private TextView text1,totalegcg,ur, uniteInject,tutoCalc,pre;
    private ArrayList<Aliment> aliments;
    @Override
    public void onBackPressed() {
        //intent = after login ...
        Intent intent2 = new Intent(ShowCalculActivity.this, WelcomeActivity.class);
        startActivity(intent2);}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_calcul);
        Toolbar bar = (Toolbar)findViewById(R.id.toolbar);
        bar.setLogo(R.drawable.shield);
        bar.setTitle(R.string.app_name);
        setSupportActionBar(bar);
        AppCompatImageView retur =findViewById(R.id.appCompatImageView);
        tConfirm = (EditText) findViewById(R.id.name_edit_text2g);
        mytable=findViewById(R.id.mytable);
        text1=findViewById(R.id.hihi);
        totalegcg=findViewById(R.id.uc);
        ur=findViewById(R.id.ur);
        ro=findViewById(R.id.ro);
        pre=findViewById(R.id.pre);
        context = getApplicationContext();
        spGlycemies = context.getSharedPreferences("glycemies", Context.MODE_PRIVATE);
        editGlyc = spGlycemies.edit();
        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        float rp = sp.getFloat("rp", 0);
        float rd = sp.getFloat("rd", 0);
        float rc = sp.getFloat("rc", 0);
        float rdi = sp.getFloat("rdi", 0);
        float obj = sp.getFloat("obj", 0);
        float is = sp.getFloat("is", 0);
        Timber.i("rm : " + String.valueOf(rp));
        //textView5.setText(" "+is);
        Intent i = getIntent();
        glucideAvantRepas = i.getDoubleExtra("glucoAvantRepas", 0.0);
        idx = i.getIntExtra("idx", 0);


        Timber.i("idx : " + String.valueOf(idx));
        alimentsDuJour = i.getStringExtra("alimentsDuJour");
        Timber.i(TAG, "alimentsDuJour : " + alimentsDuJour);
        String activitePhysique = i.getStringExtra("activitePhysique");

        Timber.i(" activitePhysique : " + activitePhysique);

        retur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onBackPressed();}
        });


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_camera:
                        Intent intent = new Intent(ShowCalculActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_gallery:
                        Intent intent1 = new Intent(ShowCalculActivity.this, ProfileActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.nav_slideshow:
                        Intent intent2 = new Intent(ShowCalculActivity.this, ReportActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.nav_manage:
                        Intent intent3 = new Intent(ShowCalculActivity.this, GlucoseActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.nav_camera2:
                        Intent intent4 = new Intent(ShowCalculActivity.this, MealActivity.class);
                        startActivity(intent4);
                        break;
                } return false;
            }
        });





                aliments =  i.getParcelableArrayListExtra("Aliments");
        String livret = i.getStringExtra("livret");
        if (livret != null) {
            if (livret.equals("avec") && aliments != null) {

                //textView5.setText("ur "+);
                c = new Calculator(aliments, rp, rd, rc, rdi, is, obj, glucideAvantRepas, idx);
                ajouter();

            } else if (livret.equals("sans")) {
                mytable.setVisibility(View.GONE);
                double glucoTotal = i.getDoubleExtra("glucoTotal", 0);
                c = new Calculator(glucoTotal, rp, rd, rc, rdi, is, obj, glucideAvantRepas, idx);

            }
            Timber.i("R1 : " + String.valueOf(c.uc()));
            Timber.i("R2 : " + String.valueOf(c.ur()));
            Timber.i("ratio : " + String.valueOf(c.getRatio()));
            resultat = c.totalGluco();
            unitInject = c.uniteInjecter();
            Timber.i(" unitInject : " + unitInject);
            String[] activitesPhysique = getResources().getStringArray(R.array.activite_physique);
//            activitePhysique
            if (activitePhysique != null) {
                if (activitePhysique.equals(activitesPhysique[0])) {
                    Timber.i(" aucune activite ");
                } else if (activitePhysique.equals(activitesPhysique[1])) {
                    unitInject = unitInject - (20 * unitInject) / 100;
                    Timber.i(" activite faible (-20%) : " + unitInject);
                } else if (activitePhysique.equals(activitesPhysique[2])) {
                    unitInject = unitInject - (30 * unitInject) / 100;
                    Timber.i(" aucune modere (-30%) : " + unitInject);
                } else if (activitePhysique.equals(activitesPhysique[3])) {
                    unitInject = unitInject - (50 * unitInject) / 100;
                    Timber.i(" aucune intensive (-50%) : " + unitInject);
                }
            }

            String dx = "1", dx2 = "1";
            try {

                DecimalFormat df = new DecimalFormat("#.##");
                DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
                sym.setDecimalSeparator('.');
                df.setDecimalFormatSymbols(sym);
                dx = df.format(resultat);
//            Log.i(TAG, "dx : "+dx);
//            dx.replaceAll(",",".");
                resultat = Double.parseDouble(dx);
                dx2 = df.format(unitInject);
//            dx2.replaceAll(",",".");
//            Log.i(TAG, "dx2 : "+dx2);

            } catch (Exception e) {
                Timber.e(e.getMessage());
            }
            unitInject = Double.parseDouble(dx2);
            tutoCalc = findViewById(R.id.name_edit_textg);
            uniteInject = findViewById(R.id.name_edit_text1g);

            System.out.println("dddd");
            tutoCalc.setText(String.valueOf(new DecimalFormat("##.##").format(resultat)));
            uniteInject.setText(String.valueOf(new DecimalFormat("##.##").format(unitInject)));

        }else{
            tutoCalc = findViewById(R.id.name_edit_textg);
            tutoCalc.setText(""+livret);
        }

        totalegcg.setText(String.format("%.2f", c.uc()));
        ur.setText(String.format("%.2f", c.ur()));
        System.out.println(String.format("%.2f", c.ur())+"hohi");

    }

    public void ajouter(){
        for (int i=0;i<aliments.size();i++){

            TableRow row=new TableRow(this);
            TextView first=new TextView(this);
            TextView second=new TextView(this);
            TextView third=new TextView(this);
            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) ro.getLayoutParams();
            TableRow.LayoutParams paramsr= (TableRow.LayoutParams) text1.getLayoutParams();
            TableRow.LayoutParams paramsrp= (TableRow.LayoutParams) pre.getLayoutParams();
            if(i!=0){
                row.setPadding(0,4,0,0);
            }
            first.setPadding(0,20,0,20);
            second.setPadding(0,20,0,20);
            third.setPadding(0,20,0,20);

            paramsr.bottomMargin=0;
            third.setGravity(Gravity.RIGHT);
            first.setTextColor(Color.parseColor("#000000"));
            second.setTextColor(Color.parseColor("#000000"));
            third.setTextColor(Color.parseColor("#000000"));
            row.setLayoutParams(params);
            first.setLayoutParams(paramsr);
            first.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            second.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            third.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            first.setGravity(Gravity.CENTER);
            second.setGravity(Gravity.CENTER);
            third.setGravity(Gravity.CENTER);
            first.setBackgroundColor(getResources().getColor(R.color.white));
            second.setBackgroundColor(getResources().getColor(R.color.white));
            third.setBackgroundColor(getResources().getColor(R.color.white));
            third.setText(aliments.get(i).getName());
            row.addView(first);
            second.setLayoutParams(paramsr);

            second.setText(aliments.get(i).getQuantiteA());
            row.addView(second);
            third.setLayoutParams(paramsr);
            String[] parts = aliments.get(i).getQuantiteA().split(" ");
            double qaS = Double.parseDouble(parts[0]);
            double qb = aliments.get(i).getQuantiteB();
            double gb = aliments.get(i).getGlucide();
            double resu=qaS*gb/qb;
            first.setText(String.format("%.2f",resu)+" g");
            row.addView(third);
            mytable.addView(row);
        }


    }
    public void onClickConfirm(View v) {
        if ((tConfirm.getText().toString().trim().equals(""))) {
            Toast.makeText(getApplicationContext(), R.string.Taux_insuline,
                    Toast.LENGTH_SHORT).show();
        } else {

            double confirmDose = Double.parseDouble(String.valueOf(tConfirm.getText()));

            switch (idx) {
                case 0:
                    editGlyc.putFloat("glucoAvantRepas0", (float) glucideAvantRepas);
                    editGlyc.putFloat("unitInject0", (float) unitInject);
                    editGlyc.putFloat("confirmDose0", (float) confirmDose);
                    editGlyc.putString("aliments0", alimentsDuJour);
                    editGlyc.putFloat("gluco0", ((float) resultat));
                    break;
                case 1:
                    editGlyc.putFloat("glucoAvantRepas1", (float) glucideAvantRepas);
                    editGlyc.putFloat("unitInject1", (float) unitInject);
                    editGlyc.putFloat("confirmDose1", (float) confirmDose);
                    editGlyc.putString("aliments1", alimentsDuJour);
                    editGlyc.putFloat("gluco1", ((float) resultat));
                    break;
                case 2:
                    editGlyc.putFloat("glucoAvantRepas2", (float) glucideAvantRepas);
                    editGlyc.putFloat("unitInject2", (float) unitInject);
                    editGlyc.putFloat("confirmDose2", (float) confirmDose);
                    editGlyc.putString("aliments2", alimentsDuJour);
                    editGlyc.putFloat("gluco2", ((float) resultat));
                    break;
                case 3:
                    editGlyc.putFloat("glucoAvantRepas3", (float) glucideAvantRepas);
                    editGlyc.putFloat("unitInject3", (float) unitInject);
                    editGlyc.putFloat("confirmDose3", (float) confirmDose);
                    editGlyc.putString("aliments3", alimentsDuJour);
                    editGlyc.putFloat("gluco3", ((float) resultat));
                    break;
            }
            editGlyc.commit();

            String res = getString(R.string.conf);
            Toast.makeText(ShowCalculActivity.this, res, Toast.LENGTH_LONG).show();

            Intent intent1 = new Intent(ShowCalculActivity.this, GlucoseActivity.class);

            //////                                                                                                             ///////
            intent1.putExtra("idrep", idx);
           // intent1.putExtra("rep", sidx);



            startActivity(intent1);

            int page = idx;
            startNotificationAfter4Hours(page);
            Timber.i("bien");

        }
    }
    public void startNotificationAfter4Hours(int page) {
        long alarmTime = System.currentTimeMillis() + (4*60*60 * 1000);
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this,OnetimeAlarmReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,1,intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,alarmTime,pendingIntent);
        Toast.makeText(ShowCalculActivity.this, R.string.bien, Toast.LENGTH_LONG).show();
    }





}
