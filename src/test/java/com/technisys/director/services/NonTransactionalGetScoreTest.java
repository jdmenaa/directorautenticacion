package com.technisys.director.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.technisys.director.services.customUtils.ServiceDirectorException;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;

import com.technisys.director.core.flow.FlowConstants;

import net.javacrumbs.jsonunit.core.Configuration;
import net.javacrumbs.jsonunit.core.internal.Diff;

class NonTransactionalGetScoreTest {
	private NonTransactionalGetScoreService service = new NonTransactionalGetScoreService();

	@Test
	void testServiceID() {
		String id = service.getServiceId();
		assertEquals("nonTransactionalGetScore",id);
	}

	@Test
	void testServiceVersion() {
		String version = service.getServiceVersion();
		assertEquals("1.0",version);
	}

	@Test
	void testServiceCoreMessageType() {
		String type = service.getServiceCoreMessageType();
		Assert.assertEquals(FlowConstants.JSON_MESSAGE_TYPE, type);
	}

	@Test
	void testServiceMessageType() {
		String type = service.getServiceMessageType();
		Assert.assertEquals(FlowConstants.JSON_MESSAGE_TYPE, type);
	}
	
	@Test
	void testChannelToBackendXML() throws IOException {

		testServiceVersion();
		testServiceID();

		String xmlIn = IOUtils.resourceToString("/nonTransactionalGetScore/requestIn.xml", StandardCharsets.UTF_8);

		Assertions.assertThrows(ServiceDirectorException.class, () -> {
					String jsonOut = service.channelToBackend(xmlIn);
		});
//		String jsonEsp = IOUtils.resourceToString("/nonTransactionalGetScore/requestOut.json", StandardCharsets.UTF_8);
//		Configuration cfg = Configuration.empty().whenIgnoringPaths("$.dinHeader.horaTransaccion", "$.dinHeader.uuid",
//				"$.dinBody.paginado", "$.dinBody.paginado.cantRegistros", "$.dinBody.paginado.numTotalPag",
//				"$.dinBody.paginado.numPagActual", "$.dinBody.SourceDate", "$.dinBody.SourceTime",
//				"$.dinBody.SourceBusinessDate", "$.dinBody.HostBusinessDate");
//
//		Diff diff = Diff.create(jsonEsp, jsonOut, "", "", cfg);
//		assertTrue(diff.similar(), diff.differences());
//
//		Assertions.assertThrows(Exception.class, () -> {
//			service.channelToBackend(null);
//		});
	}

	@Test
	void testBackendToChannelXML() throws IOException {

		testServiceVersion();
		testServiceID();

		String jsonIn = IOUtils.resourceToString("/nonTransactionalGetScore/responseIn.json", StandardCharsets.UTF_8);
		String xmlOut = service.backendToChannel(jsonIn);

		String xmlEsp = IOUtils.resourceToString("/nonTransactionalGetScore/responseOut.xml", StandardCharsets.UTF_8);
		org.xmlunit.diff.Diff diff = DiffBuilder.compare(Input.fromString(xmlOut)).withTest(Input.fromString(xmlEsp))
				.ignoreWhitespace().withNodeFilter(node -> !"executionDate".equals(node.getLocalName()))
				.checkForSimilar().build();
		assertFalse(diff.hasDifferences(), diff.toString());

		Assertions.assertThrows(Exception.class, () -> {
			service.backendToChannel(null);
		});

	}
}
