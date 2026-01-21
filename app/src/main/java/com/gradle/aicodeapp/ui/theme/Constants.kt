package com.gradle.aicodeapp.ui.theme

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween

object Animations {
    const val DurationShort: Int = 150
    const val DurationMedium: Int = 300
    const val DurationLong: Int = 500

    val AnimationSpecShort: AnimationSpec<Float> = tween(DurationShort)
    val AnimationSpecMedium: AnimationSpec<Float> = tween(DurationMedium)
    val AnimationSpecLong: AnimationSpec<Float> = tween(DurationLong)

    const val StaggerDelay: Int = 50
    const val BannerAutoScrollDelay: Long = 3000L
    const val ClickDebounceDelay: Long = 500L
}

object Sizes {
    val BannerHeight: Int = 200
    val BannerWidth: Int = 300
    val BannerItemHeight: Int = 180

    val AvatarSize: Int = 80

    val IconSizeSmall: Int = 16
    val IconSizeMedium: Int = 24
    val IconSizeLarge: Int = 32
    val IconSizeExtraLarge: Int = 48

    val ImageThumbnailSize: Int = 100
}

object Constraints {
    val MaxTitleLines: Int = 2
    val MaxDescriptionLines: Int = 3

    val MinTouchTarget: Int = 48
}