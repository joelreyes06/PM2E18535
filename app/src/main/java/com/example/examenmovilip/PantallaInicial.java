package com.example.examenmovilip;

import static android.Manifest.permission.CALL_PHONE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examenmovilip.db.DbContactos;
import com.example.examenmovilip.db.DbHelper;
import com.hbb20.CountryCodePicker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PantallaInicial extends AppCompatActivity {

    Button btntakephoto, btnGuardar, btnSalvado;
    EditText txtNombre, txtNota, editTextCarrierNumber;

    /*Declaracion de variables*/
    static final int peticion_Captura_imagen=100;
    static final int peticion_acceso_cam=201;

    ImageView objetoImagen;
    String PathImagen;

    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicial);





        setContentView(R.layout.activity_pantalla_inicial);

        Crear_BD();

        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtNota = (EditText) findViewById(R.id.txtNota);

        objetoImagen = (ImageView)findViewById(R.id.imageView);
        btntakephoto = (Button) findViewById(R.id.btntakephoto);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnSalvado = (Button) findViewById(R.id.btnSalvado);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        editTextCarrierNumber = (EditText) findViewById(R.id.editText_carrierNumber);
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbContactos dbContactos = new DbContactos(PantallaInicial.this);
                if(!txtNombre.getText().toString().equals("") && !txtNota.getText().toString().equals("") && !editTextCarrierNumber.getText().toString().equals("")) {
                    long id = dbContactos.insertar(ccp.getSelectedCountryName(),
                        txtNombre.getText().toString(),
                        ccp.getFormattedFullNumber(),
                        txtNota.getText().toString());

                    if (id > 0) {
                        Toast.makeText(PantallaInicial.this, "Contacto Guardado Correctamente", Toast.LENGTH_SHORT).show();
                        limpiar();
                    } else {
                        Toast.makeText(PantallaInicial.this, "Error Al Guardar el Contacto", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    alerta();
                }
            }
        });

        btnSalvado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Contactos_Salvados.class);
                startActivity(intent);
            }
        });

        btntakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

    }

    private void Crear_BD() {
        DbHelper dbHelper = new DbHelper(PantallaInicial.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db != null) {
            // Toast.makeText(PantallaInicial.this, "Base de datos creada", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PantallaInicial.this, "Error al crear base de datos", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiar() {
        ccp.setFullNumber("");
        txtNombre.setText("");
        ccp.setDefaultCountryUsingNameCode("us");
        txtNota.setText("");
    }

    public void alerta() {
        if(txtNombre.getText().toString().equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PantallaInicial.this);
            builder.setMessage("Debe llenar el campo Nombre");
            builder.setTitle("Alerta!");
            builder.setCancelable(false);
            builder.setNegativeButton("Cerrar", (DialogInterface.OnClickListener) (dialog, which) -> {
                dialog.cancel();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        if(editTextCarrierNumber.getText().toString().equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PantallaInicial.this);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(PantallaInicial.this);
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

    public void llamar() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" +123));

        if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{CALL_PHONE}, 1);
            }
        }
    }

    private void permisos(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, peticion_acceso_cam);
        }
        else{
            tomarfoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == peticion_acceso_cam){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                tomarfoto();
            }
        }
    }
    private void tomarfoto(){
        Intent intentfoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intentfoto.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intentfoto, peticion_Captura_imagen);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== peticion_Captura_imagen){
            Bundle extras = data.getExtras();
            Bitmap imagen =(Bitmap) extras.get("data");
            objetoImagen.setImageBitmap(imagen);
        }

        /*if(requestCode == peticion_acceso_cam && resultCode == RESULT_OK){
            File foto = new File(PathImagen);
            objetoImagen.setImageURI(Uri.fromFile(foto));

        }*/
    }

}