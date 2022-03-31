package sb.app.messageschedular.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import sb.app.messageschedular.BR
import sb.app.messageschedular.R
import sb.app.messageschedular.databinding.TypeMessageDialogDataBinding
import sb.app.messageschedular.viewmodel.MessageListViewModel
import sb.app.messageschedular.viewmodel.MessageScheduleViewModel

@AndroidEntryPoint
class TypeMessageDialog : BaseDialog<TypeMessageDialogDataBinding, MessageScheduleViewModel>() {

    private val mViewModel  : MessageScheduleViewModel by viewModels<MessageScheduleViewModel>()
    private var mDataBinding : TypeMessageDialogDataBinding?=null


    override fun getLayoutId(): Int  = R.layout.type_message_dialog

    override fun getBindingVariable(): Int  =BR.viewModel

    override fun getViewModel(): MessageScheduleViewModel  =mViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mDataBinding =   getDataBinding()


        mDataBinding?.close?.setOnClickListener {

            dismiss()

        }


    }


}