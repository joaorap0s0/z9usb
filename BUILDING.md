# Building

## Requirements

- JDK 17
- Android SDK
- Android SDK Platform 35
- Android Build Tools 35.x

Android Studio can install the required SDK components automatically.

## Local

```bash
./gradlew clean assembleDebug
```

Output:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## GitHub Actions

Push to GitHub:

```bash
git init
git add .
git commit -m "Initial Z9 USB tether project"
git branch -M main
git remote add origin YOUR_REPOSITORY_URL
git push -u origin main
```

GitHub Actions will run `.github/workflows/android.yml`.

The generated APK is available under the workflow run's Artifacts section.

## Release signing

The repository intentionally does not contain a private signing key.

For a production release, create an Android keystore and configure GitHub
repository secrets. Do not commit the keystore or passwords.
