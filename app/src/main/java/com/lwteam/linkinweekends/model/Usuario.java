package com.lwteam.linkinweekends.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.lwteam.linkinweekends.helper.ConfiguracaoFirebase;

import java.util.HashMap;
import java.util.Map;

public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private String caminhoFoto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public Usuario() {

    }
    public void salvar(){
        DatabaseReference fiebaseRef= ConfiguracaoFirebase.getFirebase();
        DatabaseReference usuariosRef = fiebaseRef.child("usuarios").child(getId());
        usuariosRef.setValue(this);
    }
    public void atualizar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference usuarioRef = firebaseRef
                .child("usuarios")
                .child(getId());
        Map<String,Object>valoresUsuario=converterParaMap();
        usuarioRef.updateChildren(valoresUsuario);
    }

    public Map<String, Object>converterParaMap(){
        HashMap<String,Object>usuarioMap=new HashMap<>();
        usuarioMap.put("email",getEmail());
        usuarioMap.put("nome",getNome());
        usuarioMap.put("id",getId());
        usuarioMap.put("caminhoFoto",getCaminhoFoto());

        return usuarioMap;
    }
}
