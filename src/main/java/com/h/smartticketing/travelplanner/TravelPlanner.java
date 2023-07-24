package com.h.smartticketing.travelplanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import org.opentripplanner.routing.api.request.request.filter.SelectRequest;
import org.opentripplanner.routing.api.request.request.filter.TransitFilterRequest;
import org.opentripplanner.routing.api.response.RoutingResponse;
import org.opentripplanner.transit.model.basic.MainAndSubMode;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
final class TravelPlanner {

	private static final int MaxNumItineraries = 5;
	private final boolean addIntermediateStops = true;

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
		final QualifiedModeSet modes = new QualifiedModeSet(travelPlannerRequest.mode);
		journey.setModes(modes.getRequestModes());
		request.setJourney(journey);
		request.setNumItineraries(MaxNumItineraries);
		request.setLocale(Locale.ENGLISH);
		request.setArriveBy(false);
		var filterBuilder = TransitFilterRequest.of();
		
		applyTransitModeFilter(journey, modes, filterBuilder);
		
		log.info("OTP Request={}", request);

		final RoutingResponse routeResponse = routingService.route(request);
		final TripPlanMapper tripPlanMapper = new TripPlanMapper(request.locale(), addIntermediateStops);
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

	private void applyTransitModeFilter(final JourneyRequest journey, final QualifiedModeSet modes,
			org.opentripplanner.routing.api.request.request.filter.TransitFilterRequest.Builder filterBuilder) {
		List<MainAndSubMode> tModes;
		if (modes == null) {
			tModes = MainAndSubMode.all();
		} else {
			tModes = modes.getTransitModes().stream().map(MainAndSubMode::new).toList();
		}
		var selectors = new ArrayList<SelectRequest.Builder>();

		if (!selectors.isEmpty()) {
			for (var selector : selectors) {
				filterBuilder.addSelect(selector.withTransportModes(tModes).build());
			}
		} else {
			filterBuilder.addSelect(SelectRequest.of().withTransportModes(tModes).build());
		}

		var transit = journey.transit();
		if (tModes.isEmpty()) {
			transit.disable();
		} else {
			transit.setFilters(List.of(filterBuilder.build()));
		}
	}
}
