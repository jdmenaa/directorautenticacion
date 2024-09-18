package com.technisys.director.services;

import com.google.gson.Gson;
import com.technisys.director.core.beans.ExecutionContext;
import com.technisys.director.core.beans.message.MessageRequest;
import com.technisys.director.core.beans.message.MessageResponse;
import com.technisys.director.core.flow.FlowConstants;
import com.technisys.director.core.utils.XPathUtils;
import com.technisys.director.services.customUtils.ServiceDirectorException;
import com.technisys.director.services.dinersServiceJavaBased.service.*;
import com.technisys.director.services.websocketserver.entities.generic.InputEntity;
import com.technisys.director.services.websocketserver.entities.generic.OutputEntity;
import com.technisys.director.services.websocketserver.services.notification.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("processResultsEvaluationRisk:1.0")
public class ProcessResultsEvaluationRiskService extends DinersServiceJavaBasedService {

    @Autowired
    private INotificationService notificationService;

    @Override
    public String getServiceId() {
        return "processResultsEvaluationRisk";
    }

    @Override
    protected String getServiceVersion() {
        return "1.0";
    }

    @Override
    protected String getServiceCoreMessageType() {
        return FlowConstants.JSON_MESSAGE_TYPE;
    }

    @Override
    public String getServiceMessageType() {
        return FlowConstants.JSON_MESSAGE_TYPE;
    }

    @Override
    protected MessageResponse<String> invokeService(MessageRequest<String> request, ExecutionContext context) {
        try {
            getLogger().info("Start service {}:{}", getServiceId(), getServiceVersion());

            String message = "{}";
            if (request.getRequestObject() != null)
                message = request.getRequestObject();

            getLogger().debug("Recibido: {}", message);
            String sessionId = this.getSession(message);

            String coreRequest = this.channelToBackend(message);
            getLogger().debug("Core RQ: {}", coreRequest);

            String coreResponse = null;
            String response;
            try {
                coreResponse = this.sendNotification(sessionId, coreRequest);
                response = this.backendToChannel(coreResponse);
            } catch (Exception e) {
                getLogger().error(e.getLocalizedMessage(), e);
                DinersError error = new DinersError(e);
                response = error.getResponse(this);
            }
            getLogger().debug("Core RS: {}", coreResponse);
            getLogger().debug("Para channel: {}", response);

            response = audit(request, response, coreRequest, coreResponse);
            return new MessageResponse<>(response, 200, this.getServiceMessageType());
        } catch (Exception e) {
            getLogger().error(e.getLocalizedMessage(), e);
            return errorMessage(e);
        }
    }

    @Override
    protected String channelToBackend(String request) {
        try {
            // Clase que facilita la lectura del xml
            ParserXML chn = new ParserXML(request);

            // Mapeo del HEADER, Prepara BODY
            RestRequest bak = new RestRequest("xmlns:www1=http://localhost/endpoint");
            bak.setTagsToExclude("externalUser");
            bak.setTagHeader("sesionId", "sessionId");
            bak.mapHeaderBody(chn, "", null);

            bak.mapBody("state", "estado");
            bak.mapBody("typeEffort", "tipoRefuerzo");

            return bak.toString();
        } catch (Exception e) {
            throw new ServiceDirectorException("Error al crear request json", e);
        }
    }

    @Override
    protected String backendToChannel(String response) {
        try {
            // Clase que facilita la lectura del xml
            ParserJson bak = new ParserJson(response);

            // Mapeo del HEADER
            RestResponse chn = new RestResponse(this);
            chn.mapHeaderBody(bak, this);

            return chn.toString();
        } catch (Exception e) {
            throw new ServiceDirectorException("Error al crear response xml", e);
        }
    }

    private String getSession(String message) {
        try {
            String value = XPathUtils.evaluateXPath(message, "/*[local-name()='Envelope']/*[local-name()='Header']/*[local-name()='metadata']/sessionId/text()");
            getLogger().debug("Param sessionId: " + value);
            return value;
        } catch (Exception e) {
            getLogger().error("Error en userName: " + e.getLocalizedMessage(), e);
            throw new ServiceDirectorException("Error: ", e);
        }
    }

    private String sendNotification(String sessionId, String body) {
        Gson gson = new Gson();
        InputEntity inputEntity = gson.fromJson(body, InputEntity.class);
        OutputEntity response = this.notificationService.processResultRisk(sessionId, inputEntity);
        return gson.toJson(response);
    }

    public MessageResponse<String> errorMessage(Exception e) {
        getLogger().error("Error al ejecutar el servicio.");

        DinersError error = new DinersError(e);
        String response = error.getResponse(this);
        return new MessageResponse<>(response, 200, getServiceMessageType());
    }
}
