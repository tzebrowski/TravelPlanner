package com.h.smartticketing.travelplanner;

import static spark.Spark.port;

import java.io.File;

import org.opentripplanner.routing.api.RoutingService;
import org.opentripplanner.routing.graph.SerializedGraphObject;
import org.opentripplanner.routing.service.DefaultRoutingService;
import org.opentripplanner.standalone.api.OtpServerRequestContext;

import com.fasterxml.jackson.core.JsonProcessingException;

public class TravelPlannerService {

	public static void main(String[] args) throws JsonProcessingException {
		startService();
	}

	private static void startService() {
		final String graphContext = "src/main/resources/berlin.obj";
		final SerializedGraphObject serializedGraphObject = SerializedGraphObject.load(new File(graphContext));
		final OtpServerRequestContext serverRequestContext = DefaultOtpServerRequestContext.createServerContext(serializedGraphObject);
		final RoutingService routingService = new DefaultRoutingService(serverRequestContext);
		
		port(4343);

		final TravelPlanner travelPlanner = new TravelPlanner(routingService);
		final TravelPlannerController travelPlannerController = new TravelPlannerController(travelPlanner);
		travelPlannerController.start();
	}
}
