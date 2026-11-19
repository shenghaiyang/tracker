# tracker

[![License: Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

**A thin abstraction layer for analytics events in Kotlin Multiplatform.**

`tracker` owns two things: the shape of an event and the act of reporting it.
Product code reports once; one or more [handlers](#writing-a-handler) decide
where the event goes. No SDK types leak into your features, and the core has no
dependencies.

## Why

Analytics code tends to spread across the codebase — a Firebase call in one
feature, a custom endpoint in another. `tracker` keeps that boundary in one place:

- one event shape: `(event, parameters)`
- one API for feature code: `Tracker.track`
- any number of destinations behind `TrackHandler`
- zero dependencies in the core

## Installation

> Maven Central publication is in progress; the first release will be `0.1.0`.

```kotlin
commonMain.dependencies {
    implementation("com.shenghaiyang:tracker:0.1.0")
}
```

## Usage

```kotlin
val tracker = buildTracker {
    addHandler(TrackHandler { event, parameters ->
        println("$event $parameters")
    })
}

tracker.track("app_open")
tracker.track(
    "purchase",
    mapOf("value" to 9.99, "currency" to "CNY"),
)
```

The same thing with the builder:

```kotlin
val tracker = TrackerBuilder()
    .addHandler(ConsoleHandler())
    .addHandler(MyAnalyticsHandler())
    .build()
```

## Writing a handler

```kotlin
class MyAnalyticsHandler : TrackHandler {
    override fun onTrack(event: String, parameters: Map<String, Any?>) {
        MyAnalyticsSdk.log(event, parameters)
    }
}
```

Parameters are passed through untouched. Common value types are `String`,
`Boolean`, `Int`, `Long`, `Float`, `Double`, and `null`; each handler maps them
to whatever its backend accepts.

## Behavior

- Handlers run synchronously on the calling thread, in registration order.
- `build()` snapshots the registered handlers; later `addHandler` calls do not
  affect an existing tracker.
- A tracker without handlers is a no-op.
- Parameters are neither validated nor transformed.
- Handlers must not throw. An exception propagates to the caller and stops
  delivery to the remaining handlers.
- A tracker is immutable after construction and safe to call from any thread.

## Modules

| Module | Purpose |
| --- | --- |
| `tracker` | Core abstraction: `Tracker`, `TrackHandler`, `TrackerBuilder` |
| `tracker-firebase` | Firebase Analytics implementation (planned) |

## Build and test

```bash
./kotlin build
./kotlin test -m tracker
```

## License

This project is licensed under the [Apache License, Version 2.0](LICENSE).

Copyright (c) 2026 shenghaiyang.
