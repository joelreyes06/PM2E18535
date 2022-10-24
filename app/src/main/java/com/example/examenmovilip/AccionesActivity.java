package com.example.examenmovilip;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.examenmovilip.db.DbContactos;
import com.example.examenmovilip.entidades.Contactos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AccionesActivity extends AppCompatActivity {

    EditText txtPais, txtNombre, txtTel, txtNota;
    Button btnGuardar, btnRegresar_ac;
    FloatingActionButton fabEditar, fabEliminar, fabCompartir;

    Contactos contactos;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones);

        txtPais = (EditText) findViewById(R.id.txtPais);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtTel = (EditText) findViewById(R.id.txtTel);
        txtNota = (EditText) findViewById(R.id.txtNota);

        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnRegresar_ac = (Button) findViewById(R.id.btnRegresar_ac);
        fabEditar = (FloatingActionButton) findViewById(R.id.fabEditar);
        fabEliminar = (FloatingActionButton) findViewById(R.id.fabEliminar);
        fabCompartir = (FloatingActionButton) findViewById(R.id.fabCompartir);

        btnRegresar_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Contactos_Salvados.class);
                startActivity(intent);
            }
        });

        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if(extras == null) {
                id = Integer.parseInt(null);
            } else {
                id = extras.getInt("ID");
            }

        } else {
            id = (int) savedInstanceState.getSerializable("ID");
        }

        DbContactos dbContactos = new DbContactos(AccionesActivity.this);
        contactos = dbContactos.VerContactos(id);

        if (contactos != null) {
            txtPais.setText(contactos.getPais());
            txtNombre.setText(contactos.getNombre());
            txtTel.setText(contactos.getTelefono());
            txtNota.setText(contactos.getNota());

            txtPais.setInputType(InputType.TYPE_NULL);
            txtNombre.setInputType(InputType.TYPE_NULL);
            txtTel.setInputType(InputType.TYPE_NULL);
            txtNota.setInputType(InputType.TYPE_NULL);

            btnGuardar.setVisibility(View.INVISIBLE);
        }

        fabCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent compartir = new Intent(android.content.Intent.ACTION_SEND);
                compartir.setType("text/plain");
                // compartir.putExtra(android.content.Intent.EXTRA_SUBJECT, txtNombre.getText().toString());
                compartir.putExtra(android.content.Intent.EXTRA_TEXT, txtNombre.getText().toString()
                        + "\n" + txtTel.getText().toString());
                startActivity(Intent.createChooser(compartir, "Compartir vía"));
            }
        });

        fabEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditarActivity.class);
                intent.putExtra("ID", id);
                startActivity(intent);
            }
        });

        fabEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AccionesActivity.this);
                builder.setMessage("¿Desea Eliminar Este Contacto?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(dbContactos.eliminar(id)) {
                                    Intent intent = new Intent(getApplicationContext(), Contactos_Salvados.class);
                                    startActivity(intent);
                                }
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //
                            }
                        }).show();
            }
        });
    }
}