package com.example.if_dose;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.if_dose.Models.Aliment;


import com.example.if_dose.Singleton.HttpSingleton;
import com.example.if_dose.db.AlimentDao;
import com.example.if_dose.db.DaoSession;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;


public class WelcomeActivity extends AppCompatActivity {
    public static final String KEY_NAME = "name", KEY_NAMEF = "namef";
    public static final String KEY_ID = "id";
    public int count;
    public static final String JSON_ARRAY = "categories";
    public static final String JSON_ARRAY_2 = "aliments";
    private final static String TAG = "CalculActivity";
    private static final int SWIPE_DURATION = 250;
    private static final int MOVE_DURATION = 150;
    boolean mItemPressed = false;
    boolean mSwiping = false;
    private ImageView imageViewBlood, imageVie,imageVie1;
    private androidx.constraintlayout.widget.ConstraintLayout.LayoutParams ly;
    private TextView textViewActivity, textView7, textView8, SHowFood;
    private Button btnbrakfast, btnlunch, btndinner, btnsnack;
    private Button buttonLogin, buttonLogin1;


    private RadioButton radioAvecLivret5, radioAvecLivret2, radioAvecLivret3, radioAvecLivret1, radioAvecLivret4, radioSansLivret;
    private EditText name_edit_text, foodQty_edit_text;
    private Spinner foodQtytype_spinner, active;
    private AutoCompleteTextView food_edit_text;
    private TextInputLayout foodQty_text_input;
    private String type_alim = "";
    private TextInputLayout name_text_inpu;
    private TextInputEditText name_edit_tex;
    private String name, unite, q;
    private String type_alim_arab = "";
    private SwipeMenuListView listview;
    private AutoCompleteTextView alimentsAutoComp;
    double g = 0;
    double gd = 0;
    private ArrayAdapter adapter;
    private ArrayList<Aliment> listAliment = new ArrayList<Aliment>();
    private ArrayList<String> listnamealim = new ArrayList<String>();
    private Context context;
    private View v;
    JSONObject jsonApi;
    private SharedPreferences sp;
    private Intent i;
    private JSONArray result;
    private Aliment food = null;
    private String calculType = "";
    private String JsonCatURL;
    private int idx = 0;
    private RadioGroup groupradio;
    private Map<String, Integer> map;
    private Map<String, Integer> map1;
    private TextView textView6;
    private DaoSession daoSession;
    private String host, port;
    boolean estoui = true;
    private ImageView imgautocomp;
    SharedPreferences.Editor edit;
    private StringBuilder alimentsDuJour;
    private List<String> alimdao;
    private List<Integer> iddb;
    private RequestQueue queue;
    private int id;
    private String date;
    private String JsonFoodURL;
    private AlimentDao alimentDao;
    private ColorStateList clrstA, clrstD;
    private LanguageHelper LanguageHelper = new LanguageHelper();
    Network network;
    private DatabaseHelp db;
    private String pid;
    protected  static int RESULT_SPEECH;

    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle(R.string.exit)
                .setMessage(R.string.estsur)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//*Change Here*
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton(R.string.no, null).show();
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LanguageHelper.loadLocale(this.getResources(), getSharedPreferences("Settings", MODE_PRIVATE));
        boolean r = true;
        //Verify if user exists in database else return to login
        db = new DatabaseHelp(this);
        Cursor data = db.getData();
        Cursor ids = db.getItemID("me");
        while (ids.moveToNext()) {
            System.out.println("===========++>" + ids.getInt(0));
        }
        ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add(data.getString(1));
        }
        System.out.println("zzzzzzzzzzzzzzzzz" + listData);
        if (listData.isEmpty()) r = false;

        if (!r) {
            //si il y a 1 problem dans sqllite on  retourn vers ...
            Intent intent1 = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent1);
        }

        setContentView(R.layout.welcome);
        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        bar.setLogo(R.drawable.shield);
        setSupportActionBar(bar);
        name_text_inpu = findViewById(R.id.name_text_inpu);
        name_edit_tex = findViewById(R.id.name_edit_tex);
        context = getBaseContext();
        network = new BasicNetwork(new HurlStack());
        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        host = sp.getString("host_url", getString(R.string.host_adr));
        port = sp.getString("host_port", getString(R.string.host_port));
        pid = sp.getString("id", "0");
        JsonCatURL = host + ":" + port + getString(R.string.urlAliments);
        map = new HashMap<String, Integer>();
        map1 = new HashMap<String, Integer>();
        queue = HttpSingleton.getInstance(this.getBaseContext()).getRequestQueue();
        queue.start();
        foodQty_edit_text = findViewById(R.id.foodQty_edit_text);
        foodQtytype_spinner = findViewById(R.id.foodQtytype_spinner);
        active = findViewById(R.id.Active);
        food_edit_text = findViewById(R.id.food_text_input);
        listview = findViewById(R.id.listview);
        btnbrakfast = findViewById(R.id.btnbrakfast);
        btnsnack = findViewById(R.id.btnsnack);
        btnlunch = findViewById(R.id.btnlunch);
        btndinner = findViewById(R.id.btndinner);
        groupradio = findViewById(R.id.groupradio);
        radioAvecLivret1 = findViewById(R.id.radioAvecLivret1);

        radioSansLivret = findViewById(R.id.radioSansLivret);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin1 = findViewById(R.id.buttonLogin1);

        imageVie = findViewById(R.id.imageVie);
        textViewActivity = findViewById(R.id.textViewActivity);

        foodQty_text_input = findViewById(R.id.foodQty_text_input);

        textView8 = findViewById(R.id.textView8);
        SHowFood = findViewById(R.id.SHowFood);
        imageViewBlood = findViewById(R.id.imageViewBlood);
        name_edit_text = findViewById(R.id.name_edit_text);

        alimentsDuJour = new StringBuilder();
        imgautocomp = findViewById(R.id.imageView);

                                                                                                    //**********************************************************//
        imageVie1 = findViewById(R.id.imageVie1);
        imageVie1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_edit_text.setText("");
            }
        });
        imageVie1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.EXTRA_LANGUAGE_MODEL);
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FA");
                } else {
                    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-MA");
                }
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");

                try {
                    startActivityForResult(i,RESULT_SPEECH);
                    food_edit_text.setText("");
                }catch (ActivityNotFoundException e){
                    Toast.makeText(getApplicationContext(),"your phone bla bla bla",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });


        daoSession = ((App) getApplication()).getDaoSession();
        alimentDao = daoSession.getAlimentDao();

        count = sp.getInt("count", 0);
        //getCategories();
        if (alimentDao.count() == 0) {
            //860...
            getCategories();
        }

        ////////////////////// x

        imageVie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_edit_text.setText("");
            }
        });


        System.out.println("+++++++++++++DAO++++++++++++" + alimentDao.count());
        final PopupMenu popupMenu = new PopupMenu(this, imgautocomp);

        /*Get aliments list from server*/
        alimdao = new ArrayList<>();
        getalimentsfromdatabase();

        System.out.println(alimdao);


        ArrayAdapter<String> adaptera = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, alimdao);

        ////popup avec les alim
        food_edit_text.setAdapter(adaptera);
        food_edit_text.setThreshold(1);
        imgautocomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(alimdao);
                popupMenu.getMenu().clear();
                for (int i = 0; i < alimdao.size(); i++) {
                    popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, alimdao.get(i));
                }


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        food_edit_text.setText(item.getTitle());

                        System.out.println(item.getTitle() + "++++++++++ttyt+++++++++++++++++++");
                        return true;
                    }
                });


                popupMenu.show();
            }
        });

        clrstA = radioAvecLivret1.getButtonTintList();
        clrstD = radioSansLivret.getButtonTintList();
        //haiden
        name_edit_tex.setVisibility(View.GONE);
        name_text_inpu.setVisibility(View.GONE);


        //livret ou sans...
        groupradio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radioSansLivret.getId()) {

                    Timber.i("Sans livret");
                    food_edit_text.setVisibility(View.GONE);

                    name_edit_tex.setVisibility(View.VISIBLE);
                    name_text_inpu.setVisibility(View.VISIBLE);
                    imageVie1.setVisibility(View.GONE);
                    foodQtytype_spinner.setVisibility(View.GONE);
                    foodQty_edit_text.setVisibility(View.GONE);
                    buttonLogin.setVisibility(View.GONE);
                    SHowFood.setVisibility(View.GONE);
                    listview.setVisibility(View.GONE);
                    imgautocomp.setVisibility(View.GONE);
                    imageVie.setVisibility(View.GONE);
                    foodQty_text_input.setVisibility(View.GONE);

                    //style :sheked
                    radioSansLivret.setBackground(getResources().getDrawable(R.drawable.rectangle179));
                    radioSansLivret.setTextColor(getResources().getColor(R.color.radiocolor));
                    radioSansLivret.setButtonTintList(clrstA);

                    radioAvecLivret1.setBackground(getResources().getDrawable(R.drawable.rectangle180));
                    radioAvecLivret1.setTextColor(getResources().getColor(R.color.radiocolordisable));
                    //???
                    radioAvecLivret1.setButtonTintList(clrstD);

                } else {
                    Timber.i("avec livret");
                    Toast.makeText(getBaseContext(), "avec livret", Toast.LENGTH_SHORT).show();

//                    categoryFoodTV.setVisibility(View.VISIBLE);

                    name_edit_tex.setVisibility(View.GONE);
                    name_text_inpu.setVisibility(View.GONE);
//                    categoriesSpinner.setVisibility(View.VISIBLE);
                    food_edit_text.setVisibility(View.VISIBLE);
                    imageVie1.setVisibility(View.VISIBLE);
                    foodQtytype_spinner.setVisibility(View.VISIBLE);
                    foodQty_edit_text.setVisibility(View.VISIBLE);
                    buttonLogin.setVisibility(View.VISIBLE);
                    imageVie.setVisibility(View.VISIBLE);
                    SHowFood.setVisibility(View.VISIBLE);
                    listview.setVisibility(View.VISIBLE);
                    imgautocomp.setVisibility(View.VISIBLE);
                    foodQty_text_input.setVisibility(View.VISIBLE);
                    radioSansLivret.setBackground(getResources().getDrawable(R.drawable.rectangle180));
                    radioSansLivret.setTextColor(getResources().getColor(R.color.radiocolordisable));
                    radioSansLivret.setButtonTintList(clrstD);
                    radioAvecLivret1.setBackground(getResources().getDrawable(R.drawable.rectangle179));
                    radioAvecLivret1.setTextColor(getResources().getColor(R.color.radiocolor));
                    radioAvecLivret1.setButtonTintList(clrstA);


                }
            }
        });


        adapter = new ArrayAdapter(WelcomeActivity.this, android.R.layout.simple_list_item_1, listnamealim);
        listview.setAdapter(adapter);


        /////////////////////////////////////////////////////ne marche pas
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(245, 246, 250)));
                // set item width
                deleteItem.setWidth(170);

                // set a icon
                deleteItem.setIcon(R.drawable.ic_sup);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };


        listview.setMenuCreator(creator);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("nossairdelete");

                Log.i("Log", "delete");
                listnamealim.remove(position);
                listAliment.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        listview.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                listview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                        System.out.println("nossairdelete");

                        Log.i("Log", "delete");
                        listnamealim.remove(position);
                        listAliment.remove(position);
                        adapter.notifyDataSetChanged();


                        // true : close the menu; false : not close the menu
                        return true;
                    }
                });
            }

            @Override
            public void onSwipeEnd(int position) {
                listview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                        System.out.println("nossairdelete");
                        Log.i("Log", "delete");
                        listnamealim.remove(position);
                        listAliment.remove(position);
                        adapter.notifyDataSetChanged();


                        // true : close the menu; false : not close the menu
                        return true;
                    }
                });
            }
        });


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        bottomNavigationView.setSelectedItemId(R.id.nav_gallery);
        menuItem.setChecked(true);
        count = sp.getInt("count", 0);
        bottomNavigationView.setSelectedItemId(R.id.nav_camera);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_camera:

                        break;

                    case R.id.nav_gallery:
                        Intent intent1 = new Intent(WelcomeActivity.this, ProfileActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.nav_slideshow:
                        Intent intent2 = new Intent(WelcomeActivity.this, ReportActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.nav_manage:
                        Intent intent3 = new Intent(WelcomeActivity.this, GlucoseActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.nav_camera2:
                        Intent intent4 = new Intent(WelcomeActivity.this, MealActivity.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });

        btnbrakfast.setBackground(getResources().getDrawable(R.drawable.rectangle186));
        btnsnack.setBackground(getResources().getDrawable(R.drawable.rectangle186));
        btnlunch.setBackground(getResources().getDrawable(R.drawable.rectangle186));
        btndinner.setBackground(getResources().getDrawable(R.drawable.rectangle186));
        //-------------------------------------------------------------------------------
        btnbrakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedbutton(view);

            }
        });

        btndinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedbutton(view);

            }
        });
        btnsnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedbutton(view);
            }
        });
        btnlunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedbutton(view);
            }
        });

        //pour l'ajout dans la listview aliment


    }
    ///////////////////////******************************////////////////////////////////////////////////////////////////////////////*********************
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_SPEECH) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                food_edit_text.setText(" ");
                //food_edit_text.setText(text.get(0));
                for (int i = 0; i < alimdao.size(); i++) {
                if (alimdao.get(i).equals(text.get(0)) ){
                        food_edit_text.setText(text.get(0));
                        break;
                    }
                }
                //listnamealim
            }
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //pour choix de type de repas                                                                                    ******************************
    public void selectedbutton(View view) {

        switch (view.getId()) {
            case R.id.btnbrakfast:
                btnbrakfast.setBackground(getResources().getDrawable(R.drawable.rectangle171));
                btnsnack.setBackground(getResources().getDrawable(R.drawable.rectangle186));
                btnlunch.setBackground(getResources().getDrawable(R.drawable.rectangle186));
                btndinner.setBackground(getResources().getDrawable(R.drawable.rectangle186));
                type_alim = "Petit déjeuner";
                type_alim_arab = "الفطور";
                break;
            case R.id.btndinner:
                btndinner.setBackground(getResources().getDrawable(R.drawable.rectangle171));
                btnsnack.setBackground(getResources().getDrawable(R.drawable.rectangle186));
                btnlunch.setBackground(getResources().getDrawable(R.drawable.rectangle186));
                btnbrakfast.setBackground(getResources().getDrawable(R.drawable.rectangle186));
                type_alim = "Le dîner";
                type_alim_arab = "العشاء";
                break;
            case R.id.btnlunch:
                btnlunch.setBackground(getResources().getDrawable(R.drawable.rectangle171));
                btnsnack.setBackground(getResources().getDrawable(R.drawable.rectangle186));
                btndinner.setBackground(getResources().getDrawable(R.drawable.rectangle186));
                btnbrakfast.setBackground(getResources().getDrawable(R.drawable.rectangle186));
                type_alim = "Déjeuner";
                type_alim_arab = "الغذاء";
                break;
            case R.id.btnsnack:
                btnsnack.setBackground(getResources().getDrawable(R.drawable.rectangle171));
                btndinner.setBackground(getResources().getDrawable(R.drawable.rectangle186));
                btnlunch.setBackground(getResources().getDrawable(R.drawable.rectangle186));
                btnbrakfast.setBackground(getResources().getDrawable(R.drawable.rectangle186));
                type_alim = "Collation";
                type_alim_arab = "وجبة خفيفة";
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("csdkncnsjsvjsdbjvbjsbjvbs");
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.action_logout) {
            db.delete("me");
            recreate();
            return true;
        }

        if (item.getItemId() == R.id.action_langue) {

            Toast.makeText(this, "langue sélectionnée", Toast.LENGTH_SHORT);
            return true;
        }
        switch (item.getItemId()) {
            case R.id.langue_AR:

                LanguageHelper.setLocale(this.getResources(), " ", getSharedPreferences("Settings", MODE_PRIVATE));
                recreate();
                break;
            case R.id.langue_FR:

                LanguageHelper.setLocale(this.getResources(), "fr", getSharedPreferences("Settings", MODE_PRIVATE));
                recreate();
                break;
        }
        return super.onOptionsItemSelected(item);

    }


    public void calcul(View view) {

        v = view;
        try {

            if ((name_edit_text.getText().toString().trim().equals(""))) {

                Toast.makeText(context, R.string.Q_glicus, Toast.LENGTH_SHORT)
                        .show();
            } else if (Double.parseDouble(name_edit_text.getText().toString()) <= 0.9 || Double.parseDouble(name_edit_text.getText().toString()) >= 5) {
                Toast.makeText(context, R.string.glucosup, Toast.LENGTH_SHORT)
                        .show();
                //////////////////////////////////////////////


            } else if (getIdx() == -1) {

                Toast.makeText(context, R.string.Q_repas, Toast.LENGTH_SHORT)
                        .show();


            } else if (Double.parseDouble(name_edit_text.getText().toString()) >= 2.5) {
                /*----------------------*/
                //i.putExtra("repas", radioGroup.getCheckedRadioButtonId());


                AlertDialog.Builder pupap = new AlertDialog.Builder(this);
                pupap.setTitle(R.string.pupaptitle);
                pupap.setMessage(R.string.pupapmsg);
                pupap.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        g = Double.parseDouble(String.valueOf(name_edit_text.getText()));

                        if (buttonLogin1.getText().equals("احسب الآن")) {
                            i = new Intent(context, ShowCalculActivity.class);
                        } else {
                            i = new Intent(context, ShowCalculActivityfr.class);
                        }

//////////////////////////////////////////////////////////::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
                        Timber.i("glucoAvantRepas :" + g);
                        i.putExtra("glucoAvantRepas", g);
                        if (radioSansLivret.isChecked()) {
                            gd = Double.parseDouble(String.valueOf(name_edit_tex.getText()));
                            Timber.i("glucodansRepas :" + gd);
                            i.putExtra("glucodansRepas", gd);
                        }
                        idx = getIdx();
                        i.putExtra("idx", idx);


                        String activitePhysique = active.getSelectedItem().toString();


                        Timber.i("activitePhysique : " + activitePhysique);
                        i.putExtra("activitePhysique", activitePhysique);


                        if (radioAvecLivret1.isChecked()) {
                            calculType = "Avec livret";
                        } else if (radioSansLivret.isChecked()) {
                            calculType = "Sans livret";
                        }

                        String calculTypes[] = getResources().getStringArray(R.array.calcul_type);

                        if (calculType.equals("Avec livret")) {
                            i.putExtra("livret", "avec");
                            System.out.println("calculbien");
                    /*Bundle args = new Bundle();
                    args.putParcelable("Array", (Parcelable) listAliment);*/
                            i.putParcelableArrayListExtra("Aliments", listAliment);
                            System.out.println("calculbien");
                            i.putExtra("alimentsDuJour", alimentsDuJour.toString());
                        } else if (calculType.equals("Sans livret")) {
                            if ((name_edit_text.getText().toString().trim().equals(""))) {
                                Toast.makeText(context, R.string.quntiteglucose, Toast.LENGTH_SHORT).show();
                            } else {
                                i.putExtra("livret", "sans");
                                String glucoTotal = name_edit_text.getText().toString();
                                i.putExtra("glucoTotal", Double.parseDouble(glucoTotal));
                            }
                        }
                        System.out.println("calculbien");
                        boolean isvalid = false;
                        for (Aliment k : listAliment) {
                            System.out.println(k);
                            if (k.getGlucide() == null) {
                                break;
                            } else {
                                isvalid = true;
                            }
                        }
                        if (!calculType.equals("Sans livret")) {
                            if (isvalid) {
                                update();
                                startActivity(i);
                                System.out.println("calculbien");
                                finish();
                            } else {
                                Toast.makeText(WelcomeActivity.this, R.string.msgconfirmation, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            startActivity(i);
                            System.out.println("calculbien");
                            finish();
                        }
                    }
                });
                pupap.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name_edit_text.setFocusable(true);
                        name_edit_text.setText("");
                    }
                });
                pupap.show();

            } else {
                if (calculType.equals("Sans livret")) {
                    if ((name_edit_text.getText().toString().trim().equals(""))) {
                        Toast.makeText(context, R.string.Q_glicusd, Toast.LENGTH_SHORT)
                                .show();
                    }
                }

                g = Double.parseDouble(String.valueOf(name_edit_text.getText()));
                if (buttonLogin1.getText().equals("احسب الآن")) {
                    i = new Intent(context, ShowCalculActivity.class);
                } else {
                    i = new Intent(context, ShowCalculActivityfr.class);
                }

                Timber.i("glucoAvantRepas :" + g);
                i.putExtra("glucoAvantRepas", g);
                if (radioSansLivret.isChecked()) {
                    gd = Double.parseDouble(String.valueOf(name_edit_tex.getText()));
                    Timber.i("glucodansRepas :" + gd);
                    i.putExtra("glucodansRepas", gd);
                }
                idx = getIdx();
                i.putExtra("idx", idx);
                String activitePhysique = active.getSelectedItem().toString();


                Timber.i("activitePhysique : " + activitePhysique);
                i.putExtra("activitePhysique", activitePhysique);

                String calculType = "";
                if (radioAvecLivret1.isChecked()) {
                    calculType = "Avec livret";
                } else if (radioSansLivret.isChecked()) {
                    calculType = "Sans livret";
                }

                String calculTypes[] = getResources().getStringArray(R.array.calcul_type);

                if (calculType.equals("Avec livret")) {
                    i.putExtra("livret", "avec");
                    System.out.println("calculbien");
                    /*Bundle args = new Bundle();
                    args.putParcelable("Array", (Parcelable) listAliment);*/
                    i.putParcelableArrayListExtra("Aliments", listAliment);
                    System.out.println("calculbien");
                    i.putExtra("alimentsDuJour", alimentsDuJour.toString());
                } else if (calculType.equals("Sans livret")) {
                    if ((name_edit_text.getText().toString().trim().equals(""))) {
                        Toast.makeText(context, R.string.quntiteglucose, Toast.LENGTH_SHORT).show();
                    } else {
                        i.putExtra("livret", "sans");
                        String glucoTotal = name_edit_tex.getText().toString();
                        i.putExtra("glucoTotal", Double.parseDouble(glucoTotal));
                    }
                }
                System.out.println("calculbien");
                boolean isvalid = false;
                for (Aliment k : listAliment) {
                    if (k.getGlucide() == null) {
                        break;
                    } else {
                        isvalid = true;
                    }
                }
                if (!calculType.equals("Sans livret")) {
                    if (isvalid) {
                        update();
                        startActivity(i);
                        System.out.println("calculbien");
                        finish();
                    } else {
                        Toast.makeText(WelcomeActivity.this, R.string.msgconfirmation, Toast.LENGTH_LONG).show();
                    }
                } else {
                    update();
                    startActivity(i);
                    System.out.println("calculbien");
                    finish();
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
            name_edit_text.setText(e.getMessage());
        }

    }


    private int getIdx() {

        String selectedMealFr = type_alim;
        String selectedMealAr = type_alim_arab;
        String[] meals = getResources().getStringArray(R.array.meal_type);

        if (selectedMealFr.equals(meals[0]) || selectedMealAr.equals(meals[0])) {
            return 0;
        } else if (selectedMealFr.equals(meals[1]) || selectedMealAr.equals(meals[1])) {
            return 1;
        } else if (selectedMealFr.equals(meals[2]) || selectedMealAr.equals(meals[2])) {
            return 2;
        } else if (selectedMealFr.equals(meals[3]) || selectedMealAr.equals(meals[3])) {
            return 3;
        }

        return -1;
    }

    private void getalimentsfromdatabase() {

        String url = host + ":" + port + "/api/aliments";

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                //Add aliment name to the array of aliments

                                if (Locale.getDefault().getLanguage().equals("fr")) {
                                    if (!obj.get("namef").toString().equals("null"))
                                        alimdao.add((String) obj.get("namef"));
                                    map.put(obj.getString(KEY_NAMEF), obj.getInt(KEY_ID));
                                } else {
                                    alimdao.add((String) obj.get("name"));
                                    map.put(obj.getString(KEY_NAME), obj.getInt(KEY_ID));
                                }

                                System.out.println(obj.get("id") + "tititoto");
                            }
                            //getCategoriesList(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        error.printStackTrace();
                        System.out.println("gggggg" + error.getMessage());
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    private void getCategories() {

        //Creating a string request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, JsonCatURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            getCategoriesList(response.getJSONArray("aliments"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println("ggggghhhhhg");
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    private void getCategoriesList(JSONArray j) {
        System.out.println("titi");
        JSONObject jsn = new JSONObject();
        alimentDao.deleteAll();
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                if (Locale.getDefault().getLanguage().equals("fr"))
                    map.put(json.getString(KEY_NAMEF), json.getInt(KEY_ID));
                else
                    map.put(json.getString(KEY_NAME), json.getInt(KEY_ID));

                //categories.add(json.getString(KEY_NAME));


                // System.out.println(categories);

            } catch (JSONException e) {
                Timber.e(e.getMessage());
            }
        }
        System.out.println(map.values());
    }

    public void addAliment(View view) {
        //getAliments();
        if ((foodQty_edit_text.getText().toString().trim().equals(""))) {
            Toast.makeText(context, R.string.qantite, Toast.LENGTH_SHORT).show();
        } else {
            id = -1;
            JsonFoodURL = host + ":" + port + getString(R.string.urlAlim);
            food = new Aliment();
            name = food_edit_text.getText().toString();
            unite = foodQtytype_spinner.getSelectedItem().toString();
            q = String.valueOf(foodQty_edit_text.getText());

            System.out.println("nossair" + "***" + map.get(name));
            for (String index : map.keySet()) {
                if (name.equals(index)) {
                    id = map.get(index);
                }

            }
            System.out.println("***ID*" + id);
            Log.i("Id", String.valueOf(id));

            food.setName(name);
            food.setQuantiteA(q + " " + unite);
            if (id != -1) {
                JsonFoodURL += id;
                getSelectedItem(unite, name, q);
                System.out.println("DataBase****");


            } else {
                com.example.if_dose.db.Aliment aliment = new com.example.if_dose.db.Aliment();
                aliment.setName(food.getName());
                aliment.setGlucide(food.getGlucide());
                aliment.setQuantite(food.getQuantiteB());
                alimentDao.insert(aliment);

                System.out.println("DataBase***DONE*" + alimentDao.count());
                System.out.println("DataBase***DONE*");
                listAliment.add(food);
                listnamealim.add(food.getName() + "                                                                  " + food.getQuantiteA());
                System.out.println("**foods" + food.getGlucide() + "**");
                System.out.println("**foods" + listnamealim + "**");
                //adapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(listview);
                alimentsDuJour.append(name + " : " + q + " " + unite + ";");
                Toast.makeText(context, R.string.bienEnvoi, Toast.LENGTH_SHORT).show();


            }
            //alimentsAutoComp.setText("");
            foodQty_edit_text.setText("");

        }
    }

    public String getCurrentDate(String format) {
        //            return DateFormat.getDateTimeInstance().format(new Date())
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(c.getTime());
    }

    private void update() {

        RequestQueue queue = Volley.newRequestQueue(this);
        date = getCurrentDate("yyyy-MM-dd");

        String url = host + ":" + port + "/api/visit/" + pid + "/" + date;
        int id;


// Request a string response from the provided URL.
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("SUCCEEEEESSSS");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("FAILURE");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getSelectedItem(final String unite, final String name, final String q) {
        final RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsObjRequest = new JsonArrayRequest

                (Request.Method.GET, JsonFoodURL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            System.out.println("bien nossair");
                            JSONObject meel = response.getJSONObject(0);


                            addAlimentInstructions(meel);
                        } catch (JSONException e) {
                            Timber.i(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("bien nossair" + error.getMessage());
                        String jsonString = PreferenceManager.
                                getDefaultSharedPreferences(context).getString("foodJson", "");
                        try {
                            JSONObject jsonObject = new JSONObject(jsonString);
                            if (!jsonObject.getJSONArray(JSON_ARRAY_2).equals(null)) {
                                result = jsonObject.getJSONArray(JSON_ARRAY_2);

                                jsonApi = getJSONObjectByID(result);
                                addAlimentInstructions(jsonApi);
                            }
                        } catch (JSONException f) {
                            Timber.e(f.getMessage());
                        }


                    }
                });
        queue.add(jsObjRequest);
    }

    void addAlimentInstructions(JSONObject jsonApi) {
        try {
            food.setGlucide(jsonApi.getDouble("glucide"));
            if (!jsonApi.isNull("quantite")) {
                food.setQuantiteB(jsonApi.getDouble("quantite"));
                Timber.i("JSON quantite " + jsonApi.getDouble("quantite"));
            } else {
                food.setQuantiteB(1);
                Timber.i("JSON quantite Null " + food.getQuantiteB());
                if (!(unite.equals("قطعة") || unite.equals("طبق")
                        || unite.equals("كاس"))) {
                    Toast.makeText(context, R.string.unite,
                            Toast.LENGTH_LONG).show();
                    return;
                }
            }
            com.example.if_dose.db.Aliment aliment = new com.example.if_dose.db.Aliment();
            aliment.setName(food.getName());
            aliment.setQuantite(food.getQuantiteB());
            aliment.setGlucide(food.getGlucide());
            aliment.setCategory_id((long) id);
            alimentDao.insert(aliment);

            System.out.println("*INSERTED*" + alimentDao.count());
            listAliment.add(food);
            listnamealim.add(food.getName() + "                                                                  " + food.getQuantiteA());
            setListViewHeightBasedOnChildren(listview);
            alimentsDuJour.append(name + " : " + q + " " + unite + ";");
            System.out.println("**foods" + food.getGlucide() + "**");
            System.out.println("**foods" + listnamealim + "**");
            Timber.i("QuantiteA : " + food.getQuantiteA());
            Timber.i("QuantiteB : " + food.getQuantiteB());
            Toast.makeText(context, R.string.bienEnvoi, Toast.LENGTH_SHORT).show();
        } catch (JSONException g) {
            g.printStackTrace();
        }

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if (listItem != null) {
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    JSONObject getJSONObjectByID(JSONArray j) {

        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject jobj = j.getJSONObject(i);
                int id = jobj.getInt("id");
                if (id == map1.get(alimentsAutoComp.getText().toString())) {
                    System.out.println(alimentsAutoComp.getText().toString() + "+++++++++++++++++========++++++++++++");
                    return jobj;
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return null;
    }
}


