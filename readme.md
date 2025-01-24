# Credit Module System

# Overview 

- This project, made with Spring Boot and Java .
- It is used to create a customer in the bank and to provide loan to customer.

# Authentication 

In the Basic auth section, you can become auth by entering your username and password.
- Username : customer
- Password : customer

#  Customer Services

## createCustomer

post service request  
: localhost:8090/api/customer/create

requestbody 
: {
"name" :"muhammed",
"surname" :"baglar",
"creditLimit": 6000,
"usedCreditLimit": 0,
"password":"muhammed"
}

## findCustomer

get service request
: localhost:8090/api/customer/find/1

#  Loan Services

## createLoan

post service request
: localhost:8090/api/loan/create

requestbody
:  {
"customerId":3,
"loanAmount":6000,
"numberOfInstallment":6,
"createDate":"2025-08-01",
"interestRate":"0.3"
}

## findByLoanId

get service request
: localhost:8090/api/loan/find/2

 
## payLoan

post service request
: localhost:8090/api/loan/pay/2/25000

# Loan Installment Services 


## findByLoanInstallmentId

get service request
: localhost:8090/api/loanInstallment/findByInstallment/1

## findByLoanId

get service request
: localhost:8090/api/loanInstallment/findByLoan/1

