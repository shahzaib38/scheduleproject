package sb.app.messageschedular.ui.actiivities

import android.os.Bundle

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseActivity<VDB :ViewDataBinding,VM :ViewModel> : AppCompatActivity(){


    private var mViewDataBinding :VDB?=null
    private var mViewModel :VM?=null

     abstract  fun getDataBindingVariale() :Int

   fun getDataBinding():VDB?{

      return mViewDataBinding }

    @LayoutRes
    abstract fun getLayoutId():Int

   private  fun performDataBinding(){

     mViewDataBinding =  DataBindingUtil.setContentView(this ,getLayoutId())

       val viewModel =if(mViewModel==null)getViewModel()else mViewModel


       mViewDataBinding?.let {


           it.setVariable(getDataBindingVariale() ,viewModel)

           it.executePendingBindings()

       }


    }

    abstract fun getViewModel(): VM?


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performDataBinding()
    }








}