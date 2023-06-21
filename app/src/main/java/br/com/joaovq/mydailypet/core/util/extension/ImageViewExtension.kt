package br.com.joaovq.mydailypet.core.util.extension

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import br.com.joaovq.mydailypet.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

fun ImageView.loadImage(
    url: String,
    error: Drawable? = ContextCompat.getDrawable(this.context, R.drawable.ic_error_image),
    placeholder: Drawable? = ContextCompat.getDrawable(this.context, R.drawable.ic_placeholder),
) {
    Glide.with(this.context).load(url)
        .placeholder(placeholder)
        .error(error)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}
