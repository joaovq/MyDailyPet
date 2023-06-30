package br.com.joaovq.mydailypet.pet.presentation.view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.joaovq.mydailypet.core.domain.model.Attach
import br.com.joaovq.mydailypet.core.util.extension.formatToInterval
import br.com.joaovq.mydailypet.core.util.extension.gone
import br.com.joaovq.mydailypet.core.util.extension.loadImage
import br.com.joaovq.mydailypet.core.util.extension.toast
import br.com.joaovq.mydailypet.core.util.extension.visible
import br.com.joaovq.mydailypet.core.util.image.BitmapWriterProvider
import br.com.joaovq.mydailypet.core.util.permission.PickImagePermissionManager
import br.com.joaovq.mydailypet.databinding.FragmentPetBinding
import br.com.joaovq.mydailypet.pet.adapter.DetailsPetAdapter
import br.com.joaovq.mydailypet.pet.presentation.viewintent.PetIntent
import br.com.joaovq.mydailypet.pet.presentation.viewmodel.PetScreenViewModel
import br.com.joaovq.mydailypet.pet.presentation.viewstate.PetState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@AndroidEntryPoint
class PetFragment : Fragment() {
    private val binding by lazy {
        FragmentPetBinding.inflate(layoutInflater)
    }
    private val petArg: PetFragmentArgs by navArgs()
    private val petViewModel: PetScreenViewModel by viewModels()
    private var mImagePet: Uri? = null
    private lateinit var pickImagePermissionManager: PickImagePermissionManager
    private lateinit var mAdapter: DetailsPetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickImagePermissionManager = PickImagePermissionManager.from(this) {
            lifecycleScope.launch {
                try {
                    val newFile = captureBitmapScreenshot()
                    if (newFile?.absolutePath != null) {
                        /*Strict Policy for MANAGE EXTERNAL STORAGE*/
                        val builder = StrictMode.VmPolicy.Builder()
                        StrictMode.setVmPolicy(builder.build())
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_STREAM, newFile.absolutePath.toUri())
                            type = "image/*"
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        val chooser = Intent.createChooser(intent, "Share with...")
                        TODO("Share not work. Search MediaStore save external storage")
                        startActivity(chooser)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    toast(text = "Error occurred")
                }
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
        if (petArg.id != -1) {
            petViewModel.dispatchIntent(PetIntent.GetPetDetails(petArg.id))
        }
        setToolbar()
        observeStates()
        setClickListeners()
        initDetailsRv()
    }

    private fun setToolbar() {
        binding.tbPetScreen.setNavigationOnClickListener {
            findNavController().popBackStack()
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
                when (collectedState) {
                    is PetState.Error -> toast(text = collectedState.message)
                    PetState.Initial -> {}
                    is PetState.Success -> {
                        collectedState.pet.apply {
                            binding.laytPhotoPet.ivPhoto.loadImage(url = imageUrl)
                            mImagePet = imageUrl.toUri()
                            binding.tvPetName.text = name
                            binding.tvPetNickname.text = nickname
                            birth?.let {
                                val interval = System.currentTimeMillis() - it.time
                                binding.tvPetBirth.text = it.formatToInterval(interval)
                            }
                            mAdapter.renderList(
                                this.copy(
                                    attachs = listOf(
                                        Attach(
                                            "dfhsd",
                                            type = "pdf",
                                            path = "",
                                        ),
                                        Attach(
                                            "dfhsd",
                                            type = "pdf",
                                            path = "",
                                        ),
                                        Attach(
                                            "dfhsd",
                                            type = "pdf",
                                            path = "",
                                        ),
                                    ),
                                ),
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setClickListeners() {
        binding.llShareDetailsPet.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                pickImagePermissionManager.checkPermission()
            }
        }
    }

    private fun initDetailsRv() {
        binding.rvDetailsPet.adapter = DetailsPetAdapter(
            object : DetailsPetAdapter.OnClickEvent {
                override fun setOnClickShareAttach(uri: String) {
                    toast(text = "Share document")
                }

                override fun setOnClickAttach(attach: Attach) {
                    toast(text = "Open Attach")
                }
            },
        ).also {
            mAdapter = it
        }
    }

    private suspend fun captureBitmapScreenshot(): File? {
        return withContext(Dispatchers.IO) {
            val bitmap = Bitmap.createBitmap(binding.root.drawToBitmap())
            val path =
                requireActivity().externalCacheDir?.let {
                    it.absoluteFile.toString() + File.separator + System.currentTimeMillis() / 1000 + ".png"
                }
            BitmapWriterProvider().convert(bitmap, path, "png")
        }
    }
}
