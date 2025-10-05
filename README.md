# Car Rental App
A simple Android application for car rental service with modern UI and comprehensive functionality.

## Features

### Main Screen (MainActivity)
- **Credit Balance Display**: Initial 500 credits, deducted upon rental
- **Theme Toggle**: Light/Dark Mode with Switch component, synchronized across screens
- **Search Functionality**: Filter cars by name or model
- **Sorting Options**:
  - Sort by Rating (High to Low)
  - Sort by Year (Newest to Oldest)
  - Sort by Cost (Low to High)
- **Car Display**: Browse through available cars one at a time
- **Favorites Feature**: Star icon to favorite cars, displayed in horizontal RecyclerView
- **Rental Function**: Navigate to detail screen with Parcelable data

### Detail Screen (DetailActivity)
- **Car Details Display**: Complete vehicle information with image
- **RatingBar**: Display car rating (read-only)
- **Slider**: Select rental period (1-7 days)
- **Credit Validation**:
  - Single rental limit ≤ 400 credits
  - Balance check to ensure sufficient funds
- **Save/Cancel**: Return to main screen with updated state

## Technical Implementation

### Architecture
- **Pattern**: MVVM (Model-View-ViewModel)
- **Components**: Activity + ViewModel + LiveData
- **UI Framework**: Material Design 3
- **Language**: Kotlin 100%

### Data Layer
- **Car**: Parcelable data model for vehicle information
- **RentalRecord**: Parcelable rental transaction data
- **InMemoryRepository**: Singleton pattern for in-memory data management
- **No Persistence**: All data stored in memory (resets on app restart)

### Key Features
- Intent-based navigation with Parcelable objects
- Theme switching with SharedPreferences persistence
- LiveData observers for reactive UI updates
- Input validation and user feedback with Snackbar
- Error handling for insufficient balance and rental limits

## Project Structure

```
app/src/main/
├── java/com/example/rentacar/
│   ├── ui/
│   │   ├── main/
│   │   │   ├── MainActivity.kt
│   │   │   └── MainViewModel.kt
│   │   ├── detail/
│   │   │   ├── DetailActivity.kt
│   │   │   └── DetailViewModel.kt
│   │   └── adapter/
│   │       └── FavoritesAdapter.kt
│   ├── model/
│   │   ├── Car.kt
│   │   └── RentalRecord.kt
│   ├── data/
│   │   └── InMemoryRepository.kt
│   ├── util/
│   │   └── Extensions.kt
│   └── MainApplication.kt
├── res/
│   ├── layout/
│   ├── drawable/
│   ├── values/
│   └── menu/
└── androidTest/
    └── java/com/example/rentacar/
        ├── MainActivityTest.kt (5 tests)
        ├── DetailActivityTest.kt (5 tests)
        └── RentalFlowTest.kt (5 tests)
```

## Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 21
- Android SDK API 24+ (Android 7.0+)
- Gradle 8.13

### Installation

1. Clone the repository
```bash
git clone https://github.com/stevoCS/assignment02-stevoCS.git
cd assignment02-stevoCS
```

2. Set Java environment (macOS/Linux)
```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@21
```

3. Build the project
```bash
./gradlew assembleDebug
```

4. Run the app
- Open in Android Studio
- Click "Run" or use Shift+F10

### Running Tests

```bash
# Build test APK
./gradlew assembleDebugAndroidTest

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest
```

## Testing

The app includes comprehensive instrumented tests using Espresso:

- **MainActivityTest** (5 tests): UI components and main functionality
- **DetailActivityTest** (5 tests): Detail screen and validation
- **RentalFlowTest** (5 tests): Integration tests for complete user flows

**Total: 15 tests** covering all major features

See [Test Documentation](app/src/androidTest/java/com/example/rentacar/README_TESTS.md) for details.

## 📋 Requirements Met

### UI Components (Non-TextView)
✅ RatingBar - for car ratings
✅ SwitchMaterial - for theme toggle
✅ Slider - for selecting rental days (1-7)

### Styling
✅ Multiple Material Design styles applied
✅ Custom theme for light/dark modes
✅ Consistent visual design across screens

### Data Management
✅ Parcelable objects for Intent data transfer
✅ In-memory repository (no persistence)
✅ LiveData for reactive updates

### Validation & Error Handling
✅ Credit balance validation
✅ Rental limit enforcement (≤400 credits)
✅ User feedback with Snackbar messages
✅ Form validation before submission

## Design Highlights

- **6 Cars Available**: BMW i7, BMW X1, Mercedes-Benz S 500, Porsche 911, Tesla Model 3, Tesla Model Y
- **Initial Balance**: 500 credits
- **Rental Limit**: Max 400 credits per rental
- **Rental Period**: 1-7 days selectable via slider
- **Color Scheme**: Material Design 3 with primary blue theme
- **Responsive Layout**: Optimized for various screen sizes

## 🛠️ Technologies Used

- **Language**: Kotlin 1.9.10
- **Min SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Architecture Components**:
  - ViewModel
  - LiveData
  - ViewBinding
- **UI Components**:
  - Material Design 3
  - RecyclerView
  - CoordinatorLayout
  - ConstraintLayout
- **Testing**:
  - JUnit 4
  - Espresso 3.5.1
  - AndroidX Test

## Notes

- Data is stored in memory only and resets on app restart (as per requirements)
- All car images are free-to-use resources
- Theme preference persists across app sessions using SharedPreferences
- No network connectivity required
- Supports both portrait and landscape orientations

## Development

Built with using Android Studio and Kotlin

---

**Academic Project** | Mobile Application Development | 2025
