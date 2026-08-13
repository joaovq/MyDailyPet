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

### play-services-ads — permanece em 22.2.0

**Correção.** Uma versão anterior deste spec afirmava que a 22.2.0 não atendia
ao requisito de **16 KB page size** e que o upgrade para 25.4.0 era obrigatório.
Isso estava errado, e a afirmação foi feita sem medição.

O requisito de 16 KB se aplica a bibliotecas nativas. Nenhum artefato do AdMob
empacota `.so` — contagem de `.so` dentro dos AARs no cache do Gradle:

```
0  play-services-ads-22.2.0.aar
0  play-services-ads-25.4.0.aar
0  play-services-ads-api-25.4.0.aar
0  play-services-ads-base-22.2.0.aar
0  play-services-ads-lite-22.2.0.aar
```

As release notes do AdMob também não mencionam 16 KB em nenhuma versão. Sem
código nativo, o requisito não se aplica, e não resta justificativa de prazo
para o upgrade.

A tentativa de subir para 25.4.0 também não era gratuita: o `-api.jar` da
25.4.0 traz metadata compilada com Kotlin 2.3.0, enquanto o projeto está em
`kotlin = "2.0.0"`. O `:app:kspDebugKotlin` falha com 19 erros de versão de
metadata. Corrigir exigiria subir Kotlin, KSP e o plugin do Compose juntos —
acoplados pela mesma chave do catalog — com risco real de regressão em
Hilt/Room/Compose e nenhum ganho para a validação do Play.

A 22.2.0 compila normalmente em `compileSdk 36` (comprovado pelo build da
migração de SDK). O comportamento em runtime no Android 16 é verificado no
emulador; se o banner falhar lá, o upgrade volta à mesa com evidência.

Os `.so` que o app de fato empacota não vêm do AdMob. A medição está em
"Resultado da verificação de 16 KB", mais abaixo.

**Não migrar para o Google Mobile Ads Next-Gen SDK.** Desde 06/07/2026 ele é o
SDK "preferido" da Google, mas é um artefato diferente
(`com.google.android.libraries.ads.mobile.sdk:ads-mobile-sdk`) com API
reescrita. É projeto próprio, não parte desta migração.

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
| Edge-to-edge obrigatório, sem opt-out | `MainActivity.kt:62` aplica `systemBars` como margem na raiz e retorna `CONSUMED`. Trata as barras de sistema, mas **não** `displayCutout()` nem `ime()` — ver "Limitações conhecidas do tratamento de insets" | Validar tela a tela no emulador; corrigir só se houver corte ou sobreposição |
| `statusBarColor` / `navigationBarColor` deprecados | `Theme.kt:57` e ambos os `themes.xml` — já são no-op desde a API 35 | Manter. Se o `lint` em API 36 falhar por isso, remover |
| Predictive back | `enableOnBackInvokedCallback="true"` no manifest; `OnBoardingFragment.kt:92` usa `OnBackPressedDispatcher` | Nenhuma |
| `screenOrientation` ignorado acima de 600dp | Nada travado no projeto | Validar em paisagem no emulador |
| Alarmes exatos | `setExactAndAllowWhileIdle` em `AndroidAlarmScheduler.kt:39` | Nenhuma. Continua funcionando no A16 |

## Verificação

### Limitação de ambiente

Builds de **release não rodam localmente**. Não existem `app/my-daily-pet.jks`
nem `app/google-services.json` de produção em nenhuma cópia do repositório —
apenas `app/src/debug/google-services.json`, que é versionado. O
`local.properties` contém somente `sdk.dir`, sem `AD_MOB_ID` nem
`BANNER_AD_MOB_ID`. Esses secrets existem apenas no GitHub Actions.

Por isso a verificação local usa **debug**, e o AAB de release assinado é
validado no CI. A cobertura real não é perdida: o debug tem
`google-services.json` versionado e usa os IDs de teste do AdMob definidos em
`app/build.gradle`.

Também é preciso preparar o ambiente antes de qualquer build: a platform
`android-36` não está instalada (build-tools 36.1.0 está), e o pacote do
emulador está em `Sdk/emulator.backup` em vez de `Sdk/emulator`.

### Passos

1. **Build limpo** — `./gradlew clean assembleDebug --stacktrace`. Confirma que
   os 13 módulos compilam em `compileSdk 36`. O `targetSdk` efetivo é conferido
   no manifest mesclado, não no `build.gradle`.
2. **Lint e testes** — `./gradlew test lint --stacktrace`, idêntico ao CI.
3. **16 KB page size** — extrair os `.so` do APK debug e checar o alinhamento
   de segmento LOAD de cada um com o `llvm-readelf` do NDK 29. Válido apesar de
   ser debug: os `.so` são copiados das mesmas dependências, sem recompilação,
   então o alinhamento ELF é idêntico ao do release. Esta checagem substitui
   qualquer suposição sobre quais dependências têm problema; a lista real de
   libs a atualizar sai daqui.
4. **Emulador API 36** — AVD sobre `system-images;android-36;google_apis_playstore;x86_64`
   (já instalada). Instalar o debug e percorrer home (com o banner do AdMob),
   onboarding, cadastro de pet, lembretes e settings, em retrato e paisagem,
   tema claro e escuro. Foco em corte/sobreposição nas barras de sistema.
5. **Anúncios no Android 16** — confirmar que o banner da home carrega com o
   AdMob 22.2.0 rodando em API 36, usando os IDs de teste do Google. Este é o
   único ponto onde a decisão de manter a 22.2.0 pode se mostrar errada; se o
   banner falhar aqui, o upgrade volta à mesa com evidência de runtime.
6. **AAB de release no CI** — abrir PR para `main` e confirmar que o job
   `build` de `.github/workflows/android.yml` produz o `app-release.aab`
   assinado.

### Critério de aceite

- AAB de release assinado gerado com sucesso
- `test` e `lint` passando
- Todos os `.so` do app alinhados a 16 KB
- ~~Nenhuma regressão visual nas telas principais no emulador API 36~~ —
  **não verificado**, ver abaixo

### Resultado de lint e testes

Executado em 2026-08-13: `./gradlew testDebugUnitTest lintDebug` →
`BUILD SUCCESSFUL in 2m 28s`, 614 tasks.

O comando do CI (`./gradlew test lint`) **não roda localmente**: falha em
`:app:processBenchmarkGoogleServices`, porque o build type `benchmark` herda do
`release` e exige o `app/google-services.json` de produção. No CI o
`.github/workflows/scripts.sh` escreve esse arquivo a partir do secret
`GOOGLE_SERVICES_DATA`, então lá o comando completo funciona. A limitação é de
ambiente e independe da migração.

O `lint` em API 36 **não sinalizou** `window.statusBarColor` nem
`android:statusBarColor` — zero ocorrências no relatório. O gatilho previsto
para removê-los não disparou, então permanecem no código, como manda o escopo
mínimo.

### Verificação no emulador API 36 — NÃO REALIZADA

Decisão do usuário em 2026-08-13, após o emulador não subir por falta de espaço
em disco:

```
FATAL | Not enough space to create userdata partition.
        Available: 4306.92 MB, need 7372.80 MB.
```

O disco estava em 99% (4,3 GB livres). As alternativas oferecidas — reduzir a
partição do AVD, apagar um AVD antigo (`Nexus_5`, 12,3 GB), ou liberar espaço
manualmente — foram recusadas em favor de seguir sem essa verificação. O único
dispositivo físico conectado é um Samsung SM_T510 com Android 11 (API 30), que
não serve para testar API 36.

**Risco assumido.** Nada do comportamento de runtime no Android 16 foi
observado. Continuam sem evidência:

- edge-to-edge — se o tratamento de insets em `MainActivity.kt:62` cobre todas
  as telas sob a imposição do Android 16, sem corte nem sobreposição
- orientação — comportamento em paisagem, agora que o A16 ignora restrições de
  orientação acima de 600dp
- tema escuro — o caminho de `values-night/themes.xml`
- **AdMob 22.2.0 em runtime na API 36** — o banner da home carregar. Este é o
  item mais sensível, porque a decisão de não subir o AdMob foi tomada com base
  em compilação, não em execução

Todos são verificáveis depois num emulador API 36, ou no canal de teste interno
do Play antes da promoção para produção.

### Resultado da verificação de 16 KB

Medido em 2026-08-13, sobre o APK debug (`./gradlew clean assembleDebug`,
`BUILD SUCCESSFUL in 1m 35s`), extraído e analisado com o `llvm-readelf` do
NDK 29.0.13113456. Nenhum artefato do AdMob (`play-services-ads` 22.2.0)
empacota `.so` — confirmado por contagem zero em todos os AARs em cache. Os
únicos `.so` do APK vêm de `androidx.graphics:graphics-path:1.0.1`
(`libandroidx.graphics.path.so`), uma dependência transitiva, presente nas
quatro ABIs:

```
ok  0x4000   lib/arm64-v8a/libandroidx.graphics.path.so
ok  0x4000   lib/armeabi-v7a/libandroidx.graphics.path.so
ok  0x4000   lib/x86/libandroidx.graphics.path.so
ok  0x4000   lib/x86_64/libandroidx.graphics.path.so
```

Todas as quatro bibliotecas nativas têm o segmento `LOAD` alinhado a `0x4000`
(16384 bytes). Nenhuma dependência precisa de upgrade por este critério.

**Ressalva sobre o alcance desta medição.** O requisito de 16 KB do Play tem
duas partes, e esta mede uma:

1. alinhamento do segmento `LOAD` no ELF ≥ 16 KB — **medido, passa**;
2. os `.so` gravados sem compressão e alinhados a 16 KB dentro do APK/AAB —
   **não medido**. É responsabilidade do AGP, que faz isso automaticamente
   desde a 8.5.1 (o projeto usa 8.11.1), mas isso é inferência, não medição.

Confirmar a parte 2 no AAB produzido pelo CI, com
`zipalign -c -P 16 -v app-release.aab`.

## Limitações conhecidas do tratamento de insets

Levantadas na revisão final. Nenhuma é regressão desta migração — todas já
valiam em `targetSdk 35` — mas o Android 16 aumenta a exposição de duas delas,
e o spec antes chamava o tratamento de "válido" com base apenas em leitura de
código, sem execução.

`MainActivity.kt:62` consulta somente `WindowInsetsCompat.Type.systemBars()`:

- **`displayCutout()` não é consultado.** Desde o `targetSdk 35` o modo padrão
  de recorte é `LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS`, e `systemBars()` não
  inclui o recorte. Em paisagem, num aparelho com notch, o conteúdo passa por
  baixo dele. O Android 16 torna isso mais provável: acima de 600dp ele ignora
  restrições de orientação, então paisagem vira caminho de primeira classe — e
  o projeto não tem `layout-land` nem `values-sw600dp` em nenhum módulo.
  Correção de uma linha:
  `systemBars() or WindowInsetsCompat.Type.displayCutout()`.
- **`CONSUMED` na raiz impede qualquer filho de ver insets**, inclusive os do
  teclado. `setOnApplyWindowInsetsListener` aparece uma única vez em todo o
  código. Com edge-to-edge, `adjustResize` não redimensiona mais a janela, o
  que expõe campos de texto ancorados na base — formulários de cadastro de pet,
  lembretes e tarefas.

**Resolução.** O `displayCutout()` foi adicionado (commit `c20b703`), por
decisão do usuário, saindo do escopo mínimo. O `CONSUMED` e os insets de
teclado **permanecem como estão** — continuam sendo limitação conhecida e não
verificada.

## Risco não mitigado: publicação direta em produção

`fastlane/Fastfile`, lane `deploy`, chama `upload_to_play_store` sem argumento
`track:`. O default do `supply` é **`production`**, e
`.github/workflows/android_fastlane.yml` dispara em tags `v*`.

Ou seja: `git tag v1.3.0 && git push --tags` publica para 100% dos usuários um
build cujo comportamento em Android 16 nunca foi observado.

Foi oferecida a troca para `track: 'internal'` e **recusada** — a decisão foi
manter o fluxo de publicação como está. Registrado para que a escolha seja
rastreável.

Quem for publicar deve, em vez disso, promover manualmente pelo Play Console a
partir de um canal de teste, ou usar `rollout` gradual.

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
