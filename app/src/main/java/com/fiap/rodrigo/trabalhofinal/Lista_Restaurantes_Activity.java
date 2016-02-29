package com.fiap.rodrigo.trabalhofinal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.fiap.rodrigo.trabalhofinal.Model.Restaurante;
import com.fiap.rodrigo.trabalhofinal.adapter.RestauranteAdapter;
import com.fiap.rodrigo.trabalhofinal.dao.RestauranteDAO;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Lista_Restaurantes_Activity extends AppCompatActivity {

    Toolbar toolbar;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista__restaurantes_);

        recyclerView = (RecyclerView) findViewById(R.id.rvRestaurantes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        String jaBaixau = "false";

        SharedPreferences prefs = getSharedPreferences("baixar", MODE_PRIVATE);
        jaBaixau = prefs.getString("jaBaixo", "false");
        if (jaBaixau == "false") {
            jaBaixau = prefs.getString("baixar", "false");

            SharedPreferences.Editor editor = getSharedPreferences("baixar", MODE_PRIVATE).edit();
            editor.putString("jaBaixo", "true");
            editor.commit();

            BuscarCoordenadas buscarCoordenadas = new BuscarCoordenadas();

            buscarCoordenadas.execute("http://heiderlopes.com.br/restaurantes/restaurantes.json");


        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RestauranteDAO dao = new RestauranteDAO(this);

        List<Restaurante> lst = new ArrayList<>();

        lst = dao.getRestaurante();
        CarregaLista(this,lst);

       // recyclerView.setAdapter(new RestauranteAdapter(this, lst, onClickRestaurante()));

    }

    public void CarregaLista(Context context,  List<Restaurante> lst)
    {

        recyclerView.setAdapter(new RestauranteAdapter(this, lst, onClickRestaurante()));
    }

    private RestauranteAdapter.RestauranteOnClickListener onClickRestaurante() {
        return new RestauranteAdapter.RestauranteOnClickListener() {

            @Override
            public void OnClickRestaurante(View view, int index) {
                showFilterPopup(view, index);
            }
        };
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_serach);
        android.widget.SearchView searchView = (android.widget.SearchView) item.getActionView();

        searchView.setOnQueryTextListener(onSearch());
        return true;
    }
    private android.widget.SearchView.OnQueryTextListener onSearch(){
        return new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

               /* String email = recuperaEmail();*/

                PesquisaAvancada(newText);

                return false;
            }
        };
    }

    public void PesquisaAvancada(String texto){

        List<Restaurante> lst = new ArrayList<>();
        RestauranteDAO dao = new RestauranteDAO(this);
        lst = dao.findRestaurante(texto);
        if(lst.size() > 0)
        {
            CarregaLista(this, lst);
        }
        else
            CarregaLista(this, dao.getRestaurante());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.mn_cadastrar:
                //Toast.makeText(this, "Cadastrar", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, CadastrarActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.mn_sobre:
                // Toast.makeText(this, "sobre", Toast.LENGTH_LONG).show();
                Intent inte = new Intent(this, SobreActivity.class);
                startActivity(inte);
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }


    private void showFilterPopup(View v, long id) {

        PopupMenu popup = new PopupMenu(this, v);
        final String idMenu = Long.toString(id);
        // Inflate the menu from xml
        popup.getMenuInflater().inflate(R.menu.menu_lista, popup.getMenu());
        // Setup menu item selection
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mn_editar:
                        Toast.makeText(Lista_Restaurantes_Activity.this, "Editar! " + idMenu, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Lista_Restaurantes_Activity.this, AlterarActivity.class);
                        int idEditar = RetornaIdGrid(idMenu);
                        Bundle params = new Bundle();
                        params.putString("TIPO", "EDITAR");
                        params.putString("ID", String.valueOf(idMenu));
                        i.putExtras(params);
                        startActivity(i);
                        //finish();

                        return true;
                    case R.id.mn_excluir:

                        DialogInterface.OnClickListener dialiog = new DialogInterface.OnClickListener(){
                            RestauranteDAO dao = new RestauranteDAO(Lista_Restaurantes_Activity.this);
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        int idApagar = RetornaIdGrid(idMenu);

                                        if (dao.ApagaRegistro(String.valueOf(idApagar))) {
                                            Toast.makeText(Lista_Restaurantes_Activity.this, "Excluido com sucesso! ", Toast.LENGTH_SHORT).show();
                                            Intent in = new Intent(Lista_Restaurantes_Activity.this, Lista_Restaurantes_Activity.class);
                                            startActivity(in);
                                            return ;
                                        }
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        return;
                                     // break;

                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(Lista_Restaurantes_Activity.this);
                        builder.setMessage("Você realmente deseja excluir este Restaurante?");
                        builder.setPositiveButton("Sim", dialiog);
                        builder.setNegativeButton("Não", dialiog);
                        builder.show();

                        Toast.makeText(Lista_Restaurantes_Activity.this, "Falha ao Excluido! ", Toast.LENGTH_SHORT).show();
                        return false;

                    case R.id.mn_ver_mapa:
                        Intent inte = new Intent(Lista_Restaurantes_Activity.this, MapsActivity.class);
                        Bundle param = new Bundle();
                        param.putString("mapaUnico", String.valueOf(idMenu));
                        inte.putExtras(param);
                        startActivity(inte);
                    default:
                        return false;
                }
            }
        });
        // Handle dismissal with: popup.setOnDismissListener(...);
        // Show the menu
        popup.show();
    }

    public int RetornaIdGrid(String index) {
        RestauranteDAO dao = new RestauranteDAO(Lista_Restaurantes_Activity.this);
        List<Restaurante> lista = new ArrayList<>();
        lista = dao.getRestaurante();
        Restaurante r = new Restaurante();
        r = lista.get(Integer.parseInt(index));

        return r.getId();
    }

    private class BuscarCoordenadas extends AsyncTask<String, Void, List<Restaurante>> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //progressDialog = ProgressDialog.show(MapsActivity.this, "Carregando...", "Buscando os dados");
            //super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Restaurante> restaurantes) {
            //super.onPostExecute(s);

            if (restaurantes.size() > 0) {

                //  colocarNoMapa(estacoes);
            }
            // progressDialog.dismiss();
        }

        @Override
        protected List<Restaurante> doInBackground(String... params) {
            List<Restaurante> restaurantes = new ArrayList<>();
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                RestauranteDAO dao = new RestauranteDAO(Lista_Restaurantes_Activity.this);

                if (connection.getResponseCode() == 200) {
                    BufferedReader stream = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String linha = "";
                    StringBuilder retorno = new StringBuilder();

                    while ((linha = stream.readLine()) != null) {
                        retorno.append(linha);
                    }

                    connection.disconnect();

                    JSONArray array = new JSONArray(retorno.toString());

                    for (int i = 0; i < array.length(); i++) {
                        Restaurante r = new Restaurante();

                        r.setNome(array.getJSONObject(i).getString("NOMERESTAURANTE"));
                        r.setTelefone(array.getJSONObject(i).getString("TELEFONE"));
                        r.setTipo(array.getJSONObject(i).getString("TIPO"));
                        r.setCustoMedio(array.getJSONObject(i).getString("CustoMedio"));
                        r.setObservacao(array.getJSONObject(i).getString("OBSERVACAO"));

                        String coordenadas = array.getJSONObject(i).getString("LOCALIZACAO");
                        String c[] = new String[2];
                        c = coordenadas.split(",");
                        r.setLatitude(c[0]);
                        r.setLongitude(c[1]);


                        restaurantes.add(r);

                        dao.inserirRestaurante(r);

                    }


                    return restaurantes;
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return restaurantes;
        }
    }

}
