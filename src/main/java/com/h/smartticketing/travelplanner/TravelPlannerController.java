package com.h.smartticketing.travelplanner;

import static spark.Spark.get;

import org.opentripplanner.api.resource.TripPlannerResponse;

import com.h.smartticketing.travelplanner.TravelPlanner.TravelPlannerRequest;

final class TravelPlannerController {
	
	final TravelPlanner travelPlanner;
	final JsonSerializer jsonSerializer = new JsonSerializer();

	TravelPlannerController(final TravelPlanner travelPlanner) {
		this.travelPlanner = travelPlanner;
	}

	void start() {
		get(Constants.API_V1_ROUTES_PLAN, (req, response) -> {
			response.type("application/json");

			final double fromLat = Double.parseDouble(req.queryParams("fromLat"));
			final double fromLong = Double.parseDouble(req.queryParams("fromLong"));
			final double toLat = Double.parseDouble(req.queryParams("toLat"));
			final double toLong = Double.parseDouble(req.queryParams("toLong"));
			final String mode = req.queryParams("mode");

			final TravelPlannerRequest plannerRequest = TravelPlannerRequest
					.builder()
					.fromLat(fromLat)
					.fromLong(fromLong)
					.toLat(toLat)
					.toLong(toLong)
					.mode(mode).build();

			final TripPlannerResponse tripPlannerResponse = travelPlanner.plan(plannerRequest);
			return jsonSerializer.serialize(tripPlannerResponse);
		});
	}
}
