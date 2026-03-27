package com.ehansih.vulnscanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.ehansih.vulnscanner.ui.screens.*
import com.ehansih.vulnscanner.ui.theme.*
import com.ehansih.vulnscanner.viewmodel.ScanViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ScanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VulnScannerTheme {
                VulnScannerApp(viewModel)
            }
        }
    }
}

private data class NavItem(val route: String, val label: String, val icon: ImageVector)

private val navItems = listOf(
    NavItem("home",    "Dashboard", Icons.Default.Dashboard),
    NavItem("device",  "Device",    Icons.Default.PhoneAndroid),
    NavItem("apps",    "Apps",      Icons.Default.Apps),
    NavItem("network", "Network",   Icons.Default.Wifi),
    NavItem("logs",    "Logs",      Icons.Default.Terminal)
)

@Composable
fun VulnScannerApp(viewModel: ScanViewModel) {
    val navController = rememberNavController()
    val uiState       by viewModel.uiState.collectAsStateWithLifecycle()
    val navBackStack  by navController.currentBackStackEntryAsState()
    val currentRoute  = navBackStack?.destination?.route ?: "home"

    Scaffold(
        containerColor = ColorSurface,
        bottomBar = {
            NavigationBar(containerColor = ColorCard) {
                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick  = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState    = true
                            }
                        },
                        icon  = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor   = MaterialTheme.colorScheme.primary,
                            selectedTextColor   = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor      = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        )
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController    = navController,
            startDestination = "home",
            modifier         = Modifier.padding(padding)
        ) {
            composable("home") {
                HomeScreen(
                    uiState     = uiState,
                    onStartScan = viewModel::startScan,
                    onNavigate  = { route -> navController.navigate(route) }
                )
            }
            composable("device") {
                val result = uiState.summary?.deviceResult
                if (result != null) {
                    DeviceScreen(result)
                } else {
                    NoDataScreen("Run a scan first to see device security details.")
                }
            }
            composable("apps") {
                val apps = uiState.summary?.appResults
                if (apps != null) {
                    AppsScreen(apps)
                } else {
                    NoDataScreen("Run a scan first to see installed app risks.")
                }
            }
            composable("network") {
                NetworkScreen(uiState.summary?.networkResult)
            }
            composable("logs") {
                LogsScreen()
            }
        }
    }
}

@Composable
private fun NoDataScreen(message: String) {
    Box(
        modifier           = Modifier.fillMaxSize().background(ColorSurface),
        contentAlignment   = androidx.compose.ui.Alignment.Center
    ) {
        Text(message, color = Color.Gray)
    }
}
