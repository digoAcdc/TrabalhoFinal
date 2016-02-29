package com.fiap.rodrigo.trabalhofinal;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.fiap.rodrigo.trabalhofinal.Model.Login;
import com.fiap.rodrigo.trabalhofinal.dao.LoginDAO;

public class CadastrarLoginActivity extends AppCompatActivity {
    private Button btCadastar;
    private TextInputLayout tiCadUsuario;
    private TextInputLayout tiCadsenha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cadastrar_login);

        btCadastar = (Button) findViewById(R.id.btCadLogin);
        tiCadUsuario = (TextInputLayout) findViewById(R.id.tiCadUsuario);
        tiCadsenha = (TextInputLayout) findViewById(R.id.tiCadSenha);

        btCadastar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDAO dao = new LoginDAO(CadastrarLoginActivity.this);

                Login l = new Login();
                l.setUsuario(tiCadUsuario.getEditText().getText().toString());
                l.setSenha(tiCadsenha.getEditText().getText().toString());

                String ret =  dao.inserirUsuario(l);
                if(ret == "Usuário cadastrado com sucesso!") {

                    Intent i = new Intent(CadastrarLoginActivity.this, OpcoesActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                    tiCadsenha.setError("Falha ao Cadastrar");
            }
        });
    }
}
