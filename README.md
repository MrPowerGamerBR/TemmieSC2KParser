# TemmieSC2KParser
<p align="center"><img src ="https://lparchive.org/Undertale/Update%2017/41-1332.png"></img></p>

## A SimCity 2000 save file parser in Kotlin
Because why not? :)

### Loading a save file and reading the city's name
(In this example I used "RETREAT.SC2" from SimCity 2000's City folder)
**Code:**
```java
SC2KCity city = TemmieSC2KParser.loadCity(new File("path to sc2 file"));
System.out.println(city.getCityName());
```
**Output:**
```
Kathy's Retreat
```

You can also use Kotlin
**Code:**
```java
var city = TemmieSC2KParser.loadCity(File("path to sc2 file"));
println(city.cityName);
```
**Output:**
```
Kathy's Retreat
```

___

Simple as that, have fun!

### Thanks to...
* David Moews, for creating the only (and the most used!) SimCity 2000 save file documentation: http://djm.cc/simcity-2000-info.txt
* [rakama](https://github.com/rakama), for creating [Minecraft-SC2MC](https://github.com/rakama/Minecraft-SC2MC) which helped me a lot to understand how decompressing SC2K data worked.
* [dwfennell](https://github.com/dwfennell), for creating [city-parser-2000](https://github.com/dwfennell/city-parser-2000), a SC2K map parser in C#

### Maven
You can use TemmieSC2KParser with Maven by using Jitpack. (sorry, I don't have a maven repo yet :cry:)
```
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
    </repository>
</repositories>
```
```
<dependency>
    <groupId>com.github.MrPowerGamerBR</groupId>
	<artifactId>TemmieSC2KParser</artifactId>
	<version>-SNAPSHOT</version>
</dependency>
```
### Dependencies
Guava

### Why Temmie?
Why not Temmie?
