# Migração para targetSdk 36 (Android 16)

Data: 2026-08-13
Branch: `claude/maxsdk-google-play-update-d7830e`

## Problema

A partir de **31/08/2026**, o Google Play exige `targetSdk 36` (Android 16) para
novas apps e para toda atualização de app existente. Extensão possível até
**01/11/2026**, mediante solicitação no Play Console.

O My Daily Pet está em `targetSdk 35`. Sem a migração, nenhuma atualização será
aceita após o prazo.

Fonte: https://developer.android.com/google/play/requirements/target-sdk

## Escopo

Escopo mínimo: subir para SDK 36 e corrigir apenas o que quebra o build ou
bloqueia a publicação. Sem upgrade geral de dependências e sem limpeza de APIs
deprecadas.

## Mudanças de build

### Version catalog

Adicionar em `gradle/libs.versions.toml`:

```
compileSdk = "36"
targetSdk = "36"
minSdk = "24"
```

### Módulos

Os 13 módulos abaixo trocam os literais `compileSdk 35` / `targetSdk 35` /
`minSdk 24` por referências ao catalog:

`app`, `benchmark`, `core`, `core-ui`, `data`, `pet/pet_data`, `pet/pet_domain`,
`reminder/reminder_data`, `reminder/reminder_domain`,
`settings/settings_presentation`, `tasks/tasks_data`, `tasks/tasks_domain`,
`tasks/tasks_presentation`.

Motivo de centralizar em vez de editar literal por literal: o projeto já usa
version catalog para todo o resto, e centralizar elimina a classe de erro
"esqueci um módulo" — que é exatamente o que faz o Play rejeitar o AAB.

### Versão do app

Em `app/build.gradle`: `versionCode 16`, `versionName "1.3.0"`.

Os valores no arquivo são os efetivamente usados — nem `.github/workflows/android.yml`
nem o Fastlane definem `VERSION_CODE`/`VERSION_NAME` no ambiente, apesar do
`System.getenv` no `build.gradle`.

### play-services-ads

`22.2.0` (jul/2023) → `25.4.0` (17/06/2026, última estável).

Obrigatório: a versão atual não atende ao requisito de **16 KB page size**,
vigente para submissões ao Play desde 01/11/2025.

Requisitos da 25.4.0, ambos já satisfeitos: `minSdk` 23 (o app está em 24) e
`compileSdk` 34+ (o app irá para 36).

Superfície de API em uso, toda em `HomeFragment.kt` e `fragment_home.xml`:
`MobileAds.initialize`, `MobileAds.setRequestConfiguration`, `AdRequest.Builder`,
e a view `com.google.android.gms.ads.AdView`.

As remoções das majors 24.0.0 e 25.0.0 (`SearchAdView`, `NativeAdViewHolder`,
APIs de interscroller, classes de mediação, métodos de orientação do
`AppOpenAd`) não tocam nada do que o app usa. Migração esperada como mecânica —
apenas a troca de versão no catalog.

**Não migrar para o Google Mobile Ads Next-Gen SDK.** Desde 06/07/2026 ele é o
SDK "preferido" da Google, mas é um artefato diferente
(`com.google.android.libraries.ads.mobile.sdk:ads-mobile-sdk`) com API
reescrita. É um projeto próprio, não parte desta migração — e o
`play-services-ads` 25.4.0 continua suportado e atende ao prazo do Play.

### Sem mudança

AGP 8.11.1, Gradle 8.13 e JDK 17 já suportam API 36. Firebase BOM, Compose BOM,
Material, Hilt e Room permanecem nas versões atuais, salvo se a verificação de
16 KB (abaixo) acusar `.so` desalinhado — nesse caso a lib responsável é
atualizada pontualmente.

## Comportamento no Android 16

A expectativa é **nenhuma mudança de código** nesta seção. O trabalho é
verificação; cada item vira correção apenas se a verificação falhar.

| Mudança do A16 | Estado atual | Ação |
|---|---|---|
| Edge-to-edge obrigatório, sem opt-out | `MainActivity.kt:62` aplica `systemBars` como margem na raiz e retorna `CONSUMED` — tratamento válido | Validar tela a tela no emulador; corrigir só se houver corte ou sobreposição |
| `statusBarColor` / `navigationBarColor` deprecados | `Theme.kt:57` e ambos os `themes.xml` — já são no-op desde a API 35 | Manter. Se o `lint` em API 36 falhar por isso, remover |
| Predictive back | `enableOnBackInvokedCallback="true"` no manifest; `OnBoardingFragment.kt:92` usa `OnBackPressedDispatcher` | Nenhuma |
| `screenOrientation` ignorado acima de 600dp | Nada travado no projeto | Validar em paisagem no emulador |
| Alarmes exatos | `setExactAndAllowWhileIdle` em `AndroidAlarmScheduler.kt:39` | Nenhuma. Continua funcionando no A16 |

## Verificação

1. **Build limpo** — `./gradlew clean assembleRelease app:bundleRelease`.
   Confirma que os 13 módulos compilam em `compileSdk 36` e que a subida do
   AdMob não quebrou nada.
2. **Lint e testes** — `./gradlew test lint --stacktrace`, idêntico ao CI.
3. **16 KB page size** — extrair os `.so` do `app-release.aab` e checar o
   alinhamento de segmento LOAD de cada um. Esta checagem substitui qualquer
   suposição sobre quais dependências têm problema; a lista real de libs a
   atualizar sai daqui.
4. **Emulador API 36** — instalar o release e percorrer home (com o banner do
   AdMob), onboarding, cadastro de pet, lembretes e settings, em retrato e
   paisagem, tema claro e escuro. Foco em corte/sobreposição nas barras de
   sistema.
5. **Regressão de anúncios** — confirmar que o banner da home carrega na 24.x.

### Critério de aceite

- AAB de release assinado gerado com sucesso
- `test` e `lint` passando
- Todos os `.so` do AAB alinhados a 16 KB
- Nenhuma regressão visual nas telas principais no emulador API 36

## Fora de escopo

Registrado aqui por ter sido levantado durante o diagnóstico, mas
deliberadamente não incluído:

- **Upgrade geral de dependências** — Firebase BOM 32.2.0 (jul/2023), Compose
  BOM 2024.10, Material 1.11, Hilt, Room. Risco de regressão incompatível com o
  prazo de 18 dias.
- **Migração para o Mobile Ads Next-Gen SDK** — preferido pela Google desde
  julho/2026, mas é artefato e API novos. Vale como projeto separado.
- **Remoção das APIs de janela mortas** — `window.statusBarColor` em `Theme.kt`
  e `android:statusBarColor` nos `themes.xml`. São no-op; removê-los é limpeza,
  não correção.
- **Revisão do `CONSUMED`** — `MainActivity.kt:70` consome todos os insets na
  raiz, o que impede qualquer filho de reagir a insets. Funciona hoje, mas
  limita evoluções de layout.
- **`AD_TEST_DEVICES` vazio** — `BuildConfig.AD_TEST_DEVICES.split(",")` produz
  `[""]` quando a string é vazia, em `HomeFragment.kt:124`. Pré-existente e
  inofensivo.

### Risco conhecido: `USE_EXACT_ALARM`

O manifest declara `USE_EXACT_ALARM`, permissão que a política do Play reserva
a apps cuja função central é despertador ou calendário. Um app de lembretes de
pet é discutível sob esse critério.

Não é uma mudança do Android 16 e a v1.2.1 já passou pela revisão com essa
permissão, então fica fora do escopo. Mas é o item mais provável de causar
rejeição por política — e não por plataforma — numa revisão futura. Se a
submissão for rejeitada por isso, o caminho é remover `USE_EXACT_ALARM`,
manter apenas `SCHEDULE_EXACT_ALARM` e adicionar a checagem de
`canScheduleExactAlarms()` antes de agendar, com fallback para alarme inexato.
