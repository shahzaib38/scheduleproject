package sb.app.messageschedular.ui.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat.is24HourFormat
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import sb.app.messageschedular.BR
import sb.app.messageschedular.R
import sb.app.messageschedular.databinding.SmsDataBinding
import sb.app.messageschedular.model.Message
import sb.app.messageschedular.model.Sms
import sb.app.messageschedular.navigator.MessageNavigator
import sb.app.messageschedular.service.SmsService
import sb.app.messageschedular.ui.actiivities.MainActivity
import sb.app.messageschedular.ui.dialog.TypeMessageDialog
import sb.app.messageschedular.util.Permissions
import sb.app.messageschedular.viewmodel.MessageScheduleViewModel


@AndroidEntryPoint
class SmsFragment : BaseFragment<SmsDataBinding, MessageScheduleViewModel>(),MessageNavigator{



    private val mViewModel : MessageScheduleViewModel by viewModels<MessageScheduleViewModel>()
    private var mDataBinding : SmsDataBinding?=null


    private var mActivity : MainActivity?=null


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
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {


        if (requestCode == Permissions.READ_CONTACT_CODE) {
            // Request for camera permission.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
             //   layout.showSnackbar(R.string.camera_permission_granted, Snackbar.LENGTH_SHORT)
            //    startCamera()

            } else {
                // Permission request was denied.
             //   layout.showSnackbar(R.string.camera_permission_denied, Snackbar.LENGTH_SHORT)

                activity?.finish()


            }
        }
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
        this.findNavController().navigate(R.id.action_smsFragment_to_message_layoutId) }

    override fun openTypeMessageDialog() {

//      val action=  SmsFragmentDirections.actionSmsFragmentToTypeMessageFragmentId()
//        findNavController().navigate(action)

    }


}