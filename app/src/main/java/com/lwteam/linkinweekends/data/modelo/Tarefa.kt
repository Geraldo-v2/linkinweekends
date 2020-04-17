package com.lwteam.linkinweekends.data.modelo

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Tarefa: RealmObject() {

    companion object {
        const val ID = "uuid"
        const val EXCLUIDA = "excluida"
    }

    @Required
    @PrimaryKey
    var uuid: String = ""
    private set

    @Required
    var descricao: String = ""

    @Required
    var dataCriacao: Date
    private set

    var concluida: Boolean = false
    var lida: Boolean = false
    var excluida: Boolean =false

    init {
        if (uuid.isEmpty())
            uuid = UUID.randomUUID().toString()

        val cal: Calendar = Calendar.getInstance()
        dataCriacao = cal.time
    }

}