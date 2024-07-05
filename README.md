This is a [Next.js](https://nextjs.org/) project bootstrapped with [`create-next-app`](https://github.com/vercel/next.js/tree/canary/packages/create-next-app).

NodeJS 20 or higher is needed to run this locally.

## Prerequisies
* java 11
* maven 18
* MYSQL
* IntelliJ

## Getting staterd
Install the packages
```bash
mvn clean install
```

## Running the project
### Pointing to prod database
The prodject points to the prod database by default so run the following commands:
```bash
mvn package && java -jar target/calculator-api.jar
```

### Point to a local database
### Setting up the database
After successfully installing MYSQL in the local machine do the following:
* create a new databse named`calculatordb`
* create a new user called `calcualtoruser` and password `ntdcalculator123`
* grant all persmissions to `calcualtoruser` to `calculatordb`
* on a new terminal start the MYSQL service running `mysqld`
* Replace the values of the lines 40 to 43 with the following values
```
  db: calculatordb
  host: 127.0.0.1
  username: calcualtoruser
  password: ntdcalculator123
```
Use IntelliJ to run the main class `CalculatorApplication`

Open [http://localhost:8080/v1/](http://localhost:8080/v1/) with your browser to see the result.

## API Documentation
Open the subpath `/specs/ui` in order to visualize the swagger documentation page
*  [http://localhost:8080/v1/specs/ui](http://localhost:8080/v1/specs/ui)
