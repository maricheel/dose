
package com.example.if_dose;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.example.if_dose.Models.CatMsg;
import com.example.if_dose.Models.Rapport;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import me.leolin.shortcutbadger.ShortcutBadger;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class MessageListActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener , SlyCalendarDialog.Callback {
    private LinearLayout m_doctor;
    //private Button bsend;
    private JSONObject ratio;
    private DatabaseHelper db;
    private DatabaseRapports dbrapp;
    private CatMsg messag;
    private SharedPreferences spGlycemies;
    private Context context;
    protected static float r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16;
    protected static float rd, rp, rc, rdi, is, obj;
    protected static String c1,c2,c3,c4;
    protected static float gluco0, gluco1, gluco2, gluco3;
    protected static String fName = "", lName = "", alimentsPDej = "", alimentsDej = "",
            alimentsCol = "", alimentsDin = "", sexe="", tel="";
    private String[] a1, a2, a3, a4;
    private Context msg_context;
    private double new_rp;
    private double new_rd;
    private double new_rc;
    private double new_rdi;
    SharedPreferences sp;
    private static String host, port;

    private DatePickerDialog dpd;
    private String datemsg ="",addrsip="";
    private String pid;
    private AppCompatImageView retur;


    public MessageListActivity() { }
    class Thread3 implements Runnable {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    loadMessg();


                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent2 = new Intent(MessageListActivity.this, ReportActivity.class);
        startActivity(intent2);
    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar bar =findViewById(R.id.toolbar);
        bar.setLogo(R.drawable.shield);
        bar.setTitle(R.string.app_name);
        retur=findViewById(R.id.appCompatImageView);
        msg_context = this;
        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        fName = sp.getString("nom", "Nom");
        lName = sp.getString("pren", "Prenom");
        host = sp.getString("host_url", getString(R.string.host_adr));
        port = sp.getString("host_port", getString(R.string.host_port));
        pid=sp.getString("id","0");
        m_doctor = findViewById(R.id.message_doctor);
        ImageView send = (ImageView) findViewById(R.id.send);
        // bsend = findViewById(R.id.bsendreport);
        context = getApplicationContext();
        spGlycemies = context.getSharedPreferences("glycemies", Context.MODE_PRIVATE);

        db = new DatabaseHelper(this);
        dbrapp = new DatabaseRapports(this);
        SharedPreferences.Editor editt = sp.edit();
        editt.putInt("comp",0);
        editt.apply();
        ShortcutBadger.removeCount(getApplicationContext()); //for 1.1.4+


       /* for (int i=0;i<10;i++){
            long j =20200701+i;
            float k = (float) 0.55+i;
            Rapport rapport =new Rapport(j,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,  "c11111111111",  "c22222222222",
                    "c33333333",  "c4444444", " alim:ents ; PD:ej",  "alim:ents ; D:ej",
                    "alime:nts ; Co:l",  "alim:ent ; D:in",  "0"+(i+1)+"-07-2020");

           if(dbrapp.getCount(j)==0)
               dbrapp.addData(rapport);
            if(dbrapp.getCount(j)!=0) {
                Toast.makeText(context, "rapport ajoute", Toast.LENGTH_SHORT).show();
            }else Toast.makeText(context, "erreur", Toast.LENGTH_SHORT).show();


            }*/



        loadMessg();


        /*sacrollviw.fullScroll(sacrollviw.FOCUS_DOWN);
                sacrollviw.scrollTo(0,m_doctor.getBottom());

                 */
        retur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onBackPressed();}
        });

        send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                       /*CatMsg m =new CatMsg("teeestt",getCurrentDate("dd-MM-yyyy HH:mm"),"rapport");
                       addMessage(m);

                        */
              /*  final android.icu.util.Calendar now = android.icu.util.Calendar.getInstance();

                dpd = DatePickerDialog.newInstance(
                        (DatePickerDialog.OnDateSetListener) MessageListActivity.this,
                        now.get(android.icu.util.Calendar.YEAR),
                        now.get(android.icu.util.Calendar.MONTH),
                        now.get(android.icu.util.Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                dpd.setAccentColor(Color.parseColor("#6A6F70"));
                dpd.setStartTitle(getString(R.string.du));
                dpd.setEndTitle(getString(R.string.au));
                dpd.setThemeDark(true);

               */

                new SlyCalendarDialog()
                        .setLang(getString(R.string.lange))
                        .setSingle(false)
                        .setFirstMonday(false)
                        .setCallback(MessageListActivity.this)
                        .show(getSupportFragmentManager(), "TAG_SLYCALENDAR");







            }
        });



        String[] values =host.split("//");
        if (values.length>1)
            addrsip = values[1];
        System.out.println("--------------------------"+addrsip+"----------------------------");
        getRatiofrompusher();

    }

    //private
    public void loadMessg() {
        DatabaseHelper db = new DatabaseHelper(this);
        m_doctor.removeAllViews();
        TextView head = new TextView(getApplicationContext());
        head.setMinHeight(130);
        m_doctor.addView(head);

        ArrayList<CatMsg> list =new  ArrayList<CatMsg>();
        list = db.getAllMsg();
        //System.out.println("==================================="+list.size()+"=======================================");
        for(CatMsg msg : list) {
            addMessage(msg);
        }
        TextView TV = new TextView(getApplicationContext());
        TV.setHeight(0);


        TV.setFocusableInTouchMode(true);
        TV.requestFocus();
        m_doctor.addView(TV);



    }

    private void addMessage(final CatMsg message) {
        LinearLayout item_msg = new LinearLayout(getApplicationContext());
        item_msg.setOrientation(LinearLayout.HORIZONTAL);
        TextView tv = new TextView(getApplicationContext());
        TextView time = new TextView(getApplicationContext());
        ImageView img = new ImageView(getApplicationContext());


        LinearLayout imag = new LinearLayout(getApplicationContext());
        item_msg.setOrientation(LinearLayout.VERTICAL);

        img.setImageResource(R.drawable.elips);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(140, 4, 4, 4);
        LinearLayout.LayoutParams prmrecu = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        prmrecu.setMargins(4, 4, 140, 4);


        tv.setText(message.getMsg());
        tv.setWidth(650);
        tv.setMinHeight(100);
        //tv.setHeight(150);
        tv.setElegantTextHeight(true);
        //  tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        tv.setTextSize(18);

        tv.setTextColor(Color.WHITE);
        time.setTextColor(Color.GRAY);
        time.setText(message.getDate());
        //time.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        time.setTextSize(11);
        if(message.getMode().equals("demande")){
            // if(message.getMsg().substring(0, message.getMsg().indexOf(" ")).equals("Veuillez")) {
            tv.setClickable(true);
            tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.aff, 0);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MessageListActivity.this);
                    builder.setTitle(R.string.envoiS);
                    builder.setMessage(message.getMsg());
                    builder.setIcon(R.drawable.messag);
                    builder.setPositiveButton(R.string.envoi, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            if (dbrapp.getCountall(message.getDate1(),message.getDate2()) != 0) {
                                ArrayList <Rapport> listrapp = new ArrayList <Rapport>() ;
                                listrapp = dbrapp.getAllRapport(message.getDate1(),message.getDate2());
                                int lastrappotr =0;
                                String per=listrapp.get(0).getDate()+"/"+listrapp.get(listrapp.size() - 1).getDate();
                                update(listrapp,per);
                                   /* datemsg="";
                                    for (int i=0; i<listrapp.size();i++){
                                        if(i==listrapp.size()-1)
                                            lastrappotr=1;
                                        update(listrapp.get(i),per,lastrappotr);

                                        try {
                                            TimeUnit.MILLISECONDS.sleep(120);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        System.out.println("#####  infooooor   ####"+datemsg);

                                    }
                                    System.out.println("#####  ouuut fooor  ####"+datemsg);
                                    CatMsg mess =new CatMsg(" Le rapport du "+datemsg+" est envoyé ",getCurrentDate("dd-MM-yyyy HH:mm"),"rapport");
                                    db.addData(mess);
                                    new Thread3().run();

                                    */
                            }else Toast.makeText(msg_context,R.string.pasdrapport, Toast.LENGTH_SHORT).show();

                        }
                    });
                    builder.setNegativeButton(R.string.annule, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) { }
                    });
                    builder.create().show();
                }
            });
            tv.setBackgroundResource(R.drawable.rounded_rectangle_blu);
            // tv.setGravity( Gravity.LEFT);
            tv.setPadding(18,10,28,10);
            //tv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            item_msg.addView(tv);
            m_doctor.addView(item_msg,prmrecu);
            m_doctor.addView(time);
            //msg web
        }else if(message.getMode().equals("mobile")){
            tv.setBackgroundResource(R.drawable.rounded_rectangle_blu);
            // tv.setGravity( Gravity.LEFT);
            tv.setPadding(18,10,28,10);
            // tv.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            item_msg.addView(tv);
            m_doctor.addView(item_msg,prmrecu);
            m_doctor.addView(time,prmrecu);
            //msg web
        }else if (message.getMode().equals("web")){
            tv.setBackgroundResource(R.drawable.rounded_rectangale_r);
            // tv.setGravity( Gravity.LEFT);
            tv.setPadding(18,10,28,10);
            imag.addView(img);
            imag.addView(tv,prmrecu);
            item_msg.addView(imag);
            m_doctor.addView(item_msg,prmrecu);
            m_doctor.addView(time,prmrecu);
            //messg d pation
        }else if (message.getMode().equals("rapport")){
            System.out.println("===================================   messg d pation  =======================================");
            tv.setBackgroundResource(R.drawable.rounded_rectangale_v);
            // tv.setGravity( Gravity.RIGHT);
            tv.setPadding(30,10,10,10);
            time.setPadding(34,8,18,8);
            //time.setGravity( Gravity.RIGHT);
            tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.aff, 0);
            item_msg.addView(tv,params);
            m_doctor.addView(item_msg,params);
            m_doctor.addView(time,params);
        }

        //item_msg.addView(time,params);
        //m_doctor.addView(item_msg);


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        int star =year*10000+(monthOfYear+1)*100+dayOfMonth;
        int end =yearEnd*10000+(monthOfYearEnd+1)*100+dayOfMonthEnd;

        if(star>end){
            star =yearEnd*10000+(monthOfYearEnd+1)*100+dayOfMonthEnd;
            end=year*10000+(monthOfYear+1)*100+dayOfMonth;
        }
        System.out.println(star+">>>>>>>>>>>|"+dbrapp.getCountall(star,end)+"|==========|"+dbrapp.getCountall(end,star)+"nnnn"+monthOfYear+monthOfYearEnd+"|>>>>>>>>>>>"+end);

        if (dbrapp.getCountall(star,end) != 0) {
            ArrayList <Rapport> listrapp = new ArrayList <Rapport>() ;
            listrapp = dbrapp.getAllRapport(star,end);
            int lastrappotr =0;
            String per=listrapp.get(0).getDate()+"/"+listrapp.get(listrapp.size() - 1).getDate();

            update(listrapp,per);
           /* datemsg ="";
            for (int i=0; i<listrapp.size();i++){
                if(i==listrapp.size()-1)
                    lastrappotr=1;
                update(listrapp.get(i),per,lastrappotr);
                try {
                    TimeUnit.MILLISECONDS.sleep(120);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            CatMsg mess =new CatMsg(" Le rapport du "+datemsg+" est envoyé ",getCurrentDate("dd-MM-yyyy HH:mm"),"rapport");
            db.addData(mess);
            new Thread3().run();

            */
        }else Toast.makeText(msg_context, R.string.pasdrapport, Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onCancelled() {

    }

    @Override
    public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {

        if (firstDate != null) {
            String d1 = new SimpleDateFormat("yyyyMMdd",new Locale(getString(R.string.lange))).format(firstDate.getTime());
            long star = Long.parseLong(d1);
            long end;
            if (secondDate == null) {
                end =star;
            } else {
                String d2 = new SimpleDateFormat("yyyyMMdd",new Locale(getString(R.string.lange))).format(secondDate.getTime());
                end = Long.parseLong(d2);
            }



            if (dbrapp.getCountall(star,end) != 0) {
                ArrayList <Rapport> listrapp = new ArrayList <Rapport>() ;
                listrapp = dbrapp.getAllRapport(star,end);
                int lastrappotr =0;
                String per=listrapp.get(0).getDate()+"/"+listrapp.get(listrapp.size() - 1).getDate();

                update(listrapp,per);
           /* datemsg ="";
            for (int i=0; i<listrapp.size();i++){
                if(i==listrapp.size()-1)
                    lastrappotr=1;
                update(listrapp.get(i),per,lastrappotr);
                try {
                    TimeUnit.MILLISECONDS.sleep(120);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            CatMsg mess =new CatMsg(" Le rapport du "+datemsg+" est envoyé ",getCurrentDate("dd-MM-yyyy HH:mm"),"rapport");
            db.addData(mess);
            new Thread3().run();

            */
            }else Toast.makeText(msg_context, R.string.pasdrapport, Toast.LENGTH_SHORT).show();
        }else Toast.makeText(this,"svp select date", Toast.LENGTH_SHORT).show();

    }




    private void getRatiofrompusher() {
        PusherOptions options = new PusherOptions()
                .setUseTLS(false)
                .setCluster("eu")
                .setWsPort(6001)
                // .setHost("192.168.1.134");
                .setHost(addrsip);





        Pusher pusher = new Pusher("882060899073b1574e7e", options);


        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("State changed from " + change.getPreviousState() +
                        " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println("There was a problem connecting! " +
                        "\ncode: " + code +
                        "\nmessage: " + message +
                        "\nException: " + e
                );
            }
        }, ConnectionState.ALL);

        Channel channel = pusher.subscribe("ratio."+pid);


        channel.bind("App\\Events\\sendRatio", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                System.out.println("Received event with data:  " + event.toString());
                new Thread3().run();
            }
        });
        Channel channel2 = pusher.subscribe("demande."+pid);
        channel2.bind("App\\Events\\Demande", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                System.out.println("Received event with data:  " + event.toString());
                new Thread3().run();
            }
        });

    }

    public String getCurrentDate(String dat) {
        //            return DateFormat.getDateTimeInstance().format(new Date()) Utils.formatDayTimeHtml(ts2)
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(dat);
        return df.format(c.getTime());}


    private void update(final ArrayList <Rapport> r, String per) {
        datemsg="";
        int last =0;
        for (int i=0; i<r.size();i++){
            if(i==r.size()-1)
                last =1;
            RequestQueue queue = Volley.newRequestQueue(this);
            a1 = r.get(i).getAlimentsPDej().split(";");
            a2 = r.get(i).getAlimentsDej().split(";");
            a3 = r.get(i).getAlimentsCol().split(";");
            a4 =r.get(i).getAlimentsDin().split(";");




            System.out.println("==========="+a1[0]+a2[0]+a3[0]+a4[0]+"==========  "+a1.length+a2.length+a3.length);
            StringBuilder PDj = new StringBuilder("");
            if(!a1[0].equals("-")&&!a1[0].equals("")){
                for(int j=0 ;j<a1.length;j++){
                    String [] t=a1[j].split(":");
                    PDj.append("&alimentsPetitDejeuner["+j+"][aliment]="+t[0]+"&alimentsPetitDejeuner["+j+"][qte]="+t[1]);
                }}else PDj.append("&alimentsPetitDejeuner[0][aliment]=-&alimentsPetitDejeuner[0][qte]=-");

            StringBuilder Dj = new StringBuilder("");
            if(!a2[0].equals("-")&&!a2[0].equals("")){
                for(int j=0 ;j<a2.length;j++){
                    String [] t=a2[j].split(":");
                    Dj.append("&alimentsDejeuner["+j+"][aliment]="+t[0]+"&alimentsDejeuner["+j+"][qte]="+t[1]);
                }}else Dj.append("&alimentsDejeuner[0][aliment]=-&alimentsDejeuner[0][qte]=-");
            StringBuilder Col = new StringBuilder("");
            if(!a3[0].equals("-")&&!a3[0].equals("")){
                for(int j=0 ;j<a3.length;j++){
                    String [] t=a3[j].split(":");
                    Col.append("&alimentsCollation["+j+"][aliment]="+t[0]+"&alimentsCollation["+j+"][qte]="+t[1]);
                }}else Col.append("&alimentsCollation[0][aliment]=-&alimentsCollation[0][qte]=-");
            StringBuilder Di = new StringBuilder("");
            if(!a4[0].equals("-")&&!a4[0].equals("")){
                for(int j=0 ;j<a4.length;j++){
                    String [] t=a4[j].split(":");
                    Di.append("&alimentsDinner["+j+"][aliment]="+t[0]+"&alimentsDinner["+j+"][qte]="+t[1]);
                }}else Di.append("&alimentsDinner[0][aliment]=-&alimentsDinner[0][qte]=-");


            String u =host+":"+port+"/api/store?dossier_id="+pid;
            String q ="&glucidePetitDejeuner="+r.get(i).getGluco0()+"&glucideDejeuner="+r.get(i).getGluco1()+"&glucideCollation="+r.get(i).getGluco2()+
                    "&glucideDinner="+r.get(i).getGluco3()+"&glycemieAvPetitDejeuner="+r.get(i).getR1()+"&glycemieAvDejeuner="+r.get(i).getR5()+"&glycemieAvCollation="+r.get(i).getR9()+"&glycemieAvDinner="+r.get(i).getR13()+
                    "&uniteAInjecterPetitDejeuner="+r.get(i).getR2()+"&uniteAInjecterDejeuner="+r.get(i).getR6()+"&uniteAInjecterCollation="+r.get(i).getR10()+"&uniteAInjecterDinner="+r.get(i).getR14()+
                    "&glycemieApres4hPetitDejeuner="+r.get(i).getR3()+"&glycemieApres4hDejeuner="+r.get(i).getR7()+"&glycemieApres4hCollation="+r.get(i).getR11()+"&glycemieApres4hDinner="+r.get(i).getR15()+
                    "&doseConfirmePetitDejeuner="+r.get(i).getR4()+"&doseConfirmeDejeuner="+r.get(i).getR8()+"&doseConfirmeCollation="+r.get(i).getR12()+"&doseConfirmeDinner="+r.get(i).getR16()+"&ratioPD=" +r.get(i).getRp()+
                    "&ratioDe="+r.get(i).getRd()+"&ratioC="+r.get(i).getRc()+"&ratioDi="+r.get(i).getRdi()+"&indiceSensibilite="+r.get(i).getIs()+"&objectif=1"+r.get(i).getObj()+PDj+Dj+Col+Di+"&fbclid=IwAR0eACqpWgShyM_Y1SuRO"+
                    "UrV4F3wRenOFQoKjR2bxvNzbpZ2E-m9emKhmSc&patientName="+lName+"&p%C3%A9riode="+per+"&date="+r.get(i).getDate()+"&islast="+last;
            String url =u+ q.replaceAll(" ","%20");



// Request a string response from the provided URL.
            final int finalI = i;
            final int finalLast = last;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            System.out.println("SUCCEEEEESSSS");
                            // datemsg= datemsg +r.get(finalI).getDate()+" ; ";
                            Councat(r.get(finalI).getDate(), finalLast);

                       /* CatMsg mess =new CatMsg(" Le rapport du "+r.getDate()+" est envoyé ",getCurrentDate("dd-MM-yyyy HH:mm"),"rapport");
                        db.addData(mess);

                        */


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("FAILURE");
                    if(finalLast==1) Councat("", finalLast);
                    Toast.makeText(msg_context, getString(R.string.RappNoEnv)+r.get(finalI).getDate(), Toast.LENGTH_SHORT).show();
                }
            });

// Add the request to the RequestQueue.
            queue.add(stringRequest);


            try {
                TimeUnit.MILLISECONDS.sleep(180);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        /*try {
            TimeUnit.MILLISECONDS.sleep(180*r.size());
            System.out.println("###uHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH######"+datemsg);
            if (!datemsg.equals("")){
                System.out.println("#####  ouuut fooor  ####"+datemsg);
                CatMsg mess =new CatMsg(" Le rapport du "+datemsg+" est envoyé ",getCurrentDate("dd-MM-yyyy HH:mm"),"rapport");
                db.addData(mess);
                new Thread3().run();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

         */


    }

    public void  Councat(String s,int last){
        datemsg= datemsg + s +"  ";
        if (datemsg.length()>2 && last==1){
            int d=0;
            CatMsg mess =new CatMsg(getString(R.string.rapportdu)+datemsg+getString(R.string.estenv),getCurrentDate("dd-MM-yyyy HH:mm"),"rapport",d);
            db.addData(mess);
            System.out.println("2020/44/66".length()+"§§§§§§§§§333333333333333333333333333333333333");
            System.out.println(datemsg.length());
            new Thread3().run();
        }

    }



}
////str.contains(" ") ? str.split(" ")[0] : str:

/*
    String date = "13-08-2016";
    String[] values = date.split("-");
    int day = Integer.parseInt(values[0]);
    int month = Integer.parseInt(values[1]);
    int year = Integer.parseInt(values[2]);

    var d = new Date("dd-MM-yyyy")
d.getDay();
d.getMonth();
d.getYear();
Similarly in Java,

DateFormat df = new SimpleDateFormat("mm/dd/yyyy");
Date startDate = df.parse(startDate);
 */









// String addrs="";
//        String[] values =host.split("//");
//        System.out.println(values.length);
//        if (values.length>2)
//            addrs=values[1];

/*
  JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {


                                JSONObject arr = response.getJSONObject("status");
                                System.out.println("SUCCEEEEESSSS");
                                // datemsg= datemsg +r.get(finalI).getDate()+" ; ";
                                Councat(r.get(finalI).getDate(), finalLast);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            System.out.println("FAILURE");
                            if(finalLast==1) Councat("", finalLast);
                            Toast.makeText(msg_context, getString(R.string.RappNoEnv)+r.get(finalI).getDate(), Toast.LENGTH_SHORT).show();


                        }
                    });
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

 */