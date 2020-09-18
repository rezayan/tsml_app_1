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

#---- glide ------>>>>>>>>>>>>
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#<<<<<<<<<<<<<<<<<<<<<<<<-------------------------


#------ retrofit ---------->>>>>>>>>>>>>>>>>>>>
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-dontwarn okio.**

-dontwarn retrofit2.**
-dontwarn org.codehaus.mojo.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleParameterAnnotations

-keepattributes EnclosingMethod

-keepclasseswithmembers class * {
    @retrofit2.* <methods>;
}

-keepclasseswithmembers interface * {
    @retrofit2.* <methods>;
}

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
#<<<<<<<<<<<<<<<<<<<<<<<<<<<<<----------------------------


##--------------- Gson  ---------->>>>>>>>>>>>>>
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keepclassmembers enum * { *; }
-keep class com.google.gson.** { *; }

#<<<<<<<<<<<<<<<<<<<<<<<<-------------------------


#-------- OkHttp --->>>>>>>>>>>>>>>>>>>>>>>>>>>>>
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
#<<<<<<<<<<<<<<<<<<<<<<<<-------------------------


#---- support ------>>>>>>>>>>>>>>>>>
-keep class android.support.v4.** { *; }
-keep class android.support.v8.** { *; }
-keep class android.support.v7.** { *; }
-keep class android.support.v8.** { *; }
-keep class android.support.v7.** { *; }
-keep class android.support.** { *; }
-dontwarn android.support.v4.**
-dontwarn android.support.v8.**
-dontwarn android.support.v7.**
#---- support ------<<<<<<<<<<<<<<<<<


#---- google ------>>>>>>>>>>>>>>>>>
-keep class com.google.android.gms.** { *; }
-keep class com.google.** { *; }
-dontwarn com.google.android.gms.**
-dontwarn com.google.**
#---- google ------<<<<<<<<<<<<<<<<<


#---- room ------>>>>>>>>>>>>>>>>>
-dontwarn android.arch.util.paging.CountedDataSource
-dontwarn android.arch.persistence.room.paging.LimitOffsetDataSource
#---- room ------<<<<<<<<<<<<<<<<<


#---- apache ------>>>>>>>>>>>>>>>>>
-keep class org.apache.** { *; }
-dontwarn org.apache.**
#---- apache ------<<<<<<<<<<<<<<<<<


#---- redinput ------>>>>>>>>>>>>>>>>>
-keep class com.redinput.** { *; }
-dontwarn com.redinput.**
#---- redinput ------<<<<<<<<<<<<<<<<<


#---- instabug ------>>>>>>>>>>>>>>>>>
-keep class com.instabug.** { *; }
-dontwarn com.instabug.**
#---- instabug ------<<<<<<<<<<<<<<<<<


#---- instabug ------>>>>>>>>>>>>>>>>>
-keep class com.priyankvasa.** { *; }
-dontwarn com.priyankvasa.**
#---- instabug ------<<<<<<<<<<<<<<<<<


#---- redinput ------>>>>>>>>>>>>>>>>>
-keep class com.otaliastudios.** { *; }
-dontwarn com.otaliastudios.**
#---- redinput ------<<<<<<<<<<<<<<<<<
