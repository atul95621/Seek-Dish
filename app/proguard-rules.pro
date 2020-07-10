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

-dontwarn okio.**
-dontwarn retrofit2.**
-keepattributes Signature
-keepattributes Exceptions

-keepclassmembernames interface * {
    @retrofit.http.* <methods>;
}

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}


# model class paths
-keep class com.dish.seekdish.ui.home.dataModel.* { *; }
-keep class com.dish.seekdish.ui.home.mapInfoWindow.* { *; }
-keep class com.dish.seekdish.ui.login.* { *; }
-keep class com.dish.seekdish.ui.signup.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.activities.model.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.checkin.data.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.dishDescription.similar.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.dishDescription.opinion.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.dishDescription.model.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.invitation.includeFriends.fragments.friends.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.invitation.includeFriends.fragments.selected.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.invitation.invited.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.myFriends.contactFetch.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.myFriends.dataModel.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.notifications.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.restaurantDiscription.checkInRestro.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.restaurantDiscription.meals.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.restaurantDiscription.similar.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.restaurantDiscription.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.restaurants.dataClass.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.restaurants.mapWindow.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.settings.dataModel.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.settings.myAlerts.* { *; }
-keep class com.dish.seekdish.ui.navDrawer.toDo.list.* { *; }

