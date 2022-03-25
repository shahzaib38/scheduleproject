package sb.app.messageschedular.scaffold
//
//import androidx.compose.foundation.layout.ColumnScope
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.material.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Shape
//import androidx.compose.ui.unit.Dp
//
//
//@Composable
//fun MessageSchedularScaffold(
//    modifier: Modifier = Modifier,
//    scaffoldState: ScaffoldState = rememberScaffoldState(),
//    topBar :@Composable (()->Unit) ={},
//    bottomBar :@Composable (()->Unit)={},
//    snackBarHost : @Composable (SnackbarHostState) -> Unit ={ SnackbarHost(it) },
//    floatingActionButton : @Composable (()->Unit)={},
//    floatingActionButtonPosition : FabPosition = FabPosition.End,
//    isFloatingActionButtonDocked :Boolean =false,
//    drawerContent : @Composable (ColumnScope.()->Unit)? =null,
//    drawerShape : Shape = MaterialTheme.shapes.large,
//    drawerElevation : Dp = DrawerDefaults.Elevation,
//    drawerBackgroundColor : Color = MaterialTheme.colors.background,
//    drawerContentColor: Color = MaterialTheme.colors.secondary,
//    drawerScrimColor: Color = MaterialTheme.colors.primary,
//    backgroundColor: Color = MaterialTheme.colors.background,
//    contentColor: Color = MaterialTheme.colors.onBackground,
//    content: @Composable (PaddingValues) -> Unit
//
//
//){
//
//
//
//
//
//
//    Scaffold(modifier = modifier,
//        scaffoldState =scaffoldState,
//        topBar =topBar,
//        bottomBar=bottomBar,
//        snackbarHost =snackBarHost,
//        floatingActionButton =floatingActionButton,
//        floatingActionButtonPosition =floatingActionButtonPosition,
//        isFloatingActionButtonDocked =isFloatingActionButtonDocked,
//
//        drawerContent = drawerContent,
//        drawerShape = drawerShape,
//        drawerElevation = drawerElevation,
//        drawerBackgroundColor = drawerBackgroundColor,
//        drawerContentColor = drawerContentColor,
//        drawerScrimColor = drawerScrimColor,
//        backgroundColor = backgroundColor,
//        contentColor = contentColor,
//        content = content
//
//
//
//    )
//
//
//
//}