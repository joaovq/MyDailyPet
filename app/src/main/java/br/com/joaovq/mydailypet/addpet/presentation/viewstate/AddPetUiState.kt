package br.com.joaovq.mydailypet.addpet.presentation.viewstate

/*TODO Transfoorm in sealed class and states in classes or objects*/
data class AddPetUiState(
    val isLoading: Boolean,
    val message: String? = null,
    val isSuccesful: Boolean = false,
)
