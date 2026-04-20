# 🚀 Quick Start Guide - Posts App

## Prerequisites

- **Android Studio**: Arctic Fox or later
- **Android SDK**: API 30 minimum (Target 36)
- **Java**: Version 11 or later
- **Git**: For cloning and version control

## 5-Minute Setup

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/posts-app.git
cd posts-app
```

### 2. Open in Android Studio
- Launch Android Studio
- Click `File` → `Open`
- Select the `posts-app` folder
- Wait for Gradle sync to complete

### 3. Configure Emulator or Device
**Using Emulator:**
- Click `AVD Manager` (Android Virtual Device)
- Create a new virtual device with API level 30+
- Start the emulator

**Using Physical Device:**
- Enable Developer Mode: Settings → About → Tap Build Number 7 times
- Enable USB Debugging: Developer Options → USB Debugging
- Connect via USB

### 4. Run the App
```bash
# Using Terminal
./gradlew installDebug

# Or use Android Studio
Click Run → Run 'app'
```

### 5. Explore the App
- 👥 Browse users on the main screen
- 👤 Tap a user to see their posts
- 📝 Tap a post to view details and comments
- ⬆️ Swipe up/down to scroll through content

## Project Structure at a Glance

```
app/src/main/java/com/intercom/posts/
│
├── presentation/          # UI Layer (MVVM)
│   ├── activities/        # MainActivity, UserDetailActivity, PostDetailsActivity
│   ├── adapter/           # RecyclerView adapters
│   ├── viewmodel/         # ViewModel classes
│   └── uiState/           # State management
│
├── domain/                # Business Logic
│   └── model/             # Data Transfer Objects (DTOs)
│
└── data/                  # Data Layer
    ├── api/               # Retrofit API services
    ├── model/             # API response models
    └── repository/        # Repository pattern implementation
```

## Key Technologies

| Component | Technology | Purpose |
|-----------|-----------|---------|
| Language | **Kotlin** | Modern Android development |
| Architecture | **MVVM** | Clean separation of concerns |
| Async | **Coroutines** | Non-blocking operations |
| Networking | **Retrofit + OkHttp** | REST API communication |
| UI Framework | **Jetpack Components** | Modern Android UI |
| Design | **Material Design 3** | Modern Material components |
| Images | **Glide** | Efficient image loading |

## Main Screens

### 1. MainActivity - User List
```
┌─────────────────────┐
│ Users List          │
├─────────────────────┤
│ [👤] User Name 1   │
│ [👤] User Name 2   │
│ [👤] User Name 3   │
│ [👤] User Name 4   │
│ [👤] User Name 5   │
└─────────────────────┘
```

### 2. UserDetailActivity - User Profile
```
┌─────────────────────┐
│ User Profile        │
├─────────────────────┤
│      [👤 Image]     │
│    User Full Name   │
│  Total Posts | Skip │
├─────────────────────┤
│ Posts List          │
│ [Post 1 Title]      │
│ [Post 2 Title]      │
│ [Post 3 Title]      │
└─────────────────────┘
```

### 3. PostDetailsActivity - Post Details
```
┌─────────────────────┐
│ Post Details        │
├─────────────────────┤
│ Post Title          │
│ Post Body Content   │
│ Views: 1000         │
│ Likes: 100          │
│ Tags: tag1, tag2    │
├─────────────────────┤
│ Comments (3)        │
│ Comment 1...        │
│ Comment 2...        │
│ Comment 3...        │
└─────────────────────┘
```

## Common Tasks

### Adding a New Feature

1. **Create ViewModel** (if needed)
```kotlin
class MyViewModel : ViewModel() {
    private val _state = MutableStateFlow<UiState>(UiState.Initial)
    val state = _state.asStateFlow()
    
    fun loadData() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val data = repository.getData()
                _state.value = UiState.Success(data)
            } catch (e: Exception) {
                _state.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
```

2. **Create Layout**
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">
    
    <!-- Your UI components here -->
    
</LinearLayout>
```

3. **Create Activity**
```kotlin
class MyActivity : AppCompatActivity() {
    private val viewModel: MyViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setup UI and observe ViewModel
    }
}
```

### Modifying API Endpoint

1. Open `data/api/ApiService.kt`
2. Add or modify the endpoint:
```kotlin
interface ApiService {
    @GET("new-endpoint")
    suspend fun getNewData(): NewDataResponse
}
```

3. Update repository to use the new endpoint
4. Call from ViewModel using coroutines

### Handling Different UI States

```kotlin
// In Activity
lifecycleScope.launch {
    viewModel.state.collect { state ->
        when (state) {
            is UiState.Loading -> showLoadingDialog()
            is UiState.Success -> displayData(state.data)
            is UiState.Error -> showErrorMessage(state.message)
        }
    }
}
```

## Debugging Tips

### Using Logcat
```bash
# View logs
./adb logcat

# Filter by app package
./adb logcat | grep "com.intercom.posts"
```

### Debug Breakpoints
1. Click line number to set breakpoint
2. Run app in Debug mode
3. Step through code with debugger

### Network Monitoring
- OkHttp logging interceptor is enabled
- Check Logcat for API request/response logs
- Use Network Profiler in Android Studio

## Build Commands

```bash
# Clean build
./gradlew clean build

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

## Environment Setup

### Add API Base URL
In your network configuration:
```kotlin
private const val BASE_URL = "https://api.example.com/"
```

### Build Variants
- **Debug**: For development with logging
- **Release**: Optimized production build

## Troubleshooting

### Gradle Sync Fails
```bash
./gradlew clean
./gradlew build
```

### Emulator Won't Start
- Increase RAM allocation in AVD settings
- Check virtualization is enabled in BIOS
- Use `Intel HAXM` or `Hyper-V`

### App Crashes on Launch
- Check logcat for stack trace
- Ensure API endpoint is correct
- Verify Android SDK is properly installed

### Network Requests Fail
- Check internet connection
- Verify API endpoint URL
- Review OkHttp logs
- Check API response format

## Performance Optimization

### Image Loading
- Glide automatically caches images
- Use appropriate image sizes
- Enable hardware acceleration

### List Scrolling
- RecyclerView reuses item views efficiently
- Avoid complex layouts in items
- Use ViewHolder pattern (handled by adapters)

### Memory Management
- ViewModel handles lifecycle properly
- Coroutines auto-cancel with lifecycle
- No memory leaks from listeners

## Resources

- [Android Documentation](https://developer.android.com/)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Material Design 3](https://m3.material.io/)
- [Android Architecture Guides](https://developer.android.com/guide/architecture)

## Next Steps

1. ✅ Run the app successfully
2. 📖 Explore the codebase
3. 🔧 Modify and add features
4. 🧪 Test your changes
5. 📤 Push to GitHub

## Getting Help

- **Android Documentation**: [developer.android.com](https://developer.android.com/)
- **Stack Overflow**: Tag with `android`, `kotlin`
- **GitHub Issues**: Open an issue in the repository
- **Community**: Android developers community forums

---

**Happy coding! 🎉**

For more details, see the main [README.md](README.md)

