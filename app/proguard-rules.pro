# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/floyd/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontoptimize
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-allowaccessmodification
-printmapping map.txt
-optimizationpasses 7
-dontskipnonpubliclibraryclassmembers
-dontwarn com.google.android.maps.**
-dontwarn android.support.v7.**
-dontwarn android.support.v4.**
-dontwarn junit.**
-keep public class * extends android.app.Activity {*;}
-keep public class * extends android.app.Application
-keep public class * extends android.support.v4.app.Fragment {*;}
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.os.IInterface
-keep public class * extends android.appwidget.AppWidgetProvider
-keep public class * extends android.webkit.*{*;}
-keep public class * extends android.widget.*{*;}
-keep public class * extends android.app.*{*;}
-keep class com.floyd.diamond.biz.vo.*{*;}
-keep class de.greenrobot.event.* {*;}
-keep interface com.floyd.diamond.biz.vo.IKeepClassForProguard {*;}
-keep class * implements com.floyd.diamond.biz.vo.IKeepClassForProguard {*;}
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,
                SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {*;}

-keepattributes  *Annotation*
-keepattributes de.greenrobot.event.Subscribe
