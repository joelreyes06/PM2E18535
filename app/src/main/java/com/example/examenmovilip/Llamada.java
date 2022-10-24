package com.example.examenmovilip;

import static android.Manifest.permission.CALL_PHONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Llamada extends AppCompatActivity {

    Button btnRegresar;
    TextView lblTel;
    String telefono;

    static final int PETICION_LLAMADA_TELEFONO = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamada);

        btnRegresar = (Button) findViewById(R.id.btnAtras2);

        lblTel = (TextView) findViewById(R.id.lblTel);

        telefono = getIntent().getExtras().getString("TEL");
        lblTel.setText(telefono);

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Contactos_Salvados.class);
                startActivity(intent);
            }
        });

        llamada();

    }

    private void llamada() {
        try{
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE}, PETICION_LLAMADA_TELEFONO);
                Toast.makeText(Llamada.this, "If", Toast.LENGTH_SHORT).show();
            }else{
                Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telefono));
                startActivity(intent);
                Toast.makeText(Llamada.this, "Else", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){
            ex.toString();
        }
    }
}