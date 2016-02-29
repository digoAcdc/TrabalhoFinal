package com.fiap.rodrigo.trabalhofinal.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fiap.rodrigo.trabalhofinal.Model.Login;
import com.fiap.rodrigo.trabalhofinal.Model.Restaurante;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodrigo on 06/02/2016.
 */
public class RestauranteDAO {
    private RestauranteDB meuBD;
    private SQLiteDatabase db;

    public RestauranteDAO(Context context) {

        meuBD = new RestauranteDB(context);
    }

    public String inserirRestaurante(Restaurante r) {


        ContentValues valores = new ContentValues();
        valores.put(RestauranteDB.CAMINHO_IMAGEM, r.getCaminhoImagem());
        valores.put(RestauranteDB.CUSTO_MEDIO, r.getCustoMedio());
        valores.put(RestauranteDB.LATITUDE, r.getLatitude());
        valores.put(RestauranteDB.LONGITUDE, r.getLongitude());
        valores.put(RestauranteDB.NOME_RESTAURANTE, r.getNome());
        valores.put(RestauranteDB.TELEFONE, r.getTelefone());
        valores.put(RestauranteDB.OBSERVACAO, r.getObservacao());
        valores.put(RestauranteDB.TIPO, r.getTipo());

        db = meuBD.getWritableDatabase();

        long resultado = db.insert(meuBD.TABELA_RESTAURANTE, null, valores);

        db.close();

        if (resultado == -1) {
            return "Erro ao cadastrar usuário. ";
        } else {
            return "Restaurante cadastrado com sucesso!";
        }

    }

    public List<Restaurante> getRestaurante() {
        List<Restaurante> lst = new ArrayList<>();

        Cursor cursor;
        db = meuBD.getReadableDatabase();

        cursor = db.query(RestauranteDB.TABELA_RESTAURANTE, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            do {
                Restaurante r = new Restaurante();

                r.setId(cursor.getInt(cursor.getColumnIndex(RestauranteDB.ID)));
                r.setLatitude(cursor.getString(cursor.getColumnIndex(RestauranteDB.LATITUDE)));
                r.setCaminhoImagem(cursor.getString(cursor.getColumnIndex(RestauranteDB.CAMINHO_IMAGEM)));
                r.setLongitude(cursor.getString(cursor.getColumnIndex(RestauranteDB.LONGITUDE)));
                r.setNome(cursor.getString(cursor.getColumnIndex(RestauranteDB.NOME_RESTAURANTE)));
                r.setTelefone(cursor.getString(cursor.getColumnIndex(RestauranteDB.TELEFONE)));
                r.setCustoMedio(cursor.getString(cursor.getColumnIndex(RestauranteDB.CUSTO_MEDIO)));
                r.setObservacao(cursor.getString(cursor.getColumnIndex(RestauranteDB.OBSERVACAO)));
                r.setTipo(cursor.getString(cursor.getColumnIndex(RestauranteDB.TIPO)));

                lst.add(r);
            }while (cursor.moveToNext());
        }
        db.close();
        return lst;
    }
    public List<Restaurante> getRestaurantePorId(String id) {

//        cursor = db.query(MeuBD.TABELA_LIVRO, new String[]{ID, TITULO, AUTOR, EDITORA, ISBN, DESCRICAO}, ID + "=" + id, null, null, null, null);
        List<Restaurante> lst = new ArrayList<>();

        Cursor cursor;
        db = meuBD.getReadableDatabase();

        cursor = db.query(RestauranteDB.TABELA_RESTAURANTE, new String[]{RestauranteDB.ID, RestauranteDB.NOME_RESTAURANTE, RestauranteDB.LATITUDE, RestauranteDB.LONGITUDE, RestauranteDB.CUSTO_MEDIO, RestauranteDB.CAMINHO_IMAGEM, RestauranteDB.OBSERVACAO, RestauranteDB.TIPO}, RestauranteDB.ID + "=" + id, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                Restaurante r = new Restaurante();

                r.setId(cursor.getInt(cursor.getColumnIndex(RestauranteDB.ID)));
                r.setNome(cursor.getString(cursor.getColumnIndex(RestauranteDB.NOME_RESTAURANTE)));
                r.setLatitude(cursor.getString(cursor.getColumnIndex(RestauranteDB.LATITUDE)));
                r.setLongitude(cursor.getString(cursor.getColumnIndex(RestauranteDB.LONGITUDE)));
                r.setCaminhoImagem(cursor.getString(cursor.getColumnIndex(RestauranteDB.CAMINHO_IMAGEM)));
                r.setTipo(cursor.getString(cursor.getColumnIndex(RestauranteDB.TIPO)));
                r.setCustoMedio(cursor.getString(cursor.getColumnIndex(RestauranteDB.CUSTO_MEDIO)));
                r.setObservacao(cursor.getString(cursor.getColumnIndex(RestauranteDB.OBSERVACAO)));
                r.setTelefone(cursor.getString(cursor.getColumnIndex(RestauranteDB.TELEFONE)));

                lst.add(r);
            }
        }
        db.close();
        return lst;

    }

    public List<Restaurante> findRestaurante(String texto) {
        List<Restaurante> lst = new ArrayList<>();
        String parametro = null;
        if (!texto.isEmpty()) {
            parametro = texto;
        }

        db = meuBD.getReadableDatabase();
        try {
            String myQuery = RestauranteDB.TIPO + " like '%" + parametro + "%'" + " OR " + RestauranteDB.CUSTO_MEDIO + " like '%" + parametro + "%'" + " OR " + RestauranteDB.NOME_RESTAURANTE + " like '%" + parametro + "%'";
            Cursor cursor = db.query(RestauranteDB.TABELA_RESTAURANTE, null, myQuery, null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                do  {
                    Restaurante r = new Restaurante();

                    r.setId(cursor.getInt(cursor.getColumnIndex(RestauranteDB.ID)));
                    r.setLatitude(cursor.getString(cursor.getColumnIndex(RestauranteDB.LATITUDE)));
                    r.setCaminhoImagem(cursor.getString(cursor.getColumnIndex(RestauranteDB.CAMINHO_IMAGEM)));
                    r.setLongitude(cursor.getString(cursor.getColumnIndex(RestauranteDB.LONGITUDE)));
                    r.setNome(cursor.getString(cursor.getColumnIndex(RestauranteDB.NOME_RESTAURANTE)));
                    r.setTelefone(cursor.getString(cursor.getColumnIndex(RestauranteDB.TELEFONE)));
                    r.setCustoMedio(cursor.getString(cursor.getColumnIndex(RestauranteDB.CUSTO_MEDIO)));
                    r.setObservacao(cursor.getString(cursor.getColumnIndex(RestauranteDB.OBSERVACAO)));
                    r.setTipo(cursor.getString(cursor.getColumnIndex(RestauranteDB.TIPO)));

                    lst.add(r);
                }while (cursor.moveToNext());

            }
        } finally {
            db.close();
            return lst;
        }
    }

    public boolean ApagaRegistro(String id) {
        try {
            db = meuBD.getWritableDatabase();
            db.delete(meuBD.TABELA_RESTAURANTE, RestauranteDB.ID + "=" + id, null);
            db.close();

            return true;

        } catch (Exception ex) {
            return false;
        }

    }

    public boolean AtualizaRestaurante(Restaurante rest) {
        try {
            db = meuBD.getWritableDatabase();
            ContentValues args = new ContentValues();
            args.put(RestauranteDB.NOME_RESTAURANTE, rest.getNome());
            args.put(RestauranteDB.TELEFONE, rest.getTelefone());
            args.put(RestauranteDB.CUSTO_MEDIO, rest.getCustoMedio());
            args.put(RestauranteDB.OBSERVACAO, rest.getObservacao());
            args.put(RestauranteDB.TIPO, rest.getTipo());
            if (rest.getCaminhoImagem() != null)
                args.put(RestauranteDB.CAMINHO_IMAGEM, rest.getCaminhoImagem());

            db.update(RestauranteDB.TABELA_RESTAURANTE, args, RestauranteDB.ID + "=" + rest.getId(), null);
        } catch (Exception ex) {
            return false;
        }

        return true;
    }
}
