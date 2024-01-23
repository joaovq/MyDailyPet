package br.com.joaovq.tasks_data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import br.com.joaovq.model.PET_ID_COLUMN_INFO
import br.com.joaovq.model.PetDto
import java.util.Date

const val TASK_TABLE_NAME = "task_tb"
const val TASK_ID_COLUMN_INFO = "task_id"

@Entity(
    tableName = TASK_TABLE_NAME,
    indices = [
        Index(PET_ID_COLUMN_INFO),
    ],
    foreignKeys = [
        ForeignKey(
            entity = PetDto::class,
            onDelete = ForeignKey.CASCADE,
            parentColumns = [PET_ID_COLUMN_INFO],
            childColumns = [PET_ID_COLUMN_INFO],
        ),
    ],
)
data class TaskDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = TASK_ID_COLUMN_INFO)
    val taskId: Int = 0,
    val nameTask: String = "",
    val createdAt: Date = Date(),
    val isCompleted: Boolean = false,
    @Embedded val pet: PetDto,
)
