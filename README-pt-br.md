# 🐶 Minha Rotina Pet
![Static Badge](https://img.shields.io/badge/Version-1.0.1-blue)
![Static Badge](https://img.shields.io/badge/Kotlin-1.8.10-purple)
![Static Badge](https://img.shields.io/badge/JUnit-4.13.2-purple)
![Static Badge](https://img.shields.io/badge/Mockk-1.13.5-red)
![Static Badge](https://img.shields.io/badge/Coroutines-1.6.4-pink)

App para donos de pets gerenciar diariamente as suas rotinas semanais.

<p style="display:flex; justify-content:center" width="100%">
  <img src="https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Google_Play-414141?style=for-the-badge&logo=google-play&logoColor=white"/>
  <img src="https://img.shields.io/badge/Trello-0052CC?style=for-the-badge&logo=trello&logoColor=white"/>
</p>

### Contexto:

📆 A rotina do meu pet foi pensada através da minha necessidade de conseguir organizar as coisas importantes do meu pet em um só lugar.

Com o app você pode criar lembretes, adicionar tarefas e dados do seu pet. Com isso você tem informações que podem ser necessárias para o dia-a-dia com seu animal de estimação. O My Daily Pet veio para facilitar o seu dia a dia.

O aplicativo está disponível na Google Play Store.

# Tecnologias
|  Tecnologia |             Link         | 
|:-----------:|:-----------------------:|
| Desenho Layouts c/ paradiga Views e XML|
| MVVM, MVP and MVI arquitetura| 
| Libraries Android Jetpack (Permissions)|
|Room Android biblioteca SQLite (persistência de dados do usuário)| [Room Android library SQLite (user data storage)](https://developer.android.com/training/data-storage/room/)|
|Alarm Manager para lembretes (exato e repetivo alarme)| [Alarm Manager for reminders (exact and repetitive alarm programming)](https://developer.android.com/training/scheduling/alarms)|
|WorkManager| [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager/basics?hl=pt-br)|
|Notification Manager e Notification Service (mandar notificações)| [Notification Manager and Notification Service (sending notifications)](https://developer.android.com/develop/ui/views/notifications)|
|Glide| [Glide](https://bumptech.github.io/glide/)|
|Firebase Crashlytics (bug e crash momnitoramento em produção)| [Firebase Crashlytics (bug and crash monitoring in app production)](https://firebase.google.com/)|
|Ad Mob (Mobile anúncios)| [Ad Mob (Mobile ads)](https://admob.google.com/home/)|
|Broadcast Receiver (Receber notificações)| [Broadcast Receiver (Receive notifications from alarms)](https://developer.android.com/guide/components/broadcasts)|
|Content Provider (para compartilhar imagens e persistência)| [Content Provider (for share images of pet)](https://developer.android.com/guide/topics/providers/content-provider-basics)|
|Data Store (Configurações de preferências do usuário)| [Data Store (user preferences settings)](https://developer.android.com/topic/libraries/architecture/datastore)|

# Design
Na versão 1.0, o app conta com 8 telas desenhadas com cores minimalistas, em figma e android studio.
<table>
  <tr>
    <th>Home</th>
    <th>Home (Dark)</th>
    <th>Lembrete</th>
    <th>Lembrete (Notificação)</th>
    <th>Configurações</th>
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
    <th>Adicionar pet</th>
    <th>Editar pet</th>
    <th>Editar Lembrete</th>
    <th>Home (Lemmbretes para hoje)</th>
    <th>Detalhes do pet</th>
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
# 📦 Pacotes do aplicativo
O aplicativo foi criado usando arquitetura de dados de recursos. A partir disso, as feições foram criadas e dentro delas divididas em Camada de Dados, Camada de Domínio e Camada de Ui.
## Todos os pacotes:

![Novo mural (1)](https://github.com/joaovq/MyDailyPet/assets/101160670/64b2c504-c92b-4f49-855c-188296e6b938)
## 📊 Camada de dados:

![Novo mural](https://github.com/joaovq/MyDailyPet/assets/101160670/c7f4a4a1-7a0a-46fd-9051-86b7274cf4fc)
## 🧑‍💼 Camada de Domínio (Negócio):

![Novo mural (4)](https://github.com/joaovq/MyDailyPet/assets/101160670/9412a6c1-31b1-438f-8404-bf4fc0b85193)

## 👁️ Camada de Interface do usuário:
![Novo mural (3)](https://github.com/joaovq/MyDailyPet/assets/101160670/a697192b-c69e-44c5-a9e4-37b35028a22f)
# Showcase
[![image](https://github.com/joaovq/MyDailyPet/assets/101160670/b2b46a60-cd28-439c-b811-aec732209ead)](https://youtu.be/845OMLleMKk)
# Change Logs
## v1.0.1
  - Correções de bugs
     - Falha na tela inicial para falha de configuração proguard-R8
     - Falha nos ícones de cores
## v1.0.0
  - Versão inicial
    - Exatos de lembretes
    - Dados do Pet (Nome, Raça, Animal, Peso, Sexo, Foto)
    - Tarefas para animais de estimação
    - Notificação
# Como rodar o projeto (Download)

O App está disponível gratuitamente na google play store, onde você pode instalá-lo de forma mais segura e leve.
<a href='https://play.google.com/store/apps/details?id=br.com.joaovq.mydailypet&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png'/></a>
