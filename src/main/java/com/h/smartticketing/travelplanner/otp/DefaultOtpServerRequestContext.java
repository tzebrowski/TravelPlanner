package com.h.smartticketing.travelplanner.otp;

import static org.opentripplanner.standalone.configure.ConstructApplication.creatTransitLayerForRaptor;

import org.opentripplanner.raptor.configure.RaptorConfig;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.graph.SerializedGraphObject;
import org.opentripplanner.service.vehiclepositions.internal.DefaultVehiclePositionService;
import org.opentripplanner.service.worldenvelope.internal.DefaultWorldEnvelopeService;
import org.opentripplanner.standalone.api.OtpServerRequestContext;
import org.opentripplanner.standalone.config.RouterConfig;
import org.opentripplanner.standalone.server.DefaultServerRequestContext;
import org.opentripplanner.transit.service.DefaultTransitService;
import org.opentripplanner.transit.service.StopModel;
import org.opentripplanner.transit.service.TransitModel;

import io.micrometer.core.instrument.Metrics;

final class DefaultOtpServerRequestContext {

	static OtpServerRequestContext createServerContext(SerializedGraphObject serializedGraphObject) {
		final TransitModel transitModel = serializedGraphObject.transitModel;
		transitModel.index();

		final RouterConfig routerConfig = serializedGraphObject.routerConfig;

		final Graph graph = serializedGraphObject.graph;
		graph.index(new StopModel());

		final DefaultServerRequestContext context = DefaultServerRequestContext.create(routerConfig.transitTuningConfig(),
				routerConfig.routingRequestDefaults(), 
				routerConfig.streetRoutingTimeout(),
				new RaptorConfig<>(routerConfig.transitTuningConfig()), 
				graph, 
				new DefaultTransitService(transitModel),
				Metrics.globalRegistry, routerConfig.vectorTileLayers(),
				new DefaultWorldEnvelopeService(serializedGraphObject.worldEnvelopeRepository),
				new DefaultVehiclePositionService(),
				routerConfig.flexConfig(), 
				null, 
				routerConfig.requestLogFile());
		creatTransitLayerForRaptor(transitModel, routerConfig.transitTuningConfig());
		return context;
	}
}