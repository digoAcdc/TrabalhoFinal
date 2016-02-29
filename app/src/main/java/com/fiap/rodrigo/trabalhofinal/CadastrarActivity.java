package com.fiap.rodrigo.trabalhofinal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiap.rodrigo.trabalhofinal.Model.Login;
import com.fiap.rodrigo.trabalhofinal.Model.Restaurante;
import com.fiap.rodrigo.trabalhofinal.dao.LoginDAO;
import com.fiap.rodrigo.trabalhofinal.dao.RestauranteDAO;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import livroandroid.lib.utils.ImageResizeUtils;
import livroandroid.lib.utils.SDCardUtils;

public class CadastrarActivity extends AppCompatActivity implements com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;

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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cadastrar);

        imageView = (ImageView) findViewById(R.id.imagem);
        tvLatitude = (TextView) findViewById(R.id.tvValorLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvValorLongitude);
        btCadastrar = (Button) findViewById(R.id.btCadastrarRestaurante);
        tiRestaurante = (TextInputLayout) findViewById(R.id.tiRestaurante);
        tiTelefone = (TextInputLayout) findViewById(R.id.tiTelefone);
        tiCustoMedio = (TextInputLayout) findViewById(R.id.tiCustoMedio);
        tiObservacao = (TextInputLayout) findViewById(R.id.tiObservacao);
        spTipo = (Spinner) findViewById(R.id.spTipo);

        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        ImageButton b = (ImageButton) findViewById(R.id.btAbrirCamera);


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
                RestauranteDAO dao = new RestauranteDAO(CadastrarActivity.this);

                if (tvLatitude.getText().toString() == "0" || tvLongitude.getText().toString() == "0") {
                    Toast.makeText(CadastrarActivity.this, "Falha ao cadastrar, não foi possivel pegar a localização", Toast.LENGTH_LONG).show();
                    return;
                }

                Restaurante r = new Restaurante();

                if (file != null && file.exists()) {
                    r.setCaminhoImagem(file.toString());
                } else {
                    r.setCaminhoImagem("sem imagem cadastrada");
                }
                r.setLatitude(tvLatitude.getText().toString());
                r.setLongitude(tvLongitude.getText().toString());
                r.setNome(tiRestaurante.getEditText().getText().toString());
                r.setTelefone(tiTelefone.getEditText().getText().toString());
                r.setCustoMedio(tiCustoMedio.getEditText().getText().toString());
                r.setObservacao(tiObservacao.getEditText().getText().toString());
                r.setTipo(String.valueOf(spTipo.getSelectedItem()));


                String ret = dao.inserirRestaurante(r);
                if (ret == "Restaurante cadastrado com sucesso!") {
                    Toast.makeText(CadastrarActivity.this, "Restaurante Cadastrado com Sucesso", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(CadastrarActivity.this, Lista_Restaurantes_Activity.class);
                    startActivity(i);
                    finish();


                } else
                    Toast.makeText(CadastrarActivity.this, "Falha ao Cadastradar", Toast.LENGTH_LONG).show();
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

    //GPS

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        mGoogleApiClient.disconnect();

    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        updateUI();
    }

    private void updateUI() {

        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            tvLatitude.setText(lat);
            tvLongitude.setText(lng);
        } else {
            //falha
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();

        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
