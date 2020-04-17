package com.lwteam.linkinweekends.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.lwteam.linkinweekends.R
import com.lwteam.linkinweekends.data.modelo.Tarefa
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class TarefaAdapter(
        private val onClick: (Tarefa) -> Unit,
        private val onLongClick: (Tarefa) -> Unit
): RecyclerView.Adapter<TarefaAdapter.ViewHolder>() {

    private val listaTarefas = HashMap<String, Tarefa>()

    fun adicionaTodasTarefas(tarefas: List<Tarefa>){
        this.listaTarefas.clear()
        for (tarefa:Tarefa in tarefas){
            listaTarefas[tarefa.uuid] = tarefa
        }
        notifyDataSetChanged()
    }

    fun lerTarefa(uuid: String) {
        val tarefa: Tarefa? = listaTarefas[uuid]

        if (tarefa != null)
            tarefa.lida = true

        notifyDataSetChanged()
    }

    class ViewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        lateinit var descricao : TextView
        lateinit var dataCriacao : TextView
        lateinit var lida : ImageView
        lateinit var cartao : CardView
    }
    //infla o layout
    fun getTarefa(position: Int) = listaTarefas.values.toList()[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarefa_layout, parent, false)

        val viewHolder = ViewHolder(view)
        viewHolder.descricao = view.findViewById(R.id.item_tarefa_descricao)
        viewHolder.dataCriacao = view.findViewById(R.id.item_tarefa_data)
        viewHolder.lida = view.findViewById(R.id.item_tarefa_lida)
        viewHolder.cartao = view.findViewById(R.id.item_tarefa_card)

        //metodo para expandir o texto
        var isTextViewClicked = false
        viewHolder.descricao.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                if(isTextViewClicked){
                    //This will shrink textview to 2 lines if it is expanded.
                    viewHolder.descricao.maxLines = 2
                    isTextViewClicked = false
                }
                else{
                    //This will expand the textview if it is of 2 lines
                    viewHolder.descricao.maxLines = Integer.MAX_VALUE
                    isTextViewClicked = true
                }
            }
        })

        return viewHolder
    }


    override fun getItemCount() = listaTarefas.values.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val tarefa = getTarefa(position)

        holder.cartao.setOnClickListener {
            onClick(tarefa)
        }
        holder.cartao.setOnLongClickListener {
            onLongClick(tarefa)
            true
        }

        holder.descricao.text =tarefa.descricao

        val data = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(tarefa.dataCriacao)
        holder.dataCriacao.text =  "Criada em:$data"

        if (tarefa.lida)
            holder.lida.visibility = View.VISIBLE
        else{
            holder.lida.visibility = View.GONE

        }

        if (tarefa.concluida){
            holder.lida.visibility = View.GONE
        }

    }


}


