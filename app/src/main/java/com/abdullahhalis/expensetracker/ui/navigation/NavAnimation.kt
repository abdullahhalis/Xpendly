package com.abdullahhalis.expensetracker.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

object NavAnimation {

    private const val FORWARD_DURATION = 300
    private const val BACK_DURATION = 250

    private fun enter(
        direction: AnimatedContentTransitionScope.SlideDirection,
        duration: Int,
    ): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideIntoContainer(
            direction,
            animationSpec = tween(duration)
        ) + fadeIn(
            tween(duration)
        )
    }

    private fun exit(
        direction: AnimatedContentTransitionScope.SlideDirection,
        duration: Int,
    ): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutOfContainer(
            direction,
            animationSpec = tween(duration)
        ) + fadeOut(
            tween(duration)
        )

    }

    val forwardEnter = enter(
        AnimatedContentTransitionScope.SlideDirection.Left,
        FORWARD_DURATION
    )

    val forwardExit = exit(
        AnimatedContentTransitionScope.SlideDirection.Left,
        FORWARD_DURATION
    )

    val backEnter = enter(
        AnimatedContentTransitionScope.SlideDirection.Right,
        BACK_DURATION
    )

    val backExit = exit(
        AnimatedContentTransitionScope.SlideDirection.Right,
        BACK_DURATION
    )
}