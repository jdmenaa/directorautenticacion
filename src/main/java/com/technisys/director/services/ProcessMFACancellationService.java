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
@Service("processMFACancellation:1.0")
public class ProcessMFACancellationService extends DinersServiceJavaBasedService {

	@Override
	protected String backendToChannel(String response) {
		try {
			// Clase que facilita la lectura del xml
			ParserJson bak = new ParserJson(response);

			// Mapeo del HEADER
			RestResponse chn = new RestResponse(this);
			chn.mapHeaderBody(bak, this);
			chn.setNamespace(null);

			// Mapeo del BODY

			return chn.toString();

		} catch (Exception e) {
			throw new ServiceDirectorException("Error al crear response xml", e);
		}
	}

	@Override
	protected String channelToBackend(String request) {
		try {
			// Clase que facilita la lectura del xml
			ParserXML chn = new ParserXML(request);

			// Mapeo del HEADER
			RestRequest bak = new RestRequest("xmlns:int=https://msd-seg-aut-genera-mfa-cal-dinersclub-migracion-cal.apps.din-ros-can-dev.9gqx.p1.openshiftapps.com/fraude/v1/autenticacion/cancela-otp-push");
			bak.setTagHeader("idioma", "locale");
			bak.setTagsToExclude("externalUser");
			bak.mapHeaderBody(chn, "", null);

			// Mapeo del BODY
			bak.mapBody("transactionType", "transactionType");
			bak.mapBody("notificationUuid", "notificationUuid");

			return bak.toString();
		} catch (Exception e) {
			throw new ServiceDirectorException("Error al crear request json", e);
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
	protected String getServiceVersion() {
		return "1.0";
	}

	@Override
	public String getServiceId() {
		return "processMFACancellation";
	}

}
