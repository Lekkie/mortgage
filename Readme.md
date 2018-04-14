Mortgage
========
Mortage app is a BTL mortgage application

Requirements
===========
-- Jdk 8
-- Maven



How to run
==========
```shell
mvn clean install && java -jar target/mortgage-1.0-SNAPSHOT.jar
```

How to build
============
```shell
mvn clean install
```

How to run unit tests
=====================
```shell
mvn clean test
```

How to run Integration tests
============================
```shell
mvn clean verify -P IntegrationTests
```

How to run End to End tests
============================
```shell
 mvn clean verify -P End2EndTests
```


Assumptions
===========
* Lenders have already been pre-registered on the platform 
* Each Loan has same interest rate for different lenders i.e. no multiple interest on same loan from different lenders
Background checks has been made on the borrower  
* A better strategy should be used in selecting loans for borrower. For test purpose, the application did not 
implement a loan selection strategy for lenders, it expects a loan id when creating an investment and linking to 
a loan.
 * Interest rate for different lenders is also out of the scope of this test application. A single flat value 
(stored in properties file) is used for all lenders.
* ".....Assuming all investments are valid for 1 month......"  I interpreted this statement to mean, only calculate investment interest for 1 month at a time since interest are paid into a separate account monthly when lenders do not re-invest their interest.  
Since a part of the investInterest is or capital is paid monthly, interest for last month is calculated using unpaid capital.
* "....Lender investment into a loan (create)....."  It will be nice to be able to spread investment across multiple loans. But the scope of this test will be limited to putting an investment into a suitable loan  
Note, in order to be able to get owed interests, for monthly interests your investment must be more than 1 month old while for daily interests your investment must be more than 1day old.
* The selection of loan to invest in is not implemented. It is assumed some selection model is created in choosing the best loan for each investment. To keep this test simple, it has been left out.
* Interest calculation service does not factor in paid capital (it calculates interest on invested capital) when calculating interests. Ideally, lender's interest should be calculated on unpaid lender's capital.



Loan Management
===============

Create (Apply for) a Loan
-----------------------
Request:  
```
CURL -0 -v -X POST -H "Content-Type:application/json" http://localhost:8080/api/v1/loans  \
-d @- << EOF
{  
  "borrowerTypeCode":"IND",
  "borrowerFirstname": "James",  
  "borrowerLastname": "Watson",  
  "borrowerSalary": 90000,  
  "borrowerNationality": "Nigerian",  
  "borrowerAge": 33,  
  "borrowerFirstTimeLandlord": false,  
  "borrowerFirstTimeBuyer": false,  
  "borrowerActiveBankAccount": true,  
  "borrowerUkCreditHistory": true,  
  "goodCreditInLastYear": true,  
  "haveMinimumDeposit":true, 
  "propertyTypeCode": "STA",  
  "propertyTypeName": "House - Semi",  
  "propertyBeds": 4,  
  "propertyValue": 250000,  
  "propertyMonthlyRent": 1200,
  "propertyPostcode": "MK40",  
  "propertyCounty": "Bedford",  
  "satisfyAssessmentThreshold":true,
  "propertyCloseByCommercial":false,
  "productCode": "EXPAT_FRY5_70",  
  "loanPurpose": "Purchase",  
  "loanTenureInMonths": 300
}
EOF
```
    
Response:
```
HTTP 201
{  
   "loanAmount":175000.0,
   "borrowerRate":4.99,
   "ltv":70.0,
   "rentalCoverage":140.0,
   "monthlyRepayment":1027.11
}
```


Get Loan with its Investments
-----------------------------
Request: 
``` 
CURL -X GET http://localhost:8080/api/v1/loans/1
```

Response: 
```
HTTP 200
{  
   "borrowerId":1,
   "postcode":"MK40",
   "county":"Bedford",
   "beds":4,
   "propertyValue":250000.0,
   "monthlyRent":1200.0,
   "product":"Expat 5 Year Fixed, 70 LTV",
   "purpose":"Purchase",
   "loanAmount":175000.0,
   "borrowerRate":4.99,
   "lenderRate":3.49,
   "ltv":70.0,
   "tenureInMonths":300,
   "completionDate":"2043-04-14 00:00:00",
   "rentalCoverage":140.0,
   "productType":"5 Fixed Rate Years",
   "propertyType":"Standard",
   "borrowerType":"Individual",
   "monthlyRepayment":1027.11,
   "investments":[  
      {  
         "loanInvestmentId":1,
         "loanId":1,
         "lenderId":1,
         "amount":10000.00,
         "termsInMonths":24
      }
   ]
}
```


Delete a Loan
-------------
Request:  
```
CURL -X DELETE -H "Content-Type:application/json" http://localhost:8080/api/v1/loans/1
```

Response:  
```
HTTP 200
```

Lender investment into a Loan
-----------------------------
Request:  
```
CURL -0 -v -X POST -H "Content-Type:application/json" http://localhost:8080/api/v1/investments  \
-d @- << EOF
{  
    "lenderId": 1,
    "loanId": 1,
    "amount": 10000,
    "termInMonths": 1
}
EOF
```

Response: 
 ```
HTTP 200
{  
   "loanId":1,
   "lenderId":1,
   "amount":10000,
   "termsInMonths":24
}
```


Get Lender Monthly Interest
---------------------------
Request:  
```
CURL -X GET http://localhost:8080/api/v1/lenders/1/interests
```

Response: 
 ```
HTTP 200
{  
   "amount":698.0
}
```


Get Lender Interest within a range
----------------------------------
Request:  
```
CURL -X GET "http://localhost:8080/api/v1/lenders/1/interests?startDate=2018-02-01&endDate=2018-04-30"
```

-- add interests rate
Response: 
 ```
HTTP 200
{  
   "amount":168.28
}
```
