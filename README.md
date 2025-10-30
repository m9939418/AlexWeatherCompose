
# 🌦️ WeatherApp – Jetpack Compose

Clean Architecture + MVVM + MVI + Jetpack Compose + Kotlin + Coroutines + Retrofit + Okhttp + Navigation Type safety + Hilt + Kotlinx Serialization + Coil + DataStore + Joda Time + Firebase


---

## 🧩 Tech Stack Overview

**Core Technologies:**
Kotlin · Jetpack Compose · Coroutines · Flow · Clean Architecture · Hilt · Retrofit · OkHttp · Kotlinx Serialization · DataStore · Coil · Joda-Time · Firebase Remote Config

**Architecture Layers:**

* **Presentation** – Compose UI, ViewModels, navigation, and UI state management
* **Domain** – Business logic, UseCases, and pure domain models
* **Data** – API, repository implementations, mappers, and local persistence

---

## 📋 Feature Overview

### ✅ Core Features

* 🌡️ **Current Weather** – Real-time temperature, feels-like, and condition summary
* ⏰ **Hourly Forecast** – 24-hour timeline with visual temperature and precipitation data
* 📅 **Weekly Forecast** – 7-day overview with trend insights
* 🏙️ **Multi-City Support** – Weather data across all Taiwan cities and counties

### 📊 Detailed Metrics

Temperature · Feels-like · Humidity · Dew Point · Precipitation · Wind Speed · Gusts · Cloud Cover · Visibility · Sunrise/Sunset · UV Index · Pressure

### 💎 User Experience

* 🌓 **Dark / Light Mode**
* 🎨 **Material Design 3** + Dynamic Color (Android 12+)
* ⚡ **Smooth Animations & Transitions**
* 📱 **Responsive Layouts** for phone/tablet
* 🔌 **Offline Detection** with network alerts

---

## 🏗️ Project Architecture

```
📦 app
├── presentation/      # Compose UI & ViewModels
├── domain/            # Business logic & UseCases
└── data/              # API, repository, mapper, and local storage
```

### 🔹 Architecture Pattern

Follows **Clean Architecture** principles with strict layer boundaries:

* **Presentation Layer**: UI logic (Compose + ViewModel)
* **Domain Layer**: Use Cases (pure business rules)
* **Data Layer**: Repository, API, DTOs, and mappers

### 🔹 Data Flow

Unidirectional data flow (MVI) with `StateFlow`:

```
UI → ViewModel (Intent) → UseCase → Repository → ViewModel (State) → UI
```

---

## ⚙️ Technology Breakdown

### Jetpack Components

* **Navigation Compose** – Type-safe navigation
* **ViewModel + StateFlow** – Lifecycle-aware state management
* **Hilt DI** – Scalable dependency injection
* **DataStore** – Key-value storage for user preferences

### Networking

* **Retrofit + OkHttp** – REST client with interceptors
* **Kotlinx Serialization** – Lightweight JSON parser
* **Mock Interceptor** – Local mock data for debugging

### UI & UX

* **Material 3** – Modern UI guidelines
* **Coil** – Image loading and caching
* **Edge-to-Edge Layout** – Immersive UI experience
* **Splash Screen API** – Native launch experience

---

## 🧭 Key Screens

| Screen                 | Description                                             |
| ---------------------- | ------------------------------------------------------- |
| **HomeScreen**         | Current + hourly + weekly forecast with refresh support |
| **TodayDetailScreen**  | 24-hour weather timeline with all metrics               |
| **WeeklyDetailScreen** | Swipeable 7-day forecast with hourly breakdown          |
| **WebScreen**          | In-app WebView with share & back navigation             |
| **CitySelectionSheet** | BottomSheet city picker for switching regions           |

---

## 🧱 Build Setup

### Prerequisites

* **Android Studio Hedgehog (2023.1.1)** or later
* **JDK 17**
* **Android SDK 36 (minSdk 24)**

### Installation

```bash
git clone https://github.com/your-username/weather-app.git
cd weather-app
```

### Configuration

1. Register for an API key on [Visual Crossing Weather API](https://www.visualcrossing.com/)
2. Add key via **Firebase Remote Config** or local config
3. Download `google-services.json` to `/app`

---

## 🧩 Main Dependencies

```gradle
// Jetpack Compose
implementation "androidx.compose.ui:ui:1.5.4"
implementation "androidx.compose.material3:material3:1.1.2"

// Navigation
implementation "androidx.navigation:navigation-compose:2.7.5"

// Dependency Injection
implementation "com.google.dagger:hilt-android:2.48"
kapt "com.google.dagger:hilt-compiler:2.48"

// Networking
implementation "com.squareup.retrofit2:retrofit:2.9.0"
implementation "com.squareup.okhttp3:okhttp:4.12.0"

// Serialization
implementation "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0"

// Persistence
implementation "androidx.datastore:datastore-preferences:1.0.0"

// Image Loading
implementation "io.coil-kt:coil-compose:2.5.0"

// Firebase
implementation "com.google.firebase:firebase-config-ktx:21.5.0"
```

---

## 🧪 Testing & Coverage

### Domain Module Testing

* Pure business logic tests using **MockK** and **Google Truth**
* Deterministic time control via fixed `DateTimeZone.forOffsetHours(8)`
* All UseCases isolated and repository calls verified with `coVerify`

**Coverage Summary (as of 2025-10-29)**

| Layer       | Coverage  | Status                                      |
| ----------- | --------- | ------------------------------------------- |
| **Model**   | 100%      | ✅ Fully tested                              |
| **UseCase** | 45%       | ⚠️ Partial coverage – add minor smoke tests |
| **Overall** | **78.6%** | ⚠️ Near CI threshold (80%)                  |

> Adding 3–5 small unit tests will raise coverage above 80% and meet the `minBound(80)` CI rule.

**Run Tests**

```bash
./gradlew :domain:test
./gradlew :domain:koverHtmlReport
```
---

### Data Module Testing (2025-10-30)

**Key Highlights**
- ✅ 100% coverage for `data.mapper` (all conversion logic verified).
- ✅ `ConfigRepository` uses `runCatching` for safe remote config handling (no crash on failure).
- ✅ `AppPreferences` testable via injected `DataStore`.
- ✅ Major `safeApiCall` branches covered (200/null/404/network/parse).
- 🧠 EC-based (Equivalence Class) testing style with `Given / When / Then` consistency.


**📊 Coverage Summary**

| Package | Class | Method | Branch | Line | Instruction |
|----------|-------:|--------:|--------:|--------:|-------------:|
| `data.api` | **100%** | **100%** | – | **75%** | **75%** |
| `data.local` | **80%** | 46.2% | **60%** | **70%** | **77.8%** |
| `data.mapper` | **100%** | **100%** | **62.1%** | **100%** | **88.1%** |
| `data.model` | **70%** | **72.7%** | 0.3% | **89.5%** | 42.1% |
| `data.repository` | **50%** | 35.7% | **83.3%** | **66.7%** | **80.3%** |
| `data.utils` | **100%** | **100%** | 25% | 21.4% | 22.6% |
| **Overall (data)** | **66.7%** | **51.7%** | **13.5%** | **73.6%** | **54.1%** |

**Highlights**
- 🟢 **Line coverage improved to 73.6% (+5%)**.
- `data.mapper` achieved **100% line coverage**.
- `data.repository` now covers both success and major error branches (**83% branch coverage**).
- `data.local` confirmed **default & fallback correctness** via Robolectric tests.



**🧩 Testing Strategy**

- **EC (Equivalence Class)**–based test cases
- **Given / When / Then** naming style for clarity
- **Robolectric** for Android environment emulation
- **kotlinx-coroutines-test** using **StandardTestDispatcher** + **MainDispatcherRule**
- **MockK** for suspend and Firebase Task mocking (`Tasks.forResult`, `Tasks.forException`)
- **Google Truth** for expressive, stable assertions


**▶︎ How to Run Tests & View Coverage (macOS)**

A simple shell script is provided to **run tests** for the `data` module and automatically **open the Kover HTML report**.

```bash
# Make the script executable (first time only)
chmod +x data/run_unit_test_data.sh

# Run tests and open report
./data/run_unit_test_data.sh
```

**What this script does**
1. Switches to the project root (where `gradlew` is located)
2. Runs the unit tests
3. Generates the coverage report
4. Automatically opens the HTML report:

---

### 🔐 Security

* API key stored in **Firebase Remote Config**
* All network traffic over **HTTPS**
* No sensitive data hardcoded
* **R8 / ProGuard** enabled for release builds

---

### ⚡ Performance Highlights

* Optimized `LazyColumn` rendering with stable keys
* Cached image loading via Coil
* Smart retry and timeout in OkHttp
* Efficient ViewModel cleanup and recomposition control

---

### 🧭 Roadmap

* [ ] Weather alerts & notifications
* [ ] Chart visualization (temperature, humidity)
* [ ] Multi-language support
* [ ] CI/CD integration
* [ ] 90% test coverage goal

---

### 👨‍💻 Author

**Alex Yang**

* GitHub: [@m9939418](https://github.com/m9939418)

---

### 🙏 Acknowledgments

* [Visual Crossing Weather API](https://www.visualcrossing.com/)
* [Material Design 3](https://m3.material.io/)
* [Jetpack Compose](https://developer.android.com/jetpack/compose)

---

> ⭐ If you find this project helpful, please consider giving it a Star on GitHub!

---

