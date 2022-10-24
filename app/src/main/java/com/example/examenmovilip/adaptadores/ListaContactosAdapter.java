package com.example.examenmovilip.adaptadores;

import static android.Manifest.permission.CALL_PHONE;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenmovilip.AccionesActivity;
import com.example.examenmovilip.Contactos_Salvados;
import com.example.examenmovilip.Llamada;
import com.example.examenmovilip.PantallaInicial;
import com.example.examenmovilip.R;
import com.example.examenmovilip.entidades.Contactos;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ListaContactosAdapter extends RecyclerView.Adapter<ListaContactosAdapter.ContactoViewHolder> {

    ArrayList<Contactos> listaContactos;
    ArrayList<Contactos> listaOriginal;

    static final int PETICION_LLAMADA_TELEFONO = 102;

    public ListaContactosAdapter(ArrayList<Contactos> listaContactos) {
        this.listaContactos = listaContactos;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(listaContactos);
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_contacto, null, false);
        return new ContactoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        holder.viewPais.setText(listaContactos.get(position).getPais());
        holder.viewNombres.setText(listaContactos.get(position).getNombre());
        holder.viewTel.setText(listaContactos.get(position).getTelefono());
        // holder.viewNota.setText(listaContactos.get(position).getNota());
    }

    public void filtro(String searchView) {
        int longitud = searchView.length();
        if(longitud == 0) {
            listaContactos.clear();
            listaContactos.addAll(listaOriginal);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Contactos> collecion = listaContactos.stream()
                        .filter(i -> i.getNombre().toLowerCase().contains(searchView.toLowerCase()))
                        .collect(Collectors.toList());
                listaContactos.clear();
                listaContactos.addAll(collecion);
            } else {
                for (Contactos c: listaOriginal) {
                    if(c.getNombre().toLowerCase().contains(searchView.toLowerCase())) {
                        listaContactos.add(c);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    public class ContactoViewHolder extends RecyclerView.ViewHolder {

        TextView viewPais, viewNombres, viewTel, viewNota;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);

            viewPais = itemView.findViewById(R.id.viewPais);
            viewNombres = itemView.findViewById(R.id.viewNombres);
            viewTel = itemView.findViewById(R.id.viewTel);
            // viewNota = itemView.findViewById(R.id.viewNota);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setMessage("¿Desea Llamar al contacto " + viewNombres.getText().toString() + " " + viewTel.getText().toString() + "?");
                    builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Context context = view.getContext();
                            Intent intent = new Intent(context, Llamada.class);
                            intent.putExtra("TEL", viewTel.getText().toString());
                            context.startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //
                        }
                    });
                    builder.show();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, AccionesActivity.class);
                    intent.putExtra("ID", listaContactos.get(getAdapterPosition()).getId());
                    context.startActivity(intent);

                    return true;
                }
            });
        }
    }
}
