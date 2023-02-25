package com.h.smartticketing.travelplanner;

import static spark.Spark.port;

import org.opentripplanner.routing.api.RoutingService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.h.smartticketing.travelplanner.otp.RoutingServiceFactory;

public class TravelPlannerService {
	static final String GRAPH_FILE = "src/main/resources/berlin.obj";
	
	public static void main(String[] args) throws JsonProcessingException {
		startService();
	}

	private static void startService() {
		
		final RoutingService routingService = RoutingServiceFactory.get(GRAPH_FILE);
		
		port(4343);

		final TravelPlanner travelPlanner = new TravelPlanner(routingService);
		final TravelPlannerController travelPlannerController = new TravelPlannerController(travelPlanner);
		travelPlannerController.start();
	}


}
