developers-pocket-knife
=======================

This tool aims to provide some useful tools for software developers.

##Motivation##

A fully-fledged IDE provides a lot of features that can be used to develop software. But sometimes you need a special
kind of tool. Then you search for the appropriate tool and hopefully you will find it. Some time later on a different machine
you are again searching for the same tool or the website you have used is no longer available.

This project therefore aims to provide some useful tools that I needed from time to time.

##Features##

The developers-pocket-knife currently provides these tools:
* A tool that encodes a given string in the **Base64** scheme or decodes a given Base64 string
* A tool that validates an **XML schema validation** with detailed listing of warnings/errors
* A tool that **finds files** within a folder structure with jar files and again recursively in these jar files
* A tool to **test regular expressions**

##Planned features##

The following features are planned and may be provided by future releases:
* A tool to compute digest values for files (MD5, SHA-1, etc)
* A tool to convert the epoch milliseconds into a date in a selected timezone and vice versa

##Usage##

You can simply download the provided jar and start it with (a JRE >= 1.7 is required):

    java -jar developers-pocket-knife-0.1.2-complete.jar

##Screenshots##

A screenshot showing the class finder tool:

<img src="https://raw.github.com/siom79/developers-pocket-knife/master/doc/screenshot_classfinder.png" alt="Class Finder tool"></img>

A screenshot showing the base64 tool:

<img src="https://raw.github.com/siom79/developers-pocket-knife/master/doc/screenshot_base64.png" alt="Base64 Tool"></img>

##Download##

You can download the latest version [here](https://github.com/siom79/developers-pocket-knife/releases).
