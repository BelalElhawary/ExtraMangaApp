package com.bebo.manga;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.bebo.manga.Common.Common;
import com.bebo.manga.model.ProfileSetting;
import com.bebo.manga.service.SettingData;

public class SettingsActivity extends AppCompatActivity {
    SeekBar downloadQualityBar;
    Button saveButton;
    SwitchCompat autoRefresh;

    private int value;
    private boolean autoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Common.profileSetting = SettingData.Load(getBaseContext());

        downloadQualityBar = (SeekBar)findViewById(R.id.slider_quality);
        autoRefresh = (SwitchCompat)findViewById(R.id.switch_autoRefresh);
        saveButton = (Button)findViewById(R.id.save_setting);
        if(Common.profileSetting != null){
            downloadQualityBar.setProgress(Common.profileSetting.ImageCompress);
            autoRefresh.setChecked(Common.profileSetting.autoRefresh);
        }

        downloadQualityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        autoRefresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                autoRef = isChecked;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingData.Save(new ProfileSetting(value, autoRef), getBaseContext());
            }
        });
    }
}