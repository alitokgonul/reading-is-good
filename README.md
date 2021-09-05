# reading-is-good
ReadingIsGood is an online books retail firm which operates only on the Internet.

# Tech Stack

![](https://img.shields.io/badge/java_11-✓-blue.svg)
![](https://img.shields.io/badge/spring_boot-✓-blue.svg)
![](https://img.shields.io/badge/h2-✓-blue.svg)
![](https://img.shields.io/badge/hibernate-✓-blue.svg)
![](https://img.shields.io/badge/jwt-✓-blue.svg)
![](https://img.shields.io/badge/swagger_2-✓-blue.svg)

# How to get token

a default user create with username: test and password : test

use the endpoint to get token : 

```ecma script level 4
{
  curl -X POST "http://localhost:8080/api/v1/customers/login?password=test&username=test" -H "accept: */*"
}
```

or you can create a new use :

```ecma script level 4
{
  curl -X POST "http://localhost:8080/api/v1/customers/register" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"email\": \"username@mail.com\", \"lastName\": \"new lastName\", \"name\": \"new name\", \"password\": \"newPass\", \"phone\": 5555555555, \"username\": \"newuser\"}"
}
```

# How to use this code?

1. Make sure you have [Java 11](https://www.java.com/download/) and [Maven](https://maven.apache.org) installed

2. Fork this repository and clone it

```
$ git clone https://github.com/<your-user>/reading-is-good.git
```
Note: contact with me to get permission since the repo is private

3. Navigate into the folder

```
$ cd reading-is-good
```

4. Install dependencies

```
$ mvn install
```

5. Run the project

```
$ mvn spring-boot:run
```

6. Navigate to `http://localhost:8080/swagger-ui.html` in your browser to check everything is working correctly. You can change the default port in the `application.yml` file

7. Navigate to `http://localhost:8080/h2-console/login.jsp` to check the db

8. Example calls for postman `reading-is-good.postman_collection.json` added to the project directory 