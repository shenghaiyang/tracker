package com.shenghaiyang.tracker

/**
 * Receives events dispatched by a [Tracker].
 *
 * Implementations must not throw. An exception propagates synchronously to the
 * caller of [Tracker.track] and prevents delivery to the remaining handlers.
 */
fun interface TrackHandler {
    /**
     * Called for every tracked event.
     *
     * @param event the event name.
     * @param parameters the event parameters, passed through without modification.
     */
    fun onTrack(
        event: String,
        parameters: Map<String, Any?>,
    )
}