S4X8 SolarFurnace
=================

What is this?
-------------

This is a plugin designed for the [Bukkit]-based server Terranova, and gives the recently-introduced [Daylight Sensors] a new usage: power [Furnaces]! No more coal mining or burning wood! Its usage it's very simple: just place a Daylight Sensor on top of a Furnace!

![Picture of a SolarFurnace][Picture of a SolarFurnace]

As every solar panels, they require a strong light source, so they don't work in the Nether or the End, and they must be directly under the sky, with no opaque block (glass is OK) above it. You can still use Coal and any other kind of fuel in them as you will do with standard furnaces. Those fuels will be used when there's not enough light, for example at night.

Installation
------------

To install the plugin on your Bukkit server, you may either [download the pre-built version], or compile it yourself.

To compile it yourself, you need [Oracle Java Development Kit] v1.7 or newer, [Maven 2], and Git. When you have all of them downloaded and installed, you have to grab the source code using GIT and compile it:

	git clone https://github.com/socram8888/SolarFurnace
	cd SolarFurnace
	mvn clean package
	
Maven will automatically download all the dependencies. It may take more than five minutes if you have a slow internet connection, but usually it will take less than a minute.

The resulting compiled Java Archive file (.jar) ready to be used will be at /target. Just move it (or copy it) to Bukkit's plugin folder.

License
-----

This software is released under the open-source MIT license:

>Copyright © 2016 Marcos Vives Del Sol
>
>Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
>
>The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
>
>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

Changelog
---------
* 19/VII/2016 1.5.3:
    * REALLY update MetricsLite.
* 23/VI/2016 1.5.2:
    * Fixed scary warning caused by an obsolete MetricsLite.
* 15/V/2016 1.5.1:
    * Added support for Spigot 1.8.4 or newer.
* 12/X/2013 1.5:
    * Now solar panels can be placed in any side, instead of only on top of a furnace.
* 20/IX/2013 1.4.1:
    * Added support for 1.6.4.
* 15/VIII/2013 1.4:
    * Support for SportBukkit
	* Added Metrics
* 9/VII/2013 1.3.1:
    * Support for 1.6.2
* 2/VII/2013 1.3.0:
    * Support for 1.6.1
* 17/VI/2013 1.2.3:
    * Display detected CraftBukkit version when running on an invalid version.
* 4/VI/2013 1.2.2:
    * Added permission: "solarfurnace.create" (allowed by default)
* 2/VI/2013 1.2.1:
    * Fix for 1.5.2 not being detected properly.
* 21/V/2013 1.2:
    * Added support for 1.5 and 1.5.1.
* 17/V/2013 1.1.1:
    * SolarFurnace in unloaded chunks are not ticked anymore.
    * Added this readme.
    * Minor cleanups.
* 16/V/2013 1.1:
    * The plugin is finally capable of updating Furnace blocks from an Idle furnace to a Burning furnace without making throwing its contents or missing its orientation.
    * Furnaces are also stored in a per-world basis, so saving, loading and ticking them it's much faster.
    * Major cleanup in furnace storage, which is now is handled in another class, rather than in the main.
    * The plugin now handles world loading and unloading successfully.
* 14/V/2013 1.0: First public release

About the author
----------------

My name is Marcos Vives Del Sol, aka "socram8888". I'm a 18-year-old Spanish guy who likes programming useless stuff that nobody uses. If you want to report a bug, ask for a new feature, or just say hello, you can contact me in my e-mail account <socram8888@gmail.com>.

  [Bukkit]: http://www.bukkit.org/
  [Daylight Sensors]: http://www.minecraftwiki.net/wiki/Daylight_Sensor
  [download the pre-built version]: https://github.com/socram8888/SolarFurnace/releases
  [Furnaces]: http://www.minecraftwiki.net/wiki/Furnace
  [Maven 2]: http://maven.apache.org/
  [Oracle Java Development Kit]: http://www.oracle.com/technetwork/java/javase/downloads/index.html
  [Picture of a SolarFurnace]: http://i.imgur.com/v7BOvEN.jpg
