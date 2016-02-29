package com.fiap.rodrigo.trabalhofinal.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fiap.rodrigo.trabalhofinal.Model.Login;

/**
 * Created by rodrigo on 06/02/2016.
 */
public class LoginDAO {

    private LoginDB meuBD;
    private SQLiteDatabase db;

    public LoginDAO(Context context) {

        meuBD = new LoginDB(context);
    }

    public String inserirUsuario(Login l){

        ContentValues valores = new ContentValues();
        valores.put(LoginDB.USUARIO, l.getUsuario());
        valores.put(LoginDB.SENHA, l.getSenha());

        db = meuBD.getWritableDatabase();

        long resultado = db.insert(meuBD.TABELA_LOGIN, null, valores);

        db.close();

        if(resultado == -1){
            return "Erro ao cadastrar usuário. ";
        }else{
            return "Usuário cadastrado com sucesso!";
        }

    }

    public boolean validaLogin(String usu, String senha) {
        String senhaBD = "";
        boolean retorno = false;

        Cursor cursor;
        db = meuBD.getReadableDatabase();

        String campo = LoginDB.USUARIO;
        cursor = db.query(LoginDB.TABELA_LOGIN, new String[]{LoginDB.USUARIO, LoginDB.SENHA}, campo + "=?", new String[]{usu}, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            String id = cursor.getString(cursor.getColumnIndex(LoginDB.USUARIO));
            String login = cursor.getString(cursor.getColumnIndex(LoginDB.SENHA));

            if(id.equals(usu) && login.equals(senha))
                retorno = true;
            else
                retorno = false;


        }
        db.close();
        return retorno;
    }
}
