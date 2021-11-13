# MAVEN

For developers that want to add NoNullProcessors to their mod's workspace:

<blockquote>
repositories {

&nbsp;&nbsp;&nbsp;maven {

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;url "https://nexus.resourcefulbees.com/repository/maven-public/"

&nbsp;&nbsp;&nbsp;}

}
</blockquote>

&nbsp;

Don't forget to change <modversion> with the actual latest version of this mod like `1.0.0` for example.

<blockquote>
dependencies {

...

&nbsp;&nbsp;&nbsp;&nbsp;modImplementation "com.telepathicgrunt:NoNullProcessors-Fabric:<modversion>+1.17.1"

}</blockquote>

**____________________________________________________________________________**

Have you been trying to pre-generate large areas of your world with pregenerator mods and keep randomly crashing with this error?

```
Caused by: java.lang.NullPointerException: Cannot invoke "net.minecraft.structure.processor.StructureProcessorList.getList()" because the return value of "java.util.function.Supplier.get()" is null
	at net.minecraft.structure.pool.SinglePoolElement.createPlacementData(SinglePoolElement:148) ~[?:?]
	at net.minecraft.structure.pool.SinglePoolElement.generate(SinglePoolElement:124) ~[?:?]
```

```
Caused by: java.lang.NullPointerException: Cannot invoke "net.minecraft.class_5497.method_31027()" because the return value of "java.util.function.Supplier.get()" is null
	at net.minecraft.structure.pool.SinglePoolElement.createPlacementData(SinglePoolElement:148) ~[?:?]
	at net.minecraft.structure.pool.SinglePoolElement.generate(SinglePoolElement:124) ~[?:?]
```

If so, congrats! You found a really wacky bug in 1.17.1 Minecraft! This mod aims to fix that.

The bug is tricky to explain but basically to trigger it, you need a mod or datapack that has processor lists created by worldgen json files, use a chunk pregenerator mod or something to load a massive amount of chunks, and then load all those chunks that contains structures that uses json processor lists. Randomly, that may crash due to a null processor list.

So what's going on? Well, Minecraft has a registry of processor lists inside that should be created at world startup. Then when the game needs it, it grabs it from the registry but it shouldn't be rewriting that registry at runtime. Cause that would be bad right? Well...

When a structure layout is created during worldgen, the game saves that layout and what pieces were picked into the chunk itself. Then later, as the chunk is being made, it loads that piece from the chunk to spawn in the world. Except, upon reading that structure piece from the chunk, it also needs to read the processor list attached to the piece to know what processors should run for that piece. And it's here that the bug is done. The code to read the processor list is incorrect and will take the processor it reads and register it again to the registry, replacing itself with itself, and then use it.

And this registry replacing with itself is not threadsafe. And since minecraft creates chunks in a multithreaded way... there exists a point where two structure pieces using the same processor could try reading/writing to that same exact entry in the registry. And when this rare collision occurs, one of the structure pieces may end up with a null processor which then crashes the game because the game cannot handle spawning a null processor because it doesn't exist!

Wacky right? Super weird and painful to debug but thanks to another modder, we were able to figure this out and create this fix. As far as we know, 1.16.5 does not have this issue and it is unknown if 1.18 has the bug.

Hope this helps!