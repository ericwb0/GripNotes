# Grip Notes
## Description
Grip Notes is a simple Android app for taking notes! Made by Eric Burkholder for CSCE 546: Mobile App Development
## Tech Stack
### Backend
This app is powered by Firebase, which means your notes are stored on the cloud!
Firebase comes with a lot of helpful features including authentication, file storage, databases, and most importantly, effortless setup.
- Firebase Firestore as our no-SQL cloud database. Their newer APIs support Kotlin flows as well which are
  used in the app.
- Firebase Authentication for handling accounts

### Frontend
My goal with making this app is to follow good Android development practices and design principles. We're
leveraging some of the latest Jetpack Compose features as well.
- Jetpack Compose for UI design
- Jetpack Compose **type safe navigation**
- Hilt for dependency injection to handle db and auth services
- All Material 3 UI components correctly utilizing theme and typography. This makes the app very adaptable
  and it handles configuration changes and light/dark mode well out of the box.

### Frontend Architecture
Grip notes uses the modern Android architecture recommendation with a Single Activity MVVM architecture.
A single activity contains the navigation graphs that contain the screens with their ViewModels. Each screen
has its own ViewModel and states are passed to the composables through StateFlows.
