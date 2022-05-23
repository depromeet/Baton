package com.depromeet.baton.presentation.ui.routing

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityRoutingBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.main.MainActivity
import com.depromeet.baton.presentation.ui.routing.RoutingViewModel.ViewEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

@AndroidEntryPoint
class RoutingActivity : BaseActivity<ActivityRoutingBinding>(R.layout.activity_routing) {

    private val viewModel: RoutingViewModel by viewModels()
    private val onSplashEnd = MutableSharedFlow<Unit>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        splashScreen.setOnExitAnimationListener {
            onSplashEnd.tryEmit(Unit)
        }

        onSplashEnd.combine(viewModel.viewEvents) { _, events -> events}
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleViewEvents(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleViewEvents(viewEvents: List<ViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                ViewEvent.ToHome -> {
                    MainActivity.start(this)
                }
                ViewEvent.ToLogIn -> TODO()
            }
            viewModel.consumeViewEvent(viewEvent)
        }
    }
}
