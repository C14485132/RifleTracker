package com.example.phili.rifletracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Settings extends AppCompatActivity {

    Switch switchDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        switchDarkMode = (Switch)findViewById(R.id.switchDarkMode);

        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setTheme(android.R.style.ThemeOverlay_Material_Dark);
                    setContentView(R.layout.activity_settings);
                } else {
                    setTheme(android.R.style.Theme_Material);
                    setContentView(R.layout.activity_settings);
                }
            }
        });
    }
}
