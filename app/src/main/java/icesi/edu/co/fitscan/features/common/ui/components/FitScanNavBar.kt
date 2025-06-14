package icesi.edu.co.fitscan.features.common.ui.components

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.background
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigation
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import icesi.edu.co.fitscan.navigation.Screen
import icesi.edu.co.fitscan.ui.theme.FitScanTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.MaterialTheme


@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@Composable
fun FitScanNavBar(navController: NavController) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        val items = listOf(
            Screen.Home,
            Screen.Workouts,
            Screen.Meal,
            Screen.Statistics // This is the statistics button, which should navigate to ExerciseStatisticsScreen
        )

        val currentRoute = currentRoute(navController)
        items.forEach { screen ->
            BottomNavigationItem(
                modifier = Modifier.height(70.dp),
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    val isSelected = currentRoute == screen.route

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .width(70.dp)
                                .then(
                                    if (isSelected) {
                                        Modifier
                                            .background(
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = RoundedCornerShape(30.dp)
                                            )
                                            .padding(8.dp)
                                    } else {
                                        Modifier.padding(8.dp)
                                    }
                                )
                        ) {                        Icon(
                                painter = painterResource(id = screen.icon),
                                contentDescription = screen.title,
                                modifier = Modifier.size(24.dp),
                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = screen.title,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },                selectedContentColor = MaterialTheme.colorScheme.onSurface,
                unselectedContentColor = MaterialTheme.colorScheme.onSurface,
                alwaysShowLabel = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FitScanNavBarPreview() {
    val navController = rememberNavController()
    FitScanTheme {
        FitScanNavBar(navController)
    }
}