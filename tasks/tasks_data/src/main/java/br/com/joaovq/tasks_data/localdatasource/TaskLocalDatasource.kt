package br.com.joaovq.tasks_data.localdatasource

import br.com.joaovq.core.data.localdatasource.LocalDataSource
import br.com.joaovq.tasks_data.dao.TaskDao
import br.com.joaovq.tasks_data.model.TaskDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

interface TaskLocalDatasource : LocalDataSource<TaskDto>

class TaskLocalDatasourceImpl @Inject constructor(
    private val taskDao: TaskDao,
) : TaskLocalDatasource {
    override fun getAll(): Flow<List<TaskDto>> {
        return try {
            taskDao.getAll()
        } catch (e: Exception) {
            emptyFlow()
        }
    }

    override fun getById(id: Int): Flow<TaskDto> {
        return try {
            taskDao.getById(id)
        } catch (e: Exception) {
            emptyFlow()
        }
    }

    override suspend fun insert(entity: TaskDto) {
        try {
            taskDao.createTask(entity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun update(entity: TaskDto) {
        try {
            taskDao.updateTask(entity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun delete(entity: TaskDto) {
        try {
            taskDao.deleteTask(entity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteAll() {
        try {
            taskDao.deleteAll()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
} 
