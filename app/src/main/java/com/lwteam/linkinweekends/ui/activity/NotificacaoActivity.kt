package com.lwteam.linkinweekends.ui.activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import com.lwteam.linkinweekends.R
import com.lwteam.linkinweekends.data.dao.TarefasDao
import com.lwteam.linkinweekends.data.dao.TipoAcoesTarefa
import com.lwteam.linkinweekends.receiver.TarefaNovaEvento
import com.lwteam.linkinweekends.ui.adapter.TarefaAdapter
import com.lwteam.linkinweekends.ui.callback.TarefaCallback
import kotlinx.android.synthetic.main.activity_notificacao.*
import kotlinx.android.synthetic.main.item_tarefa_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class NotificacaoActivity : AppCompatActivity() {
    private val dao =TarefasDao()
    private lateinit var adapter: TarefaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificacao)
        //pega notificação com app fechado
        /*if (intent.extras != null){
            for (key in intent.extras.keySet()){
                if (key == "mensagem"){
                    val descricao = intent.extras.getString(key)
                    if (descricao != null && descricao.isNotEmpty())
                        dao.criarTarefa(descricao)
                }
            }
        }*/
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
        criarAdapter()
        val itemTouchHelper = ItemTouchHelper(TarefaCallback(adapter) {
            criarAdapter()
        })
        itemTouchHelper.attachToRecyclerView(notificacao_lista_tarefas)
    }

    override fun onPause() {
        super.onPause()

        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun eventoNovaTarefa(evento:TarefaNovaEvento){
        criarAdapter()
    }

    private fun criarAdapter() {
        val tarefas = dao.pegaTodasTarefas()
        this.adapter = TarefaAdapter({
            dao.atualizaTarefa(it.uuid, TipoAcoesTarefa.LER) {
                this.adapter.lerTarefa(it.uuid)

            }
        }, {
            //TODO - fazer o clique longo
        })
        this.adapter.adicionaTodasTarefas(tarefas)
        notificacao_lista_tarefas.adapter = adapter
    }
}