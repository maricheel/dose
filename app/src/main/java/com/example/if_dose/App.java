package com.example.if_dose;



import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.bugfender.sdk.Bugfender;

import org.greenrobot.greendao.database.Database;

import com.example.if_dose.db.DaoMaster;
import com.example.if_dose.db.DaoSession;
import com.example.if_dose.utils.BugfenderTree;
import timber.log.Timber;

/**
 * Created by aaaa on 10/23/2017.
 */

public class App extends Application {
    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    public static final boolean ENCRYPTED = false;
    private DaoSession daoSession;

    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";



    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,
            ENCRYPTED ? "aliments-db-encrypted" : "aliments-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("IfDoSe") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        if (getString(R.string.app_mode).equals("dev")) {

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            boolean pref_debug = sp.getBoolean("pref_debug", false);

            String fName = sp.getString("nom", "Nom");
            String lName = sp.getString("pren", "Prenom");

            Bugfender.init(this, getString(R.string.bugfender_app_key), pref_debug);
//            Bugfender.enableLogcatLogging();
//            Bugfender.enableUIEventLogging(this);
            Bugfender.setDeviceString("Patient", fName + " " + lName);
            if (pref_debug)
                Timber.plant(new BugfenderTree());
            else
                Timber.plant(new Timber.DebugTree());
        } else
            Timber.plant(new Timber.DebugTree());

    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");
            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("This is Channel 2");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }












    public DaoSession getDaoSession() {
        return daoSession;
    }
}
