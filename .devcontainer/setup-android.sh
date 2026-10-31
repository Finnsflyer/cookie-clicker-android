#!/usr/bin/env bash
set -euo pipefail

export ANDROID_HOME="/opt/android-sdk"
export ANDROID_SDK_ROOT="/opt/android-sdk"

sudo mkdir -p "$ANDROID_HOME"
sudo chown -R "$(id -u):$(id -g)" "$ANDROID_HOME"

if [ ! -x "$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager" ]; then
  sudo mkdir -p "$ANDROID_HOME/cmdline-tools"
  cd "$ANDROID_HOME/cmdline-tools"
  curl -fsSL "https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip" -o cmdline-tools.zip
  unzip -q cmdline-tools.zip
  rm -f cmdline-tools.zip
  if [ -d "$ANDROID_HOME/cmdline-tools/cmdline-tools" ]; then
    mv "$ANDROID_HOME/cmdline-tools/cmdline-tools" "$ANDROID_HOME/cmdline-tools/latest"
  fi
fi

if [ ! -d "$ANDROID_HOME/platform-tools" ] || [ ! -d "$ANDROID_HOME/build-tools/34.0.0" ]; then
  yes | "$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager" --sdk_root="$ANDROID_HOME" "platform-tools" "platforms;android-34" "build-tools;34.0.0" "cmdline-tools;latest"
  yes | "$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager" --sdk_root="$ANDROID_HOME" --licenses >/dev/null
fi

export PATH="$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/build-tools/34.0.0:$PATH"

if ! command -v gradle >/dev/null 2>&1; then
  GRADLE_VERSION="8.7"
  curl -fsSL "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" -o /tmp/gradle.zip
  unzip -q /tmp/gradle.zip -d /tmp
  mkdir -p "$HOME/.local/bin"
  ln -sf "/tmp/gradle-${GRADLE_VERSION}/bin/gradle" "$HOME/.local/bin/gradle"
  export PATH="$HOME/.local/bin:$PATH"
fi

cd /workspaces/cookie-clicker-android
gradle wrapper --gradle-version 8.7
./gradlew assembleDebug
