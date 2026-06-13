package com.example.myquizapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Oddiy "Hello World" ekran
        setContentView(new android.widget.TextView(this) {{
            setText("Ilova ishlamoqda!");
            setTextSize(30);
        }});
    }
}
