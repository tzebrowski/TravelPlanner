package com.h.smartticketing.travelplanner;

import static spark.Spark.port;

import org.opentripplanner.routing.api.RoutingService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.h.smartticketing.travelplanner.otp.RoutingServiceFactory;

import io.github.cdimascio.dotenv.Dotenv;

public class TravelPlannerService {
	
	
	public static void main(String[] args) throws JsonProcessingException {
		startService();
	}

	private static void startService() {
		final String graphFilePath = Dotenv.load().get("GRAPH_FILE_PATH");
		
		final RoutingService routingService = RoutingServiceFactory.get(graphFilePath);
		
		port(4343);

		final TravelPlanner travelPlanner = new TravelPlanner(routingService);
		final TravelPlannerController travelPlannerController = new TravelPlannerController(travelPlanner);
		travelPlannerController.start();
	}
}
