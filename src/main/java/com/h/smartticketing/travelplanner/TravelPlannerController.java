package com.h.smartticketing.travelplanner;

import static spark.Spark.get;

import org.opentripplanner.api.parameter.QualifiedModeSet;
import org.opentripplanner.api.resource.TripPlannerResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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

			final QualifiedModeSet mode = new QualifiedModeSet(req.queryParams("mode"));

			final TripPlannerResponse tripPlannerResponse = travelPlanner.plan(fromLat, fromLong, toLat, toLong, mode);
			return objectMapper.writeValueAsString(tripPlannerResponse);

		});
	}
}
