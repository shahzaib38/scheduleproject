package sb.app.messageschedular.util
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.BasicText
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.SolidColor
//import androidx.compose.ui.layout.layoutId
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextLayoutResult
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.constraintlayout.compose.ConstraintLayout
//import androidx.constraintlayout.compose.ConstraintSet
//import androidx.constraintlayout.compose.Dimension
//import androidx.hilt.navigation.compose.hiltViewModel
//import sb.app.messageschedular.contact_list.Contact
//import sb.app.messageschedular.surface.MessageScheduleSurface
//import sb.app.messageschedular.viewmodel.MessageScheduleViewModel
//
//
//@Composable
//@Preview
//fun SearchBarPreview(){
//
//
//
//
//}
//
//@Composable
//fun SearchBar(viewModel :MessageScheduleViewModel = hiltViewModel()){
//
//    MessageTextField(query =viewModel.query.value,
//        onQueryChange =viewModel::onValueChange,
//        onSearchFocusedChanged ={it->
//            viewModel.focused.value=it
//
//        })
//
//
//    //LaunchedEffect(key1 =viewModel.){
//
//   //     viewModel.search(viewModel.query.value)
//
// //   }
//
//    when(viewModel.searchDisplay){
//        MessageScheduleViewModel.SearchDisplay.Nothing->{
//
//            viewModel.clean()
//        }
//
//        MessageScheduleViewModel.SearchDisplay.NoResult->{
//            viewModel.clean()
//
//        }
//
//        MessageScheduleViewModel.SearchDisplay.Results ->{
//
//            viewModel.searchList.value =viewModel.result.value
//
//        }
//
//
//
//    }
//
//
//}
//


//
//@Composable
//fun MessageTextField( modifier :Modifier = Modifier , query : TextFieldValue,
//                     onQueryChange: (TextFieldValue) -> Unit,
//                     onSearchFocusedChanged: (Boolean) -> Unit){
//
//
//    OutlinedTextField(value = query ,
//        onValueChange = onQueryChange ,
//        modifier = modifier
//            .fillMaxWidth()
//            .onFocusChanged { focusState ->
//                onSearchFocusedChanged(focusState.isFocused) },
//        textStyle = androidx.compose.material.MaterialTheme.typography.subtitle1,
//        singleLine = true,
//        label = {
//            Text(text = "write a number") },
//        shape = RoundedCornerShape(8.dp)
//        ,colors = TextFieldDefaults.outlinedTextFieldColors(
//            backgroundColor = Color.White,
//            unfocusedBorderColor = Color.Transparent,
//            focusedBorderColor = Color.Red,
//            focusedLabelColor = Color.Green,
//            disabledTrailingIconColor = Color.Black,
//            placeholderColor = Color.LightGray
//
//            )
//    , placeholder = {
//        Text("number or name")
//        }
//
//    )
//
//
//
//
//}
//
//
//

