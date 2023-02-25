package com.h.smartticketing.travelplanner;

import org.opentripplanner.api.mapping.TripPlanMapper;
import org.opentripplanner.api.model.ApiTripPlan;
import org.opentripplanner.api.parameter.QualifiedModeSet;
import org.opentripplanner.api.resource.TripPlannerResponse;
import org.opentripplanner.model.GenericLocation;
import org.opentripplanner.routing.api.RoutingService;
import org.opentripplanner.routing.api.request.RouteRequest;
import org.opentripplanner.routing.api.request.request.JourneyRequest;
import org.opentripplanner.routing.api.response.RoutingResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
final class TravelPlanner {
	final RoutingService routingService;

	TripPlannerResponse plan(final double fromLat, final double fromLong, final double toLat,
			final double toLong, final QualifiedModeSet mode) {
		
		log.info("Receives request,mode={}, fromLat={}, fromLong={}, toLat={}, toLong={} ", mode, fromLat, fromLong,
				toLat, toLong);

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
		return tripPlannerResponse;
	}
}
