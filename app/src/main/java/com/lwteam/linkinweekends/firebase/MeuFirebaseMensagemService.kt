package com.lwteam.linkinweekends.firebase

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lwteam.linkinweekends.R
import com.lwteam.linkinweekends.data.dao.TarefasDao
import com.lwteam.linkinweekends.receiver.TarefaNovaEvento
import org.greenrobot.eventbus.EventBus

class MeuFirebaseMensagemService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        //recebe notificação com o app aberto
        remoteMessage.notification?.let {
            val descricao :String? = it.body
            if (descricao != null && descricao.isNotEmpty()){
                TarefasDao().criarTarefa(descricao)
                mostrarNotificacao(it)
                EventBus.getDefault().post(TarefaNovaEvento())
            }
        }
    }

        //cria notificação
    private fun mostrarNotificacao(notification: RemoteMessage.Notification) {
           val builder :Notification.Builder = Notification.Builder(this)
           builder.setContentTitle(notification.title)
           builder.setContentText(notification.body)
           builder.setSmallIcon(R.drawable.icon_top_menu)
           val notificacaoGerenciador  = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
           notificacaoGerenciador.notify(0, builder.build())

    }

    override fun onDestroy() {
        sendBroadcast(Intent("com.lwteam.linkinweekends.firebase.acao.start"))
        super.onDestroy()
    }
}