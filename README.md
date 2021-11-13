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
