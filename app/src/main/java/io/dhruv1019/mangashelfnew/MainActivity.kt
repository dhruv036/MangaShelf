package io.dhruv1019.mangashelfnew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import io.dhruv1019.mangashelfnew.presentation.AppNavigation
import io.dhruv1019.mangashelfnew.presentation.viewmodel.MangaViewModel
import io.dhruv1019.mangashelfnew.ui.theme.MangashelfNewTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MangaViewModel by viewModels<MangaViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MangashelfNewTheme {
                AppNavigation(mangaViewModel = viewModel)
            }
        }
    }
}