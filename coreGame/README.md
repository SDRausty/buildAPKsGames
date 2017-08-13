[<img src="/o2genum/CoreGame/raw/master/res/drawable-ldpi/icon.png" width="20px"> CoreGame](https://market.android.com/details?id=ru.o2genum.coregame): a simple game for Android
===================================

Screenshots:
------------

<img src="/o2genum/CoreGame/raw/master/etc/screenshot-for-readme1.png" width="32.5%"> &nbsp;
<img src="/o2genum/CoreGame/raw/master/etc/screenshot-for-readme2.png" width="32.5%"> &nbsp;
<img src="/o2genum/CoreGame/raw/master/etc/screenshot-for-readme3.png" width="32.5%"> 

The game:
---------

The rules are simple:

* protect the core (big cyan dot in the center of the screen) as long as possible
* gain health (cyan dots) and use shields (blue dots)

This game is an Android version of [Hakim El Hattab's "Core"](http://www.chromeexperiments.com/detail/core/). It is available in [Android Market](https://market.android.com/details?id=ru.o2genum.coregame). If you want to install the game on your device, I recommend to search `pname:ru.o2genum.coregame` in the Market, or, you can use links and QR-code below:

https://market.android.com/details?id=ru.o2genum.coregame

<a href="market://details?id=ru.o2genum.coregame">market://details?id=ru.o2genum.coregame</a>

<img src="http://qrcode.kaywa.com/img.php?s=5&d=market%3A%2F%2Fdetails%3Fid%3Dru.o2genum.coregame" alt="qrcode"  />

The code:
--------
Game framework (`ru.o2genum.coregame.framework` package) was initially taken from [Mario Zechner's "Beginning Android Games"](http://code.google.com/p/beginning-android-games/) book. I've modified a few sources and adapted them to my needs. The game itself is contained within the `ru.o2genum.coregame.game` package. Almost all game logic is in the `World.java`.

Tools I used:
-------------
* Android SDK + JDK + ant
* vim
* Inkscape
* [as3sfxr](http://www.superflashbros.net/as3sfxr/)

What I need:
------------
* I need translations. If you know languages, just download [strings.xml](/o2genum/CoreGame/raw/master/res/values/strings.xml), translate it into your language and [send it to me](http://www.google.com/recaptcha/mailhide/d?k=01YSknRhZKApKBTTbEktzc9w==&c=iSBdraKqz8T1XKSxx8QQ8Q==).

* Could you suggest some free awesome Unicode TrueType font? I think, default Android font is out of place.

* I still think that quiet and unobtrusive background music would be good. If you have any idea, please, inform me. Oh... and, of cource, the music must be free!

And, of cource, feel free to fork this project, report bugs and issues.

Special thanks to:
------------------
* [Fivos Asimakop](http://fivasim.pcriot.com/) for Greek and German translations, advices about orientation sensor (see `AndroidOrientationHandler.java`) and wake lock (`AndroidFastRenderView.java`).

License:
-------
The game is licensed under [Open Source Initiative MIT License](http://www.opensource.org/licenses/mit-license.php).
