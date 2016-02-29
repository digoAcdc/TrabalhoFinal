package com.fiap.rodrigo.trabalhofinal.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rodrigo on 06/02/2016.
 */
public class RestauranteDB extends SQLiteOpenHelper {

    public static final String ID = "id";
    public static final String NOME_RESTAURANTE = "nome";
    public static final String CAMINHO_IMAGEM = "caminho";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String CUSTO_MEDIO = "custo";
    public static final String TELEFONE = "telefone";
    public static final String OBSERVACAO = "observaco";
    public static final String TIPO = "tipo";



    public static final String TABELA_RESTAURANTE = "Restaurante";
    public static final String NOME_BANCO_RESTAURANTE = "RestauranteBD.db";
    public static final int VERSAO = 1;

    public RestauranteDB(Context context)
    {
        super(context, NOME_BANCO_RESTAURANTE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append(" Create Table ");
        sql.append(TABELA_RESTAURANTE);
        sql.append("(");
        sql.append(ID);
        sql.append(" integer primary key autoincrement, ");
        sql.append(NOME_RESTAURANTE);
        sql.append(" text, ");
        sql.append(CAMINHO_IMAGEM);
        sql.append(" text, ");
        sql.append(CUSTO_MEDIO);
        sql.append(" text, ");
        sql.append(TELEFONE);
        sql.append(" text, ");
        sql.append(OBSERVACAO);
        sql.append(" text, ");
        sql.append(TIPO);
        sql.append(" text, ");
        sql.append(LONGITUDE);
        sql.append(" text, ");
        sql.append(LATITUDE);
        sql.append(" text ) ");
        db.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_RESTAURANTE);
        onCreate(db);
    }
}
