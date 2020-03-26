package com.lwteam.linkinweekends.model;

import com.google.firebase.database.DatabaseReference;
import com.lwteam.linkinweekends.helper.ConfiguracaoFirebase;

import java.io.Serializable;
import java.util.List;

public class Locais implements Serializable {

    //locais
    private String Estado;
    private String Regiao;
    private String Cidade;
    private String idLocal;
    private String nome;
    private String nomepesquisa;
    private String descricao;
    private String custos;
    private List<String> fotos;
    private List<String>fotosAtividade;
    private String latitude;
    private String longitude;
    private String horaAbreUtil;
    private String horaFechaUtil;
    private String horaAbreSabado;
    private String horaFechaSabado;
    private String horaAbreDomingo;
    private String horaFechaDomingo;
    private String emailLocal;
    private String telefoneLocal;
    private String Rua;
    private String Numero;
    private String Bairro;
    private String CEP;

    //atividades
    private String EstadosAtv;
    private String RegioesAtv;
    private String CidadesAtv;
    private String idLocalAtv;
    private String idAtividade;
    private String categoriaAtividade;
    private String nivelDificuldadeAtividade;
    private String nomeAtividade;
    private String descricaoAtividade;
    private String custosAtividade;
    private String horaAbreUtilAtividade;
    private String horaFechaUtilAtividade;
    private String horaAbreSabadoAtividade;
    private String horaFechaSabadoAtividade;
    private String horaAbreDomingoAtividade;
    private String horaFechaDomingoAtividade;

    public Locais() {
        DatabaseReference localRef= ConfiguracaoFirebase.getFirebase()
                .child("locais");
        setIdLocal(localRef.push().getKey());

        DatabaseReference atividadeRef=ConfiguracaoFirebase.getFirebase()
                .child("atividades");
        setIdAtividade(atividadeRef.push().getKey());


    }
    public void salvar(){
        DatabaseReference localRef= ConfiguracaoFirebase.getFirebase()
                .child("locais");
        localRef.child(getEstado())
                    .child(getRegiao())
                        .child(getCidade())
                            .child(getIdLocal())
                            .setValue(this);
    }
    public void salvarAtividade() {
        DatabaseReference localRef = ConfiguracaoFirebase.getFirebase()
                .child("locais");
        localRef.child(getEstadosAtv())
                .child(getRegioesAtv())
                .child(getCidadesAtv())
                .child(getIdLocalAtv())
                .child("atividades")
                .child(getCategoriaAtividade())
                .child(getNivelDificuldadeAtividade())
                .child(getIdAtividade())
                .setValue(this);
        salvarFotoAtividade();
    }
    public void salvarFotoAtividade(){
        DatabaseReference atividadeRef = ConfiguracaoFirebase.getFirebase()
                .child("atividades");
        atividadeRef.child(getIdAtividade())
                .child(getCategoriaAtividade())
                .child(getNivelDificuldadeAtividade())
                .child(getIdAtividade())
                .setValue(this);

    }
    public void remover(){
        DatabaseReference localRef= ConfiguracaoFirebase.getFirebase()
                .child("locais")
                    .child(getEstado())
                        .child(getRegiao())
                            .child(getCidade())
                                .child(getIdLocal());
        localRef.removeValue();
    }
    public String getIdLocal() {
        return idLocal;
    }

    public String getEstado() { return Estado; }

    public void setEstado(String estado) { Estado = estado; }

    public String getRegiao() { return Regiao; }

    public void setRegiao(String regiao) { Regiao = regiao; }

    public String getCidade() { return Cidade; }

    public void setCidade(String cidade) { Cidade = cidade; }

    public void setIdLocal(String idLocal) {
        this.idLocal = idLocal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomepesquisa() { return nomepesquisa; }

    public void setNomepesquisa(String nome) {
        this.nomepesquisa = nome.toUpperCase();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCustos() {
        return custos;
    }

    public void setCustos(String custos) {
        this.custos = custos;
    }

    public String getHoraAbreUtil() {
        return horaAbreUtil;
    }

    public void setHoraAbreUtil(String horaAbreUtil) {
        this.horaAbreUtil = horaAbreUtil;
    }

    public String getHoraFechaUtil() {
        return horaFechaUtil;
    }

    public void setHoraFechaUtil(String horaFechaUtil) {
        this.horaFechaUtil = horaFechaUtil;
    }

    public String getHoraAbreSabado() {
        return horaAbreSabado;
    }

    public void setHoraAbreSabado(String horaAbreSabado) {
        this.horaAbreSabado = horaAbreSabado;
    }

    public String getHoraFechaSabado() {
        return horaFechaSabado;
    }

    public void setHoraFechaSabado(String horaFechaSabado) {
        this.horaFechaSabado = horaFechaSabado;
    }

    public String getHoraAbreDomingo() {
        return horaAbreDomingo;
    }

    public void setHoraAbreDomingo(String horaAbreDomingo) {
        this.horaAbreDomingo = horaAbreDomingo;
    }

    public String getHoraFechaDomingo() {
        return horaFechaDomingo;
    }

    public void setHoraFechaDomingo(String horaFechaDomingo) {
        this.horaFechaDomingo = horaFechaDomingo;
    }

    public String getEmailLocal() {
        return emailLocal;
    }

    public void setEmailLocal(String emailLocal) {
        this.emailLocal = emailLocal;
    }

    public String getTelefoneLocal() {
        return telefoneLocal;
    }

    public void setTelefoneLocal(String telefoneLocal) {
        this.telefoneLocal = telefoneLocal;
    }

    public String getRua() {
        return Rua;
    }

    public void setRua(String rua) {
        Rua = rua;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) {
        Numero = numero;
    }

    public String getBairro() {
        return Bairro;
    }

    public void setBairro(String bairro) {
        Bairro = bairro;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    //atividades


    public String getEstadosAtv() {
        return EstadosAtv;
    }

    public void setEstadosAtv(String estadosAtv) {
        EstadosAtv = estadosAtv;
    }

    public String getRegioesAtv() {
        return RegioesAtv;
    }

    public void setRegioesAtv(String regioesAtv) {
        RegioesAtv = regioesAtv;
    }

    public String getCidadesAtv() {
        return CidadesAtv;
    }

    public void setCidadesAtv(String cidadesAtv) {
        CidadesAtv = cidadesAtv;
    }

    public String getIdLocalAtv() {
        return idLocalAtv;
    }

    public void setIdLocalAtv(String idLocalAtv) {
        this.idLocalAtv = idLocalAtv;
    }

    public String getCategoriaAtividade() {
        return categoriaAtividade;
    }

    public void setCategoriaAtividade(String categoriaAtividade) {
        this.categoriaAtividade = categoriaAtividade;
    }

    public String getNivelDificuldadeAtividade() {
        return nivelDificuldadeAtividade;
    }

    public void setNivelDificuldadeAtividade(String nivelDificuldadeAtividade) {
        this.nivelDificuldadeAtividade = nivelDificuldadeAtividade;
    }

    public String getNomeAtividade() {
        return nomeAtividade;
    }

    public void setNomeAtividade(String nomeAtividade) {
        this.nomeAtividade = nomeAtividade;
    }

    public String getDescricaoAtividade() {
        return descricaoAtividade;
    }

    public void setDescricaoAtividade(String descricaoAtividade) {
        this.descricaoAtividade = descricaoAtividade;
    }

    public String getCustosAtividade() {
        return custosAtividade;
    }

    public void setCustosAtividade(String custosAtividade) {
        this.custosAtividade = custosAtividade;
    }

    public String getHoraAbreUtilAtividade() {
        return horaAbreUtilAtividade;
    }

    public void setHoraAbreUtilAtividade(String horaAbreUtilAtividade) {
        this.horaAbreUtilAtividade = horaAbreUtilAtividade;
    }

    public String getHoraFechaUtilAtividade() {
        return horaFechaUtilAtividade;
    }

    public void setHoraFechaUtilAtividade(String horaFechaUtilAtividade) {
        this.horaFechaUtilAtividade = horaFechaUtilAtividade;
    }

    public String getHoraAbreSabadoAtividade() {
        return horaAbreSabadoAtividade;
    }

    public void setHoraAbreSabadoAtividade(String horaAbreSabadoAtividade) {
        this.horaAbreSabadoAtividade = horaAbreSabadoAtividade;
    }

    public String getHoraFechaSabadoAtividade() {
        return horaFechaSabadoAtividade;
    }

    public void setHoraFechaSabadoAtividade(String horaFechaSabadoAtividade) {
        this.horaFechaSabadoAtividade = horaFechaSabadoAtividade;
    }

    public String getHoraAbreDomingoAtividade() {
        return horaAbreDomingoAtividade;
    }

    public void setHoraAbreDomingoAtividade(String horaAbreDomingoAtividade) {
        this.horaAbreDomingoAtividade = horaAbreDomingoAtividade;
    }

    public String getHoraFechaDomingoAtividade() {
        return horaFechaDomingoAtividade;
    }

    public void setHoraFechaDomingoAtividade(String horaFechaDomingoAtividade) {
        this.horaFechaDomingoAtividade = horaFechaDomingoAtividade;
    }

    public List<String> getFotosAtividade() {
        return fotosAtividade;
    }

    public void setFotosAtividade(List<String> fotosAtividade) {
        this.fotosAtividade = fotosAtividade;
    }

    public String getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(String idAtividade) {
        this.idAtividade = idAtividade;
    }
}