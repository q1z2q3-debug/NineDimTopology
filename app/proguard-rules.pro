# Keep core library (it's the API surface)
-keep class com.ninedim.core.** { *; }

# Keep Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep data classes for serialization
-keep class com.ninedim.core.data.** { *; }

# General
-keepattributes *Annotation*
-keepattributes Signature
-dontwarn kotlin.**
