package br.com.joaovq.mydailypet.home.presentation.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import br.com.joaovq.core.model.NotificationAlarmItem
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.ui.theme.MyDailyPetTheme
import br.com.joaovq.pet_domain.model.Pet
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
@Suppress("Detekt.FunctionNaming")
fun PetCard(
    modifier: Modifier = Modifier,
    pet: Pet,
    listener: PetCardListener,
    actions: @Composable RowScope.() -> Unit = {},
) {
    var contextMenuWidth by remember { mutableFloatStateOf(0f) }
    val offset = remember { Animatable(initialValue = 0f) }
    var expanded by remember { mutableStateOf(false) }
    val haptics = LocalHapticFeedback.current
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val scope = rememberCoroutineScope()
    var size by remember { mutableStateOf(IntSize.Zero) }
    val interactionSource = remember { MutableInteractionSource() }
    val density = LocalDensity.current
    /* val anchors =
         with(density) {
             DraggableAnchors<DragValue> {
                 //DragValue.Start at -100.dp.toPx()
                 DragValue.Center at 0f
                 DragValue.End to 100.dp.toPx()
             }
         }
     val anchoredDraggableState = remember(size) {
         AnchoredDraggableState(
             initialValue = DragValue.Center,
             positionalThreshold = { with(density) { size.width.dp.toPx().div(2) } },
             anchors = anchors,
             velocityThreshold = { with(density) { 125.dp.toPx() } },
             animationSpec = tween(),
             confirmValueChange = {
                 if (it == DragValue.End) onExpanded()
                 true
             }
         )
     }
     LaunchedEffect(size) {
         anchoredDraggableState.updateAnchors(
             with(density) {
                 DraggableAnchors {
                     DragValue.Center at 0f
                     DragValue.End at size.width.dp.toPx()
                 }
             }
         )
     }*/
    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .onSizeChanged {
                    contextMenuWidth = it.width.toFloat()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            actions()
        }
        Surface(
            modifier = Modifier
                .onSizeChanged {
                    size = it
                }
                .fillMaxWidth()
                .offset {
                    IntOffset(
                        offset.value.toInt()
                        /*anchoredDraggableState
                            .requireOffset()
                            .toInt()*/,
                        0
                    )
                }
                /*.anchoredDraggable(
                    state = anchoredDraggableState,
                    orientation = Orientation.Horizontal
                )*/
                .indication(interactionSource, LocalIndication.current)
                .pointerInput(contextMenuWidth) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = (offset.value + dragAmount)
                                    .coerceIn(0f, contextMenuWidth)
                                offset.snapTo(newOffset.takeIf { it > 0f } ?: 0f)
                            }
                        },
                        onDragEnd = {
                            when {
                                offset.value >= contextMenuWidth / 2f -> {
                                    scope.launch {
                                        offset.animateTo(size.width.toFloat())
                                        listener.onExpanded()
                                    }
                                }

                                else -> {
                                    scope.launch {
                                        offset.animateTo(0f)
                                        listener.onCollapsed()
                                    }
                                }
                            }
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                            expanded = true
                            listener.onLongPress()
                        },
                        onPress = {
                            val press = PressInteraction.Press(it)
                            interactionSource.emit(press)
                            tryAwaitRelease()
                            interactionSource.emit(PressInteraction.Release(press))
                        }
                    )
                }
                .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
        ) {
            Card(
                modifier = Modifier.onSizeChanged {
                    itemHeight = with(density) { it.height.toDp() }
                },
                shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp),
                border = BorderStroke(0.3.dp, Color.DarkGray),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .heightIn(max = 60.dp)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        GlideImage(
                            modifier = Modifier.aspectRatio(1f),
                            failure = placeholder(br.com.joaovq.core_ui.R.drawable.ic_error_image),
                            model = pet.imageUrl,
                            contentDescription = "Photo of ${pet.nickname}",
                            transition = CrossFade
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            pet.name,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            pet.breed,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        )
                    }
                }
                DropdownMenu(
                    expanded,
                    onDismissRequest = { expanded = false },
                    offset = pressOffset.copy(
                        y = pressOffset.y - itemHeight
                    ),
                    properties = PopupProperties()
                ) {
                    DropdownMenuItem(
                        onClick = {
                            listener.onDeleteItemClick()
                            expanded = false
                        },
                        text = {
                            Text(text = stringResource(R.string.delete_pet_title_pop_menu))
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
@Suppress("Detekt.FunctionNaming", "Detekt.UnusedPrivateMember")
private fun PetCardPreview() {
    MyDailyPetTheme(dynamicColor = false) {
        PetCard(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 30.dp),
            Pet(
                name = "Nina",
                breed = "Schnauzer",
                birthAlarm = NotificationAlarmItem(
                    0,
                    messageNotification = "happy birthday",
                    description = "Hello",
                    id = UUID.randomUUID()
                ),
            ),
            listener = object : PetCardListener {
                override fun onExpanded() {
                    TODO("Not yet implemented")
                }

                override fun onCollapsed() {
                    TODO("Not yet implemented")
                }

                override fun onLongPress() {
                    TODO("Not yet implemented")
                }

                override fun onDeleteItemClick() {
                    TODO("Not yet implemented")
                }

            }
        )
    }
}

interface PetCardListener {
    fun onExpanded()
    fun onCollapsed()
    fun onLongPress()
    fun onDeleteItemClick()
}
