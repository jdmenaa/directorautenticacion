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
@Service("updateInfoBiocatch:1.0")
public class UpdateInfoBiocatchService extends DinersServiceJavaBasedService{
	private static final String ERROR_RESPONSE = "Error al crear response xml";
	private static final String ERROR_REQUEST = "Error al crear request json";

	@Override
	public String getServiceId() {
		return "updateInfoBiocatch";
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
			RestRequest bak = new RestRequest("xmlns:int=https://msd-gfr-seguimiento-sesiones-cal-dinersclub-migracion-cal.apps.din-ros-can-dev.9gqx.p1.openshiftapps.com/fraude/v1/seguimiento/actualiza");
		
			bak.setTagHeader("idioma", "locale");
			bak.setTagsToExclude("externalUser");
			bak.mapHeaderBody(chn, "", null);

			// Mapeo del BODY
			String customerId = chn.getBody("customer/customerId");
			bak.mapValue("tipoIdentificacion", ServiceHelper.splitDocumentType(customerId));
			bak.mapValue("numIdentificacion", ServiceHelper.splitDocumentNumber(customerId));
			bak.mapOptionalBody("numIdentificacionAdicional","customer/additionalCustomerType/customerTypeId");
			bak.mapOptionalBody("tipoIdentificacionAdicional","customer/additionalCustomerType/officialId");
			bak.mapBody("tipoTransaccion","transaction/type");
			mapatoAnalysisUpdate(chn, bak);
			if (!chn.getTagValue("caseUpdate/accountId").isEmpty() || !chn.getTagValue("caseUpdate/transactionId").isEmpty()) {
				mapcaseUpdate(bak);
			}
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
			chn.mapBody("bcStatus", "bcStatus");
			return chn.toString();
		
		} catch (Exception e) {
			throw new ServiceDirectorException(ERROR_RESPONSE, e);
		}
	}
	
	private void mapatoAnalysisUpdate(ParserXML chn, RestRequest bak) throws Exception
	{
		bak.openTag("atoAnalysisUpdate");
			bak.mapBody("brand","biometricAnalysis/brand");
			bak.mapBody("customerSessionId","biometricAnalysis/customerSessionId");
			bak.mapOptionalBody("accountId","biometricAnalysis/accountId");
			bak.mapOptionalBody("accountOpenDate","biometricAnalysis/accountOpenDate");
			bak.mapBody("action","biometricAnalysis/action");
			Double activityType = (Double)chn.getTagValue("biometricAnalysis/activityAmount",'D');  
			bak.mapOptionalValue("activityAmount", activityType);
			bak.mapBody("activityType","biometricAnalysis/activityType");
			Boolean biometricLogin = (Boolean)chn.getTagValue("biometricAnalysis/biometricLogin",'B');  
			bak.mapValue("biometricLogin", biometricLogin);
			bak.mapOptionalBody("payeeValue","biometricAnalysis/payeeValue");
			bak.mapOptionalBody("payerValue","biometricAnalysis/payerValue");
			bak.mapBody("solution","biometricAnalysis/solution");
			bak.mapBody("uuid","biometricAnalysis/uuid");
			bak.mapOptionalBody("yearOfBirth","biometricAnalysis/yearOfBirth");
			
			Double accountBalance = (Double)chn.getTagValue("biometricAnalysis/accountBalance",'D');  
			bak.mapValue("accountBalance", accountBalance);
			
			Double activityAmountTotal = (Double)chn.getTagValue("biometricAnalysis/activityAmountTotal",'D');  
			bak.mapValue("activityAmountTotal", activityAmountTotal);
			
			
			bak.mapOptionalBody("activityName","biometricAnalysis/activityName");
			bak.mapOptionalBody("batchFileCreationDate","biometricAnalysis/batchFileCreationDate");
			bak.mapOptionalBody("batchFileId","biometricAnalysis/batchFileId");
			
			Integer batchFilePayeesNumber = (Integer)chn.getTagValue("biometricAnalysis/batchFilePayeesNumber",'I');  
			bak.mapValue("batchFilePayeesNumber", batchFilePayeesNumber);
			
			bak.mapOptionalBody("customerApplicationVersion","biometricAnalysis/customerApplicationVersion");
			bak.mapOptionalBody("dateOfCreation","biometricAnalysis/dateOfCreation");
			bak.mapOptionalBody("deviceId","biometricAnalysis/deviceId");
			bak.mapOptionalBody("deviceModel","biometricAnalysis/deviceModel");
			bak.mapBody("ip","biometricAnalysis/ip");
			bak.mapOptionalBody("isLoginSuccess","biometricAnalysis/isLoginSuccess");
			bak.mapOptionalBody("lastDeviceRegistrationDate","biometricAnalysis/lastDeviceRegistrationDate");
			bak.mapOptionalBody("loginMethod","biometricAnalysis/loginMethod");
			bak.mapOptionalBody("membershipId","biometricAnalysis/membershipId");
			bak.mapOptionalBody("merchantName","biometricAnalysis/merchantName");
			bak.mapOptionalBody("onlineAccountOpenDate","biometricAnalysis/onlineAccountOpenDate");
			bak.mapOptionalBody("payeeAccountType","biometricAnalysis/payeeAccountType");
			bak.mapOptionalBody("payeeBankCode","biometricAnalysis/payeeBankCode");
			bak.mapOptionalBody("payeeBankCountry","biometricAnalysis/payeeBankCountry");
			bak.mapOptionalBody("payeeCreationDate","biometricAnalysis/payeeCreationDate");
			bak.mapOptionalBody("payerAccountType","biometricAnalysis/payerAccountType");
			bak.mapOptionalBody("platformType","biometricAnalysis/platformType");
			bak.mapOptionalBody("sdkVersion","biometricAnalysis/sdkVersion");
			bak.mapOptionalBody("transactionId","biometricAnalysis/transactionId");
			bak.mapOptionalBody("transactionReason","biometricAnalysis/transactionReason");
			bak.mapOptionalBody("transactionSpeed","biometricAnalysis/transactionSpeed");
			bak.mapBody("userAgent","biometricAnalysis/userAgent");
			bak.mapOptionalBody("userType","biometricAnalysis/userType");
			bak.mapOptionalBody("cardType","biometricAnalysis/cardType");
			bak.mapBody("authenticationResult","biometricAnalysis/authenticationResult");
			bak.mapBody("authMethodUsed","biometricAnalysis/authMethodUsed");
			Integer numOfFailedAuth = (Integer)chn.getTagValue("biometricAnalysis/numOfFailedAuth",'I');  
			bak.mapValue("numOfFailedAuth", numOfFailedAuth);
			bak.mapBody("yob","biometricAnalysis/yob");
			Boolean isLocked = (Boolean)chn.getTagValue("biometricAnalysis/isLocked",'B');  
			bak.mapValue("isLocked", isLocked);
		bak.closeTag();
	}
	
	private void mapcaseUpdate(RestRequest bak) throws Exception
	{

		bak.openTag("caseUpdate");
			bak.mapBody("accountId","caseUpdate/accountId");
			bak.mapBody("transactionId","caseUpdate/transactionId");
		bak.closeTag();
	}
	
}
