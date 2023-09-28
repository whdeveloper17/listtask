package com.wilsonhernandez.listtask

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.wilsonhernandez.listtask.data.database.TaskDatabase
import com.wilsonhernandez.listtask.data.repository.DatabaseRepository
import com.wilsonhernandez.listtask.data.repository.DatabaseRepositoryImpl
import com.wilsonhernandez.listtask.domain.AddTaskCaseUse
import com.wilsonhernandez.listtask.domain.DeleteTaskCaseUse
import com.wilsonhernandez.listtask.domain.FilledTaskCaseUse
import com.wilsonhernandez.listtask.domain.GetListTaskCaseUse
import com.wilsonhernandez.listtask.ui.viewmodel.AddViewModel
import com.wilsonhernandez.listtask.ui.viewmodel.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.koin.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.get
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

@Composable
fun getComposeViewModelOwner(): ViewModelOwner {
    return ViewModelOwner.from(
        LocalViewModelStoreOwner.current!!,
        LocalSavedStateRegistryOwner.current
    )
}
@Composable
inline fun <reified T : ViewModel> getComposeViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T {
    val viewModelOwner = getComposeViewModelOwner()
    return KoinJavaComponent.getKoin().getViewModel(qualifier, { viewModelOwner }, parameters)
}

val repositoryModule = module {
    single { TaskDatabase.getDatabase(androidContext()).dao }

    single<DatabaseRepository> { DatabaseRepositoryImpl(get()) }
}

val useCaseModule = module {
    single { AddTaskCaseUse(get()) }
    single { GetListTaskCaseUse(get()) }
    single { FilledTaskCaseUse(get()) }
    single { DeleteTaskCaseUse(get()) }
}

val appModule = module {
    viewModel { AddViewModel(get()) }
    viewModel { HomeViewModel(get(),get(),get()) }
}

