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
@Service("deviceRegistrationNotification:1.0")
public class DeviceRegistrationNotificationService extends DinersServiceJavaBasedService {

	private static final String ERROR_RESPONSE = "Error al crear response xml";
	private static final String ERROR_REQUEST = "Error al crear request json";

	@Override
	public String getServiceId() {
		return "deviceRegistrationNotification";
	}

	@Override
	public String getServiceVersion() {
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
			RestRequest bak = new RestRequest("xmlns:int=https://msd-gfr-gdd-gestion-dispositivos-cal-dinersclub-migracion-cal.apps.din-ros-can-dev.9gqx.p1.openshiftapps.com/fraude/v1/dispositivos/crea-notifica-caso");

			bak.setTagHeader("idioma", "locale");
			bak.setTagsToExclude("externalUser");
			bak.mapHeaderBody(chn, "", null);

			// Mapeo del BODY
			bak.mapBody("idUniken", "mfa/id");
			bak.mapBody("idDispositivo", "device/id");
			bak.mapBody("estado", "device/status");
			bak.mapBody("identificacionUsuario", "customer/customerId");
			bak.mapBody("perfil", "customer/profile");
			bak.mapOptionalBody("rucEmpresa", "establishment/ruc");
			bak.mapOptionalBody("numIdentificacionAdicional", "customer/additionalCustomerType/customerTypeId");
			bak.mapOptionalBody("tipoIdentificacionAdicional", "customer/additionalCustomerType/officialId");
			bak.mapBody("codigoTransaccion", "transaction/code");

			bak.openTag("variables");
				bak.mapOptionalBody("nombreDispositivo", "variables/deviceName");
				bak.mapOptionalBody("nombreDispositivoAnterior", "variables/previousDeviceName");
				bak.mapOptionalBody("nombreDispositivoNuevo", "variables/newDeviceName");
				bak.mapOptionalBody("pais", "variables/country");
				bak.mapOptionalBody("ciudad", "variables/city");
				bak.mapOptionalBody("cuentaDigital", "variables/digitalAccount");
			bak.closeTag();

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

			return chn.toString();
		} catch (Exception e) {
			throw new ServiceDirectorException(ERROR_RESPONSE, e);
		}
	}

}
