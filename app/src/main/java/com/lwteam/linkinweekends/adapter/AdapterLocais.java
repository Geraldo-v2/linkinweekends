package com.lwteam.linkinweekends.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lwteam.linkinweekends.R;
import com.lwteam.linkinweekends.model.Locais;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterLocais extends RecyclerView.Adapter<AdapterLocais.MyViewHolder> {

    private List<Locais> Listalocais;
    private Context context;

    public AdapterLocais(List<Locais> locais, Context context) {
        this.Listalocais = locais;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_locais, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Locais local = Listalocais.get(position);
        holder.titulo.setText(local.getNome() );
        holder.cidade.setText(local.getCidade());

        //Pega a primeira imagem da lista
        List<String> urlFotos = local.getFotos();
        String urlCapa = urlFotos.get(0);

        Picasso.get().load(urlCapa).into(holder.foto);


    }

    @Override
    public int getItemCount() {
        return Listalocais.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titulo;
        TextView cidade;
        ImageView foto;

        public MyViewHolder(View itemView){
            super(itemView);

            titulo = itemView.findViewById(R.id.txtTitulo);
            cidade = itemView.findViewById(R.id.txtCidade);
            foto = itemView.findViewById(R.id.imgLocal);
        }

    }
}
