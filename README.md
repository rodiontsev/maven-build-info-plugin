Maven Build Info Plugin
=======================

This plugin generates the *build.info* file, which contains 
- build date
- build version
- source revision (for Mercurial only)
- different system properties: java.vm.vendor, java.vm.version, os.name, os.version, etc.

This file might be packed into the *war* or the *ear* file, so you can see if you deal with the new or an outdated version deploed on the server.


Usage
-----
To use this plugin you should add it in your pom.xml


<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

    ...

    <build>
        <finalName>war</finalName>
        <plugins>
	    <!-- Pack generated build.info into the war file -->
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

	    <!-- Generate build.info -->
            <plugin>
                <groupId>*com.rodiontsev.tools.maven.plugins*</groupId>
                <artifactId>*build-info*</artifactId>
                <version>1.1</version>
                <configuration>
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
        </plugins>
    </build>

    ...

</project>


