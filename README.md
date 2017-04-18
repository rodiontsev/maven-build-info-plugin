Build Info Maven Plugin
=======================

This plugin generates the build-info file which might contain:
- build date
- project properties (project.artifactId, project.version, project.name, etc.)
- system properties (user.name, java.vm.vendor, java.vm.version, java.vm.name, os.name, os.version, os.arch, etc.)
- properties defined in a pom.xml
- environment variables (SHELL, HOME, PATH, LANG, etc)
- VCS info (Git, Mercurial or Subversion)

If you include this file in a WAR or EAR file, you will not waste time trying to figure out the application's version which is deployed to a server.

Usage
-----
To use this plugin you should add it in your pom.xml

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

    ...

    <build>
        <plugins>

            <!-- Generate build.info -->
            <plugin>
                <groupId>com.rodiontsev.maven.plugins</groupId>
                <artifactId>build-info-maven-plugin</artifactId>
                <version>1.2</version>
                <configuration>
                    <filename>build.info</filename>
                    <projectProperties>
                        <projectProperty>project[.parent].id</projectProperty>
                        <projectProperty>project[.parent].groupId</projectProperty>
                        <projectProperty>project[.parent].artifactId</projectProperty>
                        <projectProperty>project[.parent].version</projectProperty>
                        <projectProperty>project[.parent].name</projectProperty>
                        <projectProperty>project[.parent].description</projectProperty>
                        <projectProperty>project[.parent].modelVersion</projectProperty>
                        <projectProperty>project[.parent].inceptionYear</projectProperty>
                        <projectProperty>project[.parent].packaging</projectProperty>
                        <projectProperty>project[.parent].url</projectProperty>
                    </projectProperties>
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
                    <environmentVariables>
                        <environmentVariable>JAVA_HOME</environmentVariable>
                    </environmentVariables>
                    <dateTimePattern>yyyy-MM-dd HH:mm:ss</dateTimePattern>
                    <includeVcsInfo>true</includeVcsInfo>
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
```

The Build Info Maven Plugin can be downloaded from the [Maven Central](http://repo1.maven.org/maven2/com/rodiontsev/maven/plugins/build-info-maven-plugin/1.2/) repository.
