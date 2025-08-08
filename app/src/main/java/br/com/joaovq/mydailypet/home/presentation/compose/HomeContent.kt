package br.com.joaovq.mydailypet.home.presentation.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDirections
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.home.presentation.viewmodel.HomeViewModel
import br.com.joaovq.mydailypet.home.presentation.viewstate.HomeUiState
import br.com.joaovq.pet_domain.model.Pet

private const val INITIAL_LIMIT_PETS_ADAPTER = 3

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    onExpanded: (Pet) -> Unit,
    onDelete: (Pet) -> Unit,
    onClickCategory: (NavDirections) -> Unit
) {
    val state by homeViewModel.homeState.collectAsState()
    Surface {
        Column(modifier = modifier.fillMaxWidth()) {
            when (val safeState = state) {
                is HomeUiState.Success -> {
                    var isMarkerLimit by rememberSaveable {
                        mutableStateOf(true)
                    }
                    val slicedData by remember(safeState.data, isMarkerLimit) {
                        derivedStateOf {
                            if (safeState.data.size > 3 && isMarkerLimit) {
                                safeState.data.slice(0..2)
                            } else {
                                safeState.data
                            }
                        }
                    }
                    slicedData.forEach { pet ->
                        PetCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, bottom = 16.dp),
                            pet = pet,
                            listener = object : PetCardListener {
                                override fun onExpanded() {
                                    onExpanded(pet)
                                }

                                override fun onCollapsed() {
                                    // Unused
                                }

                                override fun onLongPress() {
                                    // Unused
                                }

                                override fun onDeleteItemClick() {
                                    onDelete(pet)
                                }
                            },
                            actions = {
                                Card(
                                    modifier = Modifier.fillMaxHeight(),
                                    shape = RoundedCornerShape(
                                        topStart = 10.dp,
                                        bottomStart = 10.dp
                                    ),
                                    border = BorderStroke(0.3.dp, Color.DarkGray),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(10.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clip(RoundedCornerShape(10.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                modifier = Modifier.size(30.dp),
                                                painter = painterResource(R.drawable.ic_eye),
                                                contentDescription = null
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }
                    val isShowSeeMore by remember {
                        derivedStateOf {
                            safeState.data.size > INITIAL_LIMIT_PETS_ADAPTER && isMarkerLimit
                        }
                    }
                    AnimatedVisibility(isShowSeeMore) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    isMarkerLimit = false
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                4.dp,
                                Alignment.End
                            )
                        ) {
                            Text(
                                stringResource(R.string.text_see_all),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_down),
                                contentDescription = null
                            )
                        }
                    }
                }

                else -> {
                    // TODO add result on states
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            CategoryList(
                onClickCategory = onClickCategory
            )
        }
    }
}
