package sb.app.messageschedular.two_way_binding

import android.telephony.SubscriptionInfo
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sb.app.messageschedular.adapter.ContactAdapter
import sb.app.messageschedular.adapter.SelectedAdapter
import sb.app.messageschedular.adapter.SubscriptionInfoAdapter
import sb.app.messageschedular.model.Contact
import sb.app.messageschedular.viewmodel.MessageScheduleViewModel


@BindingAdapter("app:updateContact", "app:viewModel")

 fun setContactList(
    recyclerView :RecyclerView, contactList   :List<Contact>?
    , mViewModel : MessageScheduleViewModel
)
{
    val   mAdapter = ContactAdapter(messageScheduleViewModel = mViewModel)
    val mLayoutManager = LinearLayoutManager(recyclerView.context , LinearLayoutManager.VERTICAL, false)
    recyclerView.layoutManager = mLayoutManager
    recyclerView.adapter = mAdapter

    if(contactList !=null && contactList.isNotEmpty()) {
        mAdapter?.submitList(ArrayList(ArrayList(contactList))) }

    else { mAdapter?.submitList(emptyList()) }

}




@BindingAdapter("app:updateSubscriptionInfo", "app:viewModel")
fun setSubscriptionList(
    recyclerView :RecyclerView, subscriptionInfoList   :List<SubscriptionInfo>?
    , mViewModel : MessageScheduleViewModel
) {

    val mAdapter = SubscriptionInfoAdapter(messageScheduleViewModel = mViewModel)
    val mLayoutManager =
        LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
    recyclerView.layoutManager = mLayoutManager
    recyclerView.adapter = mAdapter

    if (subscriptionInfoList != null && subscriptionInfoList.isNotEmpty()) {
        mAdapter?.submitList(ArrayList(ArrayList(subscriptionInfoList)))
    } else {
        mAdapter?.submitList(emptyList())
    }




}








@BindingAdapter("app:update", "app:viewModel")
fun setSelectedList(
    recyclerView :RecyclerView, contactSet :List<Contact>?
    , mViewModel : MessageScheduleViewModel
)
{
    val   mAdapter = SelectedAdapter(messageScheduleViewModel = mViewModel)
    val mLayoutManager = LinearLayoutManager(recyclerView.context , LinearLayoutManager.HORIZONTAL, false)
    recyclerView.layoutManager = mLayoutManager
    recyclerView.adapter = mAdapter

    val list =contactSet?.toList()
    if(list !=null && list.isNotEmpty()) {
        mAdapter?.submitList(ArrayList(ArrayList(list))) }

    else { mAdapter?.submitList(emptyList()) }

}


