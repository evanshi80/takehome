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
   ./gradlew build
```
2. Create docker image using 'docker build -t evanshi80/takehome .'
```
   docker build -t evanshi80/takehome .
```
3. Run the docker image using 'docker run -p 8080:8080 evanshi80/takehome'
```
   docker run -d -p 8080:8080 evanshi80/takehome 
```
4. the app is running on localhost:8080

### How to use

1. Use Postman to send a POST request to http://localhost:8080/findOtherCountries with a JSON body containing a list of 
   country 
   codes
```
["US","CA","CN","Z11W"]
```
2. The response will be a JSON body containing a list of JSON Objects. Different continents will be clustered in different groups.
```
[
   { // each continent is a group
       "countryCodes": [ // this represents the requested country codes grouped by continent
           "CN"
       ],
       "continentName": "Asia", // this represents the continent name
       "otherCountryCodes": [ // this represents the other country codes found in the same continent,
                                // excluding the requested country codes
           "AE",
           "AF",
       ...]
   },
   { // this is another group of the continent "North America"
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


#### Test against the rate limits
1. Use PostMan Collection's Run feature to run the test cases,create a collection 'Takehome API test'
2. Drag the above Http Request test into this collection
3. Set the runner to run the collection 10 times without delay
4. Run the collection
5. '429 Too Many Requests responses' will be returned on those requests that exceed the rate limit 5
6. Fetch the token from the Auth0 Aurthorization Server
```
curl --request POST \
  --url https://dev-ousg77uz1ajb0gz0.us.auth0.com/oauth/token \
  --header 'content-type: application/json' \
  --data '{"client_id":"AL54SE9RglWZdLUJ9hNNBqU3UJLUQ1NQ","client_secret":"LQNaSty9bSBMV0LMwsxtZcoIyNO2rmmbLWV_BLvT47uWKpJ-GevrgQgec-ZAs4NW","audience":"https://takehome.yfs/api/","grant_type":"client_credentials"}'

```
7. Set the Authorization property in HTTP Request to use OAuth 2 and select the option 'add auth data to Request Headers'
8. In the Access Token field, paste the token from the response of the step 6
9. Make sure the header prefix is set to Bearer
10. Run the collection again
11. You will see the requests are all successful with 200 OK responses
