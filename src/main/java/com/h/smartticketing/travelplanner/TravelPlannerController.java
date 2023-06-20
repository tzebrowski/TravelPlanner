package com.h.smartticketing.travelplanner;

import static spark.Spark.get;

import org.opentripplanner.api.resource.TripPlannerResponse;

import com.h.smartticketing.travelplanner.TravelPlanner.TravelPlannerRequest;

import lombok.extern.slf4j.Slf4j;
import spark.Request;

@Slf4j
final class TravelPlannerController {
	
	private final TravelPlanner travelPlanner_1;
	private final TravelPlanner travelPlanner_2;
	
	private final JsonSerializer jsonSerializer = new JsonSerializer();

	TravelPlannerController(final TravelPlanner travelPlanner_1,TravelPlanner travelPlanner_2) {
		this.travelPlanner_1 = travelPlanner_1;
		this.travelPlanner_2 = travelPlanner_2;
	}

	void start() {
		get(Constants.API_V1_ROUTES_1_PLAN, (req, response) -> {
			response.type("application/json");

			final TravelPlannerRequest plannerRequest = toTravelPlannerRequest(req);
			
			log.info("{}",plannerRequest);
			
			final TripPlannerResponse tripPlannerResponse = travelPlanner_1.plan(plannerRequest);
			return jsonSerializer.serialize(tripPlannerResponse);
		});
		
		
		get(Constants.API_V1_ROUTES_2_PLAN, (req, response) -> {
			response.type("application/json");

			final TravelPlannerRequest plannerRequest = toTravelPlannerRequest(req);
			
			log.info("{}",plannerRequest);
			
			final TripPlannerResponse tripPlannerResponse = travelPlanner_2.plan(plannerRequest);
			return jsonSerializer.serialize(tripPlannerResponse);
		});
		
	}

	private TravelPlannerRequest toTravelPlannerRequest(Request req) {
		final double fromLat = Double.parseDouble(req.queryParams("fromLat"));
		final double fromLong = Double.parseDouble(req.queryParams("fromLong"));
		final double toLat = Double.parseDouble(req.queryParams("toLat"));
		final double toLong = Double.parseDouble(req.queryParams("toLong"));
		final String mode = req.queryParams("mode");

		return TravelPlannerRequest
				.builder()
				.fromLat(fromLat)
				.fromLong(fromLong)
				.toLat(toLat)
				.toLong(toLong)
				.mode(mode).build();
	}
}
