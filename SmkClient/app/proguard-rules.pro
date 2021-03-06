# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\woozy\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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
-dontwarn com.makeramen.roundedimageview.**
-dontwarn okio.**
-dontwarn android.net.http.**
-dontwarn com.android.internal.http.**
-dontwarn org.apache.http.**
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keep public class * extends Android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
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
-keep class * implements android.os.Parcelable {
        public static final android.os.Parcelable$Creator *;
}
-dontskipnonpubliclibraryclasses
-dontwarn com.alibaba.fastjson.**
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}