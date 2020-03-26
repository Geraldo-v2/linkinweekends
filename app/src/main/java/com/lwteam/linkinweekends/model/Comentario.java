package com.lwteam.linkinweekends.model;

import com.google.firebase.database.DatabaseReference;
import com.lwteam.linkinweekends.helper.ConfiguracaoFirebase;

public class Comentario {
    private String idComentario;
    private String idLocal;
    private String idUsuario;
    private String caminhoFoto;
    private String nomeUsuario;
    private String comentario;

    public Comentario() {
    }

    public boolean salvar(){

        DatabaseReference comentariosRef = ConfiguracaoFirebase.getFirebase()
                .child("comentarios")
                .child(getIdLocal());
        String chaveComentario = comentariosRef.push().getKey();
        setIdComentario(chaveComentario);
        comentariosRef.child( getIdComentario()).setValue(this);
        return true;
    }

    public String getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(String idComentario) {
        this.idComentario = idComentario;
    }

    public String getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(String idLocal) {
        this.idLocal = idLocal;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
