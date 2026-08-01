package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.components.OnboardingDialog
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.QuestsScreen
import com.example.ui.screens.RanksScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.screens.ThemeScreen
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BgCard
import com.example.ui.theme.BgElevated
import com.example.ui.theme.BgPrimary
import com.example.ui.theme.PushYourselfTheme
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PushYourselfTheme {
                MainAppContainer(viewModel = viewModel)
            }
        }
    }
}

private sealed class NavTab(val title: String, val icon: ImageVector) {
    object Home : NavTab("Home", Icons.Default.Home)
    object Quests : NavTab("Quests", Icons.Default.CheckCircle)
    object Ranks : NavTab("Ranks", Icons.Default.Leaderboard)
    object Stats : NavTab("Stats", Icons.Default.BarChart)
    object Theme : NavTab("Theme", Icons.Default.Palette)
}

@Composable
private fun MainAppContainer(viewModel: MainViewModel) {
    var selectedTab by remember { mutableStateOf<NavTab>(NavTab.Home) }
    val showOnboarding by viewModel.showOnboarding.collectAsState()

    val tabs = listOf(
        NavTab.Home,
        NavTab.Quests,
        NavTab.Ranks,
        NavTab.Stats,
        NavTab.Theme
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = BgCard,
                contentColor = AccentCyan
            ) {
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title
                            )
                        },
                        label = {
                            Text(
                                text = tab.title.uppercase(),
                                fontFamily = FontFamily.Monospace,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 10.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = AccentCyan,
                            indicatorColor = AccentCyan,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        )
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgPrimary)
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                NavTab.Home -> HomeScreen(viewModel = viewModel)
                NavTab.Quests -> QuestsScreen(viewModel = viewModel)
                NavTab.Ranks -> RanksScreen(viewModel = viewModel)
                NavTab.Stats -> StatsScreen(viewModel = viewModel)
                NavTab.Theme -> ThemeScreen(viewModel = viewModel)
            }

            if (showOnboarding) {
                OnboardingDialog(
                    onSubmitCallsign = { callsign ->
                        viewModel.submitCallsign(callsign)
                    }
                )
            }
        }
    }
}

