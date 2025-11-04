package br.com.joaovq.mydailypet.pet.presentation.view

import android.content.Context
import android.graphics.Bitmap
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
import androidx.core.net.toUri
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.joaovq.core.data.image.BitmapHelperProvider
import br.com.joaovq.core.data.image.ImageProvider
import br.com.joaovq.core.model.NotificationAlarmItem
import br.com.joaovq.core.model.SexType
import br.com.joaovq.core.util.extension.calculateIntervalNextBirthday
import br.com.joaovq.core.util.extension.format
import br.com.joaovq.core.util.extension.stringOrBlank
import br.com.joaovq.core_ui.TextWatcherProvider
import br.com.joaovq.core_ui.extension.animateShrinkExtendedFabButton
import br.com.joaovq.core_ui.extension.goToSettingsAlertDialogForPermission
import br.com.joaovq.core_ui.extension.simpleDatePickerDialog
import br.com.joaovq.core_ui.extension.snackbar
import br.com.joaovq.core_ui.extension.toast
import br.com.joaovq.core_ui.extension.viewBinding
import br.com.joaovq.core_ui.permission.CameraPermissionManager
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.databinding.FragmentAddPetBinding
import br.com.joaovq.mydailypet.pet.presentation.viewintent.AddPetAction
import br.com.joaovq.mydailypet.pet.presentation.viewmodel.AddPetViewModel
import br.com.joaovq.pet_domain.model.AddPetChoices.CREATE
import br.com.joaovq.pet_domain.model.AddPetChoices.UPDATE
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Calendar
import java.util.Date
import java.util.UUID
import br.com.joaovq.core.R as CoreRes
import br.com.joaovq.core_ui.R as CoreUiRes

@AndroidEntryPoint
class AddPetFragment : Fragment() {
    private val log = Timber.tag(this::class.java.simpleName)
    private val binding by viewBinding(FragmentAddPetBinding::inflate)

    private val addPetViewModel: AddPetViewModel by viewModels()
    private lateinit var cameraPermissionManager: CameraPermissionManager
    private lateinit var registerPickImage: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var registerCaptureImage: ActivityResultLauncher<Void?>
    private var isImageSelected: Boolean = false
    private var mBirth: Date? = null
    private var currentWeight = ""
    private lateinit var bitmapWriterProvider: ImageProvider<Bitmap, File?>

    private val args: AddPetFragmentArgs by navArgs()
    private var interstitialAd: InterstitialAd? = null

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
        cameraPermissionManager = CameraPermissionManager.from(this) {
            if (it) {
                registerCaptureImage.launch(null)
            }
        }
        cameraPermissionManager.setOnShowRationale {
            goToSettingsAlertDialogForPermission(CoreUiRes.string.rationale_message_capture_image)
        }
        bitmapWriterProvider = BitmapHelperProvider(requireContext())
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
        animateShrinkExtendedFabButton(fabButton = binding.btnAddPet)
        loadAds()
        setUpdateView()
        setUpToolbar()
        setListenersOfView()
        setAdapterType()
        initStates()
    }

    fun loadAds() {
        InterstitialAd.load(
            requireContext(),
            getString(R.string.INTERSTICIAL_ID),
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    log.d("Ad was loaded.")
                    interstitialAd = ad
                    interstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                log.d("Ad was dismissed.")
                                interstitialAd = null
                                findNavController().popBackStack()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                log.d("Ad failed to show.")
                                interstitialAd = null
                            }

                            override fun onAdShowedFullScreenContent() {
                                log.d("Ad showed fullscreen content.")
                            }

                            override fun onAdImpression() {
                                log.d("Ad recorded an impression.")
                            }

                            override fun onAdClicked() {
                                log.d("Ad was clicked.")
                            }
                        }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    log.d(adError.message)
                    interstitialAd = null
                }
            },
        )
    }

    private fun setUpdateView() {
        args.pet?.let { safePet ->
            binding.pet = safePet
            binding.ivPhotoAddPet.ivPhoto.setImageURI(safePet.imageUrl.toUri())
            binding.etNameAddPet.setText(safePet.name)
            binding.etWeightAddPet.setText(safePet.weight.toString())
            binding.etBirthAddPet.setText(safePet.birth.format())
            mBirth = safePet.birth
            binding.atctvAnimalAddPet.setText(safePet.animal)
            binding.atctvBreddAddPet.setText(safePet.breed)
            binding.spSexAddPet.setSelection(
                when (safePet.sex) {
                    SexType.MALE -> 0
                    SexType.FEMALE -> 1
                },
            )
        }
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
                val calendar = Calendar.getInstance().apply {
                    this[Calendar.YEAR] = year
                    this[Calendar.MONTH] = month
                    this[Calendar.DAY_OF_MONTH] = day
                }
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
        } catch (e: Throwable) {
            log.e(e)
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
        materialAlertDialogBuilder.setIcon(CoreUiRes.drawable.ic_round_logo_2)

        materialAlertDialogBuilder.setTitle(getString(CoreUiRes.string.text_select_option))
        materialAlertDialogBuilder.setItems(
            R.array.picture_alert_items,
        ) { _, position ->
            when (position) {
                0 -> registerPickImage.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly,
                    ),
                )

                1 -> cameraPermissionManager.checkPermission()
            }
        }
        materialAlertDialogBuilder.show()
    }

    private fun submitForms() {
        val sexType = (binding.spSexAddPet.selectedItem as String).mapSexType(requireContext())
        val path = getPathPhotoUrl()
        val bitmap = if (isImageSelected) binding.ivPhotoAddPet.ivPhoto.drawToBitmap() else null
        try {
            if (mapChoicesAction() == UPDATE) {
                updatePet(sexType, path, bitmap)
            } else {
                createPet(sexType, path, bitmap)
            }
        } catch (e: Exception) {
            log.e(e)
            when (e) {
                is NumberFormatException ->
                    binding.tilWeightAddPet.error = getString(R.string.message_error_weight_null)
            }
        }
    }

    private fun mapChoicesAction() = when (args.pet != null) {
        true -> UPDATE
        false -> CREATE
    }

    private fun getPathPhotoUrl() = if (isImageSelected) {
        "${System.currentTimeMillis()}.jpeg"
    } else if (args.pet?.imageUrl?.isNotBlank() == true) {
        args.pet?.imageUrl ?: ""
    } else {
        ""
    }

    private fun updatePet(
        sexType: SexType,
        path: String,
        bitmap: Bitmap?,
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
                bitmap = bitmap,
            ),
        )
    }

    private fun createPet(
        sexType: SexType,
        path: String,
        bitmap: Bitmap?,
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
                    photoNameFile = path,
                    animal = binding.atctvAnimalAddPet.text.toString(),
                    birthAlarm = notifyAlarm,
                    bitmap = bitmap,
                ),
            )
        }
            ?: snackbar(message = getString(CoreRes.string.message_date_is_cannot_be_null))
    }

    private fun createNotificationForBirthday(it: Long) = NotificationAlarmItem(
        System.currentTimeMillis() + it,
        getString(R.string.notification_title_birthday, binding.etNameAddPet.text),
        getString(R.string.notification_description_birthday, binding.etNameAddPet.text),
        id = UUID.randomUUID()
    )

    private fun initStates() {
        lifecycleScope.launch {
            addPetViewModel.state.collectLatest { stateCollected ->
                binding.pbAddPetFrag.isVisible = stateCollected.isLoading
                binding.ctlAddPet.isVisible = !stateCollected.isLoading
                if (stateCollected.isSuccesful) {
                    stateCollected.message?.let { toast(text = getString(it)) }
                    interstitialAd?.show(requireActivity()) ?: findNavController().popBackStack()
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

    override fun onDestroy() {
        super.onDestroy()
        if (interstitialAd != null) {
            interstitialAd = null
        }
    }
}

private fun String.mapSexType(context: Context) = when (this) {
    context.getString(CoreRes.string.text_male) -> SexType.MALE
    context.getString(CoreRes.string.text_female) -> SexType.FEMALE
    else -> SexType.MALE
}
