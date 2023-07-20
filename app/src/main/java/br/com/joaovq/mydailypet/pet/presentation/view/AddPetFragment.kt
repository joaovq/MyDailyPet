package br.com.joaovq.mydailypet.pet.presentation.view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.net.toUri
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.core.util.extension.calculateIntervalNextBirthday
import br.com.joaovq.mydailypet.core.util.extension.format
import br.com.joaovq.mydailypet.core.util.extension.formatWeightToLocale
import br.com.joaovq.mydailypet.core.util.extension.stringOrBlank
import br.com.joaovq.mydailypet.core.util.image.BitmapHelperProvider
import br.com.joaovq.mydailypet.core.util.image.ImageProvider
import br.com.joaovq.mydailypet.data.local.service.alarm.AlarmScheduler
import br.com.joaovq.mydailypet.data.local.service.alarm.model.NotificationAlarmItem
import br.com.joaovq.mydailypet.databinding.FragmentAddPetBinding
import br.com.joaovq.mydailypet.databinding.FragmentAddReminderBinding
import br.com.joaovq.mydailypet.pet.domain.model.SexType
import br.com.joaovq.mydailypet.pet.presentation.viewintent.AddPetAction
import br.com.joaovq.mydailypet.pet.presentation.viewmodel.AddPetViewModel
import br.com.joaovq.mydailypet.ui.TextWatcherProvider
import br.com.joaovq.mydailypet.ui.permission.CameraPermissionManager
import br.com.joaovq.mydailypet.ui.permission.PickImagePermissionManager
import br.com.joaovq.mydailypet.ui.util.extension.animateShrinkExtendedFabButton
import br.com.joaovq.mydailypet.ui.util.extension.simpleAlertDialog
import br.com.joaovq.mydailypet.ui.util.extension.simpleDatePickerDialog
import br.com.joaovq.mydailypet.ui.util.extension.snackbar
import br.com.joaovq.mydailypet.ui.util.extension.toast
import br.com.joaovq.mydailypet.ui.util.extension.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class AddPetFragment : Fragment() {
    private val binding by viewBinding(FragmentAddPetBinding::inflate)

    @Inject
    lateinit var alarmScheduler: AlarmScheduler
    private val addPetViewModel: AddPetViewModel by viewModels()
    private lateinit var cameraPermissionManager: CameraPermissionManager
    private lateinit var pickImagePermissionManager: PickImagePermissionManager
    private lateinit var registerPickImage: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var registerCaptureImage: ActivityResultLauncher<Void?>
    private var isImageSelected: Boolean = false
    private var mBirth: Date? = null
    private var currentWeight = ""
    private lateinit var bitmapWriterProvider: ImageProvider<Bitmap, File?>

    private val args: AddPetFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerPickImage = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia(),
        ) { uri ->
            uri?.let {
                binding.ivPhotoAddPet.ivPhoto.setImageURI(it)
                isImageSelected = true
            }
        }
        registerCaptureImage =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
                binding.ivPhotoAddPet.ivPhoto.setImageBitmap(bitmap)
                isImageSelected = true
            }
        pickImagePermissionManager = PickImagePermissionManager.from(this) {
            registerPickImage.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly,
                ),
            )
        }
        pickImagePermissionManager.setOnShowRationale("") {
            showSettingsDialog(R.string.rationale_message_pick_image)
        }
        cameraPermissionManager = CameraPermissionManager.from(this) {
            registerCaptureImage.launch(null)
        }
        cameraPermissionManager.setOnShowRationale("") {
            showSettingsDialog(R.string.rationale_message_capture_image)
        }
        bitmapWriterProvider = BitmapHelperProvider(requireContext())
    }

    private fun showSettingsDialog(@StringRes message: Int) {
        simpleAlertDialog(
            message = message,
            textPositiveButton = R.string.text_goto_settings,
        ) {
            val uri = Uri.fromParts(
                "package",
                requireActivity().packageName,
                null,
            )
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(uri)
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(
                    intent,
                )
            }
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
        animateView()
        setUpdateView()
        setUpToolbar()
        setListenersOfView()
        setAdapterType()
        initStates()
    }

    private fun setUpdateView() {
        args.pet?.let {
            /*TODO transfer this for databinding pet*/
            binding.ivPhotoAddPet.ivPhoto.setImageURI(it.imageUrl.toUri())
            binding.etNameAddPet.setText(it.name)
            binding.etWeightAddPet.setText(it.weight.formatWeightToLocale())
            binding.etBirthAddPet.setText(it.birth.format())
            mBirth = it.birth
            binding.atctvAnimalAddPet.setText(it.animal)
            binding.atctvBreddAddPet.setText(it.breed)
            binding.spSexAddPet.setSelection(
                when (it.sex) {
                    SexType.MALE -> 0
                    SexType.FEMALE -> 1
                    SexType.NOT_IDENTIFIED -> 2
                },
            )
            binding.tbAddPet.setTitle(R.string.title_tb_update_pet)
            binding.tbAddPet.setSubtitle(R.string.description_tb_update_pet)
            binding.btnAddPet.setText(R.string.text_update_pet)
            binding.btnAddPet.setIconResource(R.drawable.ic_check)
        }
    }

    private fun animateView() {
        animateShrinkExtendedFabButton(fabButton = binding.btnAddPet)
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
            simpleDatePickerDialog(
                title = getString(R.string.text_select_birth),
            ) { year, month, day ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, day)
                mBirth = calendar.time
                binding.etBirthAddPet.setText(mBirth.format())
            }
        }
        binding.ivPhotoAddPet.ivPhoto.setOnClickListener {
            showAlertDialog()
        }
        binding.btnAddPet.setOnClickListener {
            submitForms()
        }
        binding.etWeightAddPet.addTextChangedListener(
            TextWatcherProvider { editable ->
                formatDoubleValueEditable(editable)
            },
        )
    }

    private fun formatDoubleValueEditable(editable: Editable?) {
        val value = editable.toString()
        try {
            if (value != currentWeight) {
                doubleTreatment(value, editable)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            when (e) {
                is NumberFormatException -> binding.tilWeightAddPet.error = "Weight are need number"
                else -> snackbar(message = "unexpected error")
            }
        }
    }

    private fun doubleTreatment(value: String, editable: Editable?) {
        val weightValue = value.replace(".", "")
        currentWeight = if (value.isNotBlank()) {
            (
                weightValue.toBigDecimal().divide(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP)
                ).toString()
        } else {
            ""
        }
        editable!!.filters = arrayOfNulls<InputFilter>(0)
        editable.replace(0, editable.length, currentWeight, 0, currentWeight.length)
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

    private fun submitForms() {
        val sexType = mapSexType()
        val path =
            if (isImageSelected) {
                requireContext().filesDir.absolutePath + File.separator + "${System.currentTimeMillis()}.jpeg"
            } else {
                ""
            }
        try {
            if (args.pet != null) {
                updatePet(sexType, path)
            } else {
                createPet(sexType, path)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            when (e) {
                is NumberFormatException -> binding.tilWeightAddPet.error = "Weight are need number"
            }
        }
    }

    private fun updatePet(
        sexType: SexType,
        path: String,
    ) {
        addPetViewModel.dispatchIntent(
            AddPetAction.EditPet(
                args.pet!!.id,
                name = binding.etNameAddPet.text.toString(),
                type = binding.atctvBreddAddPet.text.toString(),
                weight = binding.etWeightAddPet.text.toString().toDouble(),
                sex = sexType,
                birth = mBirth,
                photoPath = path,
                animal = binding.atctvAnimalAddPet.text.toString(),
                birthAlarm = args.pet!!.birthAlarm,
            ),
        )
    }

    private fun createPet(
        sexType: SexType,
        path: String,
    ) {
        mBirth?.let {
            val nextBirthday = it.calculateIntervalNextBirthday()
            val notifyAlarm = createNotificationForBirthday(nextBirthday)
            addPetViewModel.dispatchIntent(
                AddPetAction.Submit(
                    name = binding.etNameAddPet.text.toString(),
                    type = binding.atctvBreddAddPet.text.toString(),
                    weight = binding.etWeightAddPet.text.toString().toDouble(),
                    sex = sexType,
                    birth = it,
                    photoPath = path,
                    animal = binding.atctvAnimalAddPet.text.toString(),
                    birthAlarm = notifyAlarm,
                ),
            )
        } ?: snackbar(message = getString(R.string.message_date_is_cannot_be_null))
    }

    private fun createNotificationForBirthday(it: Long) = NotificationAlarmItem(
        System.currentTimeMillis() + it,
        getString(R.string.notification_title_birthday, binding.etNameAddPet.text),
        getString(R.string.notification_description_birthday, binding.etNameAddPet.text),
    )

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
                    saveImageInternalStorage(stateCollected.pathImage)
                    stateCollected.message?.let { toast(text = getString(it)) }
                    findNavController().popBackStack()
                }
            }
        }
        lifecycleScope.launch {
            addPetViewModel.validateStateName.collectLatest {
                binding.tilNameAddPet.error = it.errorMessage.stringOrBlank(requireContext())
            }
        }
        lifecycleScope.launch {
            addPetViewModel.validateStateAnimal.collectLatest {
                binding.tilAnimalAddPet.error = it.errorMessage.stringOrBlank(requireContext())
            }
        }
        lifecycleScope.launch {
            addPetViewModel.validateStateDate.collectLatest {
                binding.tilBirthAddPet.error = it.errorMessage.stringOrBlank(requireContext())
            }
        }
    }

    private suspend fun saveImageInternalStorage(path: String) {
        if (isImageSelected) {
            bitmapWriterProvider.write(
                binding.ivPhotoAddPet.ivPhoto.drawToBitmap(),
                path.replace(requireContext().filesDir.absolutePath + File.separator, ""),
            )
        }
    }
}
