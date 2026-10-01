#!/bin/sh
set -e
GRADLE_VERSION="8.11.1"
DIST="$HOME/.gradle/wrapper/dists/z9tether-gradle-$GRADLE_VERSION"
ZIP="$DIST/gradle-$GRADLE_VERSION-bin.zip"
DIR="$DIST/gradle-$GRADLE_VERSION"
if [ ! -x "$DIR/bin/gradle" ]; then
  mkdir -p "$DIST"
  if [ ! -f "$ZIP" ]; then
    echo "Downloading Gradle $GRADLE_VERSION..."
    curl -fL "https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip" -o "$ZIP"
  fi
  unzip -q -o "$ZIP" -d "$DIST"
fi
exec "$DIR/bin/gradle" "$@"
