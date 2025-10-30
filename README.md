
# 🌦️ WeatherApp – Jetpack Compose

Clean Architecture + MVVM + MVI + Jetpack Compose + Kotlin + Coroutines + Retrofit + Okhttp + Navigation Type safety + Hilt + Kotlinx Serialization + Coil + DataStore + Joda Time + Firebase


---

## 🧩 Tech Stack Overview

**Core Technologies:**
Kotlin｜Jetpack Compose｜Coroutines｜Flow｜Clean Architecture｜Hilt｜Retrofit｜OkHttp｜Kotlinx Serialization｜DataStore｜Joda-Time｜Firebase Remote Config｜MockK｜Kover｜Google Truth｜Robolectric

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
* 📱 **Responsive Layouts** for phone
* 🔌 **Offline Detection** with network alerts

---

### 🔹 Architecture Pattern

Follows **Clean Architecture** principles with strict layer boundaries:

* **Presentation Layer**: UI logic (Compose + ViewModel)
* **Domain Layer**: Use Cases (pure business rules)
* **Data Layer**: Repository, API, DTOs, and mappers

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
* **Edge-to-Edge Layout** – Immersive UI experience
* **Splash Screen API** – Native launch experience
* **Firebase Remote Config** – Dynamically updates UI and feature flags without releasing a new app build
* **Dynamic Dark / Light Theme** – Supports automatic theme switching and custom Material3 color schemes for consistent visual experience

### Unit Testing

* **JUnit4 / Kotlin Test** – Core testing framework for unit and integration tests
* **kotlinx-coroutines-test** – Virtual time scheduler and structured coroutine testing
* **MockK** – Mocking library for Kotlin, supports suspend functions and verification
* **Google Truth** – Fluent assertion library for human-readable test validation
* **Robolectric** – Simulates Android runtime for local DataStore and Context-based testing
* **TemporaryFolder (JUnit Rule)** – Creates isolated temp files for DataStore
* **Firebase Tasks Mocking** – Mocks async Remote Config tasks for success/failure verification
* **Kover Gradle Plugin** – Generates detailed coverage report in HTML for CI/CD pipelines
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


### Configuration

1. Register for an API key on [Visual Crossing Weather API](https://www.visualcrossing.com/)
2. Add key via **Firebase Remote Config** or local config
3. Download `google-services.json` to `/app`

---

## 🧪 Testing & Coverage

### Domain Module Testing

* Pure business logic tests using **MockK** and **Google Truth**
* Deterministic time control via fixed `DateTimeZone.forOffsetHours(8)`
* All UseCases isolated and repository calls verified with `coVerify`

**📊 COVERAGE SUMMARY (AS OF 2025-10-29)**

| Layer       | Coverage  | Status                                      |
| ----------- | --------- | ------------------------------------------- |
| **Model**   | 100%      | ✅ Fully tested                              |
| **UseCase** | 45%       | ⚠️ Partial coverage – add minor smoke tests |
| **Overall** | **78.6%** | ⚠️ Near CI threshold (80%)                  |

> Adding 3–5 small unit tests will raise coverage above 80% and meet the `minBound(80)` CI rule.

**▶ RUN TESTS**

```bash
./gradlew :domain:test
./gradlew :domain:koverHtmlReport
```
---

###Data Module Testing (Updated 2025-10-31)

**📌 HIGHLIGHTS**

* ✅ `data.mapper` maintains **100% line coverage** (complete verification for Hour/Day/Timeline conversion).
* ✅ Added `DataStoreRepositoryImplTest`, covering all DataStore I/O and serialization/deserialization logic (EC1–EC5).
* ✅ Added `AssetsLocalDataSourceImplTest`, covering UTF-8 reading, exception propagation, and Unicode (Chinese) content retention (EC1–EC4).
* ✅ Added `RemoteConfigDataSourceImplTest`, covering both success/failure of `fetchAndActivate()` and correctness of `getString()` (EC1–EC5).
* ✅ `ConfigRepositoryImplTest` covers success, empty value fallback, remote failure, and decoding scenarios (EC1–EC4).
* ✅ `WeatherRepositoryImplTest` covers all major safeApiCall branches: 200/null/404/network/parse.
* 🧠 All tests follow **Equivalence Class (EC)** design and **Given / When / Then** structure.
* 🧩 Full coverage across Firebase Remote Config, DataStore, Assets I/O, and Retrofit network layer.

---

**📊 COVERAGE SUMMARY (AFTER 2025-10-31 ENHANCEMENT)**

| Package            |              Class |               Method |              Branch |                   Line |              Instruction |
| :----------------- | -----------------: | -------------------: | ------------------: | ---------------------: | -----------------------: |
| `data.api`         |          **100 %** |            **100 %** |                   – |               **75 %** |                 **75 %** |
| `data.datasource`  |         **45.5 %** |           **38.5 %** |            **80 %** |               **62 %** |               **56.9 %** |
| `data.mapper`      |          **100 %** |            **100 %** |          **62.1 %** |              **100 %** |               **88.1 %** |
| `data.model`       |          **100 %** |            **100 %** |             **0 %** |              **100 %** |               **42.9 %** |
| `data.repository`  |           **50 %** |           **38.5 %** |            **75 %** |               **69 %** |               **81.9 %** |
| `data.utils`       |          **100 %** |            **100 %** |            **25 %** |             **21.4 %** |               **22.6 %** |
| **Overall (data)** | **60 % (18 / 30)** | **47.1 % (32 / 68)** | **14 % (62 / 442)** | **73.2 % (161 / 220)** | **53.7 % (1665 / 3101)** |

**✅ Line coverage improved to 73.2% (+ ~6%)**

---

**🧪 IMPLEMENTED TEST CLASSES**

| Test Class                       | Focus                                    | EC Count |
| -------------------------------- | ---------------------------------------- | :------: |
| `MapperTest`                     | Hour/Day/Timeline mapping verification   |     3    |
| `WeatherRepositoryImplTest`      | safeApiCall (200/null/404/network/parse) |     5    |
| `ConfigRepositoryTest`           | Remote Config fetch + Assets fallback    |     4    |
| `DataStoreRepositoryTest`        | DataStore save/restore behavior          |     5    |
| `AssetsLocalDataSourceTest`  | UTF-8 reading + exception handling       |     4    |
| `RemoteConfigDataSourceTest` | Firebase Remote Config behavior                     |     5    |
| **Total EC Cases**               | –                                        |    26    |

---

**▶ RUN TESTS AND GENERATE COVERAGE (MACOS)**

```bash
# Run all Data module tests
./gradlew :data:test

# Generate HTML coverage report
./gradlew :data:koverHtmlReport
open data/build/reports/kover/html/index.html
```

Or using the provided shell script:

```bash
chmod +x data/run_unit_test_data.sh
./data/run_unit_test_data.sh
```

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

