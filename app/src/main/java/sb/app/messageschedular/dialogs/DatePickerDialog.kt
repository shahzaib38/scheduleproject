package sb.app.messageschedular.dialogs
//
//import android.app.DatePickerDialog
//import android.content.Context
//import android.widget.DatePicker
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.Button
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import java.util.*
//
//
//@Composable
//fun DatePickerDialog(context: Context= LocalContext.current ,show :Boolean) {
//
//
//    val year: Int
//    val month: Int
//    val day: Int
//
//    val calendar = Calendar.getInstance()
//    year = calendar.get(Calendar.YEAR)
//    month = calendar.get(Calendar.MONTH)
//    day = calendar.get(Calendar.DAY_OF_MONTH)
//    calendar.time = Date()
//
//    val date = remember { mutableStateOf("") }
//    val datePickerDialog = DatePickerDialog(
//        context,
//        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
//            date.value = "$dayOfMonth/$month/$year"
//        }, year, month, day
//    )
//}