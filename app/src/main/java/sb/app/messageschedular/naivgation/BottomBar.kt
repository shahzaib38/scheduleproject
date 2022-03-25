package sb.app.messageschedular.naivgation
//
//import androidx.compose.foundation.layout.RowScope
//import androidx.compose.material.*
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavGraph.Companion.findStartDestination
//import androidx.navigation.NavHostController
//
//
//@Composable
//fun BottomBar(
//    messageSchedulerState:    MessageSchedulerState,
//    onClick: (String ) -> Unit
//)
//{
//
//
//
//    BottomNavigation {
//        messageSchedulerState.screens.forEach { screen ->
//            AddItem(
//                screen = screen,
//                currentDestination = messageSchedulerState.currentRoute?: BottomBarScreen.Sms.route
//            ,
//                onClick = onClick
//            )
//        }
//    }
//}
//
//@Composable
//fun RowScope.AddItem(
//    screen: BottomBarScreen,
//    currentDestination :String,
//    onClick :(String )->Unit
//) {
//    BottomNavigationItem(
//        label = {
//            Text(text = screen.title)
//        },
//        icon = {
//            Icon(
//                imageVector = screen.icon,
//                contentDescription = "Navigation Icon"
//            )
//        },
//        selected = currentDestination == screen.route
//        ,
//        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
//        onClick = {onClick.invoke(screen.route)}
//    )
//}
//
//
//
//
//
//
