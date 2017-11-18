# IS2-Project

[![Build Status](https://travis-ci.org/Fredy/IS2-Project.svg?branch=develop)](https://travis-ci.org/Fredy/IS2-Project/branches#)
[![Coverage Status](https://coveralls.io/repos/github/Fredy/IS2-Project/badge.svg?branch=develop)](https://coveralls.io/github/Fredy/IS2-Project?branch=develop)

# How to use

To run the application you have to set the follow file:

`IS2-Project/project/cliApplication/src/main/resources/application.properties`  

you must replace the fields in bold:

spring.datasource.url=jdbc:postgresql://localhost:5432/**DATA_BASE**

spring.datasource.username=**USERNAME**

spring.datasource.password=**PASSWORD**

## How to compile
- You need maven package installed 
- Go to "project" directory   
`cd project`
- Run following command:  
`
mvn clean install
`  
- For execution:  
  You just need to run  
`
java -jar cliApplication/target/cliApplication-0.0.1-SNAPSHOT.jar <SHOP>  
`  
Replace `<SHOP>` with the shop you want to scrap (`tottus`, `ripley`, `linio`, `oechsle`, `saga`)

- Then you will be asked for a category, you will see the available categories, you should write the number of the category you want, then you must choose in the same way a sub-category and then a sub-sub-category, after that products of the chosen sub-sub-category will be saved in the data base.
