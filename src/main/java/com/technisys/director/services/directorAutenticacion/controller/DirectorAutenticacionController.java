package com.technisys.director.services.directorAutenticacion.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.technisys.director.core.beans.message.MessageRequest;
import com.technisys.director.core.controller.DirectorController;
import com.technisys.director.core.controller.processing.CustomEndpointProcessing;
import com.technisys.director.services.DeleteDeviceManagementService;
import com.technisys.director.services.DeviceRegistrationNotificationService;
import com.technisys.director.services.MassiveSelectTrustedDeviceAndPillsService;
import com.technisys.director.services.MassiveSelectTrustedDevicesListingService;
import com.technisys.director.services.NonTransactionalGetScoreService;
import com.technisys.director.services.ProcessCustomerDeviceEnrollmentService;
import com.technisys.director.services.ProcessLockUnlockDeviceManagementService;
import com.technisys.director.services.ProcessMFACancellationService;
import com.technisys.director.services.ProcessOtpRequestUnikenService;
import com.technisys.director.services.ProcessPushGenerationService;
import com.technisys.director.services.ProcessResultsEvaluationRiskService;
import com.technisys.director.services.ProcessResultsNotificationPushService;
import com.technisys.director.services.TransactionalGetScoreService;
import com.technisys.director.services.UpdateInfoBiocatchService;
import com.technisys.director.services.UpdateUserDeviceStateService;
import com.technisys.director.services.VerifyOtpUnikenService;
import com.technisys.director.services.customUtils.DirectorUtilities;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/directorAutenticacion")
@Tag(name = "directorAutenticacion")
@Component
public class DirectorAutenticacionController extends DirectorController {

	// Enable and configure controller methods associated to the services included
	// in this director
	@Autowired
	@Qualifier("processOtpRequestUniken:1.0")
	private ProcessOtpRequestUnikenService processOtpRequestUniken;

	@Autowired
	@Qualifier("processLockUnlockDeviceManagement:1.0")
	private ProcessLockUnlockDeviceManagementService processLockUnlockDeviceManagement;

	@Autowired
	@Qualifier("updateInfoBiocatch:1.0")
	private UpdateInfoBiocatchService updateInfoBiocatch;

	@Autowired
	@Qualifier("massiveSelectTrustedDeviceAndPills:1.0")
	private MassiveSelectTrustedDeviceAndPillsService massiveSelectTrustedDeviceAndPills;

	@Autowired
	@Qualifier("transactionalGetScore:1.0")
	private TransactionalGetScoreService transactionalGetScore;

	@Autowired
	@Qualifier("nonTransactionalGetScore:1.0")
	private NonTransactionalGetScoreService nonTransactionalGetScore;

	@Autowired
	@Qualifier("massiveSelectTrustedDevicesListing:1.0")
	private MassiveSelectTrustedDevicesListingService massiveSelectTrustedDevicesListing;

	@Autowired
	@Qualifier("deleteDeviceManagement:1.0")
	private DeleteDeviceManagementService deleteDeviceManagement;

	@Autowired
	@Qualifier("processCustomerDeviceEnrollment:1.0")
	private ProcessCustomerDeviceEnrollmentService processCustomerDeviceEnrollment;

	@Autowired
	@Qualifier("verifyOtpUniken:1.0")
	private VerifyOtpUnikenService verifyOtpUniken;

	@Autowired
	@Qualifier("processResultsEvaluationRisk:1.0")
	private ProcessResultsEvaluationRiskService processResultsEvaluationRiskService;

	@Autowired
	@Qualifier("processResultsNotificationPush:1.0")
	private ProcessResultsNotificationPushService processResultsNotificationPush;

	@Autowired
	@Qualifier("deviceRegistrationNotification:1.0")
	private DeviceRegistrationNotificationService deviceRegistrationNotification;

	@Autowired
	@Qualifier("processMFACancellation:1.0")
	private ProcessMFACancellationService processMFACancellation;
	
	@Autowired
	@Qualifier("processPushGeneration:1.0")
	private ProcessPushGenerationService processPushGeneration;
	
	@Autowired
	@Qualifier("updateUserDeviceState:1.0")
	private UpdateUserDeviceStateService updateUserDeviceState;
	
	

	@PostMapping(path = "/v1/processOtpRequestUniken", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Ejemplo XML", value = "<SOAP-ENV:Envelope\n"
							+ "	xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\"\n"
							+ "	xmlns:e=\"http://www.technisys.net/cmm/services/errors/v1.0\"\n"
							+ "	xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\"\n"
							+ "	xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\n"
							+ "	xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><SOAP-ENV:Header><md:metadata><serviceId>processOtpRequestUniken</serviceId><serviceVersion>1.0</serviceVersion><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><channelId>MB</channelId><targetChannel><mnemonic>MB</mnemonic></targetChannel><organizationType><mnemonic /></organizationType><executingChannel><mnemonic>MB</mnemonic></executingChannel><address>ip=10.230.241.199;</address><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</executingOperatorId><locale>ES</locale><institutionType /><template><templateId /></template><channelDispatchDate>20220503205514381-0300</channelDispatchDate><msgTypeId /><workflowModule /><featureId>ROL@5527</featureId><sourceDate /><workflowId /><businessDate /><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)\n"
							+ "				AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1\n"
							+ "				Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36\n"
							+ "			</terminalId><sourceTime /><branchId /><userId /><executingScope /><customProperties><operatorId type=\"java.lang.String\" /><businessDate type=\"java.util.Date\" /><serviceContext type=\"java.lang.String\" /><llaveSimetrica type=\"java.lang.String\" /><funcCode type=\"java.lang.String\" /><funcType type=\"java.lang.String\">OVA</funcType><terminal type=\"java.lang.String\" /><portal type=\"java.lang.String\" /></customProperties><llaveSimetrica type=\"java.lang.String\" /><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><parityCurrencyId /><localCurrencyId /><institutionId /><localCountryId /><bankId /><ip>10.230.241.199</ip><parityQuotationNemonic /><paginationInfo /><internals><auditCore>true</auditCore><autoPaginationEnabled>false</autoPaginationEnabled><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName><translate>true</translate><serviceRequestTimestamp /></internals></md:metadata></SOAP-ENV:Header><SOAP-ENV:Body><NS3:processOtpRequestUnikenRequest\n"
							+ "			xmlns:NS3=\"http://www.technisys.net/cmm/services/processOtpRequestUniken/rq/v0.0\"><user><id>id</id></user><transaction><type>type</type><code>code</code></transaction><customer><customerId>customerId</customerId><token><value>value</value></token></customer><profile><profileType><mnemonic>mnemonic</mnemonic></profileType></profile><establishment><ruc>ruc</ruc></establishment><consumer><value>value</value></consumer><creditCard><entityCode>entityCode</entityCode><creditCardAccount><brand><mnemonic>mnemonic</mnemonic></brand></creditCardAccount></creditCard><otp><uuidOld>uuidOld</uuidOld></otp></NS3:processOtpRequestUnikenRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>", summary = "Full request") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Ejemplo JSON", value = "{\"user\":{\"id\":\"id\"},\"transaction\":{\"type\":\"type\",\"code\":\"code\"},\"customer\":{\"customerId\":\"customerId\",\"token\":{\"value\":\"value\"}},\"profile\":{\"profileType\":{\"mnemonic\":\"mnemonic\"}},\"establishment\":{\"ruc\":\"ruc\"},\"consumer\":{\"value\":\"value\"},\"creditCard\":{\"entityCode\":\"entityCode\",\"creditCardAccount\":{\"brand\":{\"mnemonic\":\"mnemonic\"}}},\"otp\":{\"uuidOld\":\"uuidOld\"}}", summary = "Full request") }) })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Resultado OK", content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Response XML", value = "<soap:Envelope xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:srv=\"http://www.technisys.net/cmm/services/processOtpRequestUniken/rs/v1.0\"><soap:Header><md:metadata><organizationType><mnemonic></mnemonic></organizationType><sourceTime>20220503205514381-0300</sourceTime><institutionId></institutionId><userId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</userId><channelId>MB</channelId><institutionType></institutionType><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)\n"
							+ "				AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1\n"
							+ "				Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36\n"
							+ "			</terminalId><channelDispatchDate>20220503205514381-0300</channelDispatchDate><address>ip=10.230.241.199;</address><traceNumber>148543394962285070756262080887232536851.302812081171890176576938438871018784169</traceNumber><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><organizationId></organizationId><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</executingOperatorId><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><featureId>ROL@5527</featureId><internals><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName></internals><executingChannel><mnemonic translationError=\"UNDEFINED_TRANSLATION\">MB</mnemonic><originalCodes>originalCode=MB;</originalCodes></executingChannel><locale>ES</locale><md:serviceId>processOtpRequestUniken</md:serviceId><md:serviceVersion>1.0</md:serviceVersion></md:metadata></soap:Header><soap:Body><srv:processOtpRequestUnikenResponse><otp><codeResult>0</codeResult><uuid>f47ac10b-58cc-4372-a567-0e02b2c3d479</uuid></otp></srv:processOtpRequestUnikenResponse></soap:Body></soap:Envelope>", summary = "Full Response") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Response JSON", value = "{\"otp\":{\"codeResult\":0,\"uuid\":\"f47ac10b-58cc-4372-a567-0e02b2c3d479\"}}", summary = "Full Response") }) }) })
	@CustomEndpointProcessing(processingFlow = "jsonXmlFlow")
	public DeferredResult<ResponseEntity<String>> postsprocessOtpRequestUniken(
			@Parameter(hidden = true) @RequestHeader Map<String, Object> headers, HttpServletRequest request,
			@org.springframework.web.bind.annotation.RequestBody String body) {
		MessageRequest<String> messageRequest = new MessageRequest<>();
		DirectorUtilities.completeRequestMap(request, headers);
		messageRequest.setHeaders(headers);
		messageRequest.setRequestObject(body);
		messageRequest.putHeader("service_id", "processOtpRequestUniken");
		messageRequest.putHeader("service_version", "1.0");
		return this.executeService(this.processOtpRequestUniken, messageRequest);
	}

	@PostMapping(path = "/v1/processLockUnlockDeviceManagement", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Ejemplo XML", value = "<SOAP-ENV:Envelope\n"
							+ "	xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\"\n"
							+ "	xmlns:e=\"http://www.technisys.net/cmm/services/errors/v1.0\"\n"
							+ "	xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\"\n"
							+ "	xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\n"
							+ "	xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><SOAP-ENV:Header><md:metadata><serviceId>processLockUnlockDeviceManagement</serviceId><serviceVersion>1.0</serviceVersion><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><channelId>MB</channelId><targetChannel><mnemonic>MB</mnemonic></targetChannel><organizationType><mnemonic /></organizationType><executingChannel><mnemonic>MB</mnemonic></executingChannel><address>ip=10.230.241.199;</address><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</executingOperatorId><locale>ES</locale><institutionType /><template><templateId /></template><channelDispatchDate>20220503205514381-0300</channelDispatchDate><msgTypeId /><workflowModule /><featureId>ROL@5527</featureId><sourceDate /><workflowId /><businessDate /><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)\n"
							+ "				AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1\n"
							+ "				Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36\n"
							+ "			</terminalId><sourceTime /><branchId /><userId /><executingScope /><customProperties><operatorId type=\"java.lang.String\" /><businessDate type=\"java.util.Date\" /><serviceContext type=\"java.lang.String\" /><llaveSimetrica type=\"java.lang.String\" /><funcCode type=\"java.lang.String\" /><funcType type=\"java.lang.String\">OVA</funcType><terminal type=\"java.lang.String\" /><portal type=\"java.lang.String\" /></customProperties><llaveSimetrica type=\"java.lang.String\" /><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><parityCurrencyId /><localCurrencyId /><institutionId /><localCountryId /><bankId /><ip>10.230.241.199</ip><parityQuotationNemonic /><paginationInfo /><internals><auditCore>true</auditCore><autoPaginationEnabled>false</autoPaginationEnabled><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName><translate>true</translate><serviceRequestTimestamp /></internals></md:metadata></SOAP-ENV:Header><SOAP-ENV:Body><NS3:processLockUnlockDeviceManagementRequest\n"
							+ "			xmlns:NS3=\"http://www.technisys.net/cmm/services/processLockUnlockDeviceManagement/rq/v0.0\"><mfa><id>123456</id></mfa><device><id>ABC123</id><status>active</status></device><customer><customerId>CUST789</customerId><profile>premium</profile><additionalCustomerType><customerTypeId>456DEF</customerTypeId><officialId>789GHI</officialId></additionalCustomerType></customer><establishment><ruc>123456789</ruc></establishment><transaction><code>TRX987</code></transaction><variables><deviceName>Smartphone</deviceName></variables></NS3:processLockUnlockDeviceManagementRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>", summary = "Full request") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Ejemplo JSON", value = "{\"mfa\":{\"id\":\"123456\"},\"device\":{\"id\":\"ABC123\",\"status\":\"active\"},\"customer\":{\"customerId\":\"CUST789\",\"profile\":\"premium\",\"additionalCustomerType\":{\"customerTypeId\":\"456DEF\",\"officialId\":\"789GHI\"}},\"establishment\":{\"ruc\":\"123456789\"},\"transaction\":{\"code\":\"TRX987\"},\"variables\":{\"deviceName\":\"Smartphone\"}}", summary = "Full request") }) })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Resultado OK", content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Response XML", value = "<soap:Envelope xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:srv=\"http://www.technisys.net/cmm/services/processLockUnlockDeviceManagement/rs/v1.0\"><soap:Header><md:metadata><organizationType><mnemonic></mnemonic></organizationType><sourceTime>20220503205514381-0300</sourceTime><institutionId></institutionId><userId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</userId><channelId>MB</channelId><institutionType></institutionType><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)\n"
							+ "				AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1\n"
							+ "				Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36\n"
							+ "			</terminalId><channelDispatchDate>20220503205514381-0300</channelDispatchDate><address>ip=10.230.241.199;</address><traceNumber>146676008485847302681809261911161092681.138187179360016139491512496265303874283</traceNumber><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><organizationId></organizationId><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</executingOperatorId><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><featureId>ROL@5527</featureId><internals><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName></internals><executingChannel><mnemonic translationError=\"UNDEFINED_TRANSLATION\">MB</mnemonic><originalCodes>originalCode=MB;</originalCodes></executingChannel><locale>ES</locale><md:serviceId>processLockUnlockDeviceManagement</md:serviceId><md:serviceVersion>1.0</md:serviceVersion></md:metadata></soap:Header><soap:Body><srv:processLockUnlockDeviceManagementResponse/></soap:Body></soap:Envelope>", summary = "Full Response") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Response JSON", value = "{}", summary = "Full Response") }) }) })
	@CustomEndpointProcessing(processingFlow = "jsonXmlFlow")
	public DeferredResult<ResponseEntity<String>> postsprocessLockUnlockDeviceManagement(
			@Parameter(hidden = true) @RequestHeader Map<String, Object> headers, HttpServletRequest request,
			@org.springframework.web.bind.annotation.RequestBody String body) {
		MessageRequest<String> messageRequest = new MessageRequest<>();
		DirectorUtilities.completeRequestMap(request, headers);
		messageRequest.setHeaders(headers);
		messageRequest.setRequestObject(body);
		messageRequest.putHeader("service_id", "processLockUnlockDeviceManagement");
		messageRequest.putHeader("service_version", "1.0");
		return this.executeService(this.processLockUnlockDeviceManagement, messageRequest);
	}

	@PostMapping(path = "/v1/updateInfoBiocatch", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Ejemplo XML", value = "<SOAP-ENV:Envelope\n"
							+ "	xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\"\n"
							+ "	xmlns:e=\"http://www.technisys.net/cmm/services/errors/v1.0\"\n"
							+ "	xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\"\n"
							+ "	xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\n"
							+ "	xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><SOAP-ENV:Header><md:metadata><serviceId>updateInfoBiocatch</serviceId><serviceVersion>1.0</serviceVersion><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><channelId>MB</channelId><targetChannel><mnemonic>MB</mnemonic></targetChannel><organizationType><mnemonic /></organizationType><executingChannel><mnemonic>MB</mnemonic></executingChannel><address>ip=10.230.241.199;</address><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</executingOperatorId><locale>ES</locale><institutionType /><template><templateId /></template><channelDispatchDate>20220503205514381-0300</channelDispatchDate><msgTypeId /><workflowModule /><featureId>ROL@5527</featureId><sourceDate /><workflowId /><businessDate /><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)\n"
							+ "				AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1\n"
							+ "				Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36\n"
							+ "			</terminalId><sourceTime /><branchId /><userId /><executingScope /><customProperties><operatorId type=\"java.lang.String\" /><businessDate type=\"java.util.Date\" /><serviceContext type=\"java.lang.String\" /><llaveSimetrica type=\"java.lang.String\" /><funcCode type=\"java.lang.String\" /><funcType type=\"java.lang.String\">OVA</funcType><terminal type=\"java.lang.String\" /><portal type=\"java.lang.String\" /></customProperties><llaveSimetrica type=\"java.lang.String\" /><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><parityCurrencyId /><localCurrencyId /><institutionId /><localCountryId /><bankId /><ip>10.230.241.199</ip><parityQuotationNemonic /><paginationInfo /><internals><auditCore>true</auditCore><autoPaginationEnabled>false</autoPaginationEnabled><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName><translate>true</translate><serviceRequestTimestamp /></internals></md:metadata></SOAP-ENV:Header><SOAP-ENV:Body><NS3:updateInfoBiocatchRequest\n"
							+ "			xmlns:NS3=\"http://www.technisys.net/cmm/services/updateInfoBiocatch/rq/v0.0\"><customer><customerId>1#CU123456</customerId><additionalCustomerType><customerTypeId>CUT789</customerTypeId><officialId>OFF456</officialId></additionalCustomerType></customer><transaction><type>purchase</type></transaction><biometricAnalysis><brand>BiometricoX</brand><customerSessionId>SESSION123</customerSessionId><accountId>ACC789</accountId><accountOpenDate>2024-02-20</accountOpenDate><action>login</action><activityAmount>100.50</activityAmount><activityType>login</activityType><biometricLogin>true</biometricLogin><payeeValue>50.25</payeeValue><payerValue>50.25</payerValue><solution>BiometricSolution</solution><uuid>UUID123</uuid><yearOfBirth>1985</yearOfBirth><accountBalance>5000.00</accountBalance><activityAmountTotal>500.00</activityAmountTotal><activityName>LoginActivity</activityName><batchFileCreationDate>2024-02-25</batchFileCreationDate><batchFileId>BATCH789</batchFileId><batchFilePayeesNumber>10</batchFilePayeesNumber><customerApplicationVersion>2.0</customerApplicationVersion><dateOfCreation>2024-02-15</dateOfCreation><deviceId>DEV123</deviceId><deviceModel>ModelX</deviceModel><ip>192.168.1.1</ip><isLoginSuccess>true</isLoginSuccess><lastDeviceRegistrationDate>2024-01-01</lastDeviceRegistrationDate><loginMethod>Biometric</loginMethod><membershipId>MEMB123</membershipId><merchantName>MerchantX</merchantName><onlineAccountOpenDate>2024-02-01</onlineAccountOpenDate><payeeAccountType>checking</payeeAccountType><payeeBankCode>BANK456</payeeBankCode><payeeBankCountry>US</payeeBankCountry><payeeCreationDate>2024-01-15</payeeCreationDate><payerAccountType>savings</payerAccountType><platformType>Mobile</platformType><sdkVersion>3.0</sdkVersion><transactionId>TRANS789</transactionId><transactionReason>Purchase</transactionReason><transactionSpeed>Fast</transactionSpeed><userAgent>Chrome</userAgent><userType>Customer</userType><authenticationResult>Success</authenticationResult><authMethodUsed>Biometric</authMethodUsed><numOfFailedAuth>0</numOfFailedAuth><yob>1985</yob><isLocked>false</isLocked></biometricAnalysis><caseUpdate><accountId>ACC123</accountId><transactionId>TRANS456</transactionId></caseUpdate></NS3:updateInfoBiocatchRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>", summary = "Full request") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Ejemplo JSON", value = "{\"customer\":{\"customerId\":\"CU123456\",\"additionalCustomerType\":{\"customerTypeId\":\"CUT789\",\"officialId\":\"OFF456\"}},\"transaction\":{\"type\":\"purchase\"},\"biometricAnalysis\":{\"brand\":\"BiometricoX\",\"customerSessionId\":\"SESSION123\",\"accountId\":\"ACC789\",\"accountOpenDate\":\"2024-02-20\",\"action\":\"login\",\"activityAmount\":100.50,\"activityType\":\"login\",\"biometricLogin\":true,\"payeeValue\":50.25,\"payerValue\":50.25,\"solution\":\"BiometricSolution\",\"uuid\":\"UUID123\",\"yearOfBirth\":1985,\"accountBalance\":5000.00,\"activityAmountTotal\":500.00,\"activityName\":\"LoginActivity\",\"batchFileCreationDate\":\"2024-02-25\",\"batchFileId\":\"BATCH789\",\"batchFilePayeesNumber\":10,\"customerApplicationVersion\":\"2.0\",\"dateOfCreation\":\"2024-02-15\",\"deviceId\":\"DEV123\",\"deviceModel\":\"ModelX\",\"ip\":\"192.168.1.1\",\"isLoginSuccess\":true,\"lastDeviceRegistrationDate\":\"2024-01-01\",\"loginMethod\":\"Biometric\",\"membershipId\":\"MEMB123\",\"merchantName\":\"MerchantX\",\"onlineAccountOpenDate\":\"2024-02-01\",\"payeeAccountType\":\"checking\",\"payeeBankCode\":\"BANK456\",\"payeeBankCountry\":\"US\",\"payeeCreationDate\":\"2024-01-15\",\"payerAccountType\":\"savings\",\"platformType\":\"Mobile\",\"sdkVersion\":\"3.0\",\"transactionId\":\"TRANS789\",\"transactionReason\":\"Purchase\",\"transactionSpeed\":\"Fast\",\"userAgent\":\"Chrome\",\"userType\":\"Customer\",\"authenticationResult\":\"Success\",\"authMethodUsed\":\"Biometric\",\"numOfFailedAuth\":0,\"yob\":1985,\"isLocked\":false},\"caseUpdate\":{\"accountId\":\"ACC123\",\"transactionId\":\"TRANS456\"}}", summary = "Full request") }) })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Resultado OK", content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Response XML", value = "<soap:Envelope xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:srv=\"http://www.technisys.net/cmm/services/updateInfoBiocatch/rs/v1.0\"><soap:Header><md:metadata><organizationType><mnemonic></mnemonic></organizationType><sourceTime>20220503205514381-0300</sourceTime><institutionId></institutionId><userId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</userId><channelId>MB</channelId><institutionType></institutionType><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)\n"
							+ "				AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1\n"
							+ "				Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36\n"
							+ "			</terminalId><channelDispatchDate>20220503205514381-0300</channelDispatchDate><address>ip=10.230.241.199;</address><traceNumber>187930897956919592454856256953371298936.173360960790668113791382117073848686434</traceNumber><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><organizationId></organizationId><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</executingOperatorId><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><featureId>ROL@5527</featureId><internals><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName></internals><executingChannel><mnemonic translationError=\"UNDEFINED_TRANSLATION\">MB</mnemonic><originalCodes>originalCode=MB;</originalCodes></executingChannel><locale>ES</locale><md:serviceId>updateInfoBiocatch</md:serviceId><md:serviceVersion>1.0</md:serviceVersion></md:metadata></soap:Header><soap:Body><srv:updateInfoBiocatchResponse><bcStatus>updated</bcStatus></srv:updateInfoBiocatchResponse></soap:Body></soap:Envelope>", summary = "Full Response") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Response JSON", value = "{\"bcStatus\":\"updated\"}", summary = "Full Response") }) }) })
	@CustomEndpointProcessing(processingFlow = "jsonXmlFlow")
	public DeferredResult<ResponseEntity<String>> postsupdateInfoBiocatch(
			@Parameter(hidden = true) @RequestHeader Map<String, Object> headers, HttpServletRequest request,
			@org.springframework.web.bind.annotation.RequestBody String body) {
		MessageRequest<String> messageRequest = new MessageRequest<>();
		DirectorUtilities.completeRequestMap(request, headers);
		messageRequest.setHeaders(headers);
		messageRequest.setRequestObject(body);
		messageRequest.putHeader("service_id", "updateInfoBiocatch");
		messageRequest.putHeader("service_version", "1.0");
		return this.executeService(this.updateInfoBiocatch, messageRequest);
	}

	@PostMapping(path = "/v1/massiveSelectTrustedDeviceAndPills", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Ejemplo XML", value = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:e=\"http://www.technisys.net/cmm/services/errors/v1.0\" xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><SOAP-ENV:Header><md:metadata><serviceId>massiveSelectTrustedDeviceAndPills</serviceId><serviceVersion>1.0</serviceVersion><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><channelId>MB</channelId><targetChannel><mnemonic>MB</mnemonic></targetChannel><organizationType><mnemonic /></organizationType><executingChannel><mnemonic>MB</mnemonic></executingChannel><address>ip=10.230.241.199;</address><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true</executingOperatorId><locale>ES</locale><institutionType /><template><templateId /></template><channelDispatchDate>20220503205514381-0300</channelDispatchDate><msgTypeId /><workflowModule /><featureId>ROL@5527</featureId><sourceDate /><workflowId /><businessDate /><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1 Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36</terminalId><sourceTime /><branchId /><userId /><executingScope /><customProperties><operatorId type=\"java.lang.String\" /><businessDate type=\"java.util.Date\" /><serviceContext type=\"java.lang.String\" /><llaveSimetrica type=\"java.lang.String\" /><funcCode type=\"java.lang.String\" /><funcType type=\"java.lang.String\">OVA</funcType><terminal type=\"java.lang.String\" /><portal type=\"java.lang.String\" /></customProperties><llaveSimetrica type=\"java.lang.String\" /><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><parityCurrencyId /><localCurrencyId /><institutionId /><localCountryId /><bankId /><ip>10.230.241.199</ip><parityQuotationNemonic /><paginationInfo /><internals><auditCore>true</auditCore><autoPaginationEnabled>false</autoPaginationEnabled><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName><translate>true</translate><serviceRequestTimestamp /></internals></md:metadata></SOAP-ENV:Header><SOAP-ENV:Body><NS3:massiveSelectTrustedDeviceAndPillsRequest xmlns:NS3=\"http://www.technisys.net/cmm/services/massiveSelectTrustedDeviceAndPills/rq/v0.0\"><customer><customerId>1#CID12345</customerId><additionalCustomerType><customerTypeId>CTID24680</customerTypeId><officialId>OID13579</officialId></additionalCustomerType></customer></NS3:massiveSelectTrustedDeviceAndPillsRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>",
							summary = "Full request") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Ejemplo JSON", value = "{\"customer\":{\"customerId\":\"1#CID12345\",\"additionalCustomerType\":{\"customerTypeId\":\"CTID24680\",\"officialId\":\"OID13579\"}}}", summary = "Full request") }) })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Resultado OK", content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Response XML", value = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:srv=\"http://www.technisys.net/cmm/services/massiveSelectTrustedDeviceAndPills/rs/v1.0\"><soap:Header><md:metadata><md:serviceId>massiveSelectTrustedDeviceAndPills</md:serviceId><md:serviceVersion>1.0</md:serviceVersion></md:metadata></soap:Header><soap:Body><srv:massiveSelectTrustedDeviceAndPillsResponse><mfa><id>18994de4ce0477600f25fa96c51ca39b8</id></mfa><fraudMonitoring><id>18994de4ce0477600f25fa96c51ca39b8</id></fraudMonitoring><biometric><id>18994de4ce0477600f25fa96c51ca39b8</id></biometric><device><isAllDeviceBlocked>false</isAllDeviceBlocked><activeDevices>true</activeDevices><isRegistered>true</isRegistered><showInformationPills>false</showInformationPills><showInformationPillsBanner>false</showInformationPillsBanner></device><isAccountRegister>true</isAccountRegister><customer><birthDate>2001</birthDate><constitutionDate>2021</constitutionDate><incomeDate>20200101</incomeDate></customer><establishment><companyName>Establecimiento de Prueb</companyName></establishment></srv:massiveSelectTrustedDeviceAndPillsResponse></soap:Body></soap:Envelope>",
							summary = "Full Response") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Response JSON", value = "{\"mfa\":{\"id\":\"18994de4ce0477600f25fa96c51ca39b8\"},\"fraudMonitoring\":{\"id\":\"18994de4ce0477600f25fa96c51ca39b8\"},\"biometric\":{\"id\":\"18994de4ce0477600f25fa96c51ca39b8\"},\"device\":{\"isAllDeviceBlocked\":false,\"activeDevices\":true,\"isRegistered\":true,\"showInformationPills\":false,\"showInformationPillsBanner\":false},\"isAccountRegister\":true,\"customer\":{\"birthDate\":2001,\"constitutionDate\":2021,\"incomeDate\":20200101},\"establishment\":{\"companyName\":\"Establecimiento de Prueba\"}}", 
							summary = "Full Response") }) }) })
	@CustomEndpointProcessing(processingFlow = "jsonXmlFlow")
	public DeferredResult<ResponseEntity<String>> postgetScoreForLogin(
			@Parameter(hidden = true) @RequestHeader Map<String, Object> headers, HttpServletRequest request,
			@org.springframework.web.bind.annotation.RequestBody String body) {
		MessageRequest<String> messageRequest = new MessageRequest<>();
		DirectorUtilities.completeRequestMap(request, headers);
		messageRequest.setHeaders(headers);
		messageRequest.setRequestObject(body);
		messageRequest.putHeader("service_id", "massiveSelectTrustedDeviceAndPills");
		messageRequest.putHeader("service_version", "1.0");
		return this.executeService(this.massiveSelectTrustedDeviceAndPills, messageRequest);
	}

	@PostMapping(path = "/v1/transactionalGetScore", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Ejemplo XML", value = "<SOAP-ENV:Envelope\n"
							+ "	xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\"\n"
							+ "	xmlns:e=\"http://www.technisys.net/cmm/services/errors/v1.0\"\n"
							+ "	xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\"\n"
							+ "	xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\n"
							+ "	xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><SOAP-ENV:Header><md:metadata><serviceId>transactionalGetScore</serviceId><serviceVersion>1.0</serviceVersion><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><channelId>MB</channelId><targetChannel><mnemonic>MB</mnemonic></targetChannel><organizationType><mnemonic /></organizationType><executingChannel><mnemonic>MB</mnemonic></executingChannel><address>ip=10.230.241.199;</address><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</executingOperatorId><locale>ES</locale><institutionType /><template><templateId /></template><channelDispatchDate>20220503205514381-0300</channelDispatchDate><msgTypeId /><workflowModule /><featureId>ROL@5527</featureId><sourceDate /><workflowId /><businessDate /><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)\n"
							+ "				AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1\n"
							+ "				Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36\n"
							+ "			</terminalId><sourceTime /><branchId /><userId /><executingScope /><customProperties><operatorId type=\"java.lang.String\" /><businessDate type=\"java.util.Date\" /><serviceContext type=\"java.lang.String\" /><llaveSimetrica type=\"java.lang.String\" /><funcCode type=\"java.lang.String\" /><funcType type=\"java.lang.String\">OVA</funcType><terminal type=\"java.lang.String\" /><portal type=\"java.lang.String\" /></customProperties><llaveSimetrica type=\"java.lang.String\" /><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><parityCurrencyId /><localCurrencyId /><institutionId /><localCountryId /><bankId /><ip>10.230.241.199</ip><parityQuotationNemonic /><paginationInfo /><internals><auditCore>true</auditCore><autoPaginationEnabled>false</autoPaginationEnabled><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName><translate>true</translate><serviceRequestTimestamp /></internals></md:metadata></SOAP-ENV:Header><SOAP-ENV:Body><NS3:transactionalGetScoreRequest\n"
							+ "			xmlns:NS3=\"http://www.technisys.net/cmm/services/transactionalGetScore/rq/v0.0\"><customer><customerId>1#CID12345</customerId><additionalCustomerType><customerTypeId>CTID24680</customerTypeId><officialId>OID13579</officialId></additionalCustomerType></customer><callback><url>http://example.com/callback</url></callback><transaction><code>TRX98765</code></transaction><biometricAnalysis><brand>BiometricBrand</brand><customerSessionId>Session123</customerSessionId><accountId>Acc123</accountId><accountOpenDate>2022-01-01</accountOpenDate><action>Login</action><activityAmount>100.00</activityAmount><activityType>Login</activityType><biometricLogin>true</biometricLogin><payeeValue>Payee123</payeeValue><payerValue>Payer456</payerValue><solution>SolutionX</solution><uuid>UUID789</uuid><yearOfBirth>1980</yearOfBirth><accountBalance>5000.00</accountBalance><activityAmountTotal>1000.00</activityAmountTotal><activityName>ActivityNameXYZ</activityName><batchFileCreationDate>2023-05-20</batchFileCreationDate><batchFileId>BatchFile123</batchFileId><batchFilePayeesNumber>50</batchFilePayeesNumber><customerApplicationVersion>2.1.0</customerApplicationVersion><dateOfCreation>2023-04-15</dateOfCreation><deviceId>Device123</deviceId><deviceModel>ModelX</deviceModel><ip>192.168.1.100</ip><isLoginSuccess>true</isLoginSuccess><lastDeviceRegistrationDate>2023-03-10</lastDeviceRegistrationDate><loginMethod>Fingerprint</loginMethod><membershipId>Member123</membershipId><merchantName>MerchantXYZ</merchantName><onlineAccountOpenDate>2022-06-30</onlineAccountOpenDate><payeeAccountType>Checking</payeeAccountType><payeeBankCode>BankCode123</payeeBankCode><payeeBankCountry>CountryX</payeeBankCountry><payeeCreationDate>2022-07-15</payeeCreationDate><payerAccountType>Savings</payerAccountType><platformType>Mobile</platformType><sdkVersion>3.0.0</sdkVersion><transactionId>Trans123</transactionId><transactionReason>Payment</transactionReason><transactionSpeed>High</transactionSpeed><userAgent>UserAgent123</userAgent><userType>Regular</userType></biometricAnalysis><monetaryTrxAnalysis><customerAcctNumber>9876543210</customerAcctNumber><debitAcctNumber>1234567890</debitAcctNumber><debitCustomerId>DebitCustomer456</debitCustomerId><creditAcctNumber>5432167890</creditAcctNumber><transactionAmount>500.00</transactionAmount><transactionType>Purchase</transactionType><accessChannel>Online</accessChannel><deviceId>Device789</deviceId><debitAcctBankId>BankId456</debitAcctBankId><debitAcctSortCode>SortCode789</debitAcctSortCode><debitAmount>200.00</debitAmount><debitCurrencyCode>USD</debitCurrencyCode><debitDate>2023-10-25</debitDate><debitName>DebitNameXYZ</debitName><debitMessage>DebitMessage123</debitMessage><creditCustomerId>CreditCustomer789</creditCustomerId><creditAcctBankId>BankId789</creditAcctBankId><creditAcctSortCode>SortCode123</creditAcctSortCode><creditAmount>300.00</creditAmount><creditCurrencyCode>EUR</creditCurrencyCode><creditDate>2023-11-05</creditDate><creditName>CreditNameABC</creditName><creditMessage>CreditMessage456</creditMessage></monetaryTrxAnalysis></NS3:transactionalGetScoreRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>", summary = "Full request") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Ejemplo JSON", value = "{\"customer\":{\"customerId\":\"Cust12345\",\"additionalCustomerType\":{\"customerTypeId\":\"CustType678\",\"officialId\":\"Official789\"}},\"callback\":{\"url\":\"http://example.com/callback\"},\"transaction\":{\"code\":\"TransCode987\"},\"biometricAnalysis\":{\"brand\":\"BiometricBrandXYZ\",\"customerSessionId\":\"SessionID123\",\"accountId\":\"AccID456\",\"accountOpenDate\":\"2023-01-15\",\"action\":\"Login\",\"activityAmount\":100.50,\"activityType\":\"Login\",\"biometricLogin\":true,\"payeeValue\":\"Payee123\",\"payerValue\":\"Payer456\",\"solution\":\"SolutionX\",\"uuid\":\"UUID789\",\"yearOfBirth\":1985,\"accountBalance\":5000.00,\"activityAmountTotal\":1500.75,\"activityName\":\"ActivityXYZ\",\"batchFileCreationDate\":\"2023-03-20\",\"batchFileId\":\"BatchFileID789\",\"batchFilePayeesNumber\":50,\"customerApplicationVersion\":\"2.0.1\",\"dateOfCreation\":\"2023-02-10\",\"deviceId\":\"DeviceID456\",\"deviceModel\":\"ModelXYZ\",\"ip\":\"192.168.0.1\",\"isLoginSuccess\":true,\"lastDeviceRegistrationDate\":\"2023-01-01\",\"loginMethod\":\"Fingerprint\",\"membershipId\":\"Membership123\",\"merchantName\":\"MerchantXYZ\",\"onlineAccountOpenDate\":\"2023-02-28\",\"payeeAccountType\":\"Checking\",\"payeeBankCode\":\"BankCode789\",\"payeeBankCountry\":\"CountryX\",\"payeeCreationDate\":\"2023-03-01\",\"payerAccountType\":\"Savings\",\"platformType\":\"Mobile\",\"sdkVersion\":\"3.1.0\",\"transactionId\":\"TransID456\",\"transactionReason\":\"Payment\",\"transactionSpeed\":\"High\",\"userAgent\":\"UserAgentXYZ\",\"userType\":\"Regular\"},\"monetaryTrxAnalysis\":{\"customerAcctNumber\":\"CustAcct123\",\"debitAcctNumber\":\"DebitAcct456\",\"debitCustomerId\":\"DebitCustID789\",\"creditAcctNumber\":\"CreditAcct123\",\"transactionAmount\":250.75,\"transactionType\":\"Purchase\",\"accessChannel\":\"Online\",\"deviceId\":\"DeviceID789\",\"debitAcctBankId\":\"BankID456\",\"debitAcctSortCode\":\"SortCode789\",\"debitAmount\":100.25,\"debitCurrencyCode\":\"USD\",\"debitDate\":\"2023-05-10\",\"debitName\":\"DebitNameXYZ\",\"debitMessage\":\"DebitMessage123\",\"creditCustomerId\":\"CreditCustID789\",\"creditAcctBankId\":\"CreditBankID123\",\"creditAcctSortCode\":\"CreditSortCode456\",\"creditAmount\":150.50,\"creditCurrencyCode\":\"EUR\",\"creditDate\":\"2023-05-15\",\"creditName\":\"CreditNameABC\",\"creditMessage\":\"CreditMessage789\"}}", summary = "Full request") }) })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Resultado OK", content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Response XML", value = "<soap:Envelope xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:srv=\"http://www.technisys.net/cmm/services/transactionalGetScore/rs/v1.0\"><soap:Header><md:metadata><organizationType><mnemonic></mnemonic></organizationType><sourceTime>20220503205514381-0300</sourceTime><institutionId></institutionId><userId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</userId><channelId>MB</channelId><institutionType></institutionType><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)\n"
							+ "				AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1\n"
							+ "				Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36\n"
							+ "			</terminalId><channelDispatchDate>20220503205514381-0300</channelDispatchDate><address>ip=10.230.241.199;</address><traceNumber>223011006134882667218446037032639135539.225211439998744960553437373183437323593</traceNumber><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><organizationId></organizationId><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</executingOperatorId><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><featureId>ROL@5527</featureId><internals><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName></internals><executingChannel><mnemonic translationError=\"UNDEFINED_TRANSLATION\">MB</mnemonic><originalCodes>originalCode=MB;</originalCodes></executingChannel><locale>ES</locale><md:serviceId>transactionalGetScore</md:serviceId><md:serviceVersion>1.0</md:serviceVersion></md:metadata></soap:Header><soap:Body><srv:transactionalGetScoreResponse><fraudMonitoring><id>12345</id></fraudMonitoring><customer><ageRange>0-25</ageRange></customer></srv:transactionalGetScoreResponse></soap:Body></soap:Envelope>", summary = "Full Response") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Response JSON", value = "{\"fraudMonitoring\":{\"id\":12345},\"customer\":{\"ageRange\":\"0-25\"}}", summary = "Full Response") }) }) })
	@CustomEndpointProcessing(processingFlow = "jsonXmlFlow")
	public DeferredResult<ResponseEntity<String>> posttransactionalGetScore(
			@Parameter(hidden = true) @RequestHeader Map<String, Object> headers, HttpServletRequest request,
			@org.springframework.web.bind.annotation.RequestBody String body) {
		MessageRequest<String> messageRequest = new MessageRequest<>();
		DirectorUtilities.completeRequestMap(request, headers);
		messageRequest.setHeaders(headers);
		messageRequest.setRequestObject(body);
		messageRequest.putHeader("service_id", "transactionalGetScore");
		messageRequest.putHeader("service_version", "1.0");
		return this.executeService(this.transactionalGetScore, messageRequest);
	}

	@PostMapping(path = "/v1/nonTransactionalGetScore", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Ejemplo XML", value = "<SOAP-ENV:Envelope\n"
							+ "	xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\"\n"
							+ "	xmlns:e=\"http://www.technisys.net/cmm/services/errors/v1.0\"\n"
							+ "	xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\"\n"
							+ "	xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\n"
							+ "	xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><SOAP-ENV:Header><md:metadata><serviceId>transactionalGetScore</serviceId><serviceVersion>1.0</serviceVersion><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><channelId>MB</channelId><targetChannel><mnemonic>MB</mnemonic></targetChannel><organizationType><mnemonic /></organizationType><executingChannel><mnemonic>MB</mnemonic></executingChannel><address>ip=10.230.241.199;</address><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</executingOperatorId><locale>ES</locale><institutionType /><template><templateId /></template><channelDispatchDate>20220503205514381-0300</channelDispatchDate><msgTypeId /><workflowModule /><featureId>ROL@5527</featureId><sourceDate /><workflowId /><businessDate /><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)\n"
							+ "				AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1\n"
							+ "				Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36\n"
							+ "			</terminalId><sourceTime /><branchId /><userId /><executingScope /><customProperties><operatorId type=\"java.lang.String\" /><businessDate type=\"java.util.Date\" /><serviceContext type=\"java.lang.String\" /><llaveSimetrica type=\"java.lang.String\" /><funcCode type=\"java.lang.String\" /><funcType type=\"java.lang.String\">OVA</funcType><terminal type=\"java.lang.String\" /><portal type=\"java.lang.String\" /></customProperties><llaveSimetrica type=\"java.lang.String\" /><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><parityCurrencyId /><localCurrencyId /><institutionId /><localCountryId /><bankId /><ip>10.230.241.199</ip><parityQuotationNemonic /><paginationInfo /><internals><auditCore>true</auditCore><autoPaginationEnabled>false</autoPaginationEnabled><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName><translate>true</translate><serviceRequestTimestamp /></internals></md:metadata></SOAP-ENV:Header><SOAP-ENV:Body><NS3:transactionalGetScoreRequest\n"
							+ "			xmlns:NS3=\"http://www.technisys.net/cmm/services/transactionalGetScore/rq/v0.0\"><customer><customerId>1#CID12345</customerId><additionalCustomerType><customerTypeId>CTID24680</customerTypeId><officialId>OID13579</officialId></additionalCustomerType></customer><callback><url>http://example.com/callback</url></callback><transaction><code>TRX98765</code></transaction><biometricAnalysis><brand>BiometricBrand</brand><customerSessionId>Session123</customerSessionId><accountId>Acc123</accountId><accountOpenDate>2022-01-01</accountOpenDate><action>Login</action><activityAmount>100.00</activityAmount><activityType>Login</activityType><biometricLogin>true</biometricLogin><payeeValue>Payee123</payeeValue><payerValue>Payer456</payerValue><solution>SolutionX</solution><uuid>UUID789</uuid><yearOfBirth>1980</yearOfBirth><accountBalance>5000.00</accountBalance><activityAmountTotal>1000.00</activityAmountTotal><activityName>ActivityNameXYZ</activityName><batchFileCreationDate>2023-05-20</batchFileCreationDate><batchFileId>BatchFile123</batchFileId><batchFilePayeesNumber>50</batchFilePayeesNumber><customerApplicationVersion>2.1.0</customerApplicationVersion><dateOfCreation>2023-04-15</dateOfCreation><deviceId>Device123</deviceId><deviceModel>ModelX</deviceModel><ip>192.168.1.100</ip><isLoginSuccess>true</isLoginSuccess><lastDeviceRegistrationDate>2023-03-10</lastDeviceRegistrationDate><loginMethod>Fingerprint</loginMethod><membershipId>Member123</membershipId><merchantName>MerchantXYZ</merchantName><onlineAccountOpenDate>2022-06-30</onlineAccountOpenDate><payeeAccountType>Checking</payeeAccountType><payeeBankCode>BankCode123</payeeBankCode><payeeBankCountry>CountryX</payeeBankCountry><payeeCreationDate>2022-07-15</payeeCreationDate><payerAccountType>Savings</payerAccountType><platformType>Mobile</platformType><sdkVersion>3.0.0</sdkVersion><transactionId>Trans123</transactionId><transactionReason>Payment</transactionReason><transactionSpeed>High</transactionSpeed><userAgent>UserAgent123</userAgent><userType>Regular</userType></biometricAnalysis></NS3:transactionalGetScoreRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>", summary = "Full request") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Ejemplo JSON", value = "{\"customer\":{\"customerId\":\"Cust12345\",\"additionalCustomerType\":{\"customerTypeId\":\"CustType678\",\"officialId\":\"Official789\"}},\"callback\":{\"url\":\"http://example.com/callback\"},\"transaction\":{\"code\":\"TransCode987\"},\"biometricAnalysis\":{\"brand\":\"BiometricBrandXYZ\",\"customerSessionId\":\"SessionID123\",\"accountId\":\"AccID456\",\"accountOpenDate\":\"2023-01-15\",\"action\":\"Login\",\"activityAmount\":100.50,\"activityType\":\"Login\",\"biometricLogin\":true,\"payeeValue\":\"Payee123\",\"payerValue\":\"Payer456\",\"solution\":\"SolutionX\",\"uuid\":\"UUID789\",\"yearOfBirth\":1985,\"accountBalance\":5000.00,\"activityAmountTotal\":1500.75,\"activityName\":\"ActivityXYZ\",\"batchFileCreationDate\":\"2023-03-20\",\"batchFileId\":\"BatchFileID789\",\"batchFilePayeesNumber\":50,\"customerApplicationVersion\":\"2.0.1\",\"dateOfCreation\":\"2023-02-10\",\"deviceId\":\"DeviceID456\",\"deviceModel\":\"ModelXYZ\",\"ip\":\"192.168.0.1\",\"isLoginSuccess\":true,\"lastDeviceRegistrationDate\":\"2023-01-01\",\"loginMethod\":\"Fingerprint\",\"membershipId\":\"Membership123\",\"merchantName\":\"MerchantXYZ\",\"onlineAccountOpenDate\":\"2023-02-28\",\"payeeAccountType\":\"Checking\",\"payeeBankCode\":\"BankCode789\",\"payeeBankCountry\":\"CountryX\",\"payeeCreationDate\":\"2023-03-01\",\"payerAccountType\":\"Savings\",\"platformType\":\"Mobile\",\"sdkVersion\":\"3.1.0\",\"transactionId\":\"TransID456\",\"transactionReason\":\"Payment\",\"transactionSpeed\":\"High\",\"userAgent\":\"UserAgentXYZ\",\"userType\":\"Regular\"}}", summary = "Full request") }) })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Resultado OK", content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Response XML", value = "<soap:Envelope xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:srv=\"http://www.technisys.net/cmm/services/nonTransactionalGetScore/rs/v1.0\"><soap:Header><md:metadata><organizationType><mnemonic></mnemonic></organizationType><sourceTime>20220503205514381-0300</sourceTime><institutionId></institutionId><userId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</userId><channelId>MB</channelId><institutionType></institutionType><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)\n"
							+ "				AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1\n"
							+ "				Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36\n"
							+ "			</terminalId><channelDispatchDate>20220503205514381-0300</channelDispatchDate><address>ip=10.230.241.199;</address><traceNumber>321688235451013891356565159695310978230.67037577010749173753325282538381571223</traceNumber><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><organizationId></organizationId><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</executingOperatorId><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><featureId>ROL@5527</featureId><internals><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName></internals><executingChannel><mnemonic translationError=\"UNDEFINED_TRANSLATION\">MB</mnemonic><originalCodes>originalCode=MB;</originalCodes></executingChannel><locale>ES</locale><md:serviceId>transactionalGetScore</md:serviceId><md:serviceVersion>1.0</md:serviceVersion></md:metadata></soap:Header><soap:Body><srv:nonTransactionalGetScoreResponse><customer><ageRange>0-25</ageRange></customer></srv:nonTransactionalGetScoreResponse></soap:Body></soap:Envelope>", summary = "Full Response") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Response JSON", value = "{\"customer\":{\"ageRange\":\"0-25\"}}", summary = "Full Response") }) }) })
	@CustomEndpointProcessing(processingFlow = "jsonXmlFlow")
	public DeferredResult<ResponseEntity<String>> postnonTransactionalGetScore(
			@Parameter(hidden = true) @RequestHeader Map<String, Object> headers, HttpServletRequest request,
			@org.springframework.web.bind.annotation.RequestBody String body) {
		MessageRequest<String> messageRequest = new MessageRequest<>();
		DirectorUtilities.completeRequestMap(request, headers);
		messageRequest.setHeaders(headers);
		messageRequest.setRequestObject(body);
		messageRequest.putHeader("service_id", "nonTransactionalGetScore");
		messageRequest.putHeader("service_version", "1.0");
		return this.executeService(this.nonTransactionalGetScore, messageRequest);
	}

	@PostMapping(path = "/v1/massiveSelectTrustedDevicesListing", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Ejemplo XML", value = "<SOAP-ENV:Envelope\n"
							+ "	xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\"\n"
							+ "	xmlns:e=\"http://www.technisys.net/cmm/services/errors/v1.0\"\n"
							+ "	xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\"\n"
							+ "	xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\n"
							+ "	xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><SOAP-ENV:Header><md:metadata><serviceId>massiveSelectTrustedDevicesListing</serviceId><serviceVersion>1.0</serviceVersion><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><channelId>MB</channelId><targetChannel><mnemonic>MB</mnemonic></targetChannel><organizationType><mnemonic /></organizationType><executingChannel><mnemonic>MB</mnemonic></executingChannel><address>ip=10.230.241.199;</address><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</executingOperatorId><locale>ES</locale><institutionType /><template><templateId /></template><channelDispatchDate>20220503205514381-0300</channelDispatchDate><msgTypeId /><workflowModule /><featureId>ROL@5527</featureId><sourceDate /><workflowId /><businessDate /><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)\n"
							+ "				AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1\n"
							+ "				Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36\n"
							+ "			</terminalId><sourceTime /><branchId /><userId /><executingScope /><customProperties><operatorId type=\"java.lang.String\" /><businessDate type=\"java.util.Date\" /><serviceContext type=\"java.lang.String\" /><llaveSimetrica type=\"java.lang.String\" /><funcCode type=\"java.lang.String\" /><funcType type=\"java.lang.String\">OVA</funcType><terminal type=\"java.lang.String\" /><portal type=\"java.lang.String\" /></customProperties><llaveSimetrica type=\"java.lang.String\" /><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><parityCurrencyId /><localCurrencyId /><institutionId /><localCountryId /><bankId /><ip>10.230.241.199</ip><parityQuotationNemonic /><paginationInfo /><internals><auditCore>true</auditCore><autoPaginationEnabled>false</autoPaginationEnabled><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName><translate>true</translate><serviceRequestTimestamp /></internals></md:metadata></SOAP-ENV:Header><SOAP-ENV:Body><NS3:massiveSelectTrustedDevicesListingRequest\n"
							+ "			xmlns:NS3=\"http://www.technisys.net/cmm/services/massiveSelectTrustedDevicesListing/rq/v0.0\"><mfa><id>id</id></mfa></NS3:massiveSelectTrustedDevicesListingRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>", summary = "Full request") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Ejemplo JSON", value = "{\"mfa\":{\"id\":\"id\"}}", summary = "Full request") }) })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Resultado OK", content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Response XML", value = "<soap:Envelope xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:srv=\"http://www.technisys.net/cmm/services/massiveSelectTrustedDevicesListing/rs/v1.0\"><soap:Header><md:metadata><organizationType><mnemonic></mnemonic></organizationType><sourceTime>20220503205514381-0300</sourceTime><institutionId></institutionId><userId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</userId><channelId>MB</channelId><institutionType></institutionType><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)\n"
							+ "				AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1\n"
							+ "				Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36\n"
							+ "			</terminalId><channelDispatchDate>20220503205514381-0300</channelDispatchDate><address>ip=10.230.241.199;</address><traceNumber>80458223349608265057883881251709836526.301790838478724638458402410681741626178</traceNumber><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><organizationId></organizationId><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</executingOperatorId><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><featureId>ROL@5527</featureId><internals><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName></internals><executingChannel><mnemonic translationError=\"UNDEFINED_TRANSLATION\">MB</mnemonic><originalCodes>originalCode=MB;</originalCodes></executingChannel><locale>ES</locale><md:serviceId>massiveSelectTrustedDevicesListing</md:serviceId><md:serviceVersion>1.0</md:serviceVersion></md:metadata></soap:Header><soap:Body><srv:massiveSelectTrustedDevicesListingResponse><collection name=\"devices\"><device><name>Samsung Galaxy S21 Ultra</name><id>5sf84as1-02gd-adcs-7632-afdc65aebac8</id><platform>Android</platform><status>ACTIVE</status><creationDate>1654604363508</creationDate><lastAccessDate>1654604363488</lastAccessDate><expirationDate>1654604663488</expirationDate></device><device><name>Xiaomi Redmi Note 10</name><id>550e8400-e29b-41d4-a716-446655440000</id><platform>Android</platform><status>BLOCK</status><creationDate>1654604363508</creationDate><lastAccessDate>1654604363488</lastAccessDate><expirationDate>1654604663488</expirationDate></device></collection></srv:massiveSelectTrustedDevicesListingResponse></soap:Body></soap:Envelope>", summary = "Full Response") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Response JSON", value = "{\"collection\":{\"@name\":\"devices\",\"device\":[{\"lastAccessDate\":\"1654604363488\",\"name\":\"Samsung Galaxy S21 Ultra\",\"id\":\"5sf84as1-02gd-adcs-7632-afdc65aebac8\",\"creationDate\":\"1654604363508\",\"platform\":\"Android\",\"status\":\"ACTIVE\",\"expirationDate\":\"1654604663488\"},{\"lastAccessDate\":\"1654604363488\",\"name\":\"Xiaomi Redmi Note 10\",\"id\":\"550e8400-e29b-41d4-a716-446655440000\",\"creationDate\":\"1654604363508\",\"platform\":\"Android\",\"status\":\"BLOCK\",\"expirationDate\":\"1654604663488\"}]}}", summary = "Full Response") }) }) })
	@CustomEndpointProcessing(processingFlow = "jsonXmlFlow")
	public DeferredResult<ResponseEntity<String>> postmassiveSelectTrustedDevicesListing(
			@Parameter(hidden = true) @RequestHeader Map<String, Object> headers, HttpServletRequest request,
			@org.springframework.web.bind.annotation.RequestBody String body) {
		MessageRequest<String> messageRequest = new MessageRequest<>();
		DirectorUtilities.completeRequestMap(request, headers);
		messageRequest.setHeaders(headers);
		messageRequest.setRequestObject(body);
		messageRequest.putHeader("service_id", "massiveSelectTrustedDevicesListing");
		messageRequest.putHeader("service_version", "1.0");
		return this.executeService(this.massiveSelectTrustedDevicesListing, messageRequest);
	}

	@PostMapping(path = "/v1/deleteDeviceManagement", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Ejemplo XML", value = "<SOAP-ENV:Envelope\n"
							+ "	xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\"\n"
							+ "	xmlns:e=\"http://www.technisys.net/cmm/services/errors/v1.0\"\n"
							+ "	xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\"\n"
							+ "	xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\n"
							+ "	xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><SOAP-ENV:Header><md:metadata><serviceId>deleteDeviceManagement</serviceId><serviceVersion>1.0</serviceVersion><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><channelId>MB</channelId><targetChannel><mnemonic>MB</mnemonic></targetChannel><organizationType><mnemonic /></organizationType><executingChannel><mnemonic>MB</mnemonic></executingChannel><address>ip=10.230.241.199;</address><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</executingOperatorId><locale>ES</locale><institutionType /><template><templateId /></template><channelDispatchDate>20220503205514381-0300</channelDispatchDate><msgTypeId /><workflowModule /><featureId>ROL@5527</featureId><sourceDate /><workflowId /><businessDate /><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)\n"
							+ "				AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1\n"
							+ "				Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36\n"
							+ "			</terminalId><sourceTime /><branchId /><userId /><executingScope /><customProperties><operatorId type=\"java.lang.String\" /><businessDate type=\"java.util.Date\" /><serviceContext type=\"java.lang.String\" /><llaveSimetrica type=\"java.lang.String\" /><funcCode type=\"java.lang.String\" /><funcType type=\"java.lang.String\">OVA</funcType><terminal type=\"java.lang.String\" /><portal type=\"java.lang.String\" /></customProperties><llaveSimetrica type=\"java.lang.String\" /><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><parityCurrencyId /><localCurrencyId /><institutionId /><localCountryId /><bankId /><ip>10.230.241.199</ip><parityQuotationNemonic /><paginationInfo /><internals><auditCore>true</auditCore><autoPaginationEnabled>false</autoPaginationEnabled><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName><translate>true</translate><serviceRequestTimestamp /></internals></md:metadata></SOAP-ENV:Header><SOAP-ENV:Body><NS3:deleteDeviceManagementRequest\n"
							+ "			xmlns:NS3=\"http://www.technisys.net/cmm/services/deleteDeviceManagement/rq/v0.0\"><mfa><id>mfa123</id></mfa><device><id>device456</id></device><customer><customerId>cust789</customerId><profile>profileXYZ</profile><additionalCustomerType><customerTypeId>custType101</customerTypeId><officialId>official202</officialId></additionalCustomerType></customer><establishment><ruc>ruc12345</ruc></establishment><transaction><code>trans789</code></transaction><variables><deviceName>deviceXYZ</deviceName></variables></NS3:deleteDeviceManagementRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>", summary = "Full request") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Ejemplo JSON", value = "{\"mfa\":{\"id\":\"mfa123\"},\"device\":{\"id\":\"device456\"},\"customer\":{\"customerId\":\"cust789\",\"profile\":\"profileXYZ\",\"additionalCustomerType\":{\"customerTypeId\":\"custType101\",\"officialId\":\"official202\"}},\"establishment\":{\"ruc\":\"ruc12345\"},\"transaction\":{\"code\":\"trans789\"},\"variables\":{\"deviceName\":\"deviceXYZ\"}}", summary = "Full request") }) })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Resultado OK", content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Response XML", value = "<soap:Envelope xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:srv=\"http://www.technisys.net/cmm/services/deleteDeviceManagement/rs/v1.0\"><soap:Header><md:metadata><organizationType><mnemonic></mnemonic></organizationType><sourceTime>20220503205514381-0300</sourceTime><institutionId></institutionId><userId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</userId><channelId>MB</channelId><institutionType></institutionType><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)\n"
							+ "				AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1\n"
							+ "				Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36\n"
							+ "			</terminalId><channelDispatchDate>20220503205514381-0300</channelDispatchDate><address>ip=10.230.241.199;</address><traceNumber>18872876222538971158586696472512755196.89017974738779144496129243616284771658</traceNumber><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><organizationId></organizationId><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</executingOperatorId><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><featureId>ROL@5527</featureId><internals><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName></internals><executingChannel><mnemonic translationError=\"UNDEFINED_TRANSLATION\">MB</mnemonic><originalCodes>originalCode=MB;</originalCodes></executingChannel><locale>ES</locale><md:serviceId>deleteDeviceManagement</md:serviceId><md:serviceVersion>1.0</md:serviceVersion></md:metadata></soap:Header><soap:Body><srv:deleteDeviceManagementResponse/></soap:Body></soap:Envelope>", summary = "Full Response") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Response JSON", value = "{}", summary = "Full Response") }) }) })
	@CustomEndpointProcessing(processingFlow = "jsonXmlFlow")
	public DeferredResult<ResponseEntity<String>> postdeleteDeviceManagement(
			@Parameter(hidden = true) @RequestHeader Map<String, Object> headers, HttpServletRequest request,
			@org.springframework.web.bind.annotation.RequestBody String body) {
		MessageRequest<String> messageRequest = new MessageRequest<>();
		DirectorUtilities.completeRequestMap(request, headers);
		messageRequest.setHeaders(headers);
		messageRequest.setRequestObject(body);
		messageRequest.putHeader("service_id", "deleteDeviceManagement");
		messageRequest.putHeader("service_version", "1.0");
		return this.executeService(this.deleteDeviceManagement, messageRequest);
	}

	@PostMapping(path = "/v1/processCustomerDeviceEnrollment", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Ejemplo XML", value = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:e=\"http://www.technisys.net/cmm/services/errors/v1.0\" xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><SOAP-ENV:Header><md:metadata><serviceId>processCustomerDeviceEnrollment</serviceId><serviceVersion>1.0</serviceVersion><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><channelId>MB</channelId><targetChannel><mnemonic>MB</mnemonic></targetChannel><organizationType><mnemonic/></organizationType><executingChannel><mnemonic>MB</mnemonic></executingChannel><address>ip=10.230.241.199;</address><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true</executingOperatorId><locale>ES</locale><institutionType/><template><templateId/></template><channelDispatchDate>20220503205514381-0300</channelDispatchDate><msgTypeId/><workflowModule/><featureId>ROL@5527</featureId><sourceDate/><workflowId/><businessDate/><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36</terminalId><sourceTime/><branchId/><userId/><executingScope/><customProperties><operatorId type=\"java.lang.String\"/><businessDate type=\"java.util.Date\"/><serviceContext type=\"java.lang.String\"/><llaveSimetrica type=\"java.lang.String\"/><funcCode type=\"java.lang.String\"/><funcType type=\"java.lang.String\">OVA</funcType><terminal type=\"java.lang.String\"/><portal type=\"java.lang.String\"/></customProperties><llaveSimetrica type=\"java.lang.String\"/><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><parityCurrencyId/><localCurrencyId/><institutionId/><localCountryId/><bankId/><ip>10.230.241.199</ip><parityQuotationNemonic/><paginationInfo/><internals><auditCore>true</auditCore><autoPaginationEnabled>false</autoPaginationEnabled><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName><translate>true</translate><serviceRequestTimestamp/></internals></md:metadata></SOAP-ENV:Header><SOAP-ENV:Body><NS3:processCustomerDeviceEnrollmentRequest xmlns:NS3=\"http://www.technisys.net/cmm/services/processCustomerDeviceEnrollment/rq/v0.0\"><customer><customerId>1#cust123</customerId><additionalCustomerType><customerTypeId>custType456</customerTypeId><officialId>official789</officialId></additionalCustomerType><faceId>faceId123</faceId><profile><profileType><mnemonic>S</mnemonic></profileType></profile></customer><mfa><id>mfa456</id></mfa><fraudMonitoring><id>fraudId789</id></fraudMonitoring><biometric><id>biometricId123</id></biometric><validation><status>validated</status></validation></NS3:processCustomerDeviceEnrollmentRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>", 
							summary = "Full request") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Ejemplo JSON", value = "{\"customer\":{\"customerId\":\"1#cust123\",\"additionalCustomerType\":{\"customerTypeId\":\"custType456\",\"officialId\":\"official789\"},\"faceId\":\"faceId123\",\"profile\":{\"profileType\":{\"mnemonic\":\"S\"}}},\"mfa\":{\"id\":\"mfa456\"},\"fraudMonitoring\":{\"id\":\"fraudId789\"},\"biometric\":{\"id\":\"biometricId123\"},\"validation\":{\"status\":\"validated\"}}", 
							summary = "Full request") }) })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Resultado OK", content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Response XML", value = "<soap:Envelope xmlns:md=\\\"http://www.technisys.net/cmm/services/metadata/v2.0\\\" xmlns:soap=\\\"http://www.w3.org/2003/05/soap-envelope\\\" xmlns:srv=\\\"http://www.technisys.net/cmm/services/processCustomerDeviceEnrollment/rs/v1.0\\\"><soap:Header><md:metadata><organizationType><mnemonic/></organizationType><sourceTime>20220503205514381-0300</sourceTime><institutionId/><userId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true</userId><channelId>MB</channelId><institutionType/><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0) AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1 Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36</terminalId><channelDispatchDate>20220503205514381-0300</channelDispatchDate><address>ip=10.230.241.199;</address><traceNumber>219565411326123441890478429281182074075.93998270560770359392221521136251392979</traceNumber><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><organizationId/><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true</executingOperatorId><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><featureId>ROL@5527</featureId><internals><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName></internals><executingChannel><mnemonic translationError=\\\"UNDEFINED_TRANSLATION\\\">MB</mnemonic><originalCodes>originalCode=MB;</originalCodes></executingChannel><locale>ES</locale><md:serviceId>processCustomerDeviceEnrollment</md:serviceId><md:serviceVersion>1.0</md:serviceVersion></md:metadata></soap:Header><soap:Body><srv:processCustomerDeviceEnrollmentResponse><mfa><id>114128</id></mfa></srv:processCustomerDeviceEnrollmentResponse></soap:Body></soap:Envelope>", summary = "Full Response") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Response JSON", value = "{\"mfa\":{\"id\":\"114128\"}}", summary = "Full Response") }) }) })
	@CustomEndpointProcessing(processingFlow = "jsonXmlFlow")
	public DeferredResult<ResponseEntity<String>> postprocessCustomerDeviceEnrollment(
			@Parameter(hidden = true) @RequestHeader Map<String, Object> headers, HttpServletRequest request,
			@org.springframework.web.bind.annotation.RequestBody String body) {
		MessageRequest<String> messageRequest = new MessageRequest<>();
		DirectorUtilities.completeRequestMap(request, headers);
		messageRequest.setHeaders(headers);
		messageRequest.setRequestObject(body);
		messageRequest.putHeader("service_id", "processCustomerDeviceEnrollment");
		messageRequest.putHeader("service_version", "1.0");
		return this.executeService(this.processCustomerDeviceEnrollment, messageRequest);
	}

	@PostMapping(path = "/v1/processResultsEvaluationRisk/session/{sessionInput}", consumes = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
					MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Ejemplo XML", value = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:e=\"http://www.technisys.net/cmm/services/errors/v1.0\" xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><SOAP-ENV:Header><md:metadata><serviceId>processResultsEvaluationRisk</serviceId><serviceVersion>1.0</serviceVersion><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><channelId>MB</channelId><targetChannel><mnemonic>MB</mnemonic></targetChannel><organizationType><mnemonic/></organizationType><executingChannel><mnemonic>MB</mnemonic></executingChannel><address>ip=10.230.241.199;</address><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "            </executingOperatorId><locale>ES</locale><institutionType/><template><templateId/></template><channelDispatchDate>20220503205514381-0300</channelDispatchDate><msgTypeId/><workflowModule/><featureId>ROL@5527</featureId><sourceDate/><workflowId/><businessDate/><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)\n"
							+ "                AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1\n"
							+ "                Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36\n"
							+ "            </terminalId><sourceTime/><branchId/><userId/><executingScope/><customProperties><operatorId type=\"java.lang.String\"/><businessDate type=\"java.util.Date\"/><serviceContext type=\"java.lang.String\"/><llaveSimetrica type=\"java.lang.String\"/><funcCode type=\"java.lang.String\"/><funcType type=\"java.lang.String\">OVA</funcType><terminal type=\"java.lang.String\"/><portal type=\"java.lang.String\"/></customProperties><llaveSimetrica type=\"java.lang.String\"/><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><parityCurrencyId/><localCurrencyId/><institutionId/><localCountryId/><bankId/><ip>10.230.241.199</ip><parityQuotationNemonic/><paginationInfo/><internals><auditCore>true</auditCore><autoPaginationEnabled>false</autoPaginationEnabled><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName><translate>true</translate><serviceRequestTimestamp/></internals></md:metadata></SOAP-ENV:Header><SOAP-ENV:Body><NS3:processResultsEvaluationRiskRequest xmlns:NS3=\"http://www.technisys.net/cmm/services/processResultsEvaluationRisk/rq/v1.0\"><estado>HOLD</estado><tipoRefuerzo>PUSH</tipoRefuerzo></NS3:processResultsEvaluationRiskRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>", summary = "Full request") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Ejemplo JSON", value = "{\"estado\": \"HOLD\",\"tipoRefuerzo\": \"PUSH\"}", summary = "Full request") }) })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Resultado OK", content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Response XML", value = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:srv=\"http://www.technisys.net/cmm/services/processResultsEvaluationRisk/rs/v1.0\"><soap:Header><md:metadata><md:serviceId>processResultsEvaluationRisk</md:serviceId><md:serviceVersion>1.0</md:serviceVersion></md:metadata></soap:Header><soap:Body><srv:processResultsEvaluationRiskResponse></srv:processResultsEvaluationRiskResponse></soap:Body></soap:Envelope>", summary = "Full Response") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Response JSON", value = "{}", summary = "Full Response") }) }) })
	@CustomEndpointProcessing(processingFlow = "jsonXmlFlow")
	public DeferredResult<ResponseEntity<String>> postprocessResultsEvaluationRisk(
			@Parameter(hidden = true) @RequestHeader Map<String, Object> headers, HttpServletRequest request,
			@org.springframework.web.bind.annotation.RequestBody String body, @PathVariable String sessionInput) {
		MessageRequest<String> messageRequest = new MessageRequest<>();
		DirectorUtilities.completeRequestMap(request, headers);
		messageRequest.setHeaders(headers);
		messageRequest.setRequestObject(body);
		messageRequest.putHeader("service_id", "processResultsEvaluationRisk");
		messageRequest.putHeader("service_version", "1.0");
		messageRequest.putHeader("session_id", sessionInput);
		return this.executeService(this.processResultsEvaluationRiskService, messageRequest);
	}

	@PostMapping(path = "/v1/processResultsNotificationPush/session/{sessionInput}", consumes = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
					MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Ejemplo XML", value = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:e=\"http://www.technisys.net/cmm/services/errors/v1.0\" xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><SOAP-ENV:Header><md:metadata><serviceId>processResultsNotificationPush</serviceId><serviceVersion>1.0</serviceVersion><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><channelId>MB</channelId><targetChannel><mnemonic>MB</mnemonic></targetChannel><organizationType><mnemonic/></organizationType><executingChannel><mnemonic>MB</mnemonic></executingChannel><address>ip=10.230.241.199;</address><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "            </executingOperatorId><locale>ES</locale><institutionType/><template><templateId/></template><channelDispatchDate>20220503205514381-0300</channelDispatchDate><msgTypeId/><workflowModule/><featureId>ROL@5527</featureId><sourceDate/><workflowId/><businessDate/><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)\n"
							+ "                AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1\n"
							+ "                Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36\n"
							+ "            </terminalId><sourceTime/><branchId/><userId/><executingScope/><customProperties><operatorId type=\"java.lang.String\"/><businessDate type=\"java.util.Date\"/><serviceContext type=\"java.lang.String\"/><llaveSimetrica type=\"java.lang.String\"/><funcCode type=\"java.lang.String\"/><funcType type=\"java.lang.String\">OVA</funcType><terminal type=\"java.lang.String\"/><portal type=\"java.lang.String\"/></customProperties><llaveSimetrica type=\"java.lang.String\"/><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><parityCurrencyId/><localCurrencyId/><institutionId/><localCountryId/><bankId/><ip>10.230.241.199</ip><parityQuotationNemonic/><paginationInfo/><internals><auditCore>true</auditCore><autoPaginationEnabled>false</autoPaginationEnabled><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName><translate>true</translate><serviceRequestTimestamp/></internals></md:metadata></SOAP-ENV:Header><SOAP-ENV:Body><NS3:processResultsNotificationPushRequest xmlns:NS3=\"http://www.technisys.net/cmm/services/processResultsNotificationPush/rq/v1.0\"><idUniken>TEST</idUniken><responseCode>0</responseCode><notificationUuid>03f67f9e-ec4f-11e6-b006-92361f002671</notificationUuid><actionResponse>Approved</actionResponse><errorCode>0</errorCode><errorMessage></errorMessage><status>ACTIVE</status><deliveryStatus>NOTIFIED</deliveryStatus><isDsVerified>true</isDsVerified><msgId></msgId><createTs>1646252183</createTs><updateTs>1646252243</updateTs><expiryTimestamp>1646252363</expiryTimestamp><clientIpAddress>192.168.1.100</clientIpAddress></NS3:processResultsNotificationPushRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>", summary = "Full request") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Ejemplo JSON", value = "{\"idUniken\":\"TEST\",\"responseCode\":\"0\",\"notificationUuid\":\"03f67f9e-ec4f-11e6-b006-92361f002671\",\"actionResponse\":\"Approved\",\"errorCode\":\"0\",\"errorMessage\":\"\",\"status\":\"ACTIVE\",\"deliveryStatus\":\"NOTIFIED\",\"isDsVerified\":\"true\",\"msgId\":\"\",\"createTs\":\"1646252183\",\"updateTs\":\"1646252243\",\"expiryTimestamp\":\"1646252363\",\"clientIpAddress\":\"192.168.1.100\"}", summary = "Full request") }) })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Resultado OK", content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Response XML", value = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:srv=\"http://www.technisys.net/cmm/services/processResultsNotificationPush/rs/v1.0\"><soap:Header><md:metadata><md:serviceId>processResultsNotificationPush</md:serviceId><md:serviceVersion>1.0</md:serviceVersion></md:metadata></soap:Header><soap:Body><srv:processResultsNotificationPushResponse></srv:processResultsNotificationPushResponse></soap:Body></soap:Envelope>", summary = "Full Response") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Response JSON", value = "{}", summary = "Full Response") }) }) })
	@CustomEndpointProcessing(processingFlow = "jsonXmlFlow")
	public DeferredResult<ResponseEntity<String>> postprocessResultsNotificationPush(
			@Parameter(hidden = true) @RequestHeader Map<String, Object> headers, HttpServletRequest request,
			@org.springframework.web.bind.annotation.RequestBody String body, @PathVariable String sessionInput) {
		MessageRequest<String> messageRequest = new MessageRequest<>();
		DirectorUtilities.completeRequestMap(request, headers);
		messageRequest.setHeaders(headers);
		messageRequest.setRequestObject(body);
		messageRequest.putHeader("service_id", "processResultsNotificationPush");
		messageRequest.putHeader("service_version", "1.0");
		messageRequest.putHeader("session_id", sessionInput);
		return this.executeService(this.processResultsNotificationPush, messageRequest);
	}

	@PostMapping(path = "/v1/verifyOtpUniken", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Ejemplo XML", value = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:e=\"http://www.technisys.net/cmm/services/errors/v1.0\" xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"><SOAP-ENV:Header><md:metadata><traceNumber>\n"
							+ "				170805548818444837509144259594724826341.174331236620762684679273915916385614770</traceNumber><serviceId>verifyOtpUniken</serviceId><serviceVersion>1.0</serviceVersion><sessionId>96619d48-2879-41cf-a2ec-2a2401090928</sessionId><channelId>IN</channelId><targetChannel><mnemonic>IN</mnemonic></targetChannel><organizationType><mnemonic /></organizationType><executingChannel><mnemonic>IN</mnemonic></executingChannel><address>ip=201.234.84.213;</address><executingOperatorId>$#{_executing_operator_id_}</executingOperatorId><locale>ES</locale><institutionType /><template><templateId /></template><channelDispatchDate>20210527095058875-0500</channelDispatchDate><msgTypeId /><workflowModule /><featureId /><sourceDate /><workflowId /><businessDate /><terminalId>Mozilla/5.0 (Windows NT 10.0; Win64; x64)\n"
							+ "				AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212\n"
							+ "				Safari/537.36</terminalId><sourceTime /><branchId /><userId /><executingScope /><customProperties><operatorId type=\"java.lang.String\" /><businessDate type=\"java.util.Date\" /><serviceContext type=\"java.lang.String\" /><llaveSimetrica type=\"java.lang.String\" /><funcCode type=\"java.lang.String\" /><terminal type=\"java.lang.String\" /><portal type=\"java.lang.String\">PBN</portal></customProperties><llaveSimetrica type=\"java.lang.String\" /><organizationOperatorId>userName=TEST</organizationOperatorId><parityCurrencyId /><localCurrencyId /><institutionId /><localCountryId /><bankId /><ip>201.234.84.213</ip><parityQuotationNemonic /><paginationInfo /><internals><auditCore>true</auditCore><autoPaginationEnabled>false</autoPaginationEnabled><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName><translate>true</translate><serviceRequestTimestamp /></internals></md:metadata></SOAP-ENV:Header><SOAP-ENV:Body><NS3:verifyOtpUnikenRequest xmlns:NS3=\"http://www.technisys.net/cmm/services/verifyOtpUniken/rq/v1.0\"><customer dataModel=\"diners.financials\" name=\"customer\" version=\"1.0\"><customerId>1714346234</customerId><token><value/></token></customer><profile dataModel=\"diners.financials\" name=\"profile\" version=\"1.0\"><profileType><mnemonic>S</mnemonic></profileType></profile><establishment dataModel=\"diners.financials\" name=\"establishment\" version=\"1.0\"><ruc/></establishment><otp dataModel=\"diners.financials\" name=\"otp\" version=\"1.0\"><value>915454</value><uuid>03f67f9e-ec4f-11e6-b006-92361f002671</uuid></otp><transaction><type>MON</type><code>ADD</code></transaction><consumer><value/></consumer><creditCard><entityCode/><creditCardAccount><brand><mnemonic/></brand></creditCardAccount></creditCard></NS3:verifyOtpUnikenRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>", summary = "Full request") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Ejemplo JSON", value = "{\"customer\":{\"customerId\":1714346234,\"token\":{\"value\":\"\"}},\"profile\":{\"profileType\":{\"mnemonic\":\"S\"}},\"establishment\":{\"ruc\":\"\"},\"otp\":{\"value\":915454,\"uuid\":\"03f67f9e-ec4f-11e6-b006-92361f002671\"},\"transaction\":{\"type\":\"MON\",\"code\":\"ADD\"},\"consumer\":{\"value\":\"\"},\"creditCard\":{\"entityCode\":\"\",\"creditCardAccount\":{\"brand\":{\"mnemonic\":\"\"}}}}", summary = "Full request") }) })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Resultado OK", content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Response XML", value = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:srv=\"http://www.technisys.net/cmm/services/verifyOtpUniken/rs/v1.0\"><soap:Header><md:metadata><md:serviceId>verifyOtpUniken</md:serviceId><md:serviceVersion>1.0</md:serviceVersion></md:metadata></soap:Header><soap:Body><srv:verifyOtpUnikenResponse><otp><codeResult>0</codeResult><status>SUCCESS</status></otp></srv:verifyOtpUnikenResponse></soap:Body></soap:Envelope>", summary = "Full Response") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Response JSON", value = "{\"otp\":{\"codeResult\":0,\"status\":\"SUCCESS\"}}", summary = "Full Response") }) }) })
	@CustomEndpointProcessing(processingFlow = "jsonXmlFlow")
	public DeferredResult<ResponseEntity<String>> postverifyOtpUniken(
			@Parameter(hidden = true) @RequestHeader Map<String, Object> headers, HttpServletRequest request,
			@org.springframework.web.bind.annotation.RequestBody String body) {
		MessageRequest<String> messageRequest = new MessageRequest<>();
		DirectorUtilities.completeRequestMap(request, headers);
		messageRequest.setHeaders(headers);
		messageRequest.setRequestObject(body);
		messageRequest.putHeader("service_id", "verifyOtpUniken");
		messageRequest.putHeader("service_version", "1.0");
		return this.executeService(this.verifyOtpUniken, messageRequest);
	}

	@PostMapping(path = "/v1/deviceRegistrationNotification", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Ejemplo XML", value = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:e=\"http://www.technisys.net/cmm/services/errors/v1.0\" xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><SOAP-ENV:Header><md:metadata><serviceId>deviceRegistrationNotification</serviceId><serviceVersion>1.0</serviceVersion><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><channelId>MB</channelId><targetChannel><mnemonic>MB</mnemonic></targetChannel><organizationType><mnemonic/></organizationType><executingChannel><mnemonic>MB</mnemonic></executingChannel><address>ip=10.230.241.199;</address><executingOperatorId>\n"
							+ "				uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true\n"
							+ "			</executingOperatorId><locale>ES</locale><institutionType/><template><templateId/></template><channelDispatchDate>20220503205514381-0300</channelDispatchDate><msgTypeId/><workflowModule/><featureId>ROL@5527</featureId><sourceDate/><workflowId/><businessDate/><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)\n"
							+ "				AppleWebKit/537.36 (KHTML, like Gecko)\n"
							+ "				ReactNativeDebugger/0.12.1\n"
							+ "				Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36\n"
							+ "			</terminalId><sourceTime/><branchId/><userId/><executingScope/><customProperties><operatorId type=\"java.lang.String\"/><businessDate type=\"java.util.Date\"/><serviceContext type=\"java.lang.String\"/><llaveSimetrica type=\"java.lang.String\"/><funcCode type=\"java.lang.String\"/><funcType type=\"java.lang.String\">OVA</funcType><terminal type=\"java.lang.String\"/><portal type=\"java.lang.String\"/></customProperties><llaveSimetrica type=\"java.lang.String\"/><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><parityCurrencyId/><localCurrencyId/><institutionId/><localCountryId/><bankId/><ip>10.230.241.199</ip><parityQuotationNemonic/><paginationInfo/><internals><auditCore>true</auditCore><autoPaginationEnabled>false</autoPaginationEnabled><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName><translate>true</translate><serviceRequestTimestamp/></internals></md:metadata></SOAP-ENV:Header><SOAP-ENV:Body><NS3:deviceRegistrationNotificationRequest xmlns:NS3=\"http://www.technisys.net/cmm/services/deviceRegistrationNotification/rq/v1.0\"><mfa><id>6e8283bf-001a-01ab-8395-afdc65abcde0</id></mfa><device><id>5sf84as1-02gd-adcs-7632-afdc65aebac8</id><status>CREATE</status></device><customer><customerId>15205603</customerId><profile>S</profile><additionalCustomerType><customerTypeId>custType101</customerTypeId><officialId>official202</officialId></additionalCustomerType></customer><establishment><ruc>ruc12345</ruc></establishment><transaction><code>FDC</code></transaction><variables><deviceName>Samsung Galaxy S21 Ultra</deviceName><previousDeviceName>Galaxy Ultra</previousDeviceName><newDeviceName>Samsung Galaxy S23 Ultra</newDeviceName><country>Ecuador</country><city>Quito</city><digitalAccount>Blu</digitalAccount></variables></NS3:deviceRegistrationNotificationRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>", summary = "Full request") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Ejemplo JSON", value = "{\"mfa\":{\"id\":\"6e8283bf-001a-01ab-8395-afdc65abcde0\"},\"device\":{\"id\":\"5sf84as1-02gd-adcs-7632-afdc65aebac8\",\"status\":\"CREATE\"},\"customer\":{\"customerId\":15205603,\"profile\":\"S\",\"additionalCustomerType\":{\"customerTypeId\":\"custType101\",\"officialId\":\"official202\"}},\"establishment\":{\"ruc\":\"ruc12345\"},\"transaction\":{\"code\":\"FDC\"},\"variables\":{\"deviceName\":\"Samsung Galaxy S21 Ultra\",\"previousDeviceName\":\"Galaxy Ultra\",\"newDeviceName\":\"Samsung Galaxy S23 Ultra\",\"country\":\"Ecuador\",\"city\":\"Quito\",\"digitalAccount\":\"Blu\"}}", summary = "Full request") }) })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Resultado OK", content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Response XML", value = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:srv=\"http://www.technisys.net/cmm/services/deviceRegistrationNotification/rs/v1.0\"><soap:Header><md:metadata><md:serviceId>deviceRegistrationNotification</md:serviceId><md:serviceVersion>1.0</md:serviceVersion></md:metadata></soap:Header><soap:Body><srv:deviceRegistrationNotificationResponse/></soap:Body></soap:Envelope>", summary = "Full Response") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Response JSON", value = "{}", summary = "Full Response") }) }) })
	@CustomEndpointProcessing(processingFlow = "jsonXmlFlow")
	public DeferredResult<ResponseEntity<String>> postdeviceRegistrationNotification(
			@Parameter(hidden = true) @RequestHeader Map<String, Object> headers, HttpServletRequest request,
			@org.springframework.web.bind.annotation.RequestBody String body) {
		MessageRequest<String> messageRequest = new MessageRequest<>();
		DirectorUtilities.completeRequestMap(request, headers);
		messageRequest.setHeaders(headers);
		messageRequest.setRequestObject(body);
		messageRequest.putHeader("service_id", "deviceRegistrationNotification");
		messageRequest.putHeader("service_version", "1.0");
		return this.executeService(this.deviceRegistrationNotification, messageRequest);
	}

	@PostMapping(path = "/v1/processMFACancellation", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Ejemplo XML", value = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:e=\"http://www.technisys.net/cmm/services/errors/v1.0\" xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><SOAP-ENV:Header><md:metadata><serviceId>processMFACancellation</serviceId><serviceVersion>1.0</serviceVersion><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><channelId>MB</channelId><targetChannel><mnemonic>MB</mnemonic></targetChannel><organizationType><mnemonic/></organizationType><executingChannel><mnemonic>MB</mnemonic></executingChannel><address>ip=10.230.241.199;</address><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true</executingOperatorId><locale>ES</locale><institutionType/><template><templateId/></template><channelDispatchDate>20220503205514381-0300</channelDispatchDate><msgTypeId/><workflowModule/><featureId>ROL@5527</featureId><sourceDate/><workflowId/><businessDate/><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0) AppleWebKit/537.36 (KHTML, like Gecko) ReactNativeDebugger/0.12.1 Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36</terminalId><sourceTime/><branchId/><userId/><executingScope/><customProperties><operatorId type=\"java.lang.String\"/><businessDate type=\"java.util.Date\"/><serviceContext type=\"java.lang.String\"/><llaveSimetrica type=\"java.lang.String\"/><funcCode type=\"java.lang.String\"/><funcType type=\"java.lang.String\">OVA</funcType><terminal type=\"java.lang.String\"/><portal type=\"java.lang.String\"/></customProperties><llaveSimetrica type=\"java.lang.String\"/><organizationOperatorId>userName=ALMEIDA001</organizationOperatorId><parityCurrencyId/><localCurrencyId/><institutionId/><localCountryId/><bankId/><ip>10.230.241.199</ip><parityQuotationNemonic/><paginationInfo/><internals><auditCore>true</auditCore><autoPaginationEnabled>false</autoPaginationEnabled><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName><translate>true</translate><serviceRequestTimestamp/></internals></md:metadata></SOAP-ENV:Header><SOAP-ENV:Body><NS3:processMFACancellationRequest xmlns:NS3=\"http://www.technisys.net/cmm/services/processMFACancellation/rq/v1.0\"><transactionType>MON</transactionType><notificationUuid>6e19cd42-8a94-4cb6-99f2-f95f5f8f9d5d</notificationUuid></NS3:processMFACancellationRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>", summary = "Full request") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Ejemplo JSON", value = "{\"transactionType\":\"MON\",\"notificationUuid\":\"6e19cd42-8a94-4cb6-99f2-f95f5f8f9d5d\"}", summary = "Full request") }) })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Resultado OK", content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Response XML", value = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:srv=\"http://www.technisys.net/cmm/services/processMFACancellation/rs/v1.0\"><soap:Header><md:metadata><md:serviceId>processMFACancellation</md:serviceId><md:serviceVersion>1.0</md:serviceVersion></md:metadata></soap:Header><soap:Body><srv:processMFACancellationResponse/></soap:Body></soap:Envelope>", summary = "Full Response") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Response JSON", value = "{}", summary = "Full Response") }) }) })
	@CustomEndpointProcessing(processingFlow = "jsonXmlFlow")
	public DeferredResult<ResponseEntity<String>> postprocessMFACancellation(
			@Parameter(hidden = true) @RequestHeader Map<String, Object> headers, HttpServletRequest request,
			@org.springframework.web.bind.annotation.RequestBody String body) {
		MessageRequest<String> messageRequest = new MessageRequest<>();
		DirectorUtilities.completeRequestMap(request, headers);
		messageRequest.setHeaders(headers);
		messageRequest.setRequestObject(body);
		messageRequest.putHeader("service_id", "processMFACancellation");
		messageRequest.putHeader("service_version", "1.0");
		return this.executeService(this.processMFACancellation, messageRequest);
	}
	
	@PostMapping(path = "/v1/processPushGeneration", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Ejemplo XML", value = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:e=\"http://www.technisys.net/cmm/services/errors/v1.0\" xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><SOAP-ENV:Header><md:metadata><serviceId>processPushGeneration</serviceId><serviceVersion>1.0</serviceVersion><sessionId>5cc236dc-c559-4621-a440-2a660e7d7157</sessionId><channelId>MB</channelId><targetChannel><mnemonic>MB</mnemonic></targetChannel><organizationType><mnemonic/></organizationType><executingChannel><mnemonic>MB</mnemonic></executingChannel><address>ip=10.230.241.199;</address><executingOperatorId>uriTech=OPERADOR_EMPRESA@10730;uriSFB=1#0500628912;uriRUC=3#0500628912001;OWNER=true</executingOperatorId><locale>ES</locale><institutionType/><template><templateId/></template><channelDispatchDate>20220503205514381-0300</channelDispatchDate><msgTypeId/><workflowModule/><featureId>ROL@5527</featureId><sourceDate/><workflowId/><businessDate/><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 11_6_0)AppleWebKit/537.36 (KHTML, like Gecko)ReactNativeDebugger/0.12.1Chrome/87.0.4280.141 Electron/11.4.6 Safari/537.36</terminalId><sourceTime/><branchId/><userId/><executingScope/><customProperties><operatorId type=\"java.lang.String\"/><businessDate type=\"java.util.Date\"/><serviceContext type=\"java.lang.String\"/><llaveSimetrica type=\"java.lang.String\"/><funcCode type=\"java.lang.String\"/><funcType type=\"java.lang.String\">OVA</funcType><terminal type=\"java.lang.String\"/><portal type=\"java.lang.String\">PAYCLUB</portal></customProperties><llaveSimetrica type=\"java.lang.String\"/><organizationOperatorId>userName=TEST</organizationOperatorId><parityCurrencyId/><localCurrencyId/><institutionId/><localCountryId/><bankId/><ip>10.230.241.199</ip><parityQuotationNemonic/><paginationInfo/><internals><auditCore>true</auditCore><autoPaginationEnabled>false</autoPaginationEnabled><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>DINERS</serviceProviderName><translate>true</translate><serviceRequestTimestamp/></internals></md:metadata></SOAP-ENV:Header><SOAP-ENV:Body><NS3:processPushGenerationRequest xmlns:NS3=\"http://www.technisys.net/cmm/services/processPushGeneration/rq/v1.0\"><transaction><type>MON</type></transaction><mfa><id>550e8400-e29b-41d4-a716-446655440000</id></mfa><message><subject>Autorizar configuración de cuenta</subject><desc>Un dispositivo web ha solicitado una actualización de datos con tus credenciales ¿Deseas autorizarlo?</desc><label><accept>Autorizar</accept><reject>Denegar</reject></label></message><notificationMsg><subject>Autorizar configuración de cuenta</subject><messageNotif>Desde la web</messageNotif></notificationMsg><notificationUuidOld>03f67f9e-ec4f-11e6-b006-92361f002671</notificationUuidOld><callbackUrl>https://49212881-fb45-4a56-a875-26ce5b0dccab.mock.pstmn.io/push</callbackUrl></NS3:processPushGenerationRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>", 
							summary = "Full request") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Ejemplo JSON", value = "{\"transaction\":{\"type\":\"MON\"},\"mfa\":{\"id\":\"550e8400-e29b-41d4-a716-446655440000\"},\"message\":{\"subject\":\"Autorizar configuración de cuenta\",\"desc\":\"Un dispositivo web ha solicitado una actualización de datos con tus credenciales ¿Deseas autorizarlo?\",\"label\":{\"accept\":\"Autorizar\",\"reject\":\"Denegar\"}},\"notificationMsg\":{\"subject\":\"Autorizar configuración de cuenta\",\"messageNotif\":\"Desde la web\"},\"notificationUuidOld\":\"03f67f9e-ec4f-11e6-b006-92361f002671\",\"callbackUrl\":\"https://49212881-fb45-4a56-a875-26ce5b0dccab.mock.pstmn.io/push\"}", 
							summary = "Full request") }) })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Resultado OK", content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Response XML", value = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\" xmlns:srv=\"http://www.technisys.net/cmm/services/processPushGeneration/rs/v1.0\"><soap:Header><md:metadata><md:serviceId>processPushGeneration</md:serviceId><md:serviceVersion>1.0</md:serviceVersion></md:metadata></soap:Header><soap:Body><srv:processPushGenerationResponse><notification><response>0</response><notificationUuid>7f587b63-09ff-4bf0-84d5-8a8f61758c29</notificationUuid></notification></srv:processPushGenerationResponse></soap:Body></soap:Envelope>", 
							summary = "Full Response") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Response JSON", value = "{\"notification\":{\"response\":0,\"notificationUuid\":\"7f587b63-09ff-4bf0-84d5-8a8f61758c29\"}}", 
							summary = "Full Response") }) }) })
	@CustomEndpointProcessing(processingFlow = "jsonXmlFlow")
	public DeferredResult<ResponseEntity<String>> postprocessPushGeneration(
			@Parameter(hidden = true) @RequestHeader Map<String, Object> headers, HttpServletRequest request,
			@org.springframework.web.bind.annotation.RequestBody String body) {
		MessageRequest<String> messageRequest = new MessageRequest<>();
		DirectorUtilities.completeRequestMap(request, headers);
		messageRequest.setHeaders(headers);
		messageRequest.setRequestObject(body);
		messageRequest.putHeader("service_id", "processPushGeneration");
		messageRequest.putHeader("service_version", "1.0");
		return this.executeService(this.processPushGeneration, messageRequest);
	}
	
	@PostMapping(path = "/v1/updateUserDeviceState", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Ejemplo XML", value = "<SOAP-ENV:Envelope\n"
							+ "        xmlns:SOAP-ENV=\"http://www.w3.org/2003/05/soap-envelope\"\n"
							+ "        xmlns:e=\"http://www.technisys.net/cmm/services/errors/v1.0\"\n"
							+ "        xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\"\n"
							+ "        xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\n"
							+ "        xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><SOAP-ENV:Header><md:metadata><serviceId>updateUserDeviceState</serviceId><serviceVersion>1.0</serviceVersion><sessionId>66c70e82-d24b-4383-9cc9-a9b52d0b77dd</sessionId><applicationId/><channelId>IN</channelId><targetChannel><mnemonic>IN</mnemonic></targetChannel><organizationType><mnemonic/></organizationType><executingChannel><mnemonic>IN</mnemonic></executingChannel><address>ip=98.98.27.122;</address><executingOperatorId>uriTech=OPERADOR_EMPRESA@20426;uriSFB=1#1300492459;uriRUC=3#1300492459;OWNER=true</executingOperatorId><locale>ES</locale><institutionType/><template><templateId/></template><channelDispatchDate>20240425120543444-0500</channelDispatchDate><msgTypeId/><workflowModule/><featureId>ROL@5628</featureId><sourceDate/><workflowId>Zot7LiU_Lc2Ob-K7EtlMt</workflowId><businessDate/><terminalId>Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36</terminalId><sourceTime/><branchId/><userId/><executingScope/><customProperties><operatorId type=\"java.lang.String\"/><businessDate type=\"java.util.Date\"/><serviceContext type=\"java.lang.String\"/><llaveSimetrica type=\"java.lang.String\"/><funcCode type=\"java.lang.String\"/><funcType type=\"java.lang.String\">OVB</funcType><terminal type=\"java.lang.String\"/><portal type=\"java.lang.String\">PBN</portal><deviceId type=\"java.lang.String\"/></customProperties><llaveSimetrica type=\"java.lang.String\"/><organizationOperatorId>userName=CASH20QA2025</organizationOperatorId><parityCurrencyId/><localCurrencyId/><institutionId/><localCountryId/><bankId/><ip>98.98.27.122</ip><parityQuotationNemonic/><paginationInfo/><internals><auditCore>true</auditCore><autoPaginationEnabled>false</autoPaginationEnabled><serviceProviderEntityName>LOCAL</serviceProviderEntityName><serviceProviderName>CMM</serviceProviderName><translate>true</translate><serviceRequestTimestamp/></internals></md:metadata></SOAP-ENV:Header><SOAP-ENV:Body><NS3:updateUserDeviceStateRequest\n"
							+ "                xmlns:NS3=\"http://www.technisys.net/cmm/services/updateUserDeviceState/rq/v1.0\"><mfa><id>300492459</id><state>a</state></mfa></NS3:updateUserDeviceStateRequest></SOAP-ENV:Body></SOAP-ENV:Envelope>", 
							summary = "Full request") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Ejemplo JSON", value = "{\"mfa\":{\"id\":300492459,\"state\":\"a\"}}", 
							summary = "Full request") }) })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Resultado OK", content = {
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_XML_VALUE, examples = {
					@ExampleObject(name = "Response XML", value = "<soap:Envelope\n"
							+ "	xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\"\n"
							+ "	xmlns:md=\"http://www.technisys.net/cmm/services/metadata/v2.0\"\n"
							+ "	xmlns:srv=\"http://www.technisys.net/cmm/services/updateUserDeviceState/rs/v1.0\"><soap:Header><md:metadata><md:serviceId>updateUserDeviceState</md:serviceId><md:serviceVersion>1.0</md:serviceVersion></md:metadata></soap:Header><soap:Body><srv:updateUserDeviceStateResponse></srv:updateUserDeviceStateResponse></soap:Body></soap:Envelope>", 
							summary = "Full Response") }),
			@Content(schema = @Schema(implementation = String.class), mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
					@ExampleObject(name = "Response JSON", value = "{}", 
							summary = "Full Response") }) }) })
	@CustomEndpointProcessing(processingFlow = "jsonXmlFlow")
	public DeferredResult<ResponseEntity<String>> postupdateUserDeviceState(
			@Parameter(hidden = true) @RequestHeader Map<String, Object> headers, HttpServletRequest request,
			@org.springframework.web.bind.annotation.RequestBody String body) {
		MessageRequest<String> messageRequest = new MessageRequest<>();
		DirectorUtilities.completeRequestMap(request, headers);
		messageRequest.setHeaders(headers);
		messageRequest.setRequestObject(body);
		messageRequest.putHeader("service_id", "updateUserDeviceState");
		messageRequest.putHeader("service_version", "1.0");
		return this.executeService(this.updateUserDeviceState, messageRequest);
	}

}