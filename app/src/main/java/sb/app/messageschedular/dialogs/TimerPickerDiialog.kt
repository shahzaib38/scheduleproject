package sb.app.messageschedular.dialogs

//import android.app.TimePickerDialog
//import android.content.Context
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.Button
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Surface
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import sb.app.messageschedular.viewmodel.MessageScheduleViewModel
//import sb.app.messageschedular.viewmodel.Time
//import java.util.*

//@Composable
//fun TimerPickerDialog(messageScheduleViewModel: MessageScheduleViewModel = hiltViewModel()){
//
//    val calendar = Calendar.getInstance()
//    val hour = calendar[Calendar.HOUR_OF_DAY]
//    val minute = calendar[Calendar.MINUTE]
//
//
//
//    val timePickerDialog = TimePickerDialog(
//        LocalContext.current,
//        {_, hour : Int, minute: Int ->
//           // time.value = "$hour:$minute"
//            messageScheduleViewModel.time.value =Time(minute ,hour)
//
//        }, hour, minute, false
//    )
//
//  val shouldShow =  messageScheduleViewModel.showTimePicker.value
//    if(shouldShow)
//        timePickerDialog.show()
//}