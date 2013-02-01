Maven Build Info Plugin
=======================

This plugin generates the *build.info* file which might contain
- build date
- build version
- source revision (support Git, Mercurial or Subversion)
- system properties (java.vm.vendor, java.vm.version, os.name, os.version, etc.)

If you include this file in the WAR or EAR file, you will not waste your time trying to figure out a version that has been deployed to a server.


Usage
-----
To use this plugin you should add it in your pom.xml


<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

    ...

    <build>
        <plugins>

            <!-- Generate build.info -->
            <plugin>
                <groupId>com.rodiontsev.maven.plugins</groupId>
                <artifactId>maven-build-info-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
                <configuration>
                    <filename>*build.info*</filename>
                    <systemProperties>
                        <systemProperty>user.name</systemProperty>
                        <systemProperty>user.timezone</systemProperty>
                        <systemProperty>java.vm.vendor</systemProperty>
                        <systemProperty>java.vm.version</systemProperty>
                        <systemProperty>java.vm.name</systemProperty>
                        <systemProperty>java.runtime.version</systemProperty>
                        <systemProperty>os.name</systemProperty>
                        <systemProperty>os.version</systemProperty>
                        <systemProperty>os.arch</systemProperty>
                    </systemProperties>
                </configuration>
                <executions>
                    <execution>
                        <phase>prepare-package</phase>
                        <goals>
                            <goal>extract</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <!-- Include build.info in your WAR file -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>2.1.1</version>
                <configuration>
                    <webResources>
                        <resource>
                            <directory>${project.build.directory}</directory>
                            <includes>
                                <include>**/build.info</include>
                            </includes>
                        </resource>
                    </webResources>
                </configuration>
            </plugin>
        </plugins>
    </build>

    ...

</project>


