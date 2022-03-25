package sb.app.messageschedular.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class SentBroadCast : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {

        println("intent"+intent?.getStringExtra("test"))


    }


}