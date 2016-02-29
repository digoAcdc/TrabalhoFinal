package com.fiap.rodrigo.trabalhofinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SobreActivity extends AppCompatActivity {
private TextView tvSobre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sobre);
        tvSobre = (TextView) findViewById(R.id.tvsobre);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_direita);
        tvSobre.startAnimation(animation);
    }
}
