package br.com.joaovq.mydailypet.home.presentation.compose

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDirections
import br.com.joaovq.mydailypet.R
import br.com.joaovq.mydailypet.home.presentation.view.HomeFragmentDirections

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesNav(
    modifier: Modifier = Modifier,
    onClickCategory: (NavDirections) -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val border = BorderStroke(.4.dp, MaterialTheme.colorScheme.onSecondary)
        categoryDirections.forEach { category ->
            Box(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(89.dp)
                        .align(Alignment.Center),
                    border = border,
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(16.dp),
                    onClick = {
                        onClickCategory(category.direction)
                    },
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = category.textId),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Icon(
                            modifier = Modifier.fillMaxWidth(),
                            painter = painterResource(id = category.iconId),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun CategoryNavPreview() {
    CategoriesNav()
}

val categoryDirections = listOf(
    CategoryDirectionButton(
        R.string.text_add_pet,
        R.drawable.ic_foot_pet,
        HomeFragmentDirections.actionHomeFragmentToAddPetFragment()
    ),
    CategoryDirectionButton(
        R.string.text_reminders_daily,
        br.com.joaovq.core_ui.R.drawable.ic_calendar,
        HomeFragmentDirections.actionHomeFragmentToReminderListFragment()
    ),
    CategoryDirectionButton(
        R.string.text_tasks,
        R.drawable.ic_add_box_category,
        HomeFragmentDirections.actionHomeFragmentToTaskFragment()
    )
)


data class CategoryDirectionButton(
    @StringRes val textId: Int,
    @DrawableRes val iconId: Int,
    val direction: NavDirections
)