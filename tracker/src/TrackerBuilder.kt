package com.shenghaiyang.tracker

/**
 * Builds a [Tracker] that dispatches events to the registered [TrackHandler]s.
 *
 * Handlers are invoked synchronously in registration order. The builder is mutable
 * and not thread-safe; build the tracker once during application initialization.
 */
class TrackerBuilder {
    private val handlers = mutableListOf<TrackHandler>()

    /**
     * Registers a handler that will receive every tracked event.
     *
     * @param handler the handler to add.
     * @return this builder, to allow chaining.
     */
    fun addHandler(handler: TrackHandler): TrackerBuilder = apply {
        handlers.add(handler)
    }

    /**
     * Creates a tracker from a snapshot of the currently registered handlers.
     *
     * Handlers added after this call do not affect the returned tracker. If no
     * handlers were registered, the returned tracker ignores every event.
     */
    fun build(): Tracker = DefaultTracker(handlers.toList())
}

/**
 * Creates a [Tracker] using a builder-style configuration block.
 *
 * @param block the configuration applied to a new [TrackerBuilder].
 * @return the built tracker.
 */
inline fun buildTracker(crossinline block: TrackerBuilder.() -> Unit): Tracker = TrackerBuilder()
    .apply(block)
    .build()

private class DefaultTracker(
    private val handlers: List<TrackHandler>,
) : Tracker {
    override fun track(event: String, parameters: Map<String, Any?>) {
        handlers.forEach { it.onTrack(event, parameters) }
    }
}