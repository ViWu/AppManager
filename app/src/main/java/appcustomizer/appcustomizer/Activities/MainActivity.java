package appcustomizer.appcustomizer.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import org.askerov.dynamicgrid.DynamicGridView;

import java.util.ArrayList;

import appcustomizer.appcustomizer.Utilities.Application;
import appcustomizer.appcustomizer.R;
import appcustomizer.appcustomizer.Utilities.GvAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private DynamicGridView gridView;
    public static GvAdapter gridViewAdapter;
    public static ArrayList<Application> res;
    public static ArrayList<String> appNames;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        gridView = (DynamicGridView) findViewById(R.id.dynamic_grid);
        res = new ArrayList<Application>();
        appNames = new ArrayList<>();

        getPackages();


        gridViewAdapter = new GvAdapter(this, res, 4);
        gridView.setAdapter(gridViewAdapter);
        setUpListeners();

        Button addButton = (Button) findViewById(R.id.switchButton);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AppInfo.class);
                intent.putStringArrayListExtra("names", appNames);
                startActivity(intent);
            }

        });

        final Button toggleButton = (Button) findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(gridView.isEditMode()) {
                    gridView.stopEditMode();
                }
                else{
                    gridView.startEditMode();
                }
            }

        });

        Button aboutButton = (Button) findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, About.class);
                startActivity(intent);

            }

        });

    }


    private void getPackages(){

        PackageManager manager = getPackageManager();
        ArrayList<PackageInfo> apps = (ArrayList<PackageInfo>) manager.getInstalledPackages(0);

        for(int i=0;i<apps.size();i++) {

            PackageInfo p = apps.get(i);
            if (((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) == false) {
                Application newInfo = new Application();
                newInfo.appname = p.applicationInfo.loadLabel(manager).toString();
                newInfo.pname = p.packageName;
                newInfo.versionName = p.versionName;
                newInfo.versionCode = p.versionCode;
                newInfo.icon = p.applicationInfo.loadIcon(manager);
                res.add(newInfo);
                appNames.add(newInfo.appname);
            }
        }
    }





    private void setUpListeners(){
        //Active dragging mode when long click at each Grid view item
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
                gridView.startEditMode();
                return true;
            }
        });


        //Handling click event of each Grid view item
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                Application app = (Application) parent.getItemAtPosition(position);
                PackageManager manager = getPackageManager();

                Intent i = manager.getLaunchIntentForPackage(app.pname);

                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);


                if (i != null) {
                    r.play();
                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    startActivity(i);
                }
                else{
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Error")
                            .setMessage( app.appname + " could not open. Please try a different " +
                                    "app")
                            .setPositiveButton(android.R.string.yes, null)

                            .setIcon(app.icon)
                            .show();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (gridView.isEditMode()) {
            gridView.stopEditMode();
        } else {
            super.onBackPressed();
        }
    }

}