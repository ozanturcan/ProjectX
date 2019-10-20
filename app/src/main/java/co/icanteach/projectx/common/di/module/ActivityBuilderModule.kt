package co.icanteach.projectx.common.di.module

import co.icanteach.projectx.MainActivity
import co.icanteach.projectx.common.di.scope.ActivityScope
import co.icanteach.projectx.ui.searchMovies.SearchMovieActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [SearchMovieActivityModule::class])
    abstract fun bindMainActivity(): MainActivity
}