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
		final String graphFilePath_1 = Dotenv.load().get("GRAPH_FILE_PATH");
		final String graphFilePath_2 = Dotenv.load().get("GRAPH_FILE_PATH");
		
		final RoutingService routingService_1 = RoutingServiceFactory.get(graphFilePath_1);
		final RoutingService routingService_2 = RoutingServiceFactory.get(graphFilePath_2);
		
		final TravelPlannerController travelPlannerController = new TravelPlannerController(new TravelPlanner(routingService_1),new TravelPlanner(routingService_2));
		
		port(4343);
		
		travelPlannerController.start();
	}
}
