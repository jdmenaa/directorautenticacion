package com.technisys.director.services;

import org.springframework.stereotype.Service;

import com.technisys.director.core.flow.FlowConstants;
import com.technisys.director.services.customUtils.ServiceDirectorException;
import com.technisys.director.services.dinersServiceJavaBased.service.DinersServiceJavaBasedService;
import com.technisys.director.services.dinersServiceJavaBased.service.ParserJson;
import com.technisys.director.services.dinersServiceJavaBased.service.ParserXML;
import com.technisys.director.services.dinersServiceJavaBased.service.RestRequest;
import com.technisys.director.services.dinersServiceJavaBased.service.RestResponse;
import com.technisys.director.services.dinersServiceJavaBased.service.ServiceHelper;

@SuppressWarnings("squid:S6830") 
@Service("processCustomerDeviceEnrollment:1.0")
public class ProcessCustomerDeviceEnrollmentService extends DinersServiceJavaBasedService{
	private static final String ERROR_RESPONSE = "Error al crear response xml";
	private static final String ERROR_REQUEST = "Error al crear request json";
	@Override
	public String getServiceId() {
		return "processCustomerDeviceEnrollment";
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
	public String channelToBackend(String request) {
		try {
			// Clase que facilita la lectura del xml
			ParserXML chn = new ParserXML(request);
			// Mapeo del HEADER, Prepara el BODY
			RestRequest bak = new RestRequest("xmlns:int=https://msd-gfr-gdd-gestion-clientes-cal-dinersclub-migracion-cal.apps.din-ros-can-dev.9gqx.p1.openshiftapps.com/fraude/v1/clientes/registra");
		
			bak.setTagHeader("idioma", "locale");
			bak.setTagsToExclude("externalUser");
			bak.mapHeaderBody(chn, "", null);
			// Mapeo del BODY
			String customerId = chn.getBody("customer/customerId");
			bak.mapValue("tipoIdentificacion", ServiceHelper.splitDocumentType(customerId));
			bak.mapValue("numIdentificacion", ServiceHelper.splitDocumentNumber(customerId));
			bak.mapOptionalBody("numIdentificacionAdicional","customer/additionalCustomerType/customerTypeId");
			bak.mapOptionalBody("tipoIdentificacionAdicional","customer/additionalCustomerType/officialId");
			bak.mapBody("perfil","customer/profile/profileType/mnemonic");
			bak.mapOptionalBody("idUniken","mfa/id");
			bak.mapOptionalBody("idFalcon","fraudMonitoring/id");
			bak.mapBody("idBiocatch","biometric/id");
			bak.mapBody("imagenRostro","customer/faceId");
			bak.mapBody("registroCuentas","validation/status");
			return bak.toString();
		} catch (Exception e) {
			throw new ServiceDirectorException(ERROR_REQUEST, e);
		}
	}
	
	@Override
	public String backendToChannel(String response) {
		try {
			ParserJson bak = new ParserJson(response);

			// Mapeo del HEADER
			RestResponse chn = new RestResponse(this);
			chn.mapHeaderBody(bak, this);
			chn.setNamespace(null);
			// Mapeo del BODY
			chn.openTag("mfa");
			chn.mapBody("id", "idUniken");
			chn.closeTag();
			
			return chn.toString();
		} catch (Exception e) {
			throw new ServiceDirectorException(ERROR_RESPONSE, e);
		}
	}
}
