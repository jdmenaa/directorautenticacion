package com.technisys.director.services;

import net.javacrumbs.jsonunit.core.Configuration;
import net.javacrumbs.jsonunit.core.internal.Diff;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class ProcessResultsNotificationPushTest {

    private ProcessResultsNotificationPushService service = new ProcessResultsNotificationPushService();

    @Test
    void testServiceID() {
        String id = service.getServiceId();
        assertEquals("processResultsNotificationPush",id);
    }

    @Test
    void testServiceVersion() {
        String version = service.getServiceVersion();
        assertEquals("1.0",version);
    }

    @Test
    void testServiceCoreMessageType(){
        String type = service.getServiceCoreMessageType();
        assertEquals("JSON",type);
    }

    @Test
    void testServiceMessageType(){
        String type = service.getServiceMessageType();
        assertEquals("JSON",type);
    }

    @Test
    void testChannelToBackendXML() {
        try {

            String xmlin = IOUtils.resourceToString("/processResultsNotificationPush/requestIn.xml", StandardCharsets.UTF_8);
            String jsonOut = service.channelToBackend(xmlin);

            String jsonEsp = IOUtils.resourceToString("/processResultsNotificationPush/requestOut.json", StandardCharsets.UTF_8);

            Configuration cfg = Configuration.empty()
                    .whenIgnoringPaths("$.dinHeader.horaTransaccion","$.dinHeader.uuid",
                            "$.dinHeader.paginado","$.dinHeader.tags","$.dinHeader.idioma",
                            "$.dinBody.SourceDate", "$.dinBody.SourceTime",
                            "$.dinBody.SourceBusinessDate","$.dinBody.HostBusinessDate");

            Diff diff = Diff.create(jsonEsp, jsonOut, "", "", cfg);
            assertTrue(diff.similar(), diff.differences());

            Assertions.assertThrows(Exception.class, () -> {
                service.channelToBackend(null);
            });
        } catch (Exception e) {
            fail("Error al ejecutar test" + e.getMessage(), e);
        }
    }

    @Test
    void testBackendToChannelXML() {
        try {
            String jsonIn = IOUtils.resourceToString("/processResultsNotificationPush/responseIn.json", StandardCharsets.UTF_8);
            String xmlout = service.backendToChannel(jsonIn);

            String xmlesp = IOUtils.resourceToString("/processResultsNotificationPush/responseOut.xml", StandardCharsets.UTF_8);
            org.xmlunit.diff.Diff diff = DiffBuilder.compare(Input.fromString(xmlout)).withTest(Input.fromString(xmlesp))
                    .ignoreWhitespace()
                    .withNodeFilter(node ->
                            !"horaTransaccion".equals(node.getLocalName()) &&
                                    !"uuid".equals(node.getLocalName()) &&
                                    !"stampDateTime".equals(node.getLocalName()) &&
                                    !"statusDate".equals(node.getLocalName())
                    )
                    .checkForSimilar().build();

            assertFalse(diff.hasDifferences(), diff.toString());

            Assertions.assertThrows(Exception.class, () -> {
                service.backendToChannel(null);
            });
        } catch (Exception e) {
            fail("Error al ejecutar test" + e.getMessage(), e);
        }
    }
}
