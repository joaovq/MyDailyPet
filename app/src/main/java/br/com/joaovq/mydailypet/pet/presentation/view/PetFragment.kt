package br.com.joaovq.mydailypet.pet.presentation.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.joaovq.core.data.image.BitmapHelperProvider
import br.com.joaovq.core.model.Attach
import br.com.joaovq.core.util.extension.calculateInterval
import br.com.joaovq.core.util.extension.format
import br.com.joaovq.core.util.extension.formatWeightToLocale
import br.com.joaovq.core_ui.extension.gone
import br.com.joaovq.core_ui.extension.loadImage
import br.com.joaovq.core_ui.extension.navWithAnim
import br.com.joaovq.core_ui.extension.snackbar
import br.com.joaovq.core_ui.extension.toast
import br.com.joaovq.core_ui.extension.viewBinding
import br.com.joaovq.core_ui.extension.visible
import br.com.joaovq.mappers.getStringRes
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.databinding.FragmentPetBinding
import br.com.joaovq.mydailypet.pet.presentation.adapter.DetailsPetAdapter
import br.com.joaovq.mydailypet.pet.presentation.viewintent.PetIntent
import br.com.joaovq.mydailypet.pet.presentation.viewmodel.PetScreenViewModel
import br.com.joaovq.mydailypet.pet.presentation.viewstate.PetState
import br.com.joaovq.pet_domain.model.Pet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Date

const val IMAGE_TYPE_SEND_INTENT = "image/*"

@AndroidEntryPoint
class PetFragment : Fragment() {
    private val binding by viewBinding(FragmentPetBinding::inflate)
    private val petArg: PetFragmentArgs by navArgs()
    private val petViewModel: PetScreenViewModel by viewModels()
    private var mImagePet: Uri? = null
    private lateinit var pickImagePermissionManager: br.com.joaovq.core_ui.permission.PickImagePermissionManager
    private lateinit var mAdapter: DetailsPetAdapter
    private lateinit var bitmapHelperProvider: BitmapHelperProvider
    private lateinit var fileProviderPath: String
    private var pet: Pet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickImagePermissionManager =
            br.com.joaovq.core_ui.permission.PickImagePermissionManager.from(this) { isGranted ->
                if (isGranted) {
                    lifecycleScope.launch {
                        shareImage()
                    }
                }
            }
        bitmapHelperProvider = BitmapHelperProvider(requireActivity())
        fileProviderPath = requireActivity().packageName + ".fileprovider"
    }

    private suspend fun shareImage() {
        try {
            val newFile = captureBitmapScreenshot()
            if (newFile?.absolutePath != null) {
                val builder = StrictMode.VmPolicy.Builder()
                StrictMode.setVmPolicy(builder.build())
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    val uriForFile = FileProvider.getUriForFile(
                        requireContext(),
                        fileProviderPath,
                        newFile,
                    )
                    putExtra(Intent.EXTRA_STREAM, uriForFile)
                    type = IMAGE_TYPE_SEND_INTENT
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                val chooser = Intent.createChooser(intent, getString(R.string.title_choser_share))
                startActivity(chooser)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            toast(text = getString(br.com.joaovq.core_ui.R.string.text_error_occurred_contact_provider))
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
        if (petArg.id != -1) {
            petViewModel.dispatchIntent(PetIntent.GetPetDetails(petArg.id))
        }
        animateTransitionPhotoExpandedPet()
        setListenersOfView()
        onNavigateToReminder()
        setToolbar()
        observeStates()
        initDetailsRv()
    }

    private fun animateTransitionPhotoExpandedPet() {
        ViewCompat.setTransitionName(binding.laytPhotoPet.ivPhoto, "pet-image")
    }

    private fun setListenersOfView() {
        binding.laytPhotoPet.ivPhoto.setOnClickListener {
            if (pet?.imageUrl?.isNotBlank() == true) {
                val extras = FragmentNavigatorExtras(
                    binding.laytPhotoPet.ivPhoto to "photo-expanded-pet",
                )
                findNavController().navigate(
                    PetFragmentDirections.actionPetFragmentToExpandPhotoPetFragment(
                        pet?.imageUrl,
                    ),
                    extras,
                )
            } else {
                toast(text = getString(R.string.message_image_not_found))
            }
        }
    }

    private fun onNavigateToReminder() {
        binding.llAddReminderPetFrag.setOnClickListener {
            findNavController().navWithAnim(
                action = PetFragmentDirections.actionPetFragmentToAddReminderFragment(pet = pet),
                animEnter = br.com.joaovq.core_ui.NavAnim.slideInLeft,
                animPopExit = br.com.joaovq.core_ui.NavAnim.slideOutLeft,
            )
        }
    }

    private fun setToolbar() {
        binding.tbPetScreen.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.tbPetScreen.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_edit_pet -> {
                    findNavController().navWithAnim(
                        PetFragmentDirections.actionPetFragmentToAddPetFragment(pet = pet),
                        animEnter = br.com.joaovq.core_ui.NavAnim.slideInLeft,
                        animPopExit = br.com.joaovq.core_ui.NavAnim.slideOutLeft,
                    )
                    true
                }

                R.id.item_share_pet -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        pickImagePermissionManager.checkPermission()
                    } else {
                        lifecycleScope.launch {
                            shareImage()
                        }
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun observeStates() {
        lifecycleScope.launch {
            petViewModel.isLoading.collectLatest { isLoading ->
                binding.pbLoadingIndicator.apply {
                    if (isLoading) {
                        visible()
                        binding.llPetProfileData.gone()
                    } else {
                        gone()
                        binding.llPetProfileData.visible()
                    }
                }
            }
        }
        lifecycleScope.launch {
            petViewModel.state.collectLatest { collectedState ->
                collectedState?.let {
                    when (collectedState) {
                        is PetState.Error -> toast(text = collectedState.message)
                        is PetState.Success -> {
                            collectedState.pet.apply {
                                setDetailsPetInView(this)
                                pet = this
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setDetailsPetInView(pet: br.com.joaovq.pet_domain.model.Pet) {
        pet.apply {
            binding.laytPhotoPet.ivPhoto.loadImage(imageUrl)
            mImagePet = imageUrl.toUri()
            binding.pet = pet
            binding.ltWeightDataPet.data = weight.formatWeightToLocale()
            binding.ltBreedDataPet.data = breed
            binding.ltAnimalDataPet.data = animal
            binding.ltSexDataPet.data = getString(sex.getStringRes())
            birth?.let {
                binding.ltBirthDataPet.data = it.format()
                getYearsInterval(it)
            }
            mAdapter.renderList(
                this.copy(attachs = attachs),
            )
        }
    }

    private fun getYearsInterval(it: Date) = try {
        it.calculateInterval { year, month, days ->
            binding.tvPetBirth.text =
                getString(
                    R.string.text_interval_life_pet,
                    year,
                    month,
                    days,
                )
        }
    } catch (e: Exception) {
        e.printStackTrace()
        e.message?.let { safeMessage -> snackbar(message = safeMessage) }
    }

    private fun initDetailsRv() {
        binding.rvDetailsPet.adapter = DetailsPetAdapter(
            object : DetailsPetAdapter.OnClickEvent {
                override fun setOnClickShareAttach(uri: String) {
                    toast(text = getString(br.com.joaovq.core_ui.R.string.text_not_implemented))
                }

                override fun setOnClickAttach(attach: Attach) {
                    toast(text = getString(br.com.joaovq.core_ui.R.string.text_not_implemented))
                }
            },
        ).also {
            mAdapter = it
        }
    }

    private suspend fun captureBitmapScreenshot(): File? {
        return withContext(Dispatchers.IO) {
            val bitmap = binding.svPetScreen.drawToBitmap()
            val imagesFolder = File(
                requireActivity().cacheDir,
                "images",
            )
            imagesFolder.mkdirs()
            val file = File(imagesFolder, "${System.currentTimeMillis()}.jpeg")
            bitmapHelperProvider.convert(bitmap, file)
        }
    }
}
