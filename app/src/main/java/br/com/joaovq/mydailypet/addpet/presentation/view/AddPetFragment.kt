package br.com.joaovq.mydailypet.addpet.presentation.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.addpet.presentation.viewintent.AddPetAction
import br.com.joaovq.mydailypet.addpet.presentation.viewmodel.AddPetViewModel
import br.com.joaovq.mydailypet.core.domain.model.SexType
import br.com.joaovq.mydailypet.core.util.extension.simpleDatePickerDialog
import br.com.joaovq.mydailypet.core.util.extension.toast
import br.com.joaovq.mydailypet.core.util.permission.CameraPermissionManager
import br.com.joaovq.mydailypet.core.util.permission.PickImagePermissionManager
import br.com.joaovq.mydailypet.databinding.FragmentAddPetBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddPetFragment : Fragment() {
    private val binding by lazy {
        FragmentAddPetBinding.inflate(layoutInflater)
    }

    private val addPetViewModel: AddPetViewModel by viewModels()
    private lateinit var cameraPermissionManager: CameraPermissionManager
    private lateinit var pickImagePermissionManager: PickImagePermissionManager
    private lateinit var registerPickImage: ActivityResultLauncher<PickVisualMediaRequest>
    private var mImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraPermissionManager = CameraPermissionManager.from(this)
        registerPickImage = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia(),
        ) { uri ->
            uri?.let {
                binding.ivPhotoAddPet.setImageURI(it)
                mImageUri = uri
            }
        }
        pickImagePermissionManager = PickImagePermissionManager.from(this) {
            registerPickImage.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly,
                ),
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setListenersOfView()
        setAdapterType()
        initStates()
    }

    private fun setAdapterType() {
        binding.atctvTypeAddPet.apply {
            setAdapter(
                ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.type,
                    android.R.layout.select_dialog_item,
                ),
            )
            threshold = 1
        }
    }

    private fun setUpToolbar() {
        binding.tbAddPet.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setListenersOfView() {
        binding.etBirthAddPet.setOnClickListener {
            simpleDatePickerDialog { year, month, dayOfMonth ->
                val dateString = "$dayOfMonth/$month/$year"
                binding.etBirthAddPet.setText(dateString)
            }
        }
        binding.ivPhotoAddPet.setOnClickListener {
            pickImagePermissionManager.checkPermission()
        }
        binding.btnAddPet.setOnClickListener {
            submitPet()
        }
    }

    private fun submitPet() {
        val sexType = when (binding.spSexAddPet.selectedItem as String) {
            "Male" -> SexType.MALE
            "Female" -> SexType.FEMALE
            else -> SexType.NOT_IDENTIFIED
        }
        val imageUriString = if (mImageUri != null) mImageUri.toString() else ""
        addPetViewModel.dispatchIntent(
            AddPetAction.Submit(
                name = binding.etNameAddPet.text.toString(),
                type = binding.atctvTypeAddPet.text.toString(),
                weight = binding.etWeightAddPet.text.toString().toDouble(),
                sex = sexType,
                birth = binding.etBirthAddPet.text.toString(),
                photoPath = imageUriString,
            ),
        )
    }

    private fun initStates() {
        lifecycleScope.launch {
            addPetViewModel.state.collectLatest { stateCollected ->
                /*if (stateCollected.isLoading) else toast(text = "finish")*/
                when (stateCollected.isSuccesful) {
                    true -> {
                        findNavController().popBackStack()
                        stateCollected.message?.let { toast(text = it) }
                    }

                    else -> {
                        /*TODO*/
                    }
                }
            }
        }
    }
}
