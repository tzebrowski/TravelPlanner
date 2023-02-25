# TravelPlanner
 
This project contains simple API server which expose just single operation `api/v1/routes/plan` and allows to find routes using `OpenTripPlanner` library.
In order to use it, you need precompiled graph object.  
 
 Example usage

```
curl --location 'http://localhost:4343/api/v1/routes/plan?fromLat=52.552976197007524&toLat=52.47148826410652&time=10%253A26pm&date=02-24-2023&mode=TRANSIT&arriveBy=true&wheelchair=false&preferredRoutes=&otherThanPreferredRoutesPenalty=19568&showIntermediateStops=true&debugItineraryFilter=false&locale=en&fromLong=13.351135253906252&toLong=13.363494873046877'

 
 ```
 
 