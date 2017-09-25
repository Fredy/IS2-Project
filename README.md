# IS2-Project
To run the application you hace to set the follow file:

* IS2-Project/project/src/main/resources/application.properties


you must replace the fields in bold:

spring.datasource.url=jdbc:postgresql://localhost:5432/**DATA_BASE**

spring.datasource.username=**USERNAME**

spring.datasource.password=**PASSWORD**

## How to compile
- You need maven package installed 
- Go to "project" directory ```cd project```
- run following commands:
```
mvn compile  
mvn package  
jar cfM src/main/java/META-INF/MANIFEST.MF target/is2project-0.0.1-SNAPSHOT.jar   target/classes/is2/Is2projectApplication.class
```
or just:
```
mvn clean install
```
For execution:
- you just need to run
```
java -jar target/is2project-0.0.1-SNAPSHOT.jar <SHOP>
```
replace ```<SHOP>``` with the shop you want to scrap (```tottus```, ```ripley```, ```linio```, ```oechsle```, ```saga```)

