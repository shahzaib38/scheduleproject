package sb.app.messageschedular.utils
//
//import androidx.compose.material.Icon
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material3.ExtendedFloatingActionButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import sb.app.messageschedular.R
//
//
//@Composable
//fun MessageFloatingButton(onFabClick :()->Unit ){
//
//    ExtendedFloatingActionButton(text = {
//        SimpleFloatText(text = stringResource(id = R.string.schedule)) },
//        onClick =onFabClick,
//        icon = { MessageIcon()})
//}
//
//
//@Composable
//fun MessageIcon(){
//
//    Icon(painter = painterResource(id = R.drawable.message_icon)
//        ,contentDescription = stringResource(id =R.string.message_icon))
//
//}
//
//@Composable
//fun SimpleFloatText(text:String ){
//
//    Text(text =text,
//        style = MaterialTheme.typography.subtitle2)
//
//}