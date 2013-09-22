QClock
=======

QClock is an Android application for the Nexus Q that shows the current time using the LEDs. 
This was a quick weekend hack leveraging some of the code I wrote for the QRemote application.
Since the Nexus Q is dead as a product, it's highly unlikely there'll be any updates, and the
information below is probably stale and incorrect.

Installation
------------
Currently the Nexus Q does not provide a supported way to install applications. Thus, you
must use the Android ADB tool to install and start QRemote:

    adb install QClock.apk
    adb shell am start -a android.intent.action.MAIN -n com.blinnikka.android.qclock/.StartServiceActivity

Usage
-----
Manage to get the application up and running, and then enjoy the light show.

Project Structure
-----------------
The code that controls the Nexus Q was copied over from the QRemote project.

Building
--------
Assuming you have a working Android development environment, such as Eclipse set up, you should be
simply able to check out the code and import it into your IDE.

Contact
-------
Additional information available on http://bliny.net