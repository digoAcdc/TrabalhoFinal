package com.fiap.rodrigo.trabalhofinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiap.rodrigo.trabalhofinal.Model.Restaurante;
import com.fiap.rodrigo.trabalhofinal.dao.RestauranteDAO;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import livroandroid.lib.utils.ImageResizeUtils;
import livroandroid.lib.utils.SDCardUtils;

public class AlterarActivity extends AppCompatActivity {
    private static String id;
    private static int idOficial;

    private TextView tvLatitude;
    private TextView tvLongitude;
    private TextInputLayout tiRestaurante;
    private TextInputLayout tiTelefone;
    private TextInputLayout tiCustoMedio;
    private TextInputLayout tiObservacao;
    private Spinner spTipo;

    private Button btCadastrar;


    private ImageView imageView;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar);

        imageView = (ImageView) findViewById(R.id.imagem);
        tvLatitude = (TextView) findViewById(R.id.tvValorAltLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvValorAltLongitude);
        btCadastrar = (Button) findViewById(R.id.btAltCadastrarRestaurante);
        tiRestaurante = (TextInputLayout) findViewById(R.id.tiAltRestaurante);
        tiTelefone = (TextInputLayout) findViewById(R.id.tiAltTelefone);
        tiCustoMedio = (TextInputLayout) findViewById(R.id.tiAltCustoMedio);
        tiObservacao = (TextInputLayout) findViewById(R.id.tiAltObservacao);
        spTipo = (Spinner) findViewById(R.id.spAltTipo);

        Intent it = getIntent();
        Bundle params = it.getExtras();

        Restaurante r = new Restaurante();

        if (params != null) {
            id = params.getString("ID").toString();

            r = RetornaRestaurante(id);

            if (r != null) {
                if (r.getCaminhoImagem() != null) {
                    file = new File(r.getCaminhoImagem());
                    if (file.exists()) {
                        imageView.setImageURI(Uri.parse(r.getCaminhoImagem()));
                    }
                }
                tvLatitude.setText(r.getLatitude());
                tvLongitude.setText(r.getLongitude());
                tiRestaurante.getEditText().setText(r.getNome());
                tiTelefone.getEditText().setText(r.getTelefone());
                tiCustoMedio.getEditText().setText(r.getCustoMedio());
                tiObservacao.getEditText().setText(r.getObservacao());

                for (int i = 0; i < spTipo.getAdapter().getCount(); i++) {
                    if (spTipo.getAdapter().getItem(i).toString().contains(r.getTipo())) {
                        spTipo.setSelection(i);
                    }
                }

            }
        } else {

        }


        ImageButton b = (ImageButton) findViewById(R.id.btAbrirAltCamera);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                //get current date time with Date()
                Date date = new Date();
                //System.out.println(dateFormat.format(date));

                file = SDCardUtils.getPrivateFile(getBaseContext(), dateFormat.format(date) + ".jpg", Environment.DIRECTORY_PICTURES);
                // Chama a intent informando o arquivo para salvar a foto
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(i, 0);
            }
        });

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestauranteDAO dao = new RestauranteDAO(AlterarActivity.this);
                Restaurante r = new Restaurante();

                if (file != null && file.exists()) {
                    r.setCaminhoImagem(file.toString());
                } else {

                }
                r.setLatitude(tvLatitude.getText().toString());
                r.setLongitude(tvLongitude.getText().toString());
                r.setNome(tiRestaurante.getEditText().getText().toString());
                r.setTelefone(tiTelefone.getEditText().getText().toString());
                r.setCustoMedio(tiCustoMedio.getEditText().getText().toString());
                r.setObservacao(tiObservacao.getEditText().getText().toString());
                r.setTipo(String.valueOf(spTipo.getSelectedItem()));
                r.setId(idOficial);


                Boolean ret = dao.AtualizaRestaurante(r);
                if (ret == true) {
                    Toast.makeText(AlterarActivity.this, "Restaurante Alterado com Sucesso", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(AlterarActivity.this, Lista_Restaurantes_Activity.class);
                    startActivity(i);
                    finish();


                } else
                    Toast.makeText(AlterarActivity.this, "Falha ao alterar restaurante", Toast.LENGTH_LONG).show();

            }
        });

        if (savedInstanceState != null) {
            // Se girou a tela recupera o estado
            file = (File) savedInstanceState.getSerializable("file");
            showImage(file);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Salvar o estado caso gire a tela
        outState.putSerializable("file", file);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && file != null) {
            showImage(file);
        }
    }

    private void showImage(File file) {
        if (file != null && file.exists()) {
            Log.d("foto", file.getAbsolutePath());

            int w = imageView.getWidth();
            int h = imageView.getHeight();
            Bitmap bitmap = ImageResizeUtils.getResizedImage(Uri.fromFile(file), w, h, false);
            // Toast.makeText(this, "w/h:" + imgView.getWidth() + "/" + imgView.getHeight() + " > " + "w/h:" + bitmap.getWidth() + "/" + bitmap.getHeight(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "file:" + file, Toast.LENGTH_LONG).show();

            imageView.setImageBitmap(bitmap);
        }
    }

    public Restaurante RetornaRestaurante(String index) {
        RestauranteDAO dao = new RestauranteDAO(this);
        List<Restaurante> lista = new ArrayList<>();
        lista = dao.getRestaurante();
        Restaurante rest = new Restaurante();
        rest = lista.get(Integer.parseInt(index));
        idOficial = rest.getId();
        return rest;
    }

}
