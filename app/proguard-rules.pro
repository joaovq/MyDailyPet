# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class br.com.joaovq.mydailypet.* { *; }
-keep class br.com.joaovq.core.* { *; }
-keep class br.com.joaovq.data.* { *; }
-keep class br.com.joaovq.reminder_data.* { *; }
-keep class br.com.joaovq.reminder_domain.* { *; }
-keep class br.com.joaovq.core_ui.* { *; }
-keep class br.com.joaovq.settings_presentation.* { *; }
-keep class br.com.joaovq.tasks_presentation.* { *; }
-keep class br.com.joaovq.pet_domain.model.* { *; }
-keep class br.com.joaovq.reminder_domain.model.* { *; }
-keep class br.com.joaovq.tasks_domain.model.* { *; }
-keep class br.com.joaovq.pet_domain.* { *; }
-keep class br.com.joaovq.pet_data.* { *; }
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken