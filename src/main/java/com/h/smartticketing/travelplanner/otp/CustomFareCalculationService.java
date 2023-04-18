package com.h.smartticketing.travelplanner.otp;

import org.opentripplanner.model.plan.Itinerary;
import org.opentripplanner.routing.core.FareType;
import org.opentripplanner.routing.core.ItineraryFares;
import org.opentripplanner.routing.fares.FareService;
import org.opentripplanner.transit.model.basic.Money;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
final class CustomFareCalculationService implements FareService {
	private final FareService fareService;

	@Override
	public ItineraryFares getCost(Itinerary itinerary) {
		final ItineraryFares originalItineraryFares = fareService.getCost(itinerary);
		
		for (final FareType fareType: FareType.values()) {
			originalItineraryFares.addFare(fareType, Money.euros(666666));
		}
		
		final ItineraryFares copyitineraryFares = new ItineraryFares(originalItineraryFares);
		log.info("{}",itinerary);
		
		return copyitineraryFares;
	}
}