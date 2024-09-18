package com.technisys.director.services;

import com.technisys.director.services.websocketserver.services.authentication.IAuthenticationService;
import com.technisys.director.services.websocketserver.services.notification.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.technisys.director.core.flow.FlowConstants;
import com.technisys.director.services.customUtils.ServiceDirectorException;
import com.technisys.director.services.dinersServiceJavaBased.service.DinersServiceJavaBasedService;
import com.technisys.director.services.dinersServiceJavaBased.service.ParserJson;
import com.technisys.director.services.dinersServiceJavaBased.service.ParserXML;
import com.technisys.director.services.dinersServiceJavaBased.service.RestRequest;
import com.technisys.director.services.dinersServiceJavaBased.service.RestResponse;
import com.technisys.director.services.dinersServiceJavaBased.service.ServiceHelper;

import java.util.Optional;

@SuppressWarnings("squid:S6830")
@Service("transactionalGetScore:1.0")
public class TransactionalGetScoreService extends DinersServiceJavaBasedService {

	@Value("${service.processResultsEvaluationRisk.webhook}")
	private String webhook;

    @Autowired
    private IAuthenticationService authenticationService;

    private static final String ERROR_RESPONSE = "Error al crear response xml";
    private static final String ERROR_REQUEST = "Error al crear request json";

    @Override
    public String getServiceId() {
        return "transactionalGetScore";
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
            RestRequest bak = new RestRequest("xmlns:int=https://msd-gfr-eval-fraudemon-ingreso-cal-dinersclub-migracion-cal.apps.din-ros-can-dev.9gqx.p1.openshiftapps.com/fraude/v1/transacciones-monetarias/evalua");

            bak.setTagHeader("idioma", "locale");
            bak.setTagsToExclude("externalUser");
            bak.mapHeaderBody(chn, "", null);

            // Mapeo del BODY
            String customerId = chn.getBody("customer/customerId");
            bak.mapValue("tipoIdentificacion", ServiceHelper.splitDocumentType(customerId));
            bak.mapValue("numIdentificacion", ServiceHelper.splitDocumentNumber(customerId));

            bak.mapOptionalBody("numIdentificacionAdicional", "customer/additionalCustomerType/customerTypeId");
            bak.mapOptionalBody("tipoIdentificacionAdicional", "customer/additionalCustomerType/officialId");

			String sessionId = chn.getMetadata("sessionId");
			if(!sessionId.trim().isEmpty())
            	bak.mapValue("callbackUrlResultadoRiesgo", this.authenticationService.createCallbackDirector(sessionId, this.webhook));

			bak.mapOptionalBody("codTransaccion", "transaction/code");
            mapatoAnalysis(chn, bak);
            mapmonetaryTrxAnalysis(chn,bak);
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
            chn.openTag("fraudMonitoring");
            chn.mapBody("id", "idTransaccionFalcon");
            chn.closeTag();
            chn.openTag("customer");
            chn.mapBody("ageRange", "rangoEdad");
            chn.closeTag();
            return chn.toString();
        } catch (Exception e) {
            throw new ServiceDirectorException(ERROR_RESPONSE, e);
        }
    }

    private void mapatoAnalysis(ParserXML chn, RestRequest bak) throws Exception {
        bak.openTag("atoAnalysis");
        bak.mapBody("brand", "biometricAnalysis/brand");
        bak.mapBody("customerSessionId", "biometricAnalysis/customerSessionId");
        bak.mapOptionalBody("accountId", "biometricAnalysis/accountId");
        bak.mapOptionalBody("accountOpenDate", "biometricAnalysis/accountOpenDate");
        bak.mapBody("action", "biometricAnalysis/action");
        Double activityAmount = (Double) chn.getTagValue("biometricAnalysis/activityAmount", 'D');
        bak.mapOptionalValue("activityAmount", activityAmount);
        bak.mapBody("activityType", "biometricAnalysis/activityType");
        Boolean biometricLogin = (Boolean) chn.getTagValue("biometricAnalysis/biometricLogin", 'B');
        bak.mapValue("biometricLogin", biometricLogin);

        bak.mapOptionalBody("payeeValue", "biometricAnalysis/payeeValue");
        bak.mapOptionalBody("payerValue", "biometricAnalysis/payerValue");
        bak.mapBody("solution", "biometricAnalysis/solution");
        bak.mapBody("uuid", "biometricAnalysis/uuid");
        bak.mapOptionalBody("yearOfBirth", "biometricAnalysis/yearOfBirth");

        Double accountBalance = (Double) chn.getTagValue("biometricAnalysis/accountBalance", 'D');
        bak.mapValue("accountBalance", accountBalance);
        Double activityAmountTotal = (Double) chn.getTagValue("biometricAnalysis/activityAmountTotal", 'D');
        bak.mapValue("activityAmountTotal", activityAmountTotal);
        bak.mapOptionalBody("activityName", "biometricAnalysis/activityName");
        bak.mapOptionalBody("batchFileCreationDate", "biometricAnalysis/batchFileCreationDate");
        bak.mapOptionalBody("batchFileId", "biometricAnalysis/batchFileId");

        Integer batchFilePayeesNumber = (Integer) chn.getTagValue("biometricAnalysis/batchFilePayeesNumber", 'I');
        bak.mapValue("batchFilePayeesNumber", batchFilePayeesNumber);

        bak.mapOptionalBody("customerApplicationVersion", "biometricAnalysis/customerApplicationVersion");
        bak.mapOptionalBody("dateOfCreation", "biometricAnalysis/dateOfCreation");
        bak.mapOptionalBody("deviceId", "biometricAnalysis/deviceId");
        bak.mapOptionalBody("deviceModel", "biometricAnalysis/deviceModel");
        
        String ip=ServiceHelper.getUriValue(chn.getTagValue("address"), "ip") ;
        bak.mapValue("ip", ip);
        bak.mapOptionalBody("isLoginSuccess", "biometricAnalysis/isLoginSuccess");
        bak.mapOptionalBody("lastDeviceRegistrationDate", "biometricAnalysis/lastDeviceRegistrationDate");
        bak.mapOptionalBody("loginMethod", "biometricAnalysis/loginMethod");
        bak.mapOptionalBody("membershipId", "biometricAnalysis/membershipId");
        bak.mapOptionalBody("merchantName", "biometricAnalysis/merchantName");
        bak.mapOptionalBody("onlineAccountOpenDate", "biometricAnalysis/onlineAccountOpenDate");
        bak.mapOptionalBody("payeeAccountType", "biometricAnalysis/payeeAccountType");
        bak.mapOptionalBody("payeeBankCode", "biometricAnalysis/payeeBankCode");
        bak.mapOptionalBody("payeeBankCountry", "biometricAnalysis/payeeBankCountry");
        bak.mapOptionalBody("payeeCreationDate", "biometricAnalysis/payeeCreationDate");
        bak.mapOptionalBody("payerAccountType", "biometricAnalysis/payerAccountType");
        bak.mapOptionalBody("platformType", "biometricAnalysis/platformType");
        bak.mapOptionalBody("sdkVersion", "biometricAnalysis/sdkVersion");
        bak.mapOptionalBody("transactionId", "biometricAnalysis/transactionId");
        bak.mapOptionalBody("transactionReason", "biometricAnalysis/transactionReason");
        bak.mapOptionalBody("transactionSpeed", "biometricAnalysis/transactionSpeed");
        
        String userAgent=chn.getTagValue("terminalId");
        bak.mapValue("userAgent", userAgent);
        bak.mapOptionalBody("userType", "biometricAnalysis/userType");
        bak.mapOptionalBody("cardType", "biometricAnalysis/cardType");
        bak.closeTag();
    }

    private void mapmonetaryTrxAnalysis(ParserXML chn,RestRequest bak) throws Exception {
        bak.openTag("monetaryTrxAnalysis");
        bak.mapBody("customerAcctNumber", "monetaryTrxAnalysis/customerAcctNumber");
        bak.mapBody("debitAcctNumber", "monetaryTrxAnalysis/debitAcctNumber");
        bak.mapBody("debitCustomerId", "monetaryTrxAnalysis/debitCustomerId");
        bak.mapBody("creditAcctNumber", "monetaryTrxAnalysis/creditAcctNumber");
        bak.mapBody("transactionAmount", "monetaryTrxAnalysis/transactionAmount");
        bak.mapBody("transactionType", "monetaryTrxAnalysis/transactionType");
        bak.mapBody("accessChannel", "monetaryTrxAnalysis/accessChannel");
        
        String ip=ServiceHelper.getUriValue(chn.getTagValue("address"), "ip") ;
        bak.mapValue("deviceId", ip);
        bak.mapOptionalBody("debitAcctBankId", "monetaryTrxAnalysis/debitAcctBankId");
        bak.mapOptionalBody("debitAcctSortCode", "monetaryTrxAnalysis/debitAcctSortCode");
        bak.mapOptionalBody("debitAmount", "monetaryTrxAnalysis/debitAmount");
        bak.mapOptionalBody("debitCurrencyCode", "monetaryTrxAnalysis/debitCurrencyCode");
        bak.mapOptionalBody("debitDate", "monetaryTrxAnalysis/debitDate");
        bak.mapOptionalBody("debitName", "monetaryTrxAnalysis/debitName");
        bak.mapOptionalBody("debitMessage", "monetaryTrxAnalysis/debitMessage");
        bak.mapOptionalBody("creditCustomerId", "monetaryTrxAnalysis/creditCustomerId");
        bak.mapOptionalBody("creditAcctBankId", "monetaryTrxAnalysis/creditAcctBankId");
        bak.mapOptionalBody("creditAcctSortCode", "monetaryTrxAnalysis/creditAcctSortCode");
        bak.mapOptionalBody("creditAmount", "monetaryTrxAnalysis/creditAmount");
        bak.mapOptionalBody("creditCurrencyCode", "monetaryTrxAnalysis/creditCurrencyCode");
        bak.mapOptionalBody("creditDate", "monetaryTrxAnalysis/creditDate");
        bak.mapOptionalBody("creditName", "monetaryTrxAnalysis/creditName");
        bak.mapBody("creditMessage", "monetaryTrxAnalysis/creditMessage");

        bak.closeTag();
    }


    @Override
    public String fixJson(String json) throws Exception {
        // Parsear el JSON a un objeto JSONObject
        org.json.JSONObject jsonObject = new org.json.JSONObject(json);

        // Obtener la matriz de beneficiarios
        org.json.JSONObject item = jsonObject.getJSONObject("fraudMonitoring");
        fixString(item, "id");
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
