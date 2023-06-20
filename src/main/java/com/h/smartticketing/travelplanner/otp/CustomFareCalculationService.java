package com.h.smartticketing.travelplanner.otp;

import org.opentripplanner.model.plan.Itinerary;
import org.opentripplanner.routing.core.ItineraryFares;
import org.opentripplanner.routing.fares.FareService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
final class CustomFareCalculationService implements FareService {
	private final FareService fareService;

	@Override
	public ItineraryFares getCost(Itinerary itinerary) {
		final ItineraryFares originalItineraryFares = fareService.getCost(itinerary);
		
		final ItineraryFares copyitineraryFares = new ItineraryFares(originalItineraryFares);
		log.info("{}",itinerary);
		
		return copyitineraryFares;
	}
}