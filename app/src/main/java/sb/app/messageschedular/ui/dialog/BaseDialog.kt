package sb.app.messageschedular.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModel

abstract class BaseDialog<VDB:ViewDataBinding ,VM:ViewModel> : DialogFragment() {


    @LayoutRes
 abstract   fun getLayoutId():Int

    private var mDataBinding : VDB?=null


    fun getDataBinding() =mDataBinding

 abstract   fun getBindingVariable():Int

 private var mViewModel : VM?=null

abstract    fun getViewModel():VM
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      mDataBinding =  DataBindingUtil.inflate(inflater,getLayoutId(),container,false )


        var viewModel =if(mViewModel==null)getViewModel() else mViewModel
        mDataBinding?.run {

            this.setVariable(getBindingVariable(),viewModel)

            this.lifecycleOwner =this@BaseDialog
            this.executePendingBindings()
        }

        return mDataBinding?.root
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = RelativeLayout(activity)

        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)

        if (dialog != null) {

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        }

        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    } }