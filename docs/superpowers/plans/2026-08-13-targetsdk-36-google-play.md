# Migração para targetSdk 36 (Android 16) — Plano de Implementação

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Subir o My Daily Pet de `targetSdk 35` para `targetSdk 36` (Android 16), para que o app continue aceito no Google Play após 31/08/2026.

**Architecture:** As versões de SDK saem dos 13 `build.gradle` e passam a viver como entradas no version catalog, referenciadas por todos os módulos. Nenhuma mudança de dependência nem de código de runtime é esperada — o trabalho restante é verificação em emulador API 36.

**Histórico:** este plano tinha uma Task 2 que subia o `play-services-ads` de 22.2.0 para 25.4.0, justificada pelo requisito de 16 KB page size. A tarefa foi **removida**: nenhum artefato do AdMob empacota `.so`, então o requisito não se aplica a ele. Detalhes e a medição no spec, seção "play-services-ads — permanece em 22.2.0". As tarefas seguintes mantêm a numeração original.

**Tech Stack:** Gradle 8.13, AGP 8.11.1, JDK 17, Kotlin 2.0.0, Groovy DSL, version catalog (`gradle/libs.versions.toml`).

**Spec:** `docs/superpowers/specs/2026-08-13-targetsdk-36-google-play-design.md`

## Global Constraints

- `compileSdk` e `targetSdk` finais: **36**. `minSdk` permanece **24**.
- `play-services-ads` permanece em **22.2.0**. Não subir de versão e não migrar para o Next-Gen SDK (`ads-mobile-sdk`).
- `versionCode` final: **16**. `versionName` final: **"1.3.0"**.
- Não atualizar Firebase BOM, Compose BOM, Material, Hilt ou Room, **salvo** se a Task 4 acusar `.so` desalinhado vindo dessas libs.
- Não remover `window.statusBarColor` (`Theme.kt:57`) nem `android:statusBarColor` (ambos `themes.xml`), **salvo** se o `lint` falhar por causa deles na Task 5.
- Não tocar em `USE_EXACT_ALARM` nem no tratamento de insets de `MainActivity.kt`.
- Todos os comandos assumem o shell **Bash** (Git Bash) a partir da raiz do worktree.

### Limitação de ambiente que molda a verificação

Builds de **release não rodam localmente**: não existem `app/my-daily-pet.jks`
nem `app/google-services.json` de produção, e `local.properties` não tem
`AD_MOB_ID`/`BANNER_AD_MOB_ID`. Esses secrets só existem no GitHub Actions.

Consequência: toda verificação local usa **debug**, que é completa para o que
importa aqui — `app/src/debug/google-services.json` é versionado e o debug usa
IDs de teste do AdMob embutidos no `build.gradle`. O AAB de release assinado é
verificado pelo CI na Task 6.

A checagem de 16 KB (Task 4) roda sobre o APK debug. Isso é válido: os `.so`
são copiados das mesmas dependências, sem recompilação, então o alinhamento
de segmento ELF é idêntico ao do release.

---

## Estrutura de arquivos

| Arquivo | Responsabilidade | Tarefa |
|---|---|---|
| `gradle/libs.versions.toml` | Fonte única das versões de SDK | 1 |
| `app/build.gradle` | Referencia o catalog; carrega `versionCode`/`versionName` | 1, 3 |
| `benchmark/build.gradle` | Referencia o catalog | 1 |
| `core/build.gradle`, `core-ui/build.gradle`, `data/build.gradle` | Referenciam o catalog | 1 |
| `pet/pet_data/build.gradle`, `pet/pet_domain/build.gradle` | Referenciam o catalog | 1 |
| `reminder/reminder_data/build.gradle`, `reminder/reminder_domain/build.gradle` | Referenciam o catalog | 1 |
| `settings/settings_presentation/build.gradle` | Referencia o catalog | 1 |
| `tasks/tasks_data/build.gradle`, `tasks/tasks_domain/build.gradle`, `tasks/tasks_presentation/build.gradle` | Referenciam o catalog | 1 |
| `local.properties` | `sdk.dir` para o worktree (não versionado) | 0 |

Nenhum arquivo de código Kotlin ou XML é modificado por este plano. Se a
verificação exigir mudança em código, ela vira uma tarefa nova, discutida antes.

---

### Task 0: Preparar o ambiente de build

O worktree não tem `local.properties`, a platform API 36 não está instalada e o
pacote do emulador está numa pasta com nome errado. Sem isso, nenhuma tarefa
seguinte roda.

**Files:**
- Create: `local.properties` e `keystore.properties` (ambos não versionados — estão no `.gitignore`)

**Interfaces:**
- Consumes: nada.
- Produces: ambiente capaz de rodar `./gradlew assembleDebug` e um AVD chamado `MyDailyPet_API36`.

- [ ] **Step 1: Confirmar que o build falha sem os arquivos de propriedades**

```bash
./gradlew :app:tasks --offline 2>&1 | tail -20
```

Esperado: FALHA. O `app/build.gradle:16` faz
`properties.load(rootProject.file('local.properties').newDataInputStream())`,
que lança `FileNotFoundException` quando o arquivo não existe.

- [ ] **Step 2: Criar `local.properties` e `keystore.properties` no worktree**

Ambos vêm do repositório principal. O `local.properties` tem o `sdk.dir`
correto; o `keystore.properties` do repositório principal está vazio (0 bytes),
o que basta — `app/build.gradle:22-23` usa `?: ""` como fallback para cada
chave ausente.

```bash
cp /c/Users/Horizon/Desktop/projetos/MyDailyPet/local.properties local.properties
cp /c/Users/Horizon/Desktop/projetos/MyDailyPet/keystore.properties keystore.properties
```

O `keystore.properties` é igualmente obrigatório: `app/build.gradle:19-24` o
carrega em tempo de configuração sempre que a variável de ambiente
`KEY_PASSWORD` não está definida — o que é o caso local, inclusive para builds
debug. Sem ele o build falha com "O sistema não pode encontrar o arquivo
especificado" antes mesmo de avaliar o bloco `android`.

- [ ] **Step 3: Corrigir a pasta do emulador**

O `sdkmanager` reporta `Warning: Observed package id 'emulator' in inconsistent
location '...\Sdk\emulator.backup' (Expected '...\Sdk\emulator')`. A pasta
`emulator` existe vazia e o pacote real (v36.5.11) está em `emulator.backup`.

```bash
rmdir "$LOCALAPPDATA/Android/Sdk/emulator" && mv "$LOCALAPPDATA/Android/Sdk/emulator.backup" "$LOCALAPPDATA/Android/Sdk/emulator"
```

Se o `rmdir` falhar por a pasta não estar vazia, pare e reporte — significa que
há duas cópias do emulador e a escolha é do usuário, não do plano.

- [ ] **Step 4: Verificar que o emulador responde**

```bash
"$LOCALAPPDATA/Android/Sdk/emulator/emulator.exe" -version
```

Esperado: imprime a versão (36.5.11) sem o warning de localização inconsistente.

- [ ] **Step 5: Instalar a platform API 36**

Instaladas hoje: `platforms;android-28`, `33`, `34`, `35`. Falta a 36.
`build-tools;36.1.0` já está presente.

```bash
"$LOCALAPPDATA/Android/Sdk/cmdline-tools/latest/bin/sdkmanager.bat" "platforms;android-36"
```

Aceite a licença se for solicitada.

- [ ] **Step 6: Confirmar a instalação**

```bash
ls "$LOCALAPPDATA/Android/Sdk/platforms"
```

Esperado: a listagem inclui `android-36`.

- [ ] **Step 7: Criar o AVD de API 36**

A imagem `system-images;android-36;google_apis_playstore;x86_64` já está
instalada.

```bash
echo no | "$LOCALAPPDATA/Android/Sdk/cmdline-tools/latest/bin/avdmanager.bat" create avd -n MyDailyPet_API36 -k "system-images;android-36;google_apis_playstore;x86_64" -d pixel_6
```

- [ ] **Step 8: Confirmar que o AVD existe**

```bash
"$LOCALAPPDATA/Android/Sdk/cmdline-tools/latest/bin/avdmanager.bat" list avd | grep -A2 MyDailyPet_API36
```

Esperado: mostra `Name: MyDailyPet_API36` e o caminho da imagem.

- [ ] **Step 9: Baseline — build debug ainda em SDK 35**

Estabelece que o projeto compila **antes** de qualquer mudança. Se falhar aqui,
o problema é de ambiente, não da migração.

```bash
./gradlew clean assembleDebug --stacktrace
```

Esperado: `BUILD SUCCESSFUL`.

Nada a commitar nesta tarefa — `local.properties` está no `.gitignore`.

---

### Task 1: Centralizar as versões de SDK no catalog e subir para 36

**Files:**
- Modify: `gradle/libs.versions.toml` (bloco `[versions]`)
- Modify: os 13 `build.gradle` de módulo listados na tabela de estrutura

**Interfaces:**
- Consumes: ambiente da Task 0.
- Produces: as chaves `compileSdk`, `targetSdk` e `minSdk` no catalog, lidas como `libs.versions.<chave>.get().toInteger()`.

- [ ] **Step 1: Adicionar as chaves ao version catalog**

Em `gradle/libs.versions.toml`, dentro de `[versions]`, logo após a linha
`agp = "8.11.1"` (linha 2):

```toml
compileSdk = "36"
targetSdk = "36"
minSdk = "24"
```

O bloco `[versions]` é ordenado alfabeticamente no resto do arquivo, mas as
chaves de SDK ficam agrupadas no topo por serem conceitualmente distintas de
versões de dependência.

- [ ] **Step 2: Trocar os literais nos 13 módulos**

Todos os 13 arquivos usam exatamente as strings `compileSdk 35`, `minSdk 24` e
`targetSdk 35`, cada uma em sua própria linha. Isso inclui o `benchmark`, que
declara `targetSdk 35` em `benchmark/build.gradle:21`. A substituição é
uniforme:

```bash
for f in app/build.gradle benchmark/build.gradle core/build.gradle \
         core-ui/build.gradle data/build.gradle \
         pet/pet_data/build.gradle pet/pet_domain/build.gradle \
         reminder/reminder_data/build.gradle reminder/reminder_domain/build.gradle \
         settings/settings_presentation/build.gradle \
         tasks/tasks_data/build.gradle tasks/tasks_domain/build.gradle \
         tasks/tasks_presentation/build.gradle; do
  sed -i \
    -e 's/^\(\s*\)compileSdk 35$/\1compileSdk libs.versions.compileSdk.get().toInteger()/' \
    -e 's/^\(\s*\)targetSdk 35$/\1targetSdk libs.versions.targetSdk.get().toInteger()/' \
    -e 's/^\(\s*\)minSdk 24$/\1minSdk libs.versions.minSdk.get().toInteger()/' \
    "$f"
done
```

`.get()` devolve `String`; o `.toInteger()` é obrigatório porque o AGP espera
`Integer` nessas propriedades.

- [ ] **Step 3: Verificar que não sobrou nenhum literal**

```bash
grep -rn "compileSdk 3\|targetSdk 3\|minSdk 2" --include=build.gradle . | grep -v "/build/"
```

Esperado: **nenhuma saída**. Qualquer linha aqui é um módulo que o `sed` não
pegou — corrija à mão antes de seguir.

- [ ] **Step 4: Verificar que as 39 referências foram criadas**

```bash
grep -rc "libs.versions.compileSdk\|libs.versions.targetSdk\|libs.versions.minSdk" --include=build.gradle . | grep -v "/build/" | grep -v ":0"
```

Esperado: 13 linhas, cada uma com contagem `3`.

- [ ] **Step 5: Confirmar que o Gradle resolve o catalog**

```bash
./gradlew :app:tasks --stacktrace 2>&1 | tail -20
```

Esperado: `BUILD SUCCESSFUL`. Um erro de `Unknown property 'compileSdk'` aqui
significa erro de digitação no `.toml`.

- [ ] **Step 6: Build debug completo em compileSdk 36**

```bash
./gradlew clean assembleDebug --stacktrace
```

Esperado: `BUILD SUCCESSFUL`. Este é o primeiro build real em API 36 e é onde
uma incompatibilidade de plataforma apareceria.

- [ ] **Step 7: Confirmar o targetSdk no manifest final**

Não confie no `build.gradle` — leia o manifest que o AGP realmente gerou:

```bash
grep -o 'targetSdkVersion="[0-9]*"' app/build/intermediates/merged_manifest/debug/processDebugMainManifest/AndroidManifest.xml
```

Esperado: `targetSdkVersion="36"`.

Se o caminho não existir, encontre-o com:
`find app/build/intermediates -name AndroidManifest.xml -path "*debug*"`.

- [ ] **Step 8: Commit**

```bash
git add gradle/libs.versions.toml app/build.gradle benchmark/build.gradle core/build.gradle core-ui/build.gradle data/build.gradle pet/ reminder/ settings/ tasks/
git commit -m "build: raise compileSdk and targetSdk to 36 via version catalog

Google Play requires targetSdk 36 for all updates from 2026-08-31.
Centralizes compileSdk/targetSdk/minSdk in libs.versions.toml so the
next bump is a single line instead of 13 edits."
```

---

---

### Task 3: Bump de versão para 1.3.0

**Files:**
- Modify: `app/build.gradle:38-39`

**Interfaces:**
- Consumes: nada das tarefas anteriores.
- Produces: `versionCode 16` / `versionName "1.3.0"` no AAB.

- [ ] **Step 1: Alterar as duas linhas**

Em `app/build.gradle`, trocar:

```groovy
        versionCode System.getenv("VERSION_CODE") ?: 15
        versionName System.getenv("VERSION_NAME") ?: "1.2.2"
```

por:

```groovy
        versionCode System.getenv("VERSION_CODE") ?: 16
        versionName System.getenv("VERSION_NAME") ?: "1.3.0"
```

O `System.getenv` fica: é um fallback inofensivo. Nem
`.github/workflows/android.yml` nem o Fastlane definem essas variáveis, então
os literais são os valores efetivos.

- [ ] **Step 2: Confirmar no manifest gerado**

```bash
./gradlew :app:processDebugMainManifest && grep -o 'versionName="[^"]*"\|versionCode="[0-9]*"' app/build/intermediates/merged_manifest/debug/processDebugMainManifest/AndroidManifest.xml
```

Esperado: `versionCode="16"` e `versionName="1.3.0- debug"` (o sufixo
`- debug` vem de `versionNameSuffix` no buildType debug — correto).

- [ ] **Step 3: Commit**

```bash
git add app/build.gradle
git commit -m "chore: bump version to 1.3.0 (versionCode 16)"
```

---

### Task 4: Verificar conformidade com 16 KB page size

Substitui suposições sobre quais dependências têm problema por uma medição.
Roda sobre o APK debug — os `.so` vêm das mesmas dependências, sem
recompilação, então o alinhamento ELF é idêntico ao do release.

**Files:**
- Nenhum modificado, a menos que a medição acuse desalinhamento.

**Interfaces:**
- Consumes: `app/build/outputs/apk/debug/app-debug.apk` das Tasks 1 e 3.
- Produces: a lista real de bibliotecas nativas a atualizar (idealmente vazia).

- [ ] **Step 1: Gerar o APK debug**

```bash
./gradlew clean assembleDebug --stacktrace
```

Esperado: `BUILD SUCCESSFUL` e `app/build/outputs/apk/debug/app-debug.apk` existe.

- [ ] **Step 2: Extrair o APK**

```bash
SCRATCH="$LOCALAPPDATA/Temp/claude/mdp-16kb" && rm -rf "$SCRATCH" && mkdir -p "$SCRATCH" && unzip -q -o app/build/outputs/apk/debug/app-debug.apk -d "$SCRATCH" && find "$SCRATCH/lib" -name "*.so" 2>/dev/null | sort
```

Se **nenhum** `.so` for listado, o app não empacota bibliotecas nativas e o
requisito de 16 KB não se aplica. Registre isso e pule para o Step 5.

- [ ] **Step 3: Medir o alinhamento de cada `.so`**

Usa o `llvm-readelf` do NDK 29.0.13113456, já instalado. O último campo de cada
linha `LOAD` é o alinhamento: `0x4000` (16384) passa, `0x1000` (4096) reprova.

```bash
READELF="$LOCALAPPDATA/Android/Sdk/ndk/29.0.13113456/toolchains/llvm/prebuilt/windows-x86_64/bin/llvm-readelf.exe"
SCRATCH="$LOCALAPPDATA/Temp/claude/mdp-16kb"
find "$SCRATCH/lib" -name "*.so" | while read -r f; do
  aligns=$("$READELF" -l "$f" | awk '$1=="LOAD" {print $NF}' | sort -u | tr '\n' ' ')
  case "$aligns" in
    *0x1000*|*0x2000*) status="REPROVADO" ;;
    *) status="ok" ;;
  esac
  echo "$status  $aligns  ${f#$SCRATCH/}"
done
```

- [ ] **Step 4: Avaliar o resultado**

Esperado: toda linha começa com `ok`.

Se alguma linha disser `REPROVADO`, identifique a dependência dona daquele
`.so` pelo nome do arquivo e:

```bash
./gradlew :app:dependencies --configuration debugRuntimeClasspath > "$LOCALAPPDATA/Temp/claude/deps.txt"
```

Consulte `deps.txt` para achar a lib e sua versão, e **pare para reportar** —
subir uma dependência fora da lista das Global Constraints é decisão do
usuário, não do plano.

- [ ] **Step 5: Registrar o resultado no spec**

Acrescente ao fim da seção `## Verificação` do arquivo
`docs/superpowers/specs/2026-08-13-targetsdk-36-google-play-design.md` uma
subseção `### Resultado da verificação de 16 KB` com a data e a saída literal
do Step 3 (ou "nenhum `.so` empacotado", se foi o caso).

- [ ] **Step 6: Commit**

```bash
git add docs/superpowers/specs/2026-08-13-targetsdk-36-google-play-design.md
git commit -m "docs: record 16 KB page size verification result"
```

---

### Task 5: Verificação visual no emulador API 36

**Files:**
- Nenhum modificado, a menos que a verificação encontre regressão.

**Interfaces:**
- Consumes: AVD `MyDailyPet_API36` da Task 0; APK debug das Tasks 1 e 3.
- Produces: confirmação de que edge-to-edge, orientação e o banner do AdMob funcionam no Android 16.

- [ ] **Step 1: Rodar testes e lint**

```bash
./gradlew test lint --stacktrace
```

Esperado: `BUILD SUCCESSFUL`. Mesmos comandos do CI
(`.github/workflows/android.yml`).

Se o lint falhar apontando `window.statusBarColor` (`Theme.kt:57`) ou
`android:statusBarColor` (`app/src/main/res/values/themes.xml:10` e
`app/src/main/res/values-night/themes.xml:9`), aí sim remova essas três linhas
— é o gatilho previsto nas Global Constraints. Rode o lint de novo e commite
com `fix: remove no-op window color APIs deprecated since API 35`.

- [ ] **Step 2: Subir o emulador**

```bash
"$LOCALAPPDATA/Android/Sdk/emulator/emulator.exe" -avd MyDailyPet_API36 -no-snapshot-load &
```

- [ ] **Step 3: Esperar o boot**

```bash
"$LOCALAPPDATA/Android/Sdk/platform-tools/adb.exe" wait-for-device shell 'while [ "$(getprop sys.boot_completed)" != "1" ]; do sleep 2; done; echo BOOTED'
```

Esperado: imprime `BOOTED`.

- [ ] **Step 4: Confirmar que é mesmo API 36**

```bash
"$LOCALAPPDATA/Android/Sdk/platform-tools/adb.exe" shell getprop ro.build.version.sdk
```

Esperado: `36`. Qualquer outro valor invalida esta tarefa inteira.

- [ ] **Step 5: Instalar o app**

```bash
./gradlew :app:installDebug
```

Esperado: `BUILD SUCCESSFUL`. O `applicationId` instalado é
`br.com.joaovq.mydailypet.debug`.

- [ ] **Step 6: Abrir o app**

```bash
"$LOCALAPPDATA/Android/Sdk/platform-tools/adb.exe" shell monkey -p br.com.joaovq.mydailypet.debug -c android.intent.category.LAUNCHER 1
```

- [ ] **Step 7: Percorrer as telas em retrato**

Manualmente no emulador, visitar: onboarding, home (com o banner do AdMob),
cadastro de pet, lembretes e settings.

Em cada tela, procurar especificamente por:
- conteúdo cortado ou escondido atrás da status bar (topo)
- conteúdo cortado ou escondido atrás da navigation bar (base)
- o banner do AdMob sobrepondo ou sendo sobreposto por outro elemento

Capturar evidência de qualquer problema:

```bash
"$LOCALAPPDATA/Android/Sdk/platform-tools/adb.exe" exec-out screencap -p > "$LOCALAPPDATA/Temp/claude/tela-<nome>.png"
```

- [ ] **Step 8: Repetir em paisagem**

O Android 16 ignora restrições de orientação acima de 600dp, então o app
precisa se comportar deitado mesmo sem nunca ter sido projetado para isso.

```bash
"$LOCALAPPDATA/Android/Sdk/platform-tools/adb.exe" shell settings put system accelerometer_rotation 0 && "$LOCALAPPDATA/Android/Sdk/platform-tools/adb.exe" shell settings put system user_rotation 1
```

Percorrer as mesmas cinco telas. Voltar ao retrato depois com
`user_rotation 0`.

- [ ] **Step 9: Repetir no tema escuro**

```bash
"$LOCALAPPDATA/Android/Sdk/platform-tools/adb.exe" shell "cmd uimode night yes"
```

Percorrer as mesmas cinco telas em retrato. O projeto tem
`values-night/themes.xml` próprio, então é um caminho de código distinto.
Voltar com `cmd uimode night no`.

- [ ] **Step 10: Confirmar que o banner do AdMob carrega**

```bash
"$LOCALAPPDATA/Android/Sdk/platform-tools/adb.exe" logcat -d -s Ads:V | tail -30
```

Esperado: mensagens de anúncio carregado, sem erro. O debug usa os IDs de teste
do Google (`ca-app-pub-3940256099942544/6300978111`, em `app/build.gradle:77`),
então o banner deve aparecer preenchido com o criativo de teste.

- [ ] **Step 11: Consolidar o resultado**

Se **nada** foi encontrado: acrescente ao spec, na seção `## Verificação`, uma
subseção `### Resultado da verificação no emulador API 36` com a data, as
telas percorridas e as três condições testadas (retrato, paisagem, tema
escuro).

Se **algo** foi encontrado: **pare e reporte** com as capturas de tela. Corrigir
layout é mudança de código, não estava previsto, e a decisão de escopo é do
usuário.

- [ ] **Step 12: Commit**

```bash
git add docs/superpowers/specs/2026-08-13-targetsdk-36-google-play-design.md
git commit -m "docs: record API 36 emulator verification result"
```

---

### Task 6: Validar o AAB de release no CI

O AAB assinado só pode ser produzido onde os secrets existem — keystore,
`google-services.json` de produção e IDs do AdMob vivem no GitHub Actions, não
na máquina local.

**Files:**
- Nenhum modificado.

**Interfaces:**
- Consumes: o branch com as Tasks 1–5 concluídas.
- Produces: um `app-release.aab` assinado como artefato do workflow.

- [ ] **Step 1: Confirmar que a árvore está limpa**

```bash
git status --porcelain
```

Esperado: **nenhuma saída**. `local.properties` não aparece porque está no
`.gitignore`.

- [ ] **Step 2: Revisar o diff completo da migração**

```bash
git diff main...HEAD --stat && git diff main...HEAD -- gradle/libs.versions.toml app/build.gradle
```

Esperado: 14 arquivos de build alterados mais os documentos. Confira à vista
que `compileSdk`/`targetSdk` são 36, que `playServicesAds` continua 22.2.0 e
que a versão é 16/1.3.0.

- [ ] **Step 3: Push do branch**

```bash
git push -u origin claude/maxsdk-google-play-update-d7830e
```

- [ ] **Step 4: Acompanhar o workflow**

O `.github/workflows/android.yml` dispara em push para `main`, não neste
branch. Então dispare o build de release abrindo um PR para `main`, ou peça ao
usuário para rodar o workflow manualmente.

**Pare aqui e confirme com o usuário** antes de abrir PR ou fazer merge —
publicar é ação de fora, e a decisão é dele.

- [ ] **Step 5: Verificar o artefato**

Quando o job `build` passar, baixar o artefato
`Release apk e aab-<ref>` e confirmar que `app-release.aab` existe.

Critério de aceite final, conforme o spec:
- AAB de release assinado gerado com sucesso
- `test` e `lint` passando
- Todos os `.so` do app alinhados a 16 KB (Task 4)
- Nenhuma regressão visual no emulador API 36 (Task 5)

---

## Fora deste plano

Registrado no spec e deliberadamente não implementado aqui: upgrade geral de
dependências, migração para o Mobile Ads Next-Gen SDK, remoção das APIs de
janela mortas, revisão do `CONSUMED` em `MainActivity.kt:70`, e a política do
`USE_EXACT_ALARM`.
