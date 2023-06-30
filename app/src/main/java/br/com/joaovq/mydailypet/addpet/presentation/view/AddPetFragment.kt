package br.com.joaovq.mydailypet.addpet.presentation.view

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.addpet.presentation.viewintent.AddPetAction
import br.com.joaovq.mydailypet.addpet.presentation.viewmodel.AddPetViewModel
import br.com.joaovq.mydailypet.core.util.app.TextWatcherProvider
import br.com.joaovq.mydailypet.core.util.extension.format
import br.com.joaovq.mydailypet.core.util.extension.simpleDatePickerDialog
import br.com.joaovq.mydailypet.core.util.extension.toast
import br.com.joaovq.mydailypet.core.util.permission.CameraPermissionManager
import br.com.joaovq.mydailypet.core.util.permission.PickImagePermissionManager
import br.com.joaovq.mydailypet.databinding.FragmentAddPetBinding
import br.com.joaovq.mydailypet.core.domain.model.SexType
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Date

@AndroidEntryPoint
class AddPetFragment : Fragment() {
    private val binding by lazy {
        FragmentAddPetBinding.inflate(layoutInflater)
    }

    private val addPetViewModel: AddPetViewModel by viewModels()
    private lateinit var cameraPermissionManager: CameraPermissionManager
    private lateinit var pickImagePermissionManager: PickImagePermissionManager
    private lateinit var registerPickImage: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var registerCaptureImage: ActivityResultLauncher<Void?>
    private var mImageUri: Uri? = null
    private var mBirth: Date? = null
    private var currentWeight = ""
    private var weightValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerPickImage = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia(),
        ) { uri ->
            uri?.let {
                binding.ivPhotoAddPet.ivPhoto.setImageURI(it)
                mImageUri = uri
            }
        }
        registerCaptureImage =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
                binding.ivPhotoAddPet.ivPhoto.setImageBitmap(bitmap)
                // TODO: Turn preview for take picture normal
            }
        pickImagePermissionManager = PickImagePermissionManager.from(this) {
            registerPickImage.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly,
                ),
            )
        }
        cameraPermissionManager = CameraPermissionManager.from(this) {
            registerCaptureImage.launch(null)
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
        binding.atctvBreddAddPet.apply {
            setAdapter(
                ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.breed,
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
            simpleDatePickerDialog(title = "Select Birth") { date ->
                mBirth = date
                binding.etBirthAddPet.setText(mBirth.format())
            }
        }
        binding.ivPhotoAddPet.ivPhoto.setOnClickListener {
            showAlertDialog()
        }
        binding.btnAddPet.setOnClickListener {
            submitPet()
        }
        binding.etWeightAddPet.addTextChangedListener(
            TextWatcherProvider { editable ->
                formatDoubleValueEditable(editable)
            },
        )
    }

    private fun formatDoubleValueEditable(editable: Editable?) {
        var value = editable.toString()
        if (value != currentWeight) {
            weightValue = if (currentWeight.isBlank()) {
                value
            } else {
                weightValue + value.last()
                TODO("Catch event for delete string")
            }
            currentWeight = (
                weightValue.toBigDecimal().divide(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP)
                ).toString()
            editable!!.filters = arrayOfNulls<InputFilter>(0)
            editable.replace(0, editable.length, currentWeight, 0, currentWeight.length)
        }
    }

    private fun showAlertDialog() {
        val materialAlertDialogBuilder =
            MaterialAlertDialogBuilder(requireContext())
        materialAlertDialogBuilder.setIcon(R.drawable.ic_round_logo_2)

        materialAlertDialogBuilder.setTitle(getString(R.string.text_select_option))
        materialAlertDialogBuilder.setItems(
            R.array.picture_alert_items,
        ) { _, position ->
            when (position) {
                0 -> pickImagePermissionManager.checkPermission()
                1 -> cameraPermissionManager.checkPermission()
            }
        }
        materialAlertDialogBuilder.show()
    }

    private fun submitPet() {
        val sexType = mapSexType()
        val imageUriString = if (mImageUri != null) mImageUri.toString() else ""
        try {
            addPetViewModel.dispatchIntent(
                AddPetAction.Submit(
                    name = binding.etNameAddPet.text.toString(),
                    type = binding.atctvBreddAddPet.text.toString(),
                    weight = binding.etWeightAddPet.text.toString().toDouble(),
                    sex = sexType,
                    birth = mBirth,
                    photoPath = imageUriString,
                    animal = binding.atctvAnimalAddPet.text.toString(),
                ),
            )
        } catch (e: Exception) {
            e.printStackTrace()
            when (e) {
                is NumberFormatException -> toast(text = "Weight are need number")
            }
        }
    }

    private fun mapSexType() = when (binding.spSexAddPet.selectedItem as String) {
        getString(R.string.text_male) -> SexType.MALE
        getString(R.string.text_female) -> SexType.FEMALE
        else -> SexType.NOT_IDENTIFIED
    }

    private fun initStates() {
        lifecycleScope.launch {
            addPetViewModel.state.collectLatest { stateCollected ->
                binding.pbAddPetFrag.isVisible = stateCollected.isLoading
                binding.ctlAddPet.isVisible = !stateCollected.isLoading
                if (stateCollected.isSuccesful) {
                    findNavController().popBackStack()
                    stateCollected.message?.let { toast(text = it) }
                }
            }
        }
    }
}
