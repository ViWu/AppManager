package appcustomizer.appcustomizer.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.askerov.dynamicgrid.DynamicGridView;

import java.util.ArrayList;

import appcustomizer.appcustomizer.R;
import appcustomizer.appcustomizer.Utilities.Application;
import appcustomizer.appcustomizer.Utilities.GvAdapter;
import appcustomizer.appcustomizer.Utilities.LvAdapter;

public class AppInfo extends AppCompatActivity {


    private static ArrayList<String> appNames;
    public static ArrayList<Application> res;
    private DynamicGridView gridView;
    public static LvAdapter gridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(android.support.design.R.drawable.abc_ic_ab_back_material);
        //getSupportActionBar().setIcon(ic_launcher-web.png);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getBaseContext(),"Back pressed!",Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(AppInfo.this, MainActivity.class);
               // intent.putStringArrayListExtra("names", appNames);
                //startActivity(intent);
                finish();
            }
        });

        getSupportActionBar().setTitle("Installed Apps");
        gridView = (DynamicGridView) findViewById(R.id.dynamic_list);

        appNames = new ArrayList<>();
        res = new ArrayList<>();

        getPackages();

        gridViewAdapter = new LvAdapter(this, res, 1);
        gridView.setAdapter(gridViewAdapter);

        Intent i = getIntent();
        appNames = i.getStringArrayListExtra("names");




        Toast.makeText(getBaseContext(),"App count = " + appNames.size(),Toast.LENGTH_SHORT).show();




        setlvListener();
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

    private void setlvListener(){

        gridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(AppInfo.this)
                        .setTitle("App info")
                        .setMessage("App Name: " + res.get(position).appname + "\n" +
                                    "Package: " + res.get(position).pname+ "\n" +
                                    "Version: " + res.get(position).versionName+ "\n")
                        .setIcon(res.get(position).icon)
                        .setPositiveButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();

            }
        });
    }



}
