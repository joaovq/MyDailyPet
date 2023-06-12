package br.com.joaovq.mydailypet.core.data.local.localdatasource

import kotlinx.coroutines.flow.Flow

interface LocalDataSource<T> {
    fun getAll(): Flow<List<T>>
    suspend fun insert(entity: T)
    suspend fun update(entity: T)
    suspend fun delete(entity: T)
    suspend fun deleteAll(entity: T)
}
