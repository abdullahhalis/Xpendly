# Xpendly

![Platform](https://img.shields.io/badge/Platform-Android-brightgreen)
![Kotlin](https://img.shields.io/badge/Kotlin-purple)
![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-orange)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-red)

Android expense tracker application built with Kotlin and Jetpack Compose using MVVM architecture
and reactive state management

---

## 🚀 Features

Core (MVP):

- Add, view, update and delete expenses
- Filter by day, week, month, year, or all time
- Total expense summary per filter
- Theme setting (Light, Dark, Auto)

---

## 📱 Preview

### Demo

<img src="docs/demo/demo.gif" alt="demo" width="250">

### Screenshots

| Home Screen| Add Expense| Detail Screen| Update Expense|
|:------------:|:------------:|:--------------:|:---------------:|
| <img src="docs/screenshots/home.jpg" width="300"> | <img src="docs/screenshots/add.jpg" width="300"> | <img src="docs/screenshots/detail-dark.jpg" width="300"> | <img src="docs/screenshots/update-dark.jpg" width="300"> |

---

## 🛠️ Tech Stack

- **Language** : Kotlin
- **UI** : Jetpack Compose + Material 3
- **Architecture** : MVVM + Clean Architecture (no domain layer)
- **DI** : Hilt
- **Database** : Room
- **Preferences** : DataStore

---

## 🏗️ Architecture

This project uses MVVM to separate UI, business logic, and data layer for better scalability, and
follows Clean Architecture principles without a domain layer, repositories are injected directly
into ViewModels for simplicity

```mermaid
flowchart LR
  UI[Compose UI] --> VM[ViewModel]
  VM --> Repo[Repository]
  Repo --> DB[Room Database]
  VM --> Pref[DataStore]

  subgraph UI Layer
  direction TB
  UI
  VM
  end

  subgraph Data Layer
  direction TB
  Repo
  DB
  Pref
  end
```

UI Layer:

- Jetpack Compose screens
- State collection from ViewModel
- Stateless composables
- State management using StateFlow

Data Layer:

- Repository pattern
- Room database
- DataStore preferences

---

## 🧠 Technical Highlights

- Implemented reactive filtering using StateFlow
- Used repository pattern for clean data abstraction
- Applied dependency injection with Hilt
- Structured Compose UI into reusable components

---

## ⚡ Technical Challenges

Challenges faced during development:

- Managing recomposition when filters change
- Maintaining single source of truth
- Designing reusable Compose components
- Handling date filtering efficiently

---

## 🔥 Future Improvements (planned)

- Search functionality
- Expense analytics charts
- Budget limit alerts
- Cloud backup