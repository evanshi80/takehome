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

### Notes
1. Use the attached file as a starting point.
2. In order to prepare this app for deployment to production, please update the Spring Boot version to the latest version.
3. To reduce the amount of logs, please update the app so that only logs with a warning level or above are logged to the console.
4. Finally, create a Dockerfile that would build this app into an image that is ready to be ran.
5. Create and include a README with instructions on how to build and run this app.

### Build & Deploy


### How to use



