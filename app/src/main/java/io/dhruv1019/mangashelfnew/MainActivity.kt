package io.dhruv1019.mangashelfnew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import io.dhruv1019.mangashelfnew.presentation.AppNavigation
import io.dhruv1019.mangashelfnew.presentation.MangaViewModel
import io.dhruv1019.mangashelfnew.ui.theme.MangashelfNewTheme
import io.dhruv1019.mangashelfnew.presentation.AppNavigation

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MangaViewModel by viewModels<MangaViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Initialize Repository with DataSources
//        val repository

        // Create ViewModel using Factory
//        val factory = MangaViewModelFactory(repository)
//        viewModel = ViewModelProvider(this, factory)[MangaViewModel::class.java]
        setContent {
            MangashelfNewTheme {
                AppNavigation(mangaViewModel = viewModel)
            }
        }
    }
}