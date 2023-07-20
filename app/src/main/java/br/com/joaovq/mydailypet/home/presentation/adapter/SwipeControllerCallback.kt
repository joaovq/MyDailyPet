package br.com.joaovq.mydailypet.home.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import br.com.joaovq.mydailypet.R

internal enum class ButtonsState {
    GONE, LEFT_VISIBLE
}

@SuppressLint("ClickableViewAccessibility")
class SwipeControllerCallback(
    private val context: Context,
    private val onSwipedAction: (position: Int) -> Unit,
) : Callback() {

    private var swipeBack: Boolean = false
    private var buttonShowedState = ButtonsState.GONE
    private var buttonInstance: RectF? = null

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        return makeMovementFlags(0, RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return true
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        drawButtons(c, viewHolder)
    }

    private fun drawButtons(c: Canvas, viewHolder: ViewHolder) {
        val buttonWidthWithoutPadding = (buttonWidth - 30).toFloat()
        val corners = 30f
        val itemView = viewHolder.itemView
        val p = Paint()
        val leftButton = RectF(
            itemView.left.toFloat(),
            itemView.top.toFloat(),
            (itemView.left + itemView.right).toFloat(),
            itemView.bottom.toFloat(),
        )
        p.color = ContextCompat.getColor(context, R.color.color_primary)
        c.drawRoundRect(leftButton, corners, corners, p)
        drawImage(c, leftButton, p)
        buttonInstance = null
        buttonInstance = leftButton
    }

    private fun drawImage(c: Canvas, button: RectF, p: Paint) {
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_eye)
        drawable?.setTint(ContextCompat.getColor(context, R.color.color_on_secondary))
        val iconSize = (buttonWidth / (buttonWidth / 100)).toInt()
        val bitmap = drawable?.toBitmap(width = iconSize, height = iconSize)
        bitmap?.let {
            c.drawBitmap(
                it,
                button.centerX() - (3 * buttonWidth / 2),
                button.centerY() - (iconSize / 2),
                p,
            )
        }
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        onSwipedAction(viewHolder.adapterPosition)
    }

    override fun getSwipeThreshold(viewHolder: ViewHolder): Float {
        return 0.5f
    }

    companion object {
        private var buttonWidth = 300F
    }
}
