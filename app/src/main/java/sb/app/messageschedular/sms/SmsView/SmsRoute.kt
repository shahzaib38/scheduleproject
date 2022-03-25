package sb.app.messageschedular.sms.SmsView

//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.LinearProgressIndicator
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import sb.app.messageschedular.R
//import sb.app.messageschedular.model.Contact
//import sb.app.messageschedular.scheduleUI.CustomerView
//import sb.app.messageschedular.scheduleUI.ListView
//import sb.app.messageschedular.util.CalenderUi
//import sb.app.messageschedular.util.SmsUiState
//import sb.app.messageschedular.viewmodel.MessageScheduleViewModel
//
//@Composable
//fun  SmsRoute(viewModel: MessageScheduleViewModel= hiltViewModel()){
//
//  val smsUiState by  viewModel.uiSearchState.collectAsState()
//
//    SmsScreen(smsUiState = smsUiState,
//        onValueChange =viewModel::onValueChanged,
//        onSearchFocusedChange =viewModel::onSearchFocusedChange
//        ,removeContact = viewModel::removeContact
//        ,contactClicked = viewModel::addContact
//        ,messageValueChange = viewModel::messageValueChange)
//
//
//
//    LaunchedEffect(key1 = smsUiState.searchInput){
//        viewModel.search(smsUiState.searchInput) }
//
//
//}
//
//@Composable
//fun SmsScreen(smsUiState: SmsUiState ,
//              onValueChange :(String )->Unit,
//
//              onSearchFocusedChange :(Boolean)->Unit ,
//              removeContact : (Int)->Unit,
//              contactClicked :(sb.app.messageschedular.contact_list.Contact)->Unit ,
//              messageValueChange :(String )->Unit
//){
//
//
//    if(smsUiState.isLoading) {
//        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
//    }
//
//    Column(modifier= Modifier
//        .padding(all = 16.dp)
//        .fillMaxWidth()) {
//
//
//        ContactLayout(
//            searchInput =smsUiState.searchInput,
//            onValueChanged =onValueChange,
//            placeHolderText = stringResource(id = R.string.search),
//            onSearchFocusedChange = onSearchFocusedChange ,
//            contact = smsUiState.selectedList,
//            removeContact = removeContact         )
//
//        Box(modifier = Modifier.fillMaxSize()) {
//
//            CustomSmsView(messageInput =smsUiState.messageInput  ,
//                messageValueChange = messageValueChange)
//            ListView(smsUiState.contactList, onItemClicked =contactClicked )
//
//        }
//
//
//
//
//
//
//
//    } }
//
//@Composable
//fun CustomSmsView(messageInput :String ,messageValueChange :(String )->Unit ){
//    Column {
//
//        SmsSpacer()
//        MessageLayout(messageInput =messageInput, onValueChanged = messageValueChange)
//        SmsSpacer()
//        DateAndTimeLayout(calenderUi = CalenderUi("3:34 PM","21-2-2022"),
//        timeClick = { }) {
//    }
//
//    }
//
//
//}
//
//@Composable
//fun SmsSpacer(){
//    Spacer(modifier=Modifier.height(8.dp ))
//
//
//}