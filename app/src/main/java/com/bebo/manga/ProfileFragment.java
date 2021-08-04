package com.bebo.manga;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {


    Button DownloadManagerButton;
    Button SettingButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        DownloadManagerButton = (Button)view.findViewById(R.id.download_manager);
        DownloadManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenDownloadManagerAct();
            }
        });

        SettingButton = (Button)view.findViewById(R.id.setting);
        SettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenSettingAct();
            }
        });

        return view;
    }

    void OpenDownloadManagerAct(){
        Intent intent = new Intent(getActivity(), DownloadManagerActivity.class);
        startActivity(intent);
    }

    void OpenSettingAct(){
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }


}
