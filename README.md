# 🐶 My Daily Pet
![Static Badge](https://img.shields.io/badge/Version-1.0.1-blue)
![Static Badge](https://img.shields.io/badge/Kotlin-1.8.10-purple)
![Static Badge](https://img.shields.io/badge/JUnit-4.13.2-purple)
![Static Badge](https://img.shields.io/badge/Mockk-1.13.5-red)
![Static Badge](https://img.shields.io/badge/Coroutines-1.6.4-pink)

App for Pet's owners controls daily with routines weekly.

See in pt-br: [Readme-pt-br](https://github.com/joaovq/MyDailyPet/blob/main/README-pt-br.md)

<!--Badges for project-->

<p style="display:flex; justify-content:center" width="100%">
  <img src="https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Google_Play-414141?style=for-the-badge&logo=google-play&logoColor=white"/>
  <img src="https://img.shields.io/badge/Trello-0052CC?style=for-the-badge&logo=trello&logoColor=white"/>
</p>

### Context: 

📆 My pet routine was thought through my need to be able to organize my pet's important things in one place.

With the app you can create reminders, add tasks and your pet's data. With this you have information that may be necessary for the day-to-day with your pet. My Daily Pet came to make your day-to-day life easier.

The app is available on the Google Play Store.

# Tecnologies
The application was developed from the good practices indicated by google and through the knowledge acquired by me so far.
These were some of the technologies, architectures and standards used:
|  Tecnology |             Link         | 
|:-----------:|:-----------------------:|
| Design Layouts w/ paradigm Views and XML|
| MVVM, MVP and MVI architecture| 
| Libraries Android Jetpack (Permissions)|
|Room Android library SQLite (user data storage)| [Room Android library SQLite (user data storage)](https://developer.android.com/training/data-storage/room/)|
|Alarm Manager for reminders (exact and repetitive alarm programming)| [Alarm Manager for reminders (exact and repetitive alarm programming)](https://developer.android.com/training/scheduling/alarms)|
|WorkManager| [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager/basics?hl=pt-br)|
|Notification Manager and Notification Service (sending notifications)| [Notification Manager and Notification Service (sending notifications)](https://developer.android.com/develop/ui/views/notifications)|
|Glide| [Glide](https://bumptech.github.io/glide/)|
|Firebase Crashlytics (bug and crash monitoring in app production)| [Firebase Crashlytics (bug and crash monitoring in app production)](https://firebase.google.com/)|
|Ad Mob (Mobile ads)| [Ad Mob (Mobile ads)](https://admob.google.com/home/)|
|Broadcast Receiver (Receive notifications from alarms)| [Broadcast Receiver (Receive notifications from alarms)](https://developer.android.com/guide/components/broadcasts)|
|Content Provider (for share images of pet)| [Content Provider (for share images of pet)](https://developer.android.com/guide/topics/providers/content-provider-basics)|
|Data Store (user preferences settings)| [Data Store (user preferences settings)](https://developer.android.com/topic/libraries/architecture/datastore)|


# Design
In version 1.0, the app has 8 screens designed with minimalist colors, in figma and android studio.
<table>
  <tr>
    <th>Home</th>
    <th>Home (Dark)</th>
    <th>Reminder</th>
    <th>Reminder (Notification)</th>
    <th>Settings</th>
  </tr>
  <tr>
    <td><img src='https://github.com/joaovq/MyDailyPet/assets/101160670/14e2175a-f7ac-4769-bc96-720c9b8c80d3' style='width:200px'/></td>
    <td><img src='https://github.com/joaovq/MyDailyPet/assets/101160670/4ef12fd0-3ad9-46a2-a67f-d0ac485f3116' style='width:200px'/></td>
    <td><img src='https://github.com/joaovq/MyDailyPet/blob/main/photos_design/Reminder.png' style='width:200px'/></td>
    <td><img src='https://github.com/joaovq/MyDailyPet/blob/main/photos_design/Reminder%20Notification.png' style='width:200px'/></td>
    <td><img src='https://github.com/joaovq/MyDailyPet/blob/main/photos_design/Settings.png' style='width:200px'/></td>  
  </tr>
</table>
<table>
  <tr>
    <th>Add pet</th>
    <th>Edit pet</th>
    <th>Edit Reminder</th>
    <th>Home (Today Reminders)</th>
    <th>Pet Screen</th>
  </tr>
  <tr>
    <td><img src='https://github.com/joaovq/MyDailyPet/blob/main/photos_design/Add%20pet.png' style='width:200px'/></td>
    <td><img src='https://github.com/joaovq/MyDailyPet/blob/main/photos_design/Edit%20Pet.png' style='width:200px'/></td>
    <td><img src='https://github.com/joaovq/MyDailyPet/blob/main/photos_design/Edit%20reminder.png' style='width:200px'/></td>
    <td><img src='https://github.com/joaovq/MyDailyPet/blob/main/photos_design/Home%20with%20reminder.png' style='width:200px'/></td>
    <td><img src='https://github.com/joaovq/MyDailyPet/blob/main/photos_design/Pet%20screen.png' style='width:200px'/></td>  
  </tr>
</table>
<table>
  <tr>
    <th>Reminder list</th>
    <th>Delete pet</th>
    <th>Photo pet expanded</th>
    <th>Reminder Details</th>
    <th>Tasks</th>
  </tr>
  <tr>
    <td><img src='https://github.com/joaovq/MyDailyPet/blob/main/photos_design/Reminder%20list.png' style='width:200px'/></td>
    <td><img src='https://github.com/joaovq/MyDailyPet/blob/main/photos_design/Delete%20pet.png' style='width:200px'/></td>
    <td><img src='https://github.com/joaovq/MyDailyPet/blob/main/photos_design/Photo%20pet%20expanded.png' style='width:200px'/></td>
    <td><img src='https://github.com/joaovq/MyDailyPet/blob/main/photos_design/Reminder%20Detail.png' style='width:200px'/></td>
    <td><img src='https://github.com/joaovq/MyDailyPet/blob/main/photos_design/Tasks.png' style='width:200px'/></td>  
  </tr>
</table>

# 📦 App packages
The app was created using feature-data architecture. From this, features were created and within them divided into Data layer, Domain layer and Ui Layer.
## All packages:
![Novo mural (1)](https://github.com/joaovq/MyDailyPet/assets/101160670/64b2c504-c92b-4f49-855c-188296e6b938)
## 📊 Data layer:
![Novo mural](https://github.com/joaovq/MyDailyPet/assets/101160670/c7f4a4a1-7a0a-46fd-9051-86b7274cf4fc)
## 🧑‍💼 Domain layer:
![Novo mural (4)](https://github.com/joaovq/MyDailyPet/assets/101160670/9412a6c1-31b1-438f-8404-bf4fc0b85193)

## 👁️ Ui layer:
![Novo mural (3)](https://github.com/joaovq/MyDailyPet/assets/101160670/a697192b-c69e-44c5-a9e4-37b35028a22f)


# 📱  App Showcase
[![image](https://github.com/joaovq/MyDailyPet/assets/101160670/b2b46a60-cd28-439c-b811-aec732209ead)](https://youtu.be/845OMLleMKk)
# 📋 Change Logs
## v1.0.1
  - Bug fixes
    - Crash in initial screen for configuration proguard-R8 fail
    - Glitch in colors icons
## v1.0.0
 - Initial version
   - Reminders exacts
   - Pet's data (Name, Breed, Animal, Weight, Sex, Photo)
   - Tasks for pet
   - Notification 
# ☑️ Lint 
This project uses [ktlint](https://pinterest.github.io/ktlint/0.50.0/) 

# 🛒 Run (Install)

The App is available for free in the google play store, where you can install it in a safer and lighter way.
<a href='https://play.google.com/store/apps/details?id=br.com.joaovq.mydailypet&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'/></a>
