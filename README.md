# SoCScanner

### Overview
This project is created with the main objective of easing the administration of events for NUS students. 

The project comprises of:
- Scanner module that connects and reads NUS matriculation cards (tested on Gemalto Prox-DU and ACS ACR122U-A4 NFC card readers)
- Excel spreadsheet reader/writer module to faciliate validation and capture attendance
- GUI

### Current Implementation
The current implementation has the following dependencies:
- __Build Tools__
 - JavaSE JDK 8
 - Gradle
- __Interface__
 - JavaFX 8
 - SceneBuilder 2.0
 - FontAwesomeFX
- __Office Documents__
 - Apache POI Framework
- __Cipher__
 - Jasypt
- __Unit Testing__
 - JUnit

> NOTE: We recommend using IDEA IntelliJ by JetBrains as the choice IDE
  https://www.jetbrains.com/idea/#chooseYourEdition

### Future Improvements
In this prototype stage, the modules are designed to be very specific. 
In the future, we aim to create event factories that allows us to customise and save templates with custom rules to become relevant for more event types.

### Setup
1. Installation of Tools
 - __JDK 8__ : 

   https://intellij-support.jetbrains.com/hc/en-us/articles/206544879-Selecting-the-JDK-version-the-IDE-will-run-under

 - __JavaFX 8__ : 

   https://www.jetbrains.com/help/idea/2016.1/preparing-for-javafx-application-development.html

 - __SceneBuilder 2.0__ : 

   https://docs.oracle.com/javase/8/scene-builder-2/installation-guide/jfxsb-installation_2_0.htm
  
2. Install Gradle
  
  ```
  brew install gradle
  ```
  
  or refer to this link: https://docs.gradle.org/current/userguide/installation.html
  
3. Install card reader driver:
  - __ACS ACR122U-A4__ : 
    
    http://www.acs.com.hk/en/products/3/acr122u-usb-nfc-reader/
  
4. Clone and import project, then set-up gradle home as:
  
  ```
  /usr/local/opt/gradle/libexec/
  ```

5. Gradle build the project and run from these entrypoints:
  - __MatricCardScanner.main__ for testing scanning module
  - __XMLSheetReader.main__ for testing reading and writing Office Documents (.xls, .xlsx in particular)
  - __SoCScanner.main__ for GUI and integration testing
  
  
### Dev Guide

#### Adding repositories and dependencies on Gradle
  1. Open ./build.gradle in the project root directory
  2. Add repositories to:
  
    ``` gradle
    repositories {
        // Put your repositories here...
    }
    ```
    
  3. Add dependencies or external packages to:
  
    ``` gradle
    dependencies {
        // Use __testCompile__ for test builds
        testCompile([group: 'junit', name: 'junit', version: '4.11']) 
        
        // Importing __annotations__ module from the  __com.intellij__ organisation with version __9.0.4__
        compile ([group: 'com.intellij', name: 'annotations', version: '9.0.4']) 
    }
    ```

#### Building the jar app
  1. Run ``` gradle clean ```
  2. Run ``` gradle fatjar build ```
    
#### Project Structure Highlight

> __NOTE:__ 
  This is not the entire representation of the the project structure. Only files that we wish to highlight are shown.

``` ruby
+-- src
|   +-- main
    |   +-- java
        |   +-- command #[to link GUI with other modules]
        |   +-- database
            |   +-- io #[readers/writers for documents and spreadsheets]
                |   +-- XMLSheetReader.java #[XLS/XLSX Reader entrypoint]
        |   +-- logic
            |   +-- analytics #[for statistics and data analysis]
            |   +-- validation #[for session/event validators]
        |   +-- main
            |   +-- SoCScanner.java #[GUI entrypoint/main app]
        |   +-- objects #[models/data structures]
        |   +-- scanners
            |   +-- APDUCommands.java                #[protocols for card reading; stubbed for security]
            |   +-- APDUStatusChecker.java           #[demystify scanner status codes]
            |   +-- MatricCardScanner.java           #[scanner entrypoint]
            |   +-- MatricCardScannerExceptions.java #[exceptions]
            |   +-- MatricCardScannerUtilities.java  #[scanner interface]
        |   +-- ui
            |   +-- controllers #[view logic for layouts and components]
        |   +-- utilities #[common/shared tools]
        +-- resources
        |   +-- documents #[attendance/validation/survey spreadsheets here]
        |   +-- ui
            |   +-- fonts.* #[offline fonts assets that are required by the UI]
            |   +-- images  #[icons and graphical assets; consider using .svg]
            |   +-- styles  #[css styling files]
            |   +-- views   #[fxml component layout files]
+-- build.gradle #[specify external repos and dependencies here]
+-- .gitignore   
+-- key.soccat   #[not distributed on VCS, also root of .jar file]
```

##### A project by:

<img src="./compclub-logo.jpg" alt="" width="200" height="36">
  
