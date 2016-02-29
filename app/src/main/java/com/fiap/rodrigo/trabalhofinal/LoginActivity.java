package com.fiap.rodrigo.trabalhofinal;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.fiap.rodrigo.trabalhofinal.dao.LoginDAO;

public class LoginActivity extends AppCompatActivity {
    private Button btEntrar;
    private Button btCadastrar;
    private TextInputLayout tiUsuario;
    private TextInputLayout tisenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        btEntrar = (Button) findViewById(R.id.entrarLogin);
        btCadastrar = (Button) findViewById(R.id.btnCadastrarLogin);
        tiUsuario = (TextInputLayout) findViewById(R.id.tiUsuario);
        tisenha = (TextInputLayout) findViewById(R.id.tiSenha);

        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tiUsuario.getEditText().getText().toString().equals("") || tisenha.getEditText().getText().toString().equals(""))
                {
                    tisenha.setError("Usuario/Senha campos obrigatórios");
                    return;
                }

                LoginDAO dao = new LoginDAO(LoginActivity.this);
                boolean ret = dao.validaLogin(tiUsuario.getEditText().getText().toString(), tisenha.getEditText().getText().toString());

                if(ret)
                {

                    Intent i = new Intent(LoginActivity.this, OpcoesActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    tisenha.setError("Usuario ou senha invalida");
                }
            }
        });

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, CadastrarLoginActivity.class);
                startActivity(i);
            }
        });

    }
}
