# TravelPlanner
 
This project contains simple API server which expose just single operation `/api/v1/routes/plan` for planning the travel for given gps coordinates using different transportation modes.
Under the hood, `OpenTripPlanner` is used.
In order to use the project, you need to provide pre-compiled graph object.  
 
Example usage

```
curl --location 'http://localhost:4343/api/v1/routes/plan?fromLat=52.552976197007524&toLat=52.47148826410652&mode=TRANSIT&fromLong=13.351135253906252&toLong=13.363494873046877'
```
 
 