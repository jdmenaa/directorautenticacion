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
@Service("massiveSelectTrustedDeviceAndPills:1.0")
public class MassiveSelectTrustedDeviceAndPillsService extends DinersServiceJavaBasedService {
	private static final String ERROR_RESPONSE = "Error al crear response xml";
	private static final String ERROR_REQUEST = "Error al crear request json";

	@Override
	public String getServiceId() {
		return "massiveSelectTrustedDeviceAndPills";
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
			RestRequest bak = new RestRequest("xmlns:int=https://msd-gfr-gdd-gestion-clientes-cal-dinersclub-migracion-cal.apps.din-ros-can-dev.9gqx.p1.openshiftapps.com/fraude/v1/clientes/valida");
		
			bak.setTagHeader("idioma", "locale");
			bak.setTagsToExclude("externalUser");
			bak.mapHeaderBody(chn, "", null);

			// Mapeo del BODY
			String customerId = chn.getBody("customer/customerId");
			bak.mapOptionalValue("tipoIdentificacion", ServiceHelper.splitDocumentType(customerId));
			bak.mapOptionalValue("numIdentificacion", ServiceHelper.splitDocumentNumber(customerId));
			bak.mapOptionalBody("numIdentificacionAdicional","customer/additionalCustomerType/customerTypeId");
			bak.mapOptionalBody("tipoIdentificacionAdicional","customer/additionalCustomerType/officialId");
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
				chn.mapBody("id","idUniken");
			chn.closeTag();
			
			chn.openTag("fraudMonitoring");
				chn.mapBody("id","idFalcon");
			chn.closeTag();
			
			chn.openTag("biometric");
				chn.mapBody("id","idBiocatch");
			chn.closeTag();
			
			chn.openTag("device");
				chn.mapBody("isAllDeviceBlocked","dispositivosBloqueados");
				chn.mapBody("activeDevices","dispositivosActivos");
				chn.mapBody("isRegistered","existeDispositivos");
				chn.mapBody("showInformationPills","mostrarPildoras");
				chn.mapBody("showInformationPillsBanner","mostrarPildorasBanner");
			chn.closeTag();
			
			chn.mapBody("isAccountRegister","registroCuentas");
			
			chn.openTag("customer");
				chn.mapOptionalBody("birthDate","fechaNacimiento");
				chn.mapOptionalBody("constitutionDate","fechaConstitucion");
				chn.mapBody("incomeDate","fechaIngreso");
			chn.closeTag();
			
			chn.openTag("establishment");
				chn.mapOptionalBody("companyName","razonSocial");
			chn.closeTag();
			return chn.toString();
		
		} catch (Exception e) {
			throw new ServiceDirectorException(ERROR_RESPONSE, e);
		}
	}
	
	@Override
    public String fixJson(String json) throws Exception {
        // Parsear el JSON a un objeto JSONObject
        org.json.JSONObject jsonObject = new org.json.JSONObject(json);
		if (jsonObject.has("customer")) {
			org.json.JSONObject item = jsonObject.getJSONObject("customer");
			fixString(item, "birthDate");
			fixString(item, "constitutionDate");
		}
        return jsonObject.toString();
    }
	
	public void fixString(org.json.JSONObject beneficiary, String name) {
    	if (beneficiary.has(name)) {
            Object name1Value = beneficiary.get(name);
            if (name1Value instanceof Number) {
                String name1String = String.valueOf(name1Value);
                beneficiary.put(name, name1String);
            }
        }
    }
}
