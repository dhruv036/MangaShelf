package io.dhruv1019.mangashelfnew.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.dhruv1019.mangashelfnew.modal.Manga
import io.dhruv1019.mangashelfnew.utils.Result
import io.dhruv1019.mangashelfnew.modal.Routes
import io.dhruv1019.mangashelfnew.modal.SortBy
import io.dhruv1019.mangashelfnew.presentation.viewmodel.MangaViewModel

@Composable
fun AppNavigation(mangaViewModel: MangaViewModel) {
    val navController = rememberNavController()
    val activity = LocalContext.current
    val mangaList = mangaViewModel.mangaList.collectAsStateWithLifecycle(initialValue = Result.loading())
    val yearList = mangaViewModel.yearIndexMap.collectAsStateWithLifecycle(initialValue = mutableMapOf())
    val sortType = mangaViewModel.sortBy.collectAsStateWithLifecycle(initialValue = SortBy.NONE)

    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen.name
    ) {
        composable(route = Routes.HomeScreen.name) {
            HomeScreen(
                mangaList =  mangaList,
                onMangaEvent = mangaViewModel::mangaEvent,
                onNavigateToMangaDetail = {mangaId ->
                    navController.navigate(route = Routes.DetailScreen.name.plus("/$mangaId"))
                },
                yearList = yearList,
                sortType = sortType
            )
        }
        composable(
            route = Routes.DetailScreen.name.plus("/{mangaId}"),
            arguments = listOf(navArgument("mangaId") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {backStack->
            backStack.arguments?.getString("mangaId").let { id ->
              val selectedManga = mangaList.value.data?.find { manga: Manga -> manga.id == id }
                if (selectedManga != null) {
                    DetailScreen(
                        manga = selectedManga,
                        onBack = {
                            if (navController.isValidBackStack) {
                                navController.popBackStack()
                            }
                        },
                        onMangaEvent = mangaViewModel::mangaEvent
                    )
                }
            }
        }
    }
}

val NavHostController.isValidBackStack
    get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED
