package com.lwteam.linkinweekends.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import com.lwteam.linkinweekends.firebase.MeuFirebaseMensagemService

class GlobalReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return
        if (intent == null) return
        //configurando o inicio do servidor nessa intent para todas as versões do android, para que
        //o app SEMPRE fique ouvindo por notificações do FIREBASE
        if (intent.action == "com.lwteam.linkinweekends.firebase.acao.start"){

            ContextCompat.startForegroundService(context, Intent(context, MeuFirebaseMensagemService::class.java))

            if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O)
                context.startService(Intent(context, MeuFirebaseMensagemService::class.java))
        }
    }
}