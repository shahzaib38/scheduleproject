package sb.app.messageschedular.contact_list

import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.gestures.ScrollableDefaults.flingBehavior
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.shape.CornerBasedShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.ButtonDefaults
//import androidx.compose.material.Icon
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.OutlinedButton
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.layoutId
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.constraintlayout.compose.ConstraintLayout
//import androidx.constraintlayout.compose.ConstraintSet
//import androidx.constraintlayout.compose.Dimension
//import sb.app.messageschedular.surface.MessageScheduleSurface
//import kotlin.math.ln

//
//@Composable
//fun ContactList(contacts : List<Contact>,OnRemove : (Int)->Unit ){
//
//    LazyRow(modifier=Modifier , reverseLayout = false ,
//        verticalAlignment = Alignment.CenterVertically
//    , flingBehavior =  flingBehavior(
//
//        )
//    ){
//
//
//        itemsIndexed(contacts){index , contact->
//
//            if(index>0)
//                Spacer(modifier =Modifier.width(8.dp))
//            ContactChips(index = index ,
//                contact = contact ,
//                OnRemove = OnRemove)
//
//        }
//
//
//    }
//}
//
//@Composable
//fun ContactText(contactText:String ,
//                style :TextStyle = MaterialTheme.typography.h6 ,
//                textAlign: TextAlign =TextAlign.Center ){
//    Text(text =contactText ,
//        modifier=Modifier,
//        textAlign = TextAlign.Start ,
//        color =Color.Black,
//        style = style)
//}
//
//
//
//@Composable
//fun ContactChips(index:Int ,contact: Contact , OnClick :(Contact)->Unit ={},OnRemove : (Int)->Unit){
//
//
//            ContactItem(index= index, contact =contact , OnClick = OnRemove )
//
//
//
//


//        MessageScheduleSurface(modifier = Modifier
//            .clickable {
//
//                OnClick.invoke(contact)
//
//            }
//            .height(32.dp)
//            .padding(horizontal = 8.dp),
//            color = Color.White,
//            shape = RoundedCornerShape(20.dp),
//            contentColor = Color.Black,
//            elevation = 2.dp)
//        {
//            Box(modifier = Modifier) {
               // ContactItem(index= index, contact =contact , OnClick = OnRemove )
//
//            }
//
//
//
//
//        }
//
//    }
//










//
//
//@Composable
//fun ContactItem( index:Int=0 ,contact: Contact ,modifier: Modifier =Modifier  ,
//                textStyle: TextStyle =MaterialTheme.typography.overline,
//              textAlign: TextAlign = TextAlign.Center
//               ,OnClick : (Int)->Unit={}
//                ){
//
//    OutlinedButton(
//        onClick = {OnClick.invoke(index)},
//        border = BorderStroke(1.dp, Color.Red),
//        shape = RoundedCornerShape(20), // = 50% percent
//        //or shape = CircleShape
//        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
//    ){
////                IconButton(onClick = {OnClick.invoke(index)} ) {
////            Icon(imageVector =Icons.Filled.Close ,
////                contentDescription ="close button") }
//
//
//        Column(
//            modifier = modifier,
//            horizontalAlignment = Alignment.Start,
//            verticalArrangement = Arrangement.Center
//        ) {
//            ContactText(contactText = contact.name, style = textStyle, textAlign = textAlign)
//            ContactText(contactText = contact.phone, style = textStyle, textAlign = textAlign)
//        }
//    }
//
////
////    Row(modifier = Modifier.border(2.dp ,Color.White,
////        RoundedCornerShape(20.dp))
////        .background(Color.White),
////        verticalAlignment = Alignment.CenterVertically,
////        horizontalArrangement = Arrangement.Start
////    ) {
////
////        IconButton(onClick = {OnClick.invoke(index)} ) {
////            Icon(imageVector =Icons.Filled.Close ,
////                contentDescription ="close button") }
////
////
////        Column(
////            modifier = modifier,
////            horizontalAlignment = Alignment.CenterHorizontally,
////            verticalArrangement = Arrangement.Center
////        ) {
////            ContactText(contactText = contact.name, style = textStyle, textAlign = textAlign)
////            ContactText(contactText = contact.phone, style = textStyle, textAlign = textAlign)
////        }
////        Spacer(modifier =Modifier.width(8.dp))
////
////    }
//
//}
//
//
//
///** Contact Seaerch List ****/
//
//@Composable
//fun ContactSearchList(contacts : List<Contact>,
//                      OnClick :(Contact)->Unit
//){
//
//
//    LazyColumn{
//
//        items(contacts){contact ->
//
//            SearchContactItem(contact =contact ,OnClick =OnClick)
//
//        }
//    }
//}
//
//
//@Composable
//fun SearchContactItem(contact: Contact, OnClick :(Contact)->Unit
//){
//
//    Box(modifier= Modifier
//        .fillMaxWidth()
//        .padding(all = 8.dp)
//        .clickable {
//            OnClick.invoke(contact)
//
//        }) {
//    Row(modifier = Modifier.fillMaxWidth()
//    ,verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Start
//    ) {
//
//            Icon(imageVector = Icons.Filled.Person, contentDescription = "person" )
//            Spacer(modifier = Modifier.width(16.dp))
//            ContactItem(contact = contact,
//                textStyle = MaterialTheme.typography.caption,
//                textAlign = TextAlign.Start)
//
//    }
//    }
//}
//
//
