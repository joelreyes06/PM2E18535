package com.example.examenmovilip.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.examenmovilip.entidades.Contactos;

import java.util.ArrayList;

public class DbContactos extends DbHelper{

    Context context;

    public DbContactos(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertar(String pais, String nombres, String telefono, String nota) {
        long id = 0;

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("pais", pais);
            values.put("nombres", nombres);
            values.put("telefono", telefono);
            values.put("nota", nota);

            id = db.insert(TABLE_CONTACTOS, null, values);
        } catch (Exception ex) {
            ex.toString();
        }

        return id;
    }

    public ArrayList<Contactos> mostrarContactos() {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Contactos> listaContactos = new ArrayList<>();
        Contactos contactos = null;
        Cursor cursorContactos = null;

        cursorContactos = db.rawQuery("SELECT * FROM " + TABLE_CONTACTOS, null);

        if (cursorContactos.moveToFirst()) {
            do{
                contactos = new Contactos();
                contactos.setId(cursorContactos.getInt(0));
                contactos.setPais(cursorContactos.getString(1));
                contactos.setNombre(cursorContactos.getString(2));
                contactos.setTelefono(cursorContactos.getString(3));
                contactos.setNota(cursorContactos.getString(4));

                listaContactos.add(contactos);
            } while (cursorContactos.moveToNext());
        }

        cursorContactos.close();

        return listaContactos;
    }

    public Contactos VerContactos(int id) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Contactos contactos = null;
        Cursor cursorContactos = null;

        cursorContactos = db.rawQuery("SELECT * FROM " + TABLE_CONTACTOS + " WHERE id = " + id + " LIMIT 1", null);

        if (cursorContactos.moveToFirst()) {
            contactos = new Contactos();
            contactos.setId(cursorContactos.getInt(0));
            contactos.setPais(cursorContactos.getString(1));
            contactos.setNombre(cursorContactos.getString(2));
            contactos.setTelefono(cursorContactos.getString(3));
            contactos.setNota(cursorContactos.getString(4));
        }

        cursorContactos.close();

        return contactos;
    }

    public boolean editar(int id, String pais, String nombres, String telefono, String nota) {
        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.execSQL("UPDATE " + TABLE_CONTACTOS + " SET pais = '"+pais+"', nombres = '"+nombres+"', telefono = '"+telefono+"', nota = '"+nota+"' WHERE id = '"+id+"'");
            correcto = true;
        } catch (Exception ex) {
            correcto = false;
        } finally {
            db.close();
        }

        return correcto;
    }

    public boolean eliminar(int id) {
        boolean correcto = false;

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM " + TABLE_CONTACTOS + " WHERE id = '" + id + "'");
            correcto = true;
        } catch (Exception ex) {
            correcto = false;
        } finally {
            db.close();
        }

        return correcto;
    }

}
