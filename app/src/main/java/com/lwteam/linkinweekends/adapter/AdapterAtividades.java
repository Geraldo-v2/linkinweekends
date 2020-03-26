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

import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterAtividades extends RecyclerView.Adapter<AdapterAtividades.MyViewHolder> {

    private List<Locais> Listalocais;
    private Context context;

    public AdapterAtividades(List<Locais> locais, Context context) {
        this.Listalocais = locais;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_atividades, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Locais local = Listalocais.get(position);
        holder.nomeatividade.setText(local.getNomeAtividade() );
        holder.dificuldadeatividade.setText(local.getNivelDificuldadeAtividade());

        //Pega a primeira imagem da lista
        List<String> urlFotosAtv = local.getFotosAtividade();
        String urlCapa = urlFotosAtv.get(0);
        Picasso.get().load(urlCapa).into(holder.fotoatividade);
    }

    @Override
    public int getItemCount() {
        return Listalocais.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomeatividade;
        TextView dificuldadeatividade;
        CircleImageView fotoatividade;

        public MyViewHolder(View itemView){
            super(itemView);

            nomeatividade = itemView.findViewById(R.id.txtNomeAtividade);
            dificuldadeatividade = itemView.findViewById(R.id.txtDificuldadeAtividade);
            fotoatividade = itemView.findViewById(R.id.imgAtividade);
        }

    }
}
