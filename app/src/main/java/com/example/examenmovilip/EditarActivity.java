package com.example.examenmovilip;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.examenmovilip.db.DbContactos;
import com.example.examenmovilip.entidades.Contactos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditarActivity extends AppCompatActivity {

    EditText txtPais, txtNombre, txtTel, txtNota;
    Button btnGuardar, btnRegresar_ac;
    FloatingActionButton fabEditar, fabEliminar, fabCompartir;

    Contactos contactos;
    int id = 0;
    boolean correcto = false;

    @SuppressLint("RestrictedApi")
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
        fabEditar.setVisibility(View.INVISIBLE);
        fabEliminar = (FloatingActionButton) findViewById(R.id.fabEliminar);
        fabEliminar.setVisibility(View.INVISIBLE);
        fabCompartir = (FloatingActionButton) findViewById(R.id.fabCompartir);
        fabCompartir.setVisibility(View.INVISIBLE);

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

        final DbContactos dbContactos = new DbContactos(EditarActivity.this);
        contactos = dbContactos.VerContactos(id);

        if (contactos != null) {
            txtPais.setText(contactos.getPais());
            txtNombre.setText(contactos.getNombre());
            txtTel.setText(contactos.getTelefono());
            txtNota.setText(contactos.getNota());

            btnRegresar_ac.setVisibility(View.INVISIBLE);
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txtPais.getText().toString().equals("") && !txtNombre.getText().toString().equals("") && !txtTel.getText().toString().equals("") && !txtNota.getText().toString().equals("")) {
                    correcto = dbContactos.editar(id, txtPais.getText().toString(),
                            txtNombre.getText().toString(),
                            txtTel.getText().toString(),
                            txtNota.getText().toString());
                    
                    if(correcto) {
                        Toast.makeText(EditarActivity.this, "Contacto Actualizado Correctamente", Toast.LENGTH_SHORT).show();
                        registroContactos();
                    } else {
                        Toast.makeText(EditarActivity.this, "Error al Actualizar Contacto", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    alerta();
                }
            }
        });
    }

    private void registroContactos() {
        Intent intent = new Intent(getApplicationContext(), Contactos_Salvados.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }

    public void alerta() {
        if(txtPais.getText().toString().equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditarActivity.this);
            builder.setMessage("Debe llenar el campo Pais");
            builder.setTitle("Alerta!");
            builder.setCancelable(false);
            builder.setNegativeButton("Cerrar", (DialogInterface.OnClickListener) (dialog, which) -> {
                dialog.cancel();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        if(txtNombre.getText().toString().equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditarActivity.this);
            builder.setMessage("Debe llenar el campo Nombre");
            builder.setTitle("Alerta!");
            builder.setCancelable(false);
            builder.setNegativeButton("Cerrar", (DialogInterface.OnClickListener) (dialog, which) -> {
                dialog.cancel();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        if(txtTel.getText().toString().equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditarActivity.this);
            builder.setMessage("Debe llenar el campo Telefono");
            builder.setTitle("Alerta!");
            builder.setCancelable(false);
            builder.setNegativeButton("Cerrar", (DialogInterface.OnClickListener) (dialog, which) -> {
                dialog.cancel();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        if(txtNota.getText().toString().equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditarActivity.this);
            builder.setMessage("Debe llenar el campo Nota");
            builder.setTitle("Alerta!");
            builder.setCancelable(false);
            builder.setNegativeButton("Cerrar", (DialogInterface.OnClickListener) (dialog, which) -> {
                dialog.cancel();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

}
