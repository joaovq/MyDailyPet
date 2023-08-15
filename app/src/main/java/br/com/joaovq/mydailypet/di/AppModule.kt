package br.com.joaovq.mydailypet.di

import br.com.joaovq.core.presentation.NavLinksApp
import br.com.joaovq.mydailypet.navigation.AppDeepLinks
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun bindsAppDeepLinks(
        appDeepLinks: AppDeepLinks,
    ): NavLinksApp
}
