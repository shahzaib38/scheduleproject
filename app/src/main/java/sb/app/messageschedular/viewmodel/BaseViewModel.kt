package sb.app.messageschedular.viewmodel

import androidx.lifecycle.ViewModel
import sb.app.messageschedular.navigator.Navigator
import sb.app.messageschedular.repo.BaseRepository
import java.lang.ref.WeakReference

abstract class BaseViewModel<N : Navigator>  constructor(val baseDataManager : BaseRepository) :  ViewModel(){



    lateinit var  mNavigator  : WeakReference<N>

    fun setNavigator(mNavigator :N){
        this.mNavigator = WeakReference<N>(mNavigator) }

    open fun getNavigator() :N{

        return mNavigator.get()!! }




}