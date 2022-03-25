package sb.app.messageschedular.naivgation
//
//import android.content.res.Resources
//import androidx.compose.material.ScaffoldState
//import androidx.compose.material.rememberScaffoldState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.navigation.NavGraph.Companion.findStartDestination
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.rememberNavController
//import kotlinx.coroutines.CoroutineScope
//import sb.app.messageschedular.model.SnackbarManager
//
//
//object MainDestinations {
//    const val SMS_ROUTE = "Sms"
//    const val EMAIL_ROUTE = "Email"
//
//}
//
//
//@Composable
//fun rememberMessageSchedulerState(
//    scaffoldState: ScaffoldState = rememberScaffoldState(),
//    navController: NavHostController = rememberNavController(),
//    snackbarManager: SnackbarManager = SnackbarManager,
//  //  resources: Resources = resources(),
//    coroutineScope: CoroutineScope = rememberCoroutineScope()
//) =
//    remember(scaffoldState, navController, snackbarManager,
//
//        //resources,
//
//        coroutineScope) {
//
//        MessageSchedulerState(scaffoldState, navController, snackbarManager,
//            //resources,
//
//            coroutineScope)
//    }
//
//class MessageSchedulerState(   val scaffoldState: ScaffoldState,
//                               val navController: NavHostController,
//                               private val snackbarManager: SnackbarManager,
//              //                 private val resources: Resources,
//                               coroutineScope: CoroutineScope){
//
//
//
//    val screens :List<BottomBarScreen> get() =  listOf(
//     BottomBarScreen.Sms,
//       BottomBarScreen.Email)
//    val currentRoute: String?
//        get() = navController.currentDestination?.route
//
//    fun upPress() {
//        navController.navigateUp()
//    }
//
//    fun navigateTo(route :String ){
//
//              navController.navigate(route) {
//              popUpTo(navController.graph.findStartDestination().id)
//            launchSingleTop = true
//
//
//            }
//
//    }
//
//
//}
