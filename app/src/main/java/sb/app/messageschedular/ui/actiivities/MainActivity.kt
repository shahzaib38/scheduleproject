package sb.app.messageschedular.ui.actiivities

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log

//
import dagger.hilt.android.AndroidEntryPoint
import sb.app.messageschedular.databinding.MainActivityDataBinding
import sb.app.messageschedular.viewmodel.MessageScheduleViewModel


import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.work.*
import com.google.gson.Gson
import sb.app.messageschedular.BR
import sb.app.messageschedular.R
import sb.app.messageschedular.api.UnSplashApi
import sb.app.messageschedular.model.Message
import sb.app.messageschedular.model.Sms
import sb.app.messageschedular.service.SmsService
import sb.app.messageschedular.util.GsonUtils
import sb.app.messageschedular.workmanager.SmsWorkManager
//import sb.app.messageschedular.workmanager.SchedulerWorker
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<MainActivityDataBinding, MessageScheduleViewModel>() {

    private val mViewModel by viewModels<MessageScheduleViewModel>()
    private var  mDataBinding :MainActivityDataBinding?=null

    val workManager =  WorkManager.getInstance(this  )



    private lateinit var mService: SmsService
    private var  mBound : Boolean =false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDataBinding = getDataBinding()



    }




    override fun getDataBindingVariale(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.activity_main
    override fun getViewModel(): MessageScheduleViewModel = mViewModel



    private var count=0


     @SuppressLint("RestrictedApi")
     fun scheduleService(sms: Sms) {


         Log.i("SmsTest","${sms}")



//
//     val serializedSms  =    GsonUtils.serializeSms(sms)
//
//             val oneTimeRequest = OneTimeWorkRequest.Builder(SmsWorkManager::class.java)
//
//             val data = Data.Builder()
//
//               data.put("SMS",serializedSms)
//               oneTimeRequest.setInputData(data.build())
//                workManager.enqueueUniqueWork("Unique",ExistingWorkPolicy.REPLACE,oneTimeRequest.build() )
//


        if(!mService.isServiceRunning()){

            val intent = Intent(this , SmsService::class.java)
            intent.action = SmsService.ADD_SERVICE
            intent.putExtra(SmsService.Add_kEY, sms)

         ContextCompat.startForegroundService(this.applicationContext ,intent)
//
        }
        else{ mService.schedule(sms)


        }




    }

     fun delete(message: Message) {


         Log.i("MAin","${         message.userInfo.userId}")


         mService.delete(message)


     }









    private  val mConnection  = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder  =service as SmsService.LocalBinder
            mService =       binder.getService()
            mBound =true }
        override fun onServiceDisconnected(name: ComponentName?) {
            mBound =false }
    }



    override fun onStart() {
        super.onStart()
        val intent = Intent(this ,SmsService::class.java)
        bindService(intent , mConnection  , Context.BIND_AUTO_CREATE )
    }

    override fun onStop() {
        super.onStop()
        unbindService(mConnection)
        mBound = false }


}

