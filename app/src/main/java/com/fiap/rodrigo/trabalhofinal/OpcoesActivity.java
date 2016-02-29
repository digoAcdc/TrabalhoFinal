package com.fiap.rodrigo.trabalhofinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class OpcoesActivity extends AppCompatActivity {
    private Button btListaRestaurantes;
    private Button btIncluirRestaurante;
    private Button btMapa;
    private Button btSobre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcoes);

        btListaRestaurantes = (Button) findViewById(R.id.btListaRestaurantes);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_esquerda);
        btListaRestaurantes.startAnimation(animation);

        btIncluirRestaurante = (Button) findViewById(R.id.btIncluirRestaurante);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_direita);
        btIncluirRestaurante.startAnimation(animation);

        btMapa = (Button) findViewById(R.id.btVisRestNoMapa);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_esquerda);
        btMapa.startAnimation(animation);

        btSobre = (Button) findViewById(R.id.btVisSobre);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_direita);
        btSobre.startAnimation(animation);

        btIncluirRestaurante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OpcoesActivity.this, CadastrarActivity.class);
                startActivity(i);
            }
        });

        btListaRestaurantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OpcoesActivity.this, Lista_Restaurantes_Activity.class);
                Bundle params = new Bundle();
                params.putString("Opcoes", "mapaOpcoes");
                i.putExtras(params);
                startActivity(i);
            }
        });

        btSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OpcoesActivity.this, SobreActivity.class);
                startActivity(i);
            }
        });

        btMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OpcoesActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });
    }
}
