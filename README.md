# Persian Date Picker
![banner](https://github.com/sham-h93/persian-dtpicker/images/banner.png)

[![](https://jitpack.io/v/sham-h93/persian-dtpicker.svg)](https://jitpack.io/#sham-h93/persian-dtpicker) ![Android lib](https://img.shields.io/badge/Android-green) ![JetpackCompose](https://img.shields.io/badge/JetpackCompose-blue)

A simaple and minimal Shamsi Date Picker for Jetpack Compose.

## Usage

**Step 1** Add it in your settings.gradle.kts at the end of repositories:


	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url = uri("https://jitpack.io") }
		}
	}

**Step 2**  Add the dependency

```css
	dependencies {
	        implementation("com.github.sham-h93:persian-dtpicker:<latest-version>")
	}
```