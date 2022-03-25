package sb.app.messageschedular.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import sb.app.messageschedular.BR
import sb.app.messageschedular.R
import sb.app.messageschedular.databinding.MessageDialogDataBinding
import sb.app.messageschedular.model.Message
import sb.app.messageschedular.viewmodel.MessageListViewModel

@AndroidEntryPoint
class MessageListDialog  : BaseDialog<MessageDialogDataBinding,MessageListViewModel>() {


    private val navArgs : MessageListDialogArgs by navArgs()
    private val mViewModel  : MessageListViewModel by viewModels<MessageListViewModel>()

    private var mDataBinding : MessageDialogDataBinding?=null

    override fun getLayoutId(): Int = R.layout.message_list_dialog

    override fun getBindingVariable(): Int =BR.viewModel
    override fun getViewModel(): MessageListViewModel = mViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDataBinding =getDataBinding()


       val message = navArgs.message

        println("message in deilaog"+message )
        mDataBinding?.message = message
        mDataBinding?.cancelId?.setOnClickListener {
            dismiss() }

    }



}