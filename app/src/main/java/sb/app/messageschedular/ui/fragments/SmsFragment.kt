package sb.app.messageschedular.ui.fragments

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.format.DateFormat.is24HourFormat
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import sb.app.messageschedular.BR
import sb.app.messageschedular.R
import sb.app.messageschedular.databinding.SmsDataBinding
import sb.app.messageschedular.model.Sms
import sb.app.messageschedular.navigator.MessageNavigator
import sb.app.messageschedular.ui.actiivities.MainActivity
import sb.app.messageschedular.viewmodel.MessageScheduleViewModel
import sb.app.messageschedular.viewmodel.TodoViewModel


@AndroidEntryPoint
class SmsFragment : BaseFragment<SmsDataBinding, MessageScheduleViewModel>(),MessageNavigator{



    private val mViewModel : MessageScheduleViewModel by viewModels<MessageScheduleViewModel>()
    private var mDataBinding : SmsDataBinding?=null


    private var mActivity : MainActivity?=null
    private val todoViewModel : TodoViewModel by activityViewModels<TodoViewModel>()


    override fun getLayoutId(): Int = R.layout.sms_fragment
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getViewModel(): MessageScheduleViewModel = mViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDataBinding =   getDataBinding()
        mViewModel.setNavigator(this)

        mActivity =    getBaseActivity() as MainActivity

        mActivity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.appbar_color)


        mViewModel.messageInput.observe(viewLifecycleOwner){

            println("value "+it )

        }

        lifecycleScope.launch {
            mViewModel.smsError.collect {

                Snackbar.make(mDataBinding!!.root ,it ,Snackbar.LENGTH_LONG).show()

            }

        }

        todoViewModel.todoList.observe(viewLifecycleOwner){list->

            if(list!=null && list.isNotEmpty()){

             val todoString =   if(list.size>1) {
                    String.format("%d items",list.size)
                }else{
                 String.format("%d todo item",list.size) }

                mViewModel.addTodoList(todoString)
            }else{
                mViewModel.addTodoList("Todo List")


            }

        }




//        mViewModel.subscriptionInfoDetial.observe(viewLifecycleOwner){
//
//
//            println("Value "+it )
//        }

    }




    private fun timePickerDialog(){


      val is24HourFormat =   is24HourFormat(requireContext())
        val timeFormat = if(is24HourFormat)TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val picker = MaterialTimePicker.Builder()
            .setHour(12).setMinute(30).setTimeFormat(timeFormat).build()

        picker.addOnPositiveButtonClickListener {
            mViewModel.updateTime(picker.hour , picker.minute  ,is24HourFormat = is24HourFormat)}

        picker.addOnCancelListener {   }

            picker.show(childFragmentManager ,"timePicker")

    }

    override fun changeTime() {
        timePickerDialog()
    }

    override fun changeDate() {
      val datePicker =  MaterialDatePicker.Builder.datePicker().build()
        datePicker.addOnPositiveButtonClickListener {date:Long ->
            mViewModel.updateDate(date) }
        datePicker.show(childFragmentManager ,"DatePicker") }

    override fun scheduleService(sms: Sms) {
        mActivity?.scheduleService(sms)
        this.findNavController().navigateUp()



    }

    override fun openTypeMessageDialog() {

//      val action=  SmsFragmentDirections.actionSmsFragmentToTypeMessageFragmentId()
//        findNavController().navigate(action)

    }


    /*************** Permissoin ******************
     *
     */

//
//    private val requestPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts
//        .RequestPermission()){isGranted ->
//
//        if(isGranted){
//
//        }else{
//
//
//            val alertDialogBuilder =    MaterialAlertDialogBuilder(requireContext())
//            alertDialogBuilder.setTitle("Permission")
//            alertDialogBuilder.setMessage("You must provide permission to access the app")
//
//
//            alertDialogBuilder.setPositiveButton(R.string.settings , DialogInterface.OnClickListener {
//                    dialog, which ->
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                val uri: Uri = Uri.fromParts("package", "sb.app.messageschedular", null)
//                intent.data = uri
//                startActivity(intent)
//
//            })
//            alertDialogBuilder.setNegativeButton(R.string.canceled , DialogInterface.OnClickListener {
//                    dialog, which ->
//
//
//            })
//
//            alertDialogBuilder.show()
//
//
//        }
//
//
//
//    }


//
//   override  fun requestSmsPermission(permission :String ){
//
//
//
//        when{
//            ContextCompat.checkSelfPermission(requireContext() ,
//                permission) == PackageManager.PERMISSION_GRANTED ->{
//
//            }
//
//            ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
//                permission)->{
//
//                requestPermissionLauncher.launch(permission)
//            }
//
//            else ->{
//                requestPermissionLauncher.launch(permission)
//
//            }
//        }
//
//
//
//
//    }

    override fun changeSim() {

//        requestPhonePermission(Manifest.permission.READ_PHONE_STATE)
         }

    override fun todo() {

        val action = SmsFragmentDirections.actionSmsFragmentToTodoDialogId()
          this.findNavController().navigate(action)
    }



    /************************ Phone State Permission **********/
//
//    private val requestPhoneStateLauncher = registerForActivityResult(
//        ActivityResultContracts
//            .RequestPermission()){isGranted ->
//
//        if(isGranted){
//
//
//            val simDestination = SmsFragmentDirections.actionSmsFragmentToSimDialog()
//            this.findNavController().navigate(simDestination)
//        }else{
//
//
//            val alertDialogBuilder =    MaterialAlertDialogBuilder(requireContext())
//            alertDialogBuilder.setTitle("Permission")
//            alertDialogBuilder.setMessage("You must provide permission to access the app")
//
//
//            alertDialogBuilder.setPositiveButton(R.string.settings , DialogInterface.OnClickListener {
//                    dialog, which ->
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                val uri: Uri = Uri.fromParts("package", "sb.app.messageschedular", null)
//                intent.data = uri
//                startActivity(intent)
//
//            })
//            alertDialogBuilder.setNegativeButton(R.string.canceled , DialogInterface.OnClickListener {
//                    dialog, which ->
//
//
//            })
//
//            alertDialogBuilder.show()
//
//
//        }
//
//
//
//    }



//   private  fun requestPhonePermission(permission :String ){
//
//
//
//        when{
//            ContextCompat.checkSelfPermission(requireContext() ,
//                permission) == PackageManager.PERMISSION_GRANTED ->{
//
//
//                val simDestination = SmsFragmentDirections.actionSmsFragmentToSimDialog()
//                this.findNavController().navigate(simDestination)
//            }
//
//            ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
//                permission)->{
//
//                requestPhoneStateLauncher.launch(permission)
//            }
//
//            else ->{
//                requestPhoneStateLauncher.launch(permission)
//
//            }
//        }
//
//
//
//
//    }













}