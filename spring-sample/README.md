#Spring Boot Application with Hazelcast Cloud Cluster

<h3>Spring Boot Sample Application</h3>
<p>
This project contains a minimal configuration to launch Spring Boot application and establish connection to
Hazelcast Cloud Cluster.

<h3>Configuration for Hazelcast Cloud Cluster</h3>
<p>
Once Hazelcast cluster is created in Cloud then <code>Cluster Name</code>, <code>Discovery Token</code>, 
<code>Keystore</code> and <code>Keystore Passwords</code> can be found in 
<code>Connect Your Application</code>  - <code>Advanced Setup</code>
<p>
- Zip archive with Keystore should be downloaded and extracted to <code>spring-sample/src/main/resources</code><br>
- <code>Cluster Name</code> should be added in <code>spring-sample/src/main/resources/application.properties</code> 
for property <code>hazelcast.cloud.clusterName</code><br>
- <code>Discovery Token</code> should be added in <code>spring-sample/src/main/resources/application.properties</code> 
for property <code>hazelcast.cloud.discoveryToken</code><br>
- <code>Keystore Passwords</code> should be added in <code>spring-sample/src/main/resources/application.properties</code> 
for properties <code>hazelcast.cloud.keyStorePassword</code> and <code>hazelcast.cloud.trustStorePassword</code> <br>

<h3>Launch Spring boot application</h3>
Execute next command from <code>spring-sample</code> folder to launch application
```shell
./mvnw spring-boot:run
```


<h3>How to create everything from scratch</h3>

<p>
Generate sample project at <a href="https://start.spring.io/">https://start.spring.io/</a> with required for you dependencies. 
Download and open in your favourite IDE.
<p>
Add dependency for Hazelcast Client to the project. For Maven project add next snippet to <code>pom.xml</code> file 

```xml
<dependency>
    <groupId>com.hazelcast</groupId>
    <artifactId>hazelcast-enterprise</artifactId>
    <version>5.1.2</version>
</dependency>
```

<p>
Add managed @Bean of type <code>com.hazelcast.client.config.ClientConfig</code> as this is done in <code>HzCloudDemoApplication.java</code>.
<p>
Add configuration to the <code>spring-sample/src/main/resources/application.properties</code> 
as described above at Configuration for Hazelcast Cloud Cluster.
<p>
Once all these are completed, instance of <code>com.hazelcast.core.HazelcastInstance</code> can be Injected into application classes 
(as it done in <code>SomeService</code> class). This will be fully initialized client with established connection to
Hazelcast Cluster in Cloud.
