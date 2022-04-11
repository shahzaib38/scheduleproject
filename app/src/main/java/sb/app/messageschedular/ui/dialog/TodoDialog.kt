package sb.app.messageschedular.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sb.app.messageschedular.BR
import sb.app.messageschedular.R
import sb.app.messageschedular.databinding.TodoDataBinding
import sb.app.messageschedular.navigator.TodoNavigator
import sb.app.messageschedular.viewmodel.TodoViewModel

@AndroidEntryPoint
class TodoDialog : BaseDialog<TodoDataBinding,TodoViewModel>() , TodoNavigator {

    private val mViewModel : TodoViewModel by activityViewModels<TodoViewModel>()
        private var mDataBinding : TodoDataBinding?=null

    override fun getLayoutId(): Int = R.layout.todo_dialog

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): TodoViewModel = mViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       mDataBinding = getDataBinding()

        mViewModel.setNavigator(this )


    }

    override fun addItem(value: String) {
        println(value )
    }

    override fun done() {

        this.dismiss()
    }

    override fun checkStatus() {

        mDataBinding?.addItemLayoutId?.contactId?.let {editText->

          val text =  editText.text
            if(text==null)return
               // if (editText.isFocused && text.isEmpty()) {


                    editText.background =
                        ContextCompat.getDrawable(
                            editText.context,
                            R.drawable.edit_text_background_empty
                        );

                    editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);

//                }
//                else if(!editText.isFocused && text.isEmpty()){
//
//                    editText.background = ContextCompat.getDrawable(editText.context, R.drawable.edit_text_background_empty);
//                    editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
//
//                }






        }

    }

    override fun validate() {
        println("validate")
    }

}