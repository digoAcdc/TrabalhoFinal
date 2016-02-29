package com.fiap.rodrigo.trabalhofinal.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rodrigo on 06/02/2016.
 */
public class LoginDB extends SQLiteOpenHelper {

    public static final String USUARIO = "usuario";
    public static final String SENHA = "senha";
    public static final String ID = "id";


    public static final String TABELA_LOGIN = "Login";
    public static final String NOME_BANCO = "LoginBD.db";
    public static final int VERSAO = 1;

    public LoginDB(Context context)
    {
        super(context, NOME_BANCO, null, VERSAO);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder();
        sql.append(" Create Table ");
        sql.append(TABELA_LOGIN);
        sql.append("(");
        sql.append(ID);
        sql.append(" integer primary key autoincrement, ");
        sql.append(USUARIO);
        sql.append(" text, ");
        sql.append(SENHA);
        sql.append(" text ) ");
        db.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_LOGIN);
        onCreate(db);
    }
}
