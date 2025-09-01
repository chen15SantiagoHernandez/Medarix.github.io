package com.example.medarix.listaAjuste;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.medarix.R;

public class AyudaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda_simple);

        TextView txtVersion = findViewById(R.id.txtVersion);

        String versionText = "v?";
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            long code = (android.os.Build.VERSION.SDK_INT >= 28) ? pi.getLongVersionCode() : pi.versionCode;
            versionText = "v" + pi.versionName + " (" + code + ")";
        } catch (Exception ignored) {}

        txtVersion.setText(versionText);

    }
}