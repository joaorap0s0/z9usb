# Z9 Tether

Android USB/MTP/PTP tethering MVP for the Nikon Z9.

## What it does

- Detects USB devices through Android USB Host mode.
- Identifies Nikon USB devices by vendor ID and inspects USB interfaces.
- Requests Android USB permission.
- Opens the USB connection and claims a PTP/MTP-style interface.
- Sends basic PTP commands: OpenSession, GetDeviceInfo, GetStorageIDs,
  GetObjectHandles, GetObjectInfo and GetObject.
- Transfers JPEG objects into Android MediaStore under `Pictures/Z9 Tether/YYYY-MM-DD/`.
- Uses pending MediaStore writes so incomplete files are not exposed.
- Provides a foreground USB tether service.
- Includes a simple Live Shoot status screen.
- GitHub Actions builds a debug APK and uploads it as an artifact.

## Important hardware note

This is a real buildable USB/PTP foundation, but the Nikon Z9 protocol behavior must
be validated against a physical Z9. Nikon-specific vendor extensions, event handling,
burst prioritization, NEF transfer, JPEG/NEF pairing, and Lightroom behavior are not
claimed as hardware-verified by this repository.

On the Z9 select:

Network menu -> USB -> MTP/PTP

Then connect the camera to an Android USB host with a USB-C data cable.

## Build locally

```bash
./gradlew assembleDebug
```

APK:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Install:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Build on GitHub

Push the repository to GitHub. The workflow at:

```text
.github/workflows/android.yml
```

runs Gradle and publishes:

```text
app-debug.apk
```

as a GitHub Actions artifact.

You can also trigger it manually from the Actions tab.

## Architecture

```text
Z9
 |
 | USB-C
 v
Android USB Host
 |
 +-- UsbCameraManager
 |
 +-- PtpProtocol
 |
 +-- NikonZ9Device
 |
 +-- TransferEngine
 |
 +-- MediaStorePublisher
 |
 v
Pictures/Z9 Tether/YYYY-MM-DD/
```

## Roadmap

1. Validate Z9 USB/PTP enumeration.
2. Validate GetDeviceInfo and storage discovery.
3. Validate JPEG ObjectAdded/event behavior.
4. Add robust object enumeration and incremental discovery.
5. Add NEF transfer.
6. Add JPEG/NEF association.
7. Add JPEG-first transfer scheduling.
8. Add burst queue and diagnostics.
9. Validate MediaStore import behavior with Lightroom.
10. Add release signing configuration.
