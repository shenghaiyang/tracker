package com.shenghaiyang.tracker

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class TrackerTest {

    @Test
    fun `handlers receive events in registration order`() {
        val calls = mutableListOf<Pair<String, Map<String, Any?>>>()
        val tracker = TrackerBuilder()
            .addHandler(TrackHandler { event, parameters -> calls += "first:$event" to parameters })
            .addHandler(TrackHandler { event, parameters -> calls += "second:$event" to parameters })
            .build()
        val parameters = mapOf<String, Any?>("source" to "launcher", "user_id" to null)

        tracker.track("app_open", parameters)

        assertEquals(
            listOf("first:app_open" to parameters, "second:app_open" to parameters),
            calls,
        )
    }

    @Test
    fun `parameters default to an empty map`() {
        var received: Map<String, Any?>? = null
        val tracker = TrackerBuilder()
            .addHandler(TrackHandler { _, parameters -> received = parameters })
            .build()

        tracker.track("app_open")

        assertEquals(emptyMap<String, Any?>(), received)
    }

    @Test
    fun `empty builder does nothing`() {
        TrackerBuilder().build().track("app_open")
    }

    @Test
    fun `build snapshots handlers`() {
        val calls = mutableListOf<String>()
        val builder = TrackerBuilder()
            .addHandler(TrackHandler { event, _ -> calls += "first:$event" })
        val tracker = builder.build()
        builder.addHandler(TrackHandler { event, _ -> calls += "second:$event" })

        tracker.track("app_open")

        assertEquals(listOf("first:app_open"), calls)
    }

    @Test
    fun `handler exception stops dispatch`() {
        var secondCalled = false
        val tracker = TrackerBuilder()
            .addHandler(TrackHandler { _, _ -> error("boom") })
            .addHandler(TrackHandler { _, _ -> secondCalled = true })
            .build()

        assertFailsWith<IllegalStateException> { tracker.track("app_open") }
        assertFalse(secondCalled)
    }
}
