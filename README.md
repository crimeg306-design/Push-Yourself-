# Push Yourself - Daily Tactical Habit Tracker

**Push Yourself** is an offline-first tactical habit tracker and gamified fitness RPG for Android.

---

## 🚀 Automatic GitHub Releases & APK Download

A GitHub Actions workflow is pre-configured in `.github/workflows/android-release.yml`.

### How It Works:
1. **Push to GitHub**: Whenever you push code or updates to `main` / `master` (or push a tag like `v1.0.0`), GitHub Actions will automatically run.
2. **Build APK**: GitHub will build the APK (`app-debug.apk`) in the cloud.
3. **Publish Release**: GitHub will automatically publish a new entry under **Releases** on your repository page with the compiled `app-debug.apk` attached as an asset.

---

## 📱 How to Install on Your Android Phone

1. Go to your GitHub Repository page.
2. Click on **Releases** (on the right sidebar or under the repo name).
3. Tap **Assets** under the latest release.
4. Download `app-debug.apk` directly to your phone.
5. Tap the downloaded APK to install or update the app on your phone.

---

## 🛠️ Updating the App in the Future

Whenever you ask AI Studio to make improvements or add new features:
1. Export / Sync your project to GitHub.
2. GitHub Actions will trigger automatically.
3. Download the new `app-debug.apk` from GitHub Releases and tap to update!
