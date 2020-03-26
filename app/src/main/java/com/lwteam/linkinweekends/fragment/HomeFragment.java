package com.lwteam.linkinweekends.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lwteam.linkinweekends.R;

import com.lwteam.linkinweekends.activity.LocalActivity;
import com.lwteam.linkinweekends.activity.MeusLocaisActivity;
import com.lwteam.linkinweekends.activity.PerfilActivity;
import com.lwteam.linkinweekends.adapter.AdapterLocais;
import com.lwteam.linkinweekends.helper.ConfiguracaoFirebase;
import com.lwteam.linkinweekends.helper.RecyclerItemClickListener;
import com.lwteam.linkinweekends.model.Locais;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment{

    private RecyclerView recyclerLocaisHome;

    private SearchView searchViewHome;
    private AdapterLocais adapterLocais;
    private List<Locais> Listalocais = new ArrayList<>();
    private DatabaseReference locaisPublicosRef;
    private ImageButton btOpcoes;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //INFLA O LAYOUT PARA SER EXIBINO NO MENU
        View view = inflater.inflate(R.layout.fragment_home, container,false);


        btOpcoes=view.findViewById(R.id.btnOpcoes);
        searchViewHome=view.findViewById(R.id.searchHome);
        recyclerLocaisHome= view.findViewById(R.id.recyclerLocaisPublicos);

        //CARREGA TODOS OS LOCAIS
        locaisPublicosRef = ConfiguracaoFirebase.getFirebase()
                .child("locais");

        //CONFIGURA A LISTA
        recyclerLocaisHome.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerLocaisHome.setHasFixedSize(true);
        adapterLocais = new AdapterLocais(Listalocais,getActivity());
        recyclerLocaisHome.setAdapter(adapterLocais);

        //CONFIGURA O PESQUISA QUE AINDA NAO FUNCIONA

        searchViewHome.setQueryHint("Buscar Locais");
        searchViewHome.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
               if(newText!=null && !newText.isEmpty()){
                   String textoDigitado = newText.toUpperCase();
                   pesquisarLocais(textoDigitado);
               }
                return true;
            }
        });
        //CARREGA O MENU DE OPCOES  - PRA FUNCIONAR USAMOS INTENT NOS INFLATE
        btOpcoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog OpcoesDialog;
                ImageView btFecharDialogOpcoes;
                Button btCadastroLocalDialog,btPerfilDialog,btSugerirLocalDialog;
                OpcoesDialog = new Dialog(getActivity());

                //CONECTA COM OS BOTOES DO FRONT
                OpcoesDialog.setContentView(R.layout.dialog_opcoes_home);
                btFecharDialogOpcoes = (ImageView) OpcoesDialog.findViewById(R.id.btnFecharDialogOpcoes);
                btCadastroLocalDialog=OpcoesDialog.findViewById(R.id.btnCadLocalDialog);
                btPerfilDialog=OpcoesDialog.findViewById(R.id.btnPerfilDialog);
                btSugerirLocalDialog=OpcoesDialog.findViewById(R.id.btnSugerirLocalDialog);

                //BOTAO DE CADASTRAR LOCAL - SOMENTE POR AGORA
                btCadastroLocalDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(getActivity(), MeusLocaisActivity.class);
                        startActivity(it);
                        OpcoesDialog.dismiss();
                    }
                });
                //PARA ACESSAR PERFIL DO USUARIO
                btPerfilDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(getActivity(), PerfilActivity.class);
                        startActivity(it);
                        OpcoesDialog.dismiss();
                    }
                });
                //PARA SUBEGIR LOCAIS
                btSugerirLocalDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String []{"linkin.weekends@gmail.com"});
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT,"LW App: Sugestão de Local");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Informe dados como: Nome do local, Endereço e Tipos de atividades");
                        sendIntent.setType("message/rfc822");

                        try {
                            startActivity(Intent.createChooser(sendIntent, "Mande a mensagem via Email"));
                            OpcoesDialog.dismiss();
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getActivity(), "Você não tem um aplicativo de email cadastrado .", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //O "X" QUE FECHA A TELA
                btFecharDialogOpcoes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpcoesDialog.dismiss();
                    }
                });

                //SETA A COR DE FUNDO DA TELA
                OpcoesDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                OpcoesDialog.show();
            }
        });


        recuperarLocais();

        //APLICA A CLICK SOB O LOCAL SELECIONADO - PRA FUNCIONAR USAMOS INTENT NOS INFLATE
        recyclerLocaisHome.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerLocaisHome,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Locais localSelecionado = Listalocais.get(position);
                                Intent i = new Intent(getActivity(),LocalActivity.class);
                                i.putExtra("localSelecionado",localSelecionado);
                                startActivity(i);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
        return view;
    }

    //PESQUISA QUE NAO TA RETORNANDO PORRA NENHUMA
    private void pesquisarLocais(String texto) {
        List<Locais> listalocaisBusca = new ArrayList<>();
        for (Locais locais : listalocaisBusca){
            String nomelocal = locais.getNome().toUpperCase();

            if(nomelocal.contains(texto)){
                listalocaisBusca.add(locais);
            }
        }
        adapterLocais = new AdapterLocais(listalocaisBusca,getActivity());
        recyclerLocaisHome.setAdapter(adapterLocais);
        adapterLocais.notifyDataSetChanged();
    }

    //RECUPERA DO FIREBASE TODOS OS LOCAIS PARA EXIBIR, FAZ O CAMINHO DO NÓ
    public void recuperarLocais() {

        Listalocais.clear();
        locaisPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot estados : dataSnapshot.getChildren()){
                    for(DataSnapshot regioes : estados.getChildren()){
                        for(DataSnapshot cidades : regioes.getChildren()){
                            for(DataSnapshot ds : cidades.getChildren()){
                                Locais locais = (ds.getValue(Locais.class));
                                Listalocais.add(locais);
                            }
                        }
                    }
                }
                Collections.reverse(Listalocais);
                adapterLocais.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
