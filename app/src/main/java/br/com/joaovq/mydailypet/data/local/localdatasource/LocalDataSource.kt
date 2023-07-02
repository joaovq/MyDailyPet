package br.com.joaovq.mydailypet.data.local.localdatasource

import kotlinx.coroutines.flow.Flow

interface LocalDataSource<T> {
    fun getAll(): Flow<List<T>>
    fun getById(id: Int): Flow<T>
    suspend fun insert(entity: T)
    suspend fun update(entity: T)
    suspend fun delete(entity: T)
    suspend fun deleteAll()
}
