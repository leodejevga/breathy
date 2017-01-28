
# breathy

### Android Studio Project

### Projekt App und Projekt Spiel miteinander verbinden
1. Import Gradle Projekt
  File -> Project Structure -> grün "Plus" Button oben link -> Import Gradle Project
2. Configuration
  2.1 in build.gradle von Unterprojekt 
	- ändern:  apply plugin: 'com.android.application' -> apply plugin: 'com.android.library'
	- löschen: applicationId "<package>"
  2.2 in AndroidManifest.xml von Unterprojekt
	- löschen: <category android:name="android.intent.category.LAUNCHER" />
  2.3 add Unterprojekt als dependencies von Oberprojekt
	File -> Project Structure -> Dependencies Tab -> add Unterprojekt als Library
  
### Projektaufbau

**app** --hat--> **source**<br>
**app** --hat--> **AudioSurf**<br>
**AudioSurf** --hat--> **source**<br>

**app** beinhaltet die ganze Logik (Backend) und stellt das Menü dar.<br>
**source** beinhaltet alle nötigen Interfaces und abstrakte Implementierungen dieser Interfaces die beschreiben WAS ein Spiel alles haben muss um von der App akzeptiert zu werden.<br>
**AudioSurf** muss alle Elemente aus **source** implementieren um richtig arbeiten zu können<br>
<br>
