package com.example.nine_men_morris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.nine_men_morris.model.InternalFile;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button startButton;
    private Button loadButton;
    private InternalFile file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        file = InternalFile.getInstance();
        startButton = findViewById(R.id.startButton);
        loadButton = findViewById(R.id.loadButton);
        startButton.setOnClickListener(this);
        loadButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.startButton){
            file.clearData();
        }
        Intent intent = new Intent(this, SplashScreen.class);
        startActivity(intent);
    }
}