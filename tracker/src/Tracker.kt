package com.shenghaiyang.tracker

/**
 * Entry point for reporting analytics events.
 *
 * A tracker synchronously forwards each event to every [TrackHandler] registered
 * through [TrackerBuilder], in registration order. Implementations are stateless
 * after construction and may be called from any thread.
 */
interface Tracker {
    /**
     * Reports an event.
     *
     * @param event the event name, for example `app_open` or `purchase`.
     * @param parameters optional event parameters. Values are passed to handlers
     *   as-is. Common value types are [String], [Boolean], [Int], [Long], [Float],
     *   [Double], and `null`; each handler defines its own mapping. Defaults to an
     *   empty map.
     */
    fun track(
        event: String,
        parameters: Map<String, Any?> = emptyMap(),
    )
}
