package com.example.examenmovilip;

import static android.Manifest.permission.CALL_PHONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.example.examenmovilip.adaptadores.ListaContactosAdapter;
import com.example.examenmovilip.db.DbContactos;
import com.example.examenmovilip.entidades.Contactos;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;

public class Contactos_Salvados extends AppCompatActivity implements SearchView.OnQueryTextListener{

    Button btnRegresar;
    SearchView searchView;
    RecyclerView listaContactos;
    ArrayList<Contactos> listaArrayContactos;

    CountryCodePicker ccp;

    ListaContactosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos_salvados);

        btnRegresar = (Button) findViewById(R.id.btnRegresar);
        searchView = (SearchView) findViewById(R.id.searchView);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PantallaInicial.class);
                startActivity(intent);
            }
        });

        // Disparar
        searchView.setOnQueryTextListener(this);

        listaContactos = (RecyclerView) findViewById(R.id.listaContactos);
        listaContactos.setLayoutManager(new LinearLayoutManager(this));

        DbContactos dbContactos = new DbContactos(this);

        listaArrayContactos = new ArrayList<>();

        adapter = new ListaContactosAdapter(dbContactos.mostrarContactos());
        listaContactos.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.filtro(s);
        return false;
    }
}