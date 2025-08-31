package br.com.joaovq.tasks_presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.joaovq.core_ui.extension.snackbar
import br.com.joaovq.pet_domain.model.Pet
import br.com.joaovq.tasks_domain.model.Task
import br.com.joaovq.tasks_presentation.R
import br.com.joaovq.tasks_presentation.adapter.SelectorPetsAdapter
import br.com.joaovq.tasks_presentation.adapter.TaskListAdapter
import br.com.joaovq.tasks_presentation.databinding.FragmentTaskBinding
import br.com.joaovq.tasks_presentation.viewintent.TaskListAction
import br.com.joaovq.tasks_presentation.viewmodel.TaskListViewModel
import br.com.joaovq.tasks_presentation.viewstate.TaskListState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class TaskFragment : Fragment() {
    private val binding: FragmentTaskBinding by lazy {
        FragmentTaskBinding.inflate(layoutInflater)
    }
    private lateinit var taskListAdapter: TaskListAdapter
    private val taskListViewModel: TaskListViewModel by viewModels()
    private var allTasks: List<Task>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListenersOfView()
        setToolbar()
        setInitialState()
        observeState()
    }

    private fun setListenersOfView() {
        binding.spSelectPetTaskList.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                filterTasksByPetSelected()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun setToolbar() {
        binding.tbTasksFrag.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setInitialState() {
        binding.rvTaskList.adapter = TaskListAdapter(
            taskListItemClickListener,
        ).also {
            taskListAdapter = it
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            taskListViewModel.pets.collectLatest { pets ->
                binding.spSelectPetTaskList.adapter =
                    SelectorPetsAdapter(
                        requireContext(),
                        pets,
                    )
                filterTasksByPetSelected()
            }
        }
        lifecycleScope.launch {
            taskListViewModel.state.collectLatest { state ->
                state?.let { safeState ->
                    when (safeState) {
                        is TaskListState.Error -> {
                            snackbar(message = getString(safeState.message))
                        }

                        TaskListState.SubmittedSuccess -> {
                            snackbar(message = getString(safeState.message))
                        }

                        is TaskListState.Success -> {
                            allTasks = safeState.data
                            filterTasksByPetSelected()
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    private fun filterTasksByPetSelected() {
        allTasks?.let { safeTasks ->
            try {
                val pet =
                    binding.spSelectPetTaskList.selectedItem as br.com.joaovq.pet_domain.model.Pet
                val filteredTasksPetSelected = safeTasks.filter { task ->
                    task.pet == pet
                }
                Timber.tag(this@TaskFragment.javaClass.simpleName)
                    .e(filteredTasksPetSelected.toString())
                taskListAdapter.renderTaskList(
                    filteredTasksPetSelected,
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val taskListItemClickListener = object : TaskListAdapter.TaskListOnClickListener {
        override fun setOnCheckTask(isChecked: Boolean, task: Task) {
            taskListViewModel.dispatchIntent(
                TaskListAction.UpdateStatusCompletedTask(
                    task.id,
                    task,
                    isChecked,
                ),
            )
        }

        override fun setCompletedCreatedTask(name: String) {
            try {
                taskListViewModel.dispatchIntent(
                    TaskListAction.CreateTask(
                        Task(
                            name = name,
                            pet = binding.spSelectPetTaskList.selectedItem as Pet,
                        ),
                    ),
                )
            } catch (e: Exception) {
                when (e) {
                    is NullPointerException -> snackbar(message = getString(R.string.message_cannot_create_task))
                }
            }
        }

        override fun setOnClickDeleteButton(task: Task) {
            taskListViewModel.dispatchIntent(
                TaskListAction.DeleteTask(
                    task.id,
                    task,
                ),
            )
        }
    }
}
