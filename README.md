# WeatherApp - Compose

Clean Architecture + MVVM + MVI + Jetpack Compose + Kotlin + Coroutines + Retrofit + Okhttp + Navigation Type safety + Hilt + Kotlinx Serialization + Coil + DataStore + Joda Time + Firebase

## 📋 Feature Requirements

Build an Android application that displays weather forecasts with the following features:

- ✅ **Display "Current" Weather Forecast** - Provide detailed weather information for today with hourly forecasts
- ✅ **Display "Weekly" Weather Forecast** - Show weather overview and detailed data for the next 7 days
- ✅ **Provide "City List"** - Allow users to select cities and view weather information for different regions

## ✨ Key Features

### Core Features
- 🌡️ **Real-time Weather Information** - Display current temperature, feels-like temperature, and weather conditions
- ⏰ **Hourly Forecast** - Provide detailed weather changes for the next 24 hours
- 📅 **Weekly Forecast** - Show weather trends for the next 7 days
- 🏙️ **Multi-city Support** - Cover weather data for all cities and counties in Taiwan

### Detailed Weather Metrics
- 🌡️ Temperature and Feels-like Temperature
- 💧 Humidity and Dew Point
- 🌧️ Precipitation Probability
- 💨 Wind Speed and Gusts
- ☁️ Cloud Cover Percentage
- 👁️ Visibility
- 🌅 Sunrise and Sunset Times
- ☀️ UV Index
- 🔘 Atmospheric Pressure

### User Experience
- 🌓 **Dark Mode Support** - Automatically adapts to system theme
- 🎨 **Material Design 3** - Modern UI design
- 🎭 **Dynamic Colors** - Android 12+ system dynamic color support
- 📱 **Responsive Layout** - Adapts to different screen sizes
- ⚡ **Smooth Animations** - Splash Screen and page transition animations
- 🔌 **Offline Alerts** - Smart network status detection

## 🏗️ Technical Architecture

### Architecture Pattern
Adopts **Clean Architecture** with three layers:

```
📦 app
├── 📂 presentation (UI Layer)
│   ├── screens/       # Feature screens
│   ├── components/    # Reusable UI components
│   ├── navigation/    # Navigation configuration
│   └── theme/         # Theme styles
│
├── 📂 domain (Domain Layer)
│   ├── model/         # Domain models
│   ├── repository/    # Repository interfaces
│   └── usecase/       # Business logic Use Cases
│
└── 📂 data (Data Layer)
    ├── api/           # API service definitions
    ├── model/         # DTO data models
    ├── mapper/        # Data converters
    ├── repository/    # Repository implementations
    └── local/         # Local storage (DataStore)
```

### Tech Stack

#### Core Technologies
- **Kotlin** - Primary development language
- **Jetpack Compose** - Declarative UI framework
- **Coroutines & Flow** - Asynchronous processing and reactive programming
- **Clean Architecture approach**

#### Jetpack Components
- **Navigation Compose** - Type-safe navigation system
- **ViewModel** - MVVM architecture state management
- **Hilt** - Dependency injection framework
- **DataStore** - Modern data persistence
- **Lifecycle** - Lifecycle-aware components

#### Network Layer
- **Retrofit** - REST API client
- **OkHttp** - HTTP client and interceptors
- **Kotlinx Serialization** - JSON serialization/deserialization

#### Data Sources
- **Visual Crossing Weather API** - Weather data provider
- **Firebase Remote Config** - Remote configuration management

#### UI/UX
- **Material 3** - Latest Material Design version
- **Coil** - High-performance image loading library
- **Joda-Time** - Date and time handling

#### Other Tools
- **Splash Screen API** - Launch screen
- **Edge-to-Edge** - Full-screen immersive experience

## 📱 App Screenshots

### Main Pages
- **Home** - Today's weather + hourly forecast + weekly forecast
- **Today Detail** - Complete 24-hour weather data
- **Weekly Detail** - 7-day detailed forecast with swipe support
- **City Selection** - ModalBottomSheet for city/county selection
- **Side Menu** - About API, GitHub links

## 🎯 Page Function Description

### 1. Home Screen (HomeScreen)
- **Current Weather Card** - Display today's high/low temperatures, weather conditions, icon
- **Hourly Forecast** - Horizontal scrolling list showing the next 16 hours of weather
- **Weekly Forecast** - Overview of the next 7 days' weather
- **Other Metrics** - Sunrise/sunset, UV index, humidity, visibility, wind speed, pressure
- **City Switching** - Click title bar to select different cities
- **Pull to Refresh** - Update latest weather data

### 2. Today Detail Screen (TodayDetailScreen)
- Display detailed data for 24 hours after the selected time point
- Hourly information includes:
    - Time
    - Temperature and feels-like temperature
    - Weather conditions and icon
    - Precipitation probability
    - Wind speed
    - Cloud cover

### 3. Weekly Detail Screen (WeeklyDetailScreen)
- **Tab Switching** - Scrollable date tabs at the top
- **HorizontalPager** - Swipe to switch between different dates
- **Daily Summary** - High/low temperatures, weather conditions
- **Hourly List** - 24-hour detailed data for that day

### 4. WebView Page (WebScreen)
- Embedded browser displaying external links
- Loading progress bar support
- Share functionality
- Back button browsing history support

## 🔧 Project Setup

### Environment Requirements
- Android Studio Hedgehog (2023.1.1) or higher
- JDK 17 or higher
- Android SDK 34
- Minimum SDK: 26 (Android 8.0)
- Target SDK: 34 (Android 14)

### Build Steps

1. **Clone Project**
```bash
git clone https://github.com/your-username/weather-app.git
cd weather-app
```

2. **Set up API Key**
    - Register at [Visual Crossing Weather API](https://www.visualcrossing.com/)
    - Set `api_key` in Firebase Remote Config
    - Or configure API Key locally

3. **Firebase Setup**
    - Create a project in Firebase Console
    - Download `google-services.json` and place in `app/` directory
    - Configure Remote Config parameters:
        - `api_key`: Weather API key
        - `tw_counties_v1`: Taiwan cities/counties JSON data (optional)

4. **Build Project**
```bash
./gradlew build
```

5. **Run Application**
```bash
./gradlew installDebug
```

## 📦 Main Dependencies

```gradle
dependencies {
    // Jetpack Compose
    implementation "androidx.compose.ui:ui:1.5.4"
    implementation "androidx.compose.material3:material3:1.1.2"
    
    // Navigation
    implementation "androidx.navigation:navigation-compose:2.7.5"
    
    // Hilt
    implementation "com.google.dagger:hilt-android:2.48"
    kapt "com.google.dagger:hilt-compiler:2.48"
    
    // Retrofit & OkHttp
    implementation "com.squareup.retrofit2:retrofit:2.9.0"
    implementation "com.squareup.okhttp3:okhttp:4.12.0"
    
    // Kotlinx Serialization
    implementation "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0"
    
    // Coil (Image Loading)
    implementation "io.coil-kt:coil-compose:2.5.0"
    
    // DataStore
    implementation "androidx.datastore:datastore-preferences:1.0.0"
    
    // Joda-Time
    implementation "joda-time:joda-time:2.12.5"
    
    // Firebase
    implementation "com.google.firebase:firebase-config-ktx:21.5.0"
}
```

## 📂 Project Structure

```
com.alex.yang.weather
│
├── 📂 core                      # Core module
│   ├── component/               # Shared UI components
│   ├── extension/               # Kotlin extension functions
│   ├── network/                 # Network layer encapsulation
│   └── utils/                   # Utility classes
│
├── 📂 data                      # Data layer
│   ├── api/                     # API service definitions
│   │   └── VisualCrossingWebServices.kt
│   ├── di/                      # Dependency injection modules
│   │   ├── AppModule.kt
│   │   ├── DataModule.kt
│   │   └── RepositoryBinds.kt
│   ├── local/                   # Local storage
│   │   └── AppPreferences.kt
│   ├── mapper/                  # Data mappers
│   │   ├── DayMapper.kt
│   │   ├── HourMapper.kt
│   │   └── TimelineMapper.kt
│   ├── model/                   # DTO models
│   │   ├── Counties.kt
│   │   ├── County.kt
│   │   ├── DayDto.kt
│   │   ├── HourDto.kt
│   │   └── TimelineDto.kt
│   ├── repository/              # Repository implementations
│   │   ├── ConfigRepository.kt
│   │   └── WeatherRepositoryImpl.kt
│   └── utils/                   # Data layer utilities
│       ├── MapperUtil.kt
│       └── MockServerInterceptor.kt
│
├── 📂 domain                    # Domain layer
│   ├── model/                   # Domain models
│   │   ├── Day.kt
│   │   ├── Hour.kt
│   │   └── Timeline.kt
│   ├── repository/              # Repository interfaces
│   │   └── WeatherRepository.kt
│   └── usecase/                 # Use Cases
│       ├── FindTodayDayUseCase.kt
│       ├── GetWeatherUseCase.kt
│       ├── IsCurrentHourUseCase.kt
│       └── IsTodayUseCase.kt
│
└── 📂 demo                      # Presentation layer
    ├── feature/                 # Feature modules
    │   ├── component/           # Shared components
    │   │   └── NoNetworkDialog.kt
    │   ├── home/                # Home
    │   │   ├── domain/
    │   │   │   └── DrawerItem.kt
    │   │   └── presentation/
    │   │       ├── HomeScreen.kt
    │   │       ├── HomeViewModel.kt
    │   │       ├── HomeSharedViewModel.kt
    │   │       └── component/   # Home components
    │   ├── main/                # Main page
    │   │   ├── MainActivity.kt
    │   │   └── MainViewModel.kt
    │   ├── today_detail/        # Today detail
    │   │   ├── TodayDetailScreen.kt
    │   │   └── component/
    │   │       ├── HourRow.kt
    │   │       └── MetricRow.kt
    │   ├── web/                 # WebView
    │   │   └── WebScreen.kt
    │   └── weekly_detail/       # Weekly detail
    │       └── WeeklyDetailScreen.kt
    ├── navigation/              # Navigation
    │   ├── AppNavGraph.kt
    │   └── AppRoute.kt
    └── ui/theme/                # Theme
        ├── Color.kt
        ├── Shape.kt
        ├── Theme.kt
        └── Type.kt
```

## 🎨 Design Patterns and Best Practices

### MVVM + MVI
- ViewModel manages UI state
- Implements unidirectional data flow using StateFlow
- UI State encapsulated as immutable data class

### Clean Architecture
- **Presentation Layer**: Compose UI + ViewModel
- **Domain Layer**: Use Cases + Domain Models
- **Data Layer**: Repository + API + Local Storage

### Dependency Injection (Hilt)
```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val networkManager: NetworkManager,
    // ...
) : ViewModel()
```

### Reactive Programming
```kotlin
val uiState = _uiState.asStateFlow()

viewModelScope.launch {
    when (val result = getWeatherUseCase(...)) {
    is Resource.Success -> { /* Handle success */ }
    is Resource.Error -> { /* Handle error */ }
}
}
```

### Error Handling
```kotlin
sealed interface Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>
    data class Error(val message: String) : Resource<Nothing>
}
```

## 🔒 Security Considerations

- ✅ API Key managed using Firebase Remote Config
- ✅ Network requests use HTTPS
- ✅ Sensitive information not hardcoded in source code
- ✅ ProGuard/R8 obfuscation protection

## 📈 Performance Optimization

### UI Performance
- LazyColumn/Grid optimized with `key` and `contentType`
- Images use Coil's memory and disk cache
- Avoid unnecessary recomposition

### Network Optimization
- HTTP caching strategy
- Request timeout configuration
- Error retry mechanism

### Memory Management
- ViewModel automatic cleanup
- Image size constraints
- WebView lifecycle management

## 🧪 Testing

```bash
# Unit tests
./gradlew test

# UI tests
./gradlew connectedAndroidTest
```

## 📝 API Documentation

### Visual Crossing Weather API

**Endpoint**:
```
GET /VisualCrossingWebServices/rest/services/timeline/{location}/{startDay}/{endDay}
```

**Parameters**:
- `location`: City name (e.g., "Taipei,TW")
- `startDay`: Start date (format: yyyy-MM-dd)
- `endDay`: End date (format: yyyy-MM-dd)
- `unitGroup`: Unit system (metric/us)
- `include`: Include data (hours,current)
- `lang`: Language code (zh)
- `key`: API key

## 🐛 Known Issues

- [ ] Weather data for some regions may be incomplete
- [ ] Mock data interceptor requires manual switching

## 🔮 Future Plans

- [ ] Support for more countries/regions
- [ ] Weather alert notifications
- [ ] Weather chart visualization
- [ ] Home screen widget
- [ ] Multi-language support
- [ ] Increase unit test coverage
- [ ] CI/CD pipeline integration

## 📄 License

This project is for learning and demonstration purposes only.

## 👨‍💻 Author

**Alex Yang**
- GitHub: [@m9939418](https://github.com/m9939418)

## 🙏 Acknowledgments

- [Visual Crossing Weather API](https://www.visualcrossing.com/) - Weather data provider
- [Material Design](https://m3.material.io/) - UI design guidelines
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern Android UI framework

---

**⭐ If this project helps you, please give it a Star!**