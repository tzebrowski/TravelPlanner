package com.h.smartticketing.travelplanner;

import static spark.Spark.get;

import org.opentripplanner.api.mapping.TripPlanMapper;
import org.opentripplanner.api.model.ApiTripPlan;
import org.opentripplanner.api.parameter.QualifiedModeSet;
import org.opentripplanner.api.resource.TripPlannerResponse;
import org.opentripplanner.model.GenericLocation;
import org.opentripplanner.routing.api.RoutingService;
import org.opentripplanner.routing.api.request.RouteRequest;
import org.opentripplanner.routing.api.request.request.JourneyRequest;
import org.opentripplanner.routing.api.response.RoutingResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public final class TravelPlannerController {

	void registerAPI(RoutingService routingService) {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		routesPlan(routingService, objectMapper);
	}

	private void routesPlan(RoutingService routingService, ObjectMapper objectMapper) {
		get("/api/v1/routes/plan", (req, response) -> {

			final double fromLat = Double.parseDouble(req.queryParams("fromLat"));
			final double fromLong = Double.parseDouble(req.queryParams("fromLong"));
			final double toLat = Double.parseDouble(req.queryParams("toLat"));
			final double toLong = Double.parseDouble(req.queryParams("toLong"));

			QualifiedModeSet mode = new QualifiedModeSet(req.queryParams("mode"));

			log.info("Receives request,mode={}, fromLat={}, fromLong={}, toLat={}, toLong={} ", mode, fromLat, fromLong,
					toLat, toLong);

			response.type("application/json");

			long time = System.currentTimeMillis();

			final RouteRequest request = new RouteRequest();

			request.setFrom(new GenericLocation(fromLat, fromLong));
			request.setTo(new GenericLocation(toLat, toLong));

			final JourneyRequest journey = new JourneyRequest();
			journey.setModes(mode.getRequestModes());
			request.setJourney(journey);

			final RoutingResponse route = routingService.route(request);
			final TripPlanMapper tripPlanMapper = new TripPlanMapper(request.locale(), true);
			final ApiTripPlan mapTripPlan = tripPlanMapper.mapTripPlan(route.getTripPlan());

			final TripPlannerResponse tripPlannerResponse = new TripPlannerResponse(null);
			tripPlannerResponse.setPlan(mapTripPlan);
			time = System.currentTimeMillis() - time;
			log.info("Request processing time: {}", time);
			return objectMapper.writeValueAsString(tripPlannerResponse);

		});
	}
}
