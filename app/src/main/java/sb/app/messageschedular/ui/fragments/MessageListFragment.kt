package sb.app.messageschedular.ui.fragments


import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import sb.app.messageschedular.BR
import sb.app.messageschedular.R
import sb.app.messageschedular.adapter.MessageListAdapter
import sb.app.messageschedular.databinding.MessageDataBinding
import sb.app.messageschedular.enums.ListEnum
import sb.app.messageschedular.model.*
import sb.app.messageschedular.navigator.ListNavigator
import sb.app.messageschedular.ui.actiivities.MainActivity
import sb.app.messageschedular.viewmodel.MessageListViewModel


@AndroidEntryPoint
class MessageListFragment : BaseFragment<MessageDataBinding, MessageListViewModel>() , ListNavigator {

    private val mViewModel : MessageListViewModel by viewModels<MessageListViewModel>()


    private var mMessageBinding : MessageDataBinding?=null
    private var mActivity : MainActivity?=null
      private var mAdapter : MessageListAdapter?=null

    override fun getLayoutId(): Int =R.layout.message_list_layout

    override fun getBindingVariable(): Int =BR.viewModel

    override fun getViewModel(): MessageListViewModel = mViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mMessageBinding =   getDataBinding()
         mViewModel.setNavigator(this )
        mActivity =    getBaseActivity() as MainActivity



    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mMessageBinding = getDataBinding()
         mAdapter = MessageListAdapter(messageScheduleViewModel = mViewModel)

        mMessageBinding?.messageListId?.apply {

            val mLayoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false)

            val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider)!!)

            this.addItemDecoration(itemDecorator)

            layoutManager = mLayoutManager
            adapter = mAdapter

        }

      mViewModel.collectUserMessages()



        mViewModel.smsList.observe(viewLifecycleOwner) {messageList ->

            if(messageList!=null && messageList.isNotEmpty()) {

                mMessageBinding?.messageListId?.visibility =View.VISIBLE
                mMessageBinding?.nomessageId?.visibility =View.GONE

                mAdapter?.submitList(messageList)

            }else{
                mMessageBinding?.messageListId?.visibility =View.GONE
                mMessageBinding?.nomessageId?.visibility =View.VISIBLE

            }

        }




        mMessageBinding?.floatingActionButton?.setOnClickListener {

            smsSchedule()
        }


        mActivity?.window?.statusBarColor =ContextCompat.getColor(requireContext(), R.color.message_background_color)


        mViewModel.updateVisibililty(ListEnum.SELECTALL)

    }



    private fun smsSchedule(){

       val action =     MessageListFragmentDirections.actionMessageLayoutIdToSmsFragment()

        mMessageBinding?.root?.findNavController()?.navigate(action)

    }


    override fun delete(message: Message) {
        mActivity?.delete(message) }

    override fun changeAdapter() {
        val  listAdapter = mAdapter ?: return
        when(listAdapter.getStatus()) {
            ListEnum.UNSELECT -> {
                listAdapter.unselect() }
            ListEnum.SELECTALL -> {
                listAdapter.selectAll() }

            ListEnum.DELETE ->{
                listAdapter.deleteAll() } }


    }

    override fun showDialog(message: Message) {

        println("show Dialog "+message)
   val action =     MessageListFragmentDirections.actionMessageLayoutIdToMessageListDialogId(message)

        findNavController().navigate(action )


    }
}