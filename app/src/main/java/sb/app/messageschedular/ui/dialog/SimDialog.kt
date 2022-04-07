package sb.app.messageschedular.ui.dialog

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.telephony.SubscriptionManager
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import sb.app.messageschedular.BR
import sb.app.messageschedular.R
import sb.app.messageschedular.databinding.ChangeSimDataBinding
import sb.app.messageschedular.navigator.MessageNavigator
import sb.app.messageschedular.viewmodel.MessageScheduleViewModel


@AndroidEntryPoint
class SimDialog : BaseDialog<ChangeSimDataBinding, MessageScheduleViewModel>() {


    private val mViewModel : MessageScheduleViewModel by activityViewModels<MessageScheduleViewModel>()
     private var mDataBinding : ChangeSimDataBinding?=null

    override fun getLayoutId(): Int = R.layout.sim_dialog

    override fun getBindingVariable(): Int  =BR.viewModel

    override fun getViewModel(): MessageScheduleViewModel =mViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDataBinding =getDataBinding()

        requestSmsPermission(Manifest.permission.READ_PHONE_STATE)




        lifecycleScope.launch {

            mViewModel.closeDialog.collect {

                if(it)dismiss()

            }


        }

        mViewModel.uiSearchState.observe(viewLifecycleOwner){

        }

    }




    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts
            .RequestPermission()){isGranted ->

        if(isGranted){
            checkSubscription()
        }else{


            val alertDialogBuilder =    MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle("Permission")
            alertDialogBuilder.setMessage("You must provide permission to access the app")


            alertDialogBuilder.setPositiveButton(R.string.settings , DialogInterface.OnClickListener {
                    dialog, which ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", "sb.app.messageschedular", null)
                intent.data = uri
                startActivity(intent)

            })
            alertDialogBuilder.setNegativeButton(R.string.canceled , DialogInterface.OnClickListener {
                    dialog, which ->


            })

            alertDialogBuilder.show()


        }



    }



      fun requestSmsPermission(permission :String ){



        when{
            ContextCompat.checkSelfPermission(requireContext() ,
                permission) == PackageManager.PERMISSION_GRANTED ->{
                checkSubscription()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                permission)->{

                requestPermissionLauncher.launch(permission)
            }

            else ->{
                requestPermissionLauncher.launch(permission)

            }
        }




    }

    @SuppressLint("MissingPermission")
    private fun checkSubscription(){
       val subscriptionManager = requireContext().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
              if(subscriptionManager.activeSubscriptionInfoCount>0){

             val activeSubscriptionList =     subscriptionManager.activeSubscriptionInfoList


                  mViewModel.setSubscription(activeSubscriptionList)

              }





//        var subscriptionManager  = requireContext()
//            .getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
//
// val suscription =       subscriptionManager.activeSubscriptionInfoList
//
//        for(ids  in   suscription){
//
//            println("Sim"+ids.displayName)
//            println("Sim"+ids.subscriptionId)
//
//        }

    }





}