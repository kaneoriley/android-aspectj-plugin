[![Release](https://jitpack.io/v/com.github.oriley-me/android-aspectj-plugin.svg)](https://jitpack.io/#com.github.oriley-me/android-aspectj-plugin) [![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0) [![Build Status](https://travis-ci.org/oriley-me/android-aspectj-plugin.svg?branch=master)](https://travis-ci.org/oriley-me/android-aspectj-plugin) [![Dependency Status](https://www.versioneye.com/user/projects/56b09a5e3d82b9002b5269b1/badge.svg?style=flat)](https://www.versioneye.com/user/projects/56b09a5e3d82b9002b5269b1)

# android-aspectj-plugin

Debug logging and field value replacement. Initially forked from Hugo by Jake Wharton.

# Gradle Dependency

Firstly, you need to add JitPack.io to your repositories list in the root projects build.gradle:

```gradle
repositories {
    maven { url "https://jitpack.io" }
}
```

Then, add android-aspectj-plugin to your buildscript dependencies:

```gradle
buildscript {
    dependencies {
        classpath 'me.oriley:android-aspectj-plugin:0.1'
    }
}
```

The last step is to apply the plugin to your application or library project, for example:

```gradle
apply plugin: 'com.android.application' || apply plugin: 'com.android.library'
apply plugin: 'me.oriley.android-aspectj' || apply plugin: 'me.oriley.android-aspectj-debug'
```
