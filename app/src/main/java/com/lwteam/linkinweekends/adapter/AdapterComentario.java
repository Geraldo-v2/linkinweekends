package com.lwteam.linkinweekends.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lwteam.linkinweekends.R;
import com.lwteam.linkinweekends.model.Comentario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterComentario extends RecyclerView.Adapter<AdapterComentario.MyViewholder> {

    private List<Comentario> listaComentarios;
    private Context context;

    public AdapterComentario(List<Comentario> listaComentarios, Context context) {
        this.listaComentarios = listaComentarios;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comentario, parent, false);
        return new AdapterComentario.MyViewholder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {

        Comentario comentario = listaComentarios.get(position);
        holder.nomeUsuario.setText(comentario.getNomeUsuario());
        holder.comentário.setText(comentario.getComentario());
        Glide.with(context).load(comentario.getCaminhoFoto()).into(holder.imagemPerfil);
    }

    @Override
    public int getItemCount() {
        return listaComentarios.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder{
        CircleImageView imagemPerfil;
        TextView nomeUsuario, comentário;

        public  MyViewholder (View itemView){
            super(itemView);
            imagemPerfil = itemView.findViewById(R.id.imageFotoComentario);
            nomeUsuario = itemView.findViewById(R.id.textNomeComentario);
            comentário = itemView.findViewById(R.id.textAdapterComentario);
        }
    }
}
