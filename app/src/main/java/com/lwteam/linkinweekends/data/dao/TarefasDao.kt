package com.lwteam.linkinweekends.data.dao

import com.lwteam.linkinweekends.data.modelo.Tarefa
import io.realm.Realm

open class TarefasDao {
    private val classeTarefa = Tarefa::class.java
    private val realm: Realm = Realm.getDefaultInstance()

    //metodo para listar todas as notificações do banco
    fun pegaTodasTarefas(): List<Tarefa>{
        try {
            return realm.where(classeTarefa).and().equalTo(Tarefa.EXCLUIDA, false).findAll().toList()
        } catch (e: Exception) {
            throw e
        }
    }
    fun criarTarefa(descricao: String): Tarefa{
        val tarefa = Tarefa()
        tarefa.descricao = descricao
        realm.executeTransaction{
            it.insertOrUpdate(tarefa)
        }

        return tarefa
    }
    //metodos para marcar como lida, ou excluir a notificacao do app, e não do banco
    fun atualizaTarefa (id: String, acao: TipoAcoesTarefa, retorno: () ->Unit){
        realm.executeTransaction{
            val tarefa = realm.where(classeTarefa).equalTo(Tarefa.ID, id).findFirst()
            if (tarefa != null){
                when (acao){
                    TipoAcoesTarefa.LER ->{
                        if (!tarefa.lida)
                            tarefa.lida = !tarefa.lida

                    }else -> {
                    if (!tarefa.excluida)
                        tarefa.excluida = !tarefa.excluida
                }
                }
                retorno()
            }
        }
    }
}