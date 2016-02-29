package com.fiap.rodrigo.trabalhofinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
   private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.mn_cadastrar:
                // Toast.makeText(this, "Cadastrar", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, CadastrarActivity.class);
                i.putExtra("TIPO", "CADASTRAR");
                startActivity(i);
                finish();
                break;
            case R.id.mn_sobre:
                Toast.makeText(this, "sobre", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
