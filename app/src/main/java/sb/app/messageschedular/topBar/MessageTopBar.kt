package sb.app.messageschedular.topBar

//
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.width
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Check
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.unit.dp
//import sb.app.messageschedular.R
//
//
//@Composable
//fun MessageTopBar(navigateTo :()->Unit ,onSubmit :()->Unit ) {
//    SmallTopAppBar(
//        colors = TopAppBarDefaults
//            .smallTopAppBarColors(
//            containerColor = MaterialTheme.colors.surface ,
//                titleContentColor = MaterialTheme.colors.onSurface,
//                navigationIconContentColor = MaterialTheme.colors.onSurface)
//
//          ,title = { Text(text = stringResource(id = R.string.schedule_message)
//         ,style =MaterialTheme.typography.h6
//
//        ) },
//
//        navigationIcon = {
//            IconButton(onClick = navigateTo)
//            {
//                Icon(
//                    imageVector = Icons.Filled.ArrowBack,
//                    contentDescription = stringResource(id =R.string.back_arrow)
//                )
//            } },
//        actions = {
//
//            IconButton(onClick = onSubmit
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Check,
//                    tint = Color.Green,
//                    contentDescription = "tick"
//                )
//                Spacer(modifier = Modifier.width(10.dp))
//            }
//        }
//
//    ) }
