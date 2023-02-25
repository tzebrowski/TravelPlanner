package com.h.smartticketing.travelplanner.otp;

import java.io.File;

import org.opentripplanner.routing.api.RoutingService;
import org.opentripplanner.routing.graph.SerializedGraphObject;
import org.opentripplanner.routing.service.DefaultRoutingService;
import org.opentripplanner.standalone.api.OtpServerRequestContext;

public final class RoutingServiceFactory {

	public static RoutingService get(final String graphFile) {
		final SerializedGraphObject serializedGraphObject = SerializedGraphObject.load(new File(graphFile));
		final OtpServerRequestContext serverRequestContext = DefaultOtpServerRequestContext
				.createServerContext(serializedGraphObject);
		return new DefaultRoutingService(serverRequestContext);
	}
}
