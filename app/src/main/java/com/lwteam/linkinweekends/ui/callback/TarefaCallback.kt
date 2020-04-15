package com.lwteam.linkinweekends.ui.callback

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.lwteam.linkinweekends.data.dao.TarefasDao
import com.lwteam.linkinweekends.data.dao.TipoAcoesTarefa
import com.lwteam.linkinweekends.ui.adapter.TarefaAdapter

class TarefaCallback(private val adapter: TarefaAdapter, private val retorno: () -> Unit) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val horizontal = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(0, horizontal)
    }

    override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val dao = TarefasDao()

        if (ItemTouchHelper.LEFT == direction){
            dao.atualizaTarefa(adapter.getTarefa(position).uuid, TipoAcoesTarefa.APAGAR) {retorno()}
        }else{
            dao.atualizaTarefa(adapter.getTarefa(position).uuid, TipoAcoesTarefa.LER) {retorno()}
        }
    }

}
