package com.h.smartticketing.travelplanner;

import static spark.Spark.get;

import org.opentripplanner.api.resource.TripPlannerResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.h.smartticketing.travelplanner.TravelPlanner.TravelPlannerRequest;

final class TravelPlannerController {

	final ObjectMapper objectMapper;
	final TravelPlanner travelPlanner;

	TravelPlannerController(final TravelPlanner travelPlanner) {
		this.objectMapper = new ObjectMapper();
		this.travelPlanner = travelPlanner;

		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	}

	void start() {
		get("/api/v1/routes/plan", (req, response) -> {
			response.type("application/json");

			final double fromLat = Double.parseDouble(req.queryParams("fromLat"));
			final double fromLong = Double.parseDouble(req.queryParams("fromLong"));
			final double toLat = Double.parseDouble(req.queryParams("toLat"));
			final double toLong = Double.parseDouble(req.queryParams("toLong"));

			final String mode = req.queryParams("mode");

			final TravelPlannerRequest plannerRequest = TravelPlannerRequest.builder().fromLat(fromLat)
					.fromLong(fromLong).toLat(toLat).toLong(toLong).mode(mode).build();

			final TripPlannerResponse tripPlannerResponse = travelPlanner.plan(plannerRequest);
			return objectMapper.writeValueAsString(tripPlannerResponse);
		});
	}
}
