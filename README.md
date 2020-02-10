# CE530 Applets

A launcher that allows running the [CE530 educational applets](http://www.eng.buffalo.edu/~kofke/ce530/Applets/applets.html)
as desktop applications, since browser applets are no longer supported. Uses `jlink` to produce a
distribution that can run without a system Java dependency.

## Usage

Build the runtime image with `./gradlew runtime` or `./gradlew runtimeZip`. Then run the launcher
with `./build/ce530_applets/bin/ce530-applets`. If you downloaded the zip distribution, extract it
then run `ce530_applets/bin/ce530-applets`. 