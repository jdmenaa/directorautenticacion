package com.technisys.director.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.technisys.director.core.flow.FlowConstants;
import com.technisys.director.services.customUtils.ServiceDirectorException;
import com.technisys.director.services.dinersServiceJavaBased.service.DinersServiceJavaBasedService;
import com.technisys.director.services.dinersServiceJavaBased.service.ParserJson;
import com.technisys.director.services.dinersServiceJavaBased.service.ParserXML;
import com.technisys.director.services.dinersServiceJavaBased.service.RestRequest;
import com.technisys.director.services.dinersServiceJavaBased.service.RestResponse;

@SuppressWarnings("squid:S6830") 
@Service("massiveSelectTrustedDevicesListing:1.0")
public class MassiveSelectTrustedDevicesListingService extends DinersServiceJavaBasedService{
	private static final String ERROR_RESPONSE = "Error al crear response xml";
	private static final String ERROR_REQUEST = "Error al crear request json";
	private static final String COLLECTION="collection";
    private static final String DEVICE="device";
	@Override
	public String getServiceId() {
		return "massiveSelectTrustedDevicesListing";
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
			RestRequest bak = new RestRequest("xmlns:int=https://msd-gfr-gdd-gestion-dispositivos-cal-dinersclub-migracion-cal.apps.din-ros-can-dev.9gqx.p1.openshiftapps.com/fraude/v1/dispositivos/consulta");
		
			bak.setTagHeader("idioma", "locale");
			bak.setTagsToExclude("externalUser");
			bak.mapHeaderBody(chn, "", null);
			// Mapeo del BODY
			bak.mapBody("idUniken","mfa/id");
			
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
			mapdevices(bak, chn);
			return chn.toString();
		} catch (Exception e) {
			throw new ServiceDirectorException(ERROR_RESPONSE, e);
		}
	}
	
	private void mapdevices(ParserJson bak, RestResponse chn)
	{
		try {
			List<Object> listaServicios = bak.getNodeList("dispositivos");
			if (!listaServicios.isEmpty()) {
				chn.openTag(COLLECTION, "name=devices");
				for (Object item : listaServicios) {
					chn.openTag(DEVICE);
						chn.mapOptionalBody("name", "nombreDispositivo",item);
						chn.mapOptionalBody("id", "idDispositivo",item);
						chn.mapOptionalBody("platform", "plataforma",item);
						chn.mapOptionalBody("status", "estado",item);
						chn.mapOptionalBody("creationDate", "fechaCreacion",item);
						chn.mapOptionalBody("lastAccessDate", "fechaUltimoAcceso",item);
						chn.mapOptionalBody("token", "tokenDispositivo",item);
						chn.mapOptionalBody("expirationDate", "fechaExpiracion",item);
					chn.closeTag();				
				}
				chn.closeTag();
			}	
		} catch (Exception e) {
			throw new ServiceDirectorException(ERROR_RESPONSE, e);
		}
	}	
	
	@Override
    public String fixJson(String json) throws Exception {
        // Parsear el JSON a un objeto JSONObject
        org.json.JSONObject jsonObject = new org.json.JSONObject(json);

        // Obtener la matriz de beneficiarios
        if (jsonObject.getJSONObject(COLLECTION).get(DEVICE) instanceof org.json.JSONArray) {
            org.json.JSONArray devicesArray = jsonObject.getJSONObject(COLLECTION).getJSONArray(DEVICE);
            for (int i = 0; i < devicesArray.length(); i++) {
                org.json.JSONObject devices = devicesArray.getJSONObject(i);
                fixString(devices,"creationDate");
                fixString(devices,"lastAccessDate");
                fixString(devices,"expirationDate");
            }
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
