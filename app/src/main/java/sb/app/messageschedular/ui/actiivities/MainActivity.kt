package sb.app.messageschedular.ui.actiivities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder

//
import dagger.hilt.android.AndroidEntryPoint
import sb.app.messageschedular.databinding.MainActivityDataBinding
import sb.app.messageschedular.viewmodel.MessageScheduleViewModel


import androidx.activity.viewModels
import sb.app.messageschedular.BR
import sb.app.messageschedular.R
import sb.app.messageschedular.api.UnSplashApi
import sb.app.messageschedular.model.Message
import sb.app.messageschedular.model.Sms
import sb.app.messageschedular.service.SmsService
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<MainActivityDataBinding, MessageScheduleViewModel>() {

    private val mViewModel by viewModels<MessageScheduleViewModel>()
    private var  mDataBinding :MainActivityDataBinding?=null



    private lateinit var mService: SmsService
    private var  mBound : Boolean =false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDataBinding = getDataBinding()



    }




    override fun getDataBindingVariale(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.activity_main
    override fun getViewModel(): MessageScheduleViewModel = mViewModel





     fun scheduleService(sms: Sms) {
        if(!mService.isServiceRunning()){
            val intent = Intent(this , SmsService::class.java)
            intent.action = SmsService.ADD_SERVICE
            intent.putExtra(SmsService.Add_kEY, sms)
            startService(intent)
        }
        else{ mService.schedule(sms)   }


    }

     fun delete(message: Message) {

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

