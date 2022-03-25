package sb.app.messageschedular.sms.SmsView
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.animateColorAsState
//import androidx.compose.animation.slideInHorizontally
//import androidx.compose.animation.slideOutHorizontally
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.LazyVerticalGrid
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.SmallTopAppBar
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import sb.app.messageschedular.R
//import sb.app.messageschedular.contact_list.Contact
//import sb.app.messageschedular.scheduleUI.SearchItem
//import sb.app.messageschedular.ui.theme.Primary
//import sb.app.messageschedular.ui.theme.Secondary
//import sb.app.messageschedular.util.CalenderUi
//
///******* Sms Top Bar********/
//@Composable
//fun SmsTopBar(onCLick :()->Unit ,showProgress :Boolean){
//
//    SmallTopAppBar(title = {
//        Text(text = stringResource(id =R.string.sms),
//            style =MaterialTheme.typography.h6) }
//    , navigationIcon = {
//            IconButton(onClick = onCLick, modifier = Modifier.size(24.dp)) {
//                Icon(
//                    imageVector = Icons.Default.ArrowBack,
//                    contentDescription = stringResource(id = R.string.back_arrow))
//            } },
//               actions = {
//            if(showProgress) {
//                CircularProgressIndicator(color = Color.Black)
//
//            }
//               }) }
//
///********* MessageLayout **********/
//@Composable
//fun MessageLayout(messageInput: String ,onValueChanged: (String) -> Unit){
//    Column(modifier=Modifier.fillMaxWidth()) {
//
//        Text(text = stringResource(id = R.string.message_text),
//            style =MaterialTheme.typography.subtitle1)
//
//        MessageView(messageInput =messageInput, onValueChanged =onValueChanged) }
//
//
//}
//
//
//
//@Composable
//fun MessageView(messageInput :String,
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
//            .height(260.dp)
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
//            cursorColor =Primary,
//            textColor = Primary,
//            disabledIndicatorColor = Color.Transparent,
//            placeholderColor =Secondary
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
//
//        )
//
//
//}
//
//
//
//
///********* Message Layout End ******/
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
///**** COntact Layout *****/
//@Composable
//fun ContactLayout(searchInput : String,
//                  onValueChanged :(String )->Unit,
//                  placeHolderText:String,
//                  onSearchFocusedChange :(Boolean)->Unit,
//                  contact: List<Contact>,
//                  removeContact: (Int) -> Unit
//                  ){
//    Column(modifier=Modifier.fillMaxWidth()) {
//
//        Text(text = stringResource(id = R.string.contact_item),
//            style =MaterialTheme.typography.subtitle1)
//
//        SearchLayout(
//            searchInput =searchInput,
//            onValueChanged =onValueChanged,
//            placeHolderText =placeHolderText,
//            onSearchFocusedChange =onSearchFocusedChange
//        )
//
//        ContactList(selectedList =contact, removeContact =removeContact )
//
//    }
//}
//
///**** SelectedList **************/
//
//
//@Composable
//fun ContactList(selectedList  :List<Contact>, removeContact :(Int)->Unit ){
//
//    AnimatedVisibility(visible =selectedList.isNotEmpty()
//        , enter = slideInHorizontally()
//        , exit = slideOutHorizontally()
//    )
//    {
//
//        SelectedList(selectedList, OnClick = removeContact)
//
//    }
//
//
//}
//
//@Composable
//fun SelectedList(contact : List<Contact>, OnClick: (Int) -> Unit){
//
//
//    LazyRow(modifier =Modifier.fillMaxWidth()){
//
//
//        itemsIndexed(contact){index ,contact->
//
//            if(index>0){
//                Spacer(modifier =Modifier.width(8.dp ))
//            }
//
//            SearchItem(index =index, contact =contact, OnClick =OnClick)
//
//        }
//
//
//
//    } }
//
//
//
//
//
//
//
//
//
//
///******** Selected List End ************/
//
//
//@Composable
//private  fun SearchLayout(searchInput : String   ,
//               onValueChanged :(String )->Unit,
//               placeHolderText:String,
//               onSearchFocusedChange :(Boolean)->Unit
//){
//
//    println("SearchView")
//    var isFocused by remember{ mutableStateOf(false) }
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
//            .border(width = 2.dp, color = color, shape = RoundedCornerShape(8.dp))
//        , shape = RoundedCornerShape(8.dp),
//        colors = TextFieldDefaults.textFieldColors(
//            backgroundColor = backgroundColor,
//            focusedIndicatorColor = Color.Transparent,
//            unfocusedIndicatorColor = Color.Transparent,
//            cursorColor = Primary,
//            textColor = Primary,
//            disabledIndicatorColor = Color.Transparent,
//            placeholderColor = Secondary)
//        ,placeholder = {
//            Text(text =placeHolderText ,
//                style = MaterialTheme.typography.body2)
//                       }
//        , trailingIcon = {
//
//                         IconButton(onClick = {
//
//                         }) {
//
//                             Icon(imageVector = Icons.Default.Person,
//                                 contentDescription = stringResource(id =R.string.contact_item))
//                         }
//        }
//
//        , singleLine = true
//
//    )
//
//
//
//}
//
//
///**** Contact Layout End ****/
//
//
//
//
//@Composable
//fun DateAndTimeLayout(calenderUi : CalenderUi, timeClick :()->Unit,
//                 dateClick :()->Unit ){
//
//
//    Column {
//        Text(
//            text = stringResource(id = R.string.dateAndTime),
//            style = MaterialTheme.typography.subtitle1
//        )
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//        ) {
//
//
//            DateCard(backgroundColor = Color.LightGray,
//                onClick = timeClick,
//                content = {
//
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = calenderUi.time,
//                            style = MaterialTheme.typography.h6,
//                            color = Color.Black
//                        )
//                    }
//
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.BottomCenter
//                    ) {
//
//                        Text(
//                            text = "Time",
//                            style = MaterialTheme.typography.subtitle1,
//                            color = Color.Black
//                        )
//
//
//                    }
//                }
//
//            )
//
//            DateCard(backgroundColor = Color.LightGray, onClick = dateClick, content = {
//
//                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//
//                    Text(
//                        text = calenderUi.date,
//                        style = MaterialTheme.typography.h6,
//                        color = Color.Black
//                    )
//                }
//
//                Box(
//                    modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.BottomCenter
//                ) {
//
//                    Text(
//                        text = "Date",
//                        style = MaterialTheme.typography.subtitle1,
//                        color = Color.Black
//                    )
//
//
//                }
//            })
//
//        }
//    }
//}
//
//@Composable
//fun RowScope.DateCard(backgroundColor :Color  ,
//                      content : @Composable ()->Unit,
//                      onClick : ()->Unit){
//
//
//    Card(modifier = Modifier
//        .padding(8.dp)
//        .clickable {
//            onClick()
//        }
//        .weight(1f)
//
//        .height(120.dp),
//        content = content,
//        shape = RoundedCornerShape(16.dp),
//        backgroundColor = backgroundColor,
//        elevation = 8.dp)
//
//}
//
//
//
//
//@Preview
//@Composable
//fun ContactLayoutPreview() {
//
//    Column {
//
//
//
//    }
//
//}
//
//
//
//@Preview
//@Composable
//fun TopBarPreview(){
//
//    Column {
//
//
//        SmsTopBar(
//            onCLick = {},
//            true
//        )
//        Spacer(modifier=Modifier.height(16.dp ))
//        ContactLayout(
//            searchInput ="Shahzaib",
//            onValueChanged ={},
//            placeHolderText ="search",
//            onSearchFocusedChange ={},
//            contact = emptyList(),
//            removeContact = {}
//        )
//
//        Spacer(modifier=Modifier.height(16.dp ))
//
//        MessageLayout(messageInput="write a message" ,onValueChanged={
//
//        })
//
//}}
//
//
//
//
