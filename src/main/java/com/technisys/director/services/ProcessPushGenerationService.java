package com.technisys.director.services;

import org.springframework.stereotype.Service;

import com.technisys.director.core.flow.FlowConstants;
import com.technisys.director.services.customUtils.ServiceDirectorException;
import com.technisys.director.services.dinersServiceJavaBased.service.DinersServiceJavaBasedService;
import com.technisys.director.services.dinersServiceJavaBased.service.ParserJson;
import com.technisys.director.services.dinersServiceJavaBased.service.ParserXML;
import com.technisys.director.services.dinersServiceJavaBased.service.RestRequest;
import com.technisys.director.services.dinersServiceJavaBased.service.RestResponse;

@SuppressWarnings("squid:S6830")
@Service("processPushGeneration:1.0")
public class ProcessPushGenerationService extends DinersServiceJavaBasedService {
	
	private static final String ERROR_RESPONSE = "Error al crear response xml";
	private static final String ERROR_REQUEST = "Error al crear request json";

	@Override
	protected String backendToChannel(String response) {
		try {
			ParserJson bak = new ParserJson(response);

			// Mapeo del HEADER
			RestResponse chn = new RestResponse(this);
			chn.mapHeaderBody(bak, this);
			chn.setNamespace(null);
			// Mapeo del BODY
			chn.openTag("notification");
				chn.mapBody("response", "respuestaSolicitud");
				chn.mapBody("notificationUuid", "notificationUuid");
			chn.closeTag();

			return chn.toString();
		
		} catch (Exception e) {
			throw new ServiceDirectorException(ERROR_RESPONSE, e);
		}
	}

	@Override
	protected String channelToBackend(String request) {
		try {
			// Clase que facilita la lectura del xml
			ParserXML chn = new ParserXML(request);
			// Mapeo del HEADER, Prepara el BODY
			RestRequest bak = new RestRequest("xmlns:int=https://msd-seg-aut-genera-mfa-cal-dinersclub-migracion-cal.apps.din-ros-can-dev.9gqx.p1.openshiftapps.com/fraude/v1/autenticacion/genera-push");
			bak.setTagsToExclude("externalUser");
			bak.mapHeaderBody(chn, "", null);

			// Mapeo del BODY
			bak.mapBody("transactionType","transaction/type");
			bak.mapBody("idUniken","mfa/id");
			
			bak.openTag("msg");	
				bak.mapBody("subject", "message/subject");
				bak.mapBody("message", "message/desc");
			
				bak.openTag("label");
					bak.mapBody("accept", "message/label/accept");
					bak.mapBody("reject", "message/label/reject");
				bak.closeTag();
			bak.closeTag();
			
			bak.openTag("notificationMsg");
				bak.mapBody("subject", "notificationMsg/subject");
				bak.mapBody("message", "notificationMsg/messageNotif");
			bak.closeTag();
			
			bak.mapOptionalBody("notificationUuidOld","notificationUuidOld");
			bak.mapBody("callbackUrl","callbackUrl");

		
			return bak.toString();
		} catch (Exception e) {
			throw new ServiceDirectorException(ERROR_REQUEST, e);
		}
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
	public String getServiceId() {
		return "processPushGeneration";
	}

	@Override
	protected String getServiceVersion() {
		return "1.0";
	}
	
	

}
