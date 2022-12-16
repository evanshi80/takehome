### About This Project
Design and implement a production-ready REST API that accepts a list of country codes and returns a list of country codes that are in the same continent as the country code input.

Use the endpoint at https://countries.trevorblades.com/graphql to get the up-to-date data required for you to implement your API.

[Bonus Task] In order to not overwhelm the server, add a rate limit of 5 requests per sec for unauthenticated users and 20 requests per second for authenticated users. You may use Docker to include and integrate any containers that you might need for this.

List any assumptions that you make.

e.g. for input with "CA" and "US",

the expected output is:
```
{
  continent: [{
    countries: ["CA", "US"],
    name: "North America",
    otherCountries: ["AG", "AI", "AW", "BB", ...] // should not have CA or US here
  }]
}
```

### Build & Deploy
1. Build the project using 'gradle build'
```
   gradle build
```
2. Create docker image using 'docker build -t evanshi80/takehome .'
```
   docker build -t evanshi80/takehome .
```
3. Run the docker image using 'docker run -p 8080:8080 evanshi80/takehome'
```
   docker run -p 8080:8080 evanshi80/takehome 
```
4. the app is running on localhost:8080

### How to use
1. Use Postman to send a POST request to http://localhost:8080/find_other_countries with a JSON body containing a list of country codes
```
["US","CA","CN","Z11W"]
```
2. The response will be a JSON body containing a list of country codes that are in the same continent as the country code input. Different continents will be clustered in different groups.
```
[
   {
       "countryCodes": [
           "CN"
       ],
       "continentName": "Asia",
       "otherCountryCodes": [
           "AE",
           "AF",
       ...]
   },
   {
       "countryCodes": [
           "US",
           "CA"
       ],
       "continentName": "North America",
       "otherCountryCodes": [
           "AG",
           "AI",
           "AW",
        ...]
   }
]
 ```
3. If one of the country codes is invalid, that code will be ignored.
4. If all the country codes are invalid, the response will be a JSON body containing an empty list
5. If the request is sent more than 5 times in 1 second, a 429 Too Many Requests response will be returned
6. To mock an authenticated user, add request header key "Authorization" and any non-empty value before sending the request
7. Then you can send up to 20 requests in 1 second


