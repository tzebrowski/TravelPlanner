package com.h.smartticketing.travelplanner;

import org.opentripplanner.api.mapping.PlannerErrorMapper;
import org.opentripplanner.api.mapping.TripPlanMapper;
import org.opentripplanner.api.mapping.TripSearchMetadataMapper;
import org.opentripplanner.api.model.ApiTripPlan;
import org.opentripplanner.api.parameter.QualifiedModeSet;
import org.opentripplanner.api.resource.TripPlannerResponse;
import org.opentripplanner.model.GenericLocation;
import org.opentripplanner.routing.api.RoutingService;
import org.opentripplanner.routing.api.request.RouteRequest;
import org.opentripplanner.routing.api.request.request.JourneyRequest;
import org.opentripplanner.routing.api.response.RoutingResponse;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
final class TravelPlanner {

	private static final int MaxNumItineraries = 5;

	@Builder
	@ToString
	static class TravelPlannerRequest {
		private final double fromLat;
		private final double fromLong;
		private final double toLat;
		private final double toLong;
		private final String mode;
	}

	final RoutingService routingService;

	TripPlannerResponse plan(TravelPlannerRequest travelPlannerRequest) {

		log.info("Receives request={} ", travelPlannerRequest);

		long time = System.currentTimeMillis();

		final RouteRequest request = new RouteRequest();

		request.setFrom(new GenericLocation(travelPlannerRequest.fromLat, travelPlannerRequest.fromLong));
		request.setTo(new GenericLocation(travelPlannerRequest.toLat, travelPlannerRequest.toLong));

		final JourneyRequest journey = new JourneyRequest();
		final QualifiedModeSet qualifiedModeSet = new QualifiedModeSet(travelPlannerRequest.mode);
		journey.setModes(qualifiedModeSet.getRequestModes());
		request.setJourney(journey);
		request.setNumItineraries(MaxNumItineraries);
		
		
		final RoutingResponse routeResponse = routingService.route(request);
		final TripPlanMapper tripPlanMapper = new TripPlanMapper(request.locale(), true);
		final ApiTripPlan mapTripPlan = tripPlanMapper.mapTripPlan(routeResponse.getTripPlan());

		final TripPlannerResponse tripPlannerResponse = new TripPlannerResponse(null);
		tripPlannerResponse.setPlan(mapTripPlan);

		tripPlannerResponse.setMetadata(TripSearchMetadataMapper.mapTripSearchMetadata(routeResponse.getMetadata()));
		if (!routeResponse.getRoutingErrors().isEmpty()) {
			tripPlannerResponse.setError(PlannerErrorMapper.mapMessage(routeResponse.getRoutingErrors().get(0)));
		}

		time = System.currentTimeMillis() - time;
		log.info("Request processing time: {}", time);
		return tripPlannerResponse;
	}
}
