**Basic requirement**

Java 8 or higher

Spring boot 2.1.1.RELEASE
Apache Maven 4.0.0
Postman or any other REST API Testing tool
(Can use swagger documentation as well)

**To run**

clone https://github.com/snbiju/offer-service.git / download

go to offer-service-master

mvn spring-boot:run

**Swagger Documentation**
url: http://localhost:8080/swagger-ui.html

**Create new offer**
(POST REQUEST)
http://localhost:8080/offers(POST REQUEST)

Request body

  {
    
    "description": "Product description",
  	"price": 100.03,    
  	"currency": "USD",
  	"expiryTime": "2019-01-01T00:00:00Z",
  	"expired": false
   
   }
   
 **Update Offer**
 
 (PUT REQUEST)
 
 Update
 
  http://localhost:8080/offers
  eg:-http://localhost:8080/offers
  
  {
  
    "id":"11dcb515-17ef-4205-8bac-78f9da7b07f2",
 	"description": "Product description",  
 	"price": 205.03, 
 	"currency": "USD", 
 	"expiryTime": "2019-01-01T00:00:00Z",
 	"expired": false
 }
 
   
**Match Offer By id**

(GET REQUEST)

getById({id})

http://localhost:8080/offers/{id}

eg:- http://localhost:8080/offers/fbbca6ea-4191-4556-99d7-9bf704629365
   
 
       
**Cancel Offer**

(DELETE REQUEST)

http://localhost:8080/offers/{id}
eg:-http://localhost:8080/offers/e64d0e0a-7dd9-4f9f-a2d1-cdaae6b64800
 
    
**Get Offer by Currency**

(GET REQUEST)

 http://localhost:8080/offers?currency={currency}
 
 eg:-http://localhost:8080/offers?currency=USD
 
"# offer-service" 
