package sb.app.messageschedular.scheduleUI
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.animateColorAsState
//import androidx.compose.animation.slideInHorizontally
//import androidx.compose.animation.slideOutHorizontally
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import sb.app.messageschedular.R
//import sb.app.messageschedular.contact_list.Contact
//import sb.app.messageschedular.naivgation.BottomBar
//import sb.app.messageschedular.naivgation.BottomNavGraph
//import sb.app.messageschedular.naivgation.MessageSchedulerState
//import sb.app.messageschedular.naivgation.rememberMessageSchedulerState
//import sb.app.messageschedular.scaffold.MessageSchedularScaffold
//import sb.app.messageschedular.sms.SmsView.ContactList
//import sb.app.messageschedular.topBar.MessageTopBar
//import sb.app.messageschedular.util.CalenderUi
//import sb.app.messageschedular.util.SmsUiState
//import sb.app.messageschedular.viewmodel.DateModel
//import sb.app.messageschedular.viewmodel.MessageScheduleViewModel
//
//
//@Composable
//fun SchedularUI(viewModel :MessageScheduleViewModel= hiltViewModel(),
//              smsScheduleNavigation : MessageSchedulerState =  rememberMessageSchedulerState())
//{
//
//
//    MessageSchedularScaffold(topBar = {
//        MessageTopBar(navigateTo ={   } ,
//            onSubmit =viewModel::submit
//
//
//        )
//                                      },
//        floatingActionButton = {
//
//        }
//
//        , bottomBar = {
//            BottomBar(smsScheduleNavigation){
//                smsScheduleNavigation.navigateTo(it) }
//
//        }
//
//    ){
//
//
//        BottomNavGraph(smsScheduleNavigation.navController)
//
//
////   SearchRoute(viewModel)
//
//    }
//
//
//}
//
//
//@Composable
//fun SearchRoute(viewModel: MessageScheduleViewModel){
//    val uiState by   viewModel.uiSearchState.collectAsState()
//
//    SchedularSearch(searchUiState = uiState ,
//        onValueChanged =viewModel::onValueChanged
//        , onSearchFocusedChange = viewModel::onSearchFocusedChange
//    , removeContact = viewModel::removeContact,
//        onItemClicked = viewModel::addContact
//        , onMessageValueChanged =viewModel::messageValueChange)
//
//    LaunchedEffect(key1 = uiState.searchInput){
//        viewModel.search(uiState.searchInput) }
//
//
//
//
//}
//
//
//
//
//
//@Composable
//fun SearchView(searchInput : String   ,
//               onValueChanged :(String )->Unit,
//               placeHolderText:String,
//               onSearchFocusedChange :(Boolean)->Unit
//               ){
//
//    println("SearchView")
//    var isFocused by remember{mutableStateOf(false)}
//    val color by animateColorAsState(targetValue =if(isFocused) Color.Blue else Color.Transparent)
//    val backgroundColor by animateColorAsState(targetValue =if(isFocused) Color.White else Color.LightGray)
//
//    TextField(value = searchInput,
//        onValueChange = onValueChanged,
//        modifier= Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp)
//            .onFocusChanged {
//                onSearchFocusedChange(it.isFocused)
//
//                isFocused = it.isFocused
//
//            }
//            .border(width = 2.dp, color = color, shape = RoundedCornerShape(14.dp))
//        , shape = RoundedCornerShape(14.dp),
//        colors =TextFieldDefaults.textFieldColors(
//            backgroundColor = backgroundColor,
//            focusedIndicatorColor = Color.Transparent,
//            unfocusedIndicatorColor = Color.Transparent,
//            cursorColor = sb.app.messageschedular.ui.theme.Primary,
//            textColor = sb.app.messageschedular.ui.theme.Primary,
//            disabledIndicatorColor = Color.Transparent,
//            placeholderColor = sb.app.messageschedular.ui.theme.Secondary
//        )
//        ,placeholder = {
//            Text(text =placeHolderText ,
//                style = MaterialTheme.typography.body2)
//
//
//                       }
//        , trailingIcon = {
//
//                         }
//
//        , singleLine = true
//
//    )
//
//
//
//}
//
//@Composable
//fun SchedularSearch(searchUiState: SmsUiState,
//                    onValueChanged: (String ) -> Unit,
//                    onSearchFocusedChange: (Boolean) -> Unit,
//                    removeContact: (Int) -> Unit,
//                    onItemClicked: (Contact) -> Unit,
//                    onMessageValueChanged: (String) -> Unit
//                    ){
//
//    println("Schedular Search")
//
//    Column(modifier = Modifier
//        .padding(all = 20.dp)
//        .fillMaxSize()) {
//
//        SearchView(searchInput = searchUiState.searchInput
//                   ,onValueChanged =onValueChanged
//                   ,placeHolderText = "Search"
//                   ,onSearchFocusedChange =onSearchFocusedChange )
//
//
//        Box(modifier = Modifier.fillMaxSize()) {
//
//                  CustomerView(uiState = searchUiState ,
//                      removeContact = removeContact, onMessageValueChanged =onMessageValueChanged )
//
//            ListView(searchUiState.contactList, onItemClicked =onItemClicked )
//
//        }
//    }
//}
//
//
//
//@Composable
//fun CustomerView(uiState: SmsUiState,
//                 removeContact: (Int) -> Unit,
//
//                 onMessageValueChanged :(String )->Unit
//                 ) {
//
//
//    Column(modifier=Modifier.fillMaxSize()) {
//        ContactList(uiState.selectedList ,
//            removeContact = removeContact)
//
//        Spacer(modifier =Modifier.height(42.dp))
//
//        MessageVie1(messageInput = uiState.messageInput
//            ,onValueChanged = onMessageValueChanged)
//
//        Spacer(modifier =Modifier.height(42.dp))
//
//        CalenderView(calenderUi = uiState.calenderUi ,timeClick = {
//
//
//        },dateClick = {
//
//
//        })
//
//
//    }
//}
//
//
//@Composable
//fun MessageVie1(messageInput :String,
//                onValueChanged: (String) -> Unit){
//
//
//    println("SearchView")
//    var isFocused by remember{mutableStateOf(false)}
//    val color by animateColorAsState(targetValue =if(isFocused) Color.Blue else Color.Transparent)
//    val backgroundColor by animateColorAsState(targetValue =if(isFocused) Color.White else Color.LightGray)
//
//
//
//
//    TextField(value = messageInput,
//        onValueChange = onValueChanged,
//        modifier= Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp)
//            .onFocusChanged {
//                isFocused = it.isFocused
//            }
//            .border(width = 2.dp, color = color, shape = RoundedCornerShape(14.dp))
//        , shape = RoundedCornerShape(14.dp),
//
//        colors =TextFieldDefaults.textFieldColors(
//            backgroundColor = backgroundColor,
//            focusedIndicatorColor = Color.Transparent,
//            unfocusedIndicatorColor = Color.Transparent,
//            cursorColor = sb.app.messageschedular.ui.theme.Primary,
//            textColor = sb.app.messageschedular.ui.theme.Primary,
//            disabledIndicatorColor = Color.Transparent,
//            placeholderColor = sb.app.messageschedular.ui.theme.Secondary
//        )
//        ,placeholder = {
//            //  Text(text =placeHolderText ,
//            //        style = MaterialTheme.typography.body2)
//
//
//        }
//        , trailingIcon = {
//
//        }
//
//        , singleLine = true,
//        maxLines = 11,
//
//    )
//
//
//}
//
//
//
//
//@Composable
//fun CalenderView(calenderUi :CalenderUi ,  timeClick :()->Unit ,
//                   dateClick :()->Unit ){
//    Row(modifier = Modifier
//        .fillMaxWidth()) {
//
//
//        DateCard(backgroundColor=Color.Red, onClick = timeClick, content =  {
//
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text(
//                    text = calenderUi.time,
//                    style = MaterialTheme.typography.h6,
//                    color = Color.Black
//                )
//            }
//
//            Box(modifier = Modifier.fillMaxSize() ,
//                contentAlignment = Alignment.BottomCenter) {
//
//                Text(text = "Time",
//                    style = MaterialTheme.typography.subtitle1,
//                    color=Color.Black)
//
//
//            }
//        }
//
//        )
//
//        DateCard(backgroundColor=Color.Red , onClick = dateClick, content =  {
//
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//
//                Text(
//                    text = calenderUi.date,
//                    style = MaterialTheme.typography.h6,
//                    color = Color.Black
//                )
//            }
//
//            Box(modifier = Modifier.fillMaxSize() ,
//                contentAlignment = Alignment.BottomCenter) {
//
//                Text(text = "Date",
//                    style = MaterialTheme.typography.subtitle1,
//                    color=Color.Black)
//
//
//            }
//        })
//
//    }
//
//}
//
//@Composable
//fun RowScope.DateCard(backgroundColor :Color  ,
//                      content : @Composable ()->Unit,
//                      onClick : ()->Unit
//                      ){
//    Card(modifier = Modifier
//        .clickable {
//            onClick()
//        }
//        .weight(1f)
//        .padding(5.dp)
//        .height(150.dp),
//        content = content,
//        shape = RoundedCornerShape(16.dp),
//        backgroundColor = backgroundColor,
//        elevation = 8.dp)
//
//}
//
//@Composable
//fun SearchItem(index :Int ,contact : Contact ,OnClick :(Int )->Unit){
//
//    Surface(
//        modifier = Modifier.wrapContentSize()
//        ,color = Color.LightGray,
//        shape = RoundedCornerShape(10.dp )
//
//    ) {
//
//        Row {
//
//            IconButton(onClick = { OnClick.invoke(index) }) {
//                Icon(
//                    painter = painterResource(id = R.drawable.close_icon),
//                    contentDescription = "contact",
//                    tint = Color.Black
//                )
//            }
//
//
//            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
//                ContactText(contact = contact.name)
//                ContactText(contact = contact.phone)
//
//
//            }
//
//        }
//    }
//}
//
//
//
//
//
//@Composable
//fun ListView(contactList :List<Contact> ,onItemClicked :(Contact ) ->Unit ){
//
//    println("ListView")
//    AnimatedVisibility(visible = contactList.isNotEmpty()) {
//
//        println("Animation Visibility")
//        LazyColumn(modifier =Modifier.background(Color.White)){
//
//            itemsIndexed(contactList){index , contact ->
//
//                ContactItem(index = index , contact = contact , OnClick =onItemClicked
//
//                )
//
//
//            }
//        }
//
//    }
//}
//
//
//
//@Composable
//fun ContactItem(index :Int ,contact : Contact ,OnClick :(Contact )->Unit){
//
//    Row( verticalAlignment = Alignment.CenterVertically ,modifier = Modifier
//        .fillMaxWidth()
//        .padding(12.dp)
//        .clickable { OnClick.invoke(contact) }){
//        Icon(painter = painterResource(id = R.drawable.contact_icon)
//            , contentDescription ="contact",tint=Color.Black )
//
//        Column(modifier =Modifier.padding(horizontal = 20.dp)) {
//            ContactText(contact =contact.name)
//            ContactText(contact =contact.phone)
//
//        }
//
//    } }
//
//@Composable
//fun ContactText(contact : String){
//    Text(text = contact,
//        style =MaterialTheme.typography.subtitle2
//        , textAlign = TextAlign.Start,
//        color = Color.Black
//
//    )
//
//
//}
//
//
//@Composable
//fun CustomDate(date : DateModel, OnClick :()->Unit ){
//
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .border(1.dp, Color.White, shape = RoundedCornerShape(16.dp))
//            .background(Color.White)
//            .padding(
//                vertical = 16.dp,
//                horizontal = 8.dp
//            ),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    )
//    {
//        Text(
//            text = "${date.dayOfMonth}/${date.month}/${date.year}", style = MaterialTheme.typography.h6,
//            color = Color.Black)
//        Button(onClick = OnClick
//            , shape = RoundedCornerShape(20.dp))
//        { Text(text = stringResource(id = R.string.date)) }
//
//
//    }
//
//
//
//
//
//}
//
//
//
//
//
//@Composable
//@Preview(showBackground = true)
//fun DefaultMessageScheduleUI(){
//    SchedularUI()
//
//}