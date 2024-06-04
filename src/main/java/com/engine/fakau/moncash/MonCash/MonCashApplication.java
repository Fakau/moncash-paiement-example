package com.engine.fakau.moncash.MonCash;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.digicelgroup.moncash.APIContext;
import com.digicelgroup.moncash.http.Constants;

@SpringBootApplication
public class MonCashApplication {
	private static final Logger logger = Logger.getLogger(MonCashApplication.class);

	@Autowired
	private ConstantsConfig constantsConfig;

	@Autowired
	private PaymentServiceImpl paymentServiceImpl;

	public static void main(String[] args) {
		SpringApplication.run(MonCashApplication.class, args);
	}
	
	@Bean
	public void init(){
		BasicConfigurator.configure();
		APIContext apiContext = new APIContext(constantsConfig.clientId, constantsConfig.clientSecret, Constants.SANDBOX);
		paymentServiceImpl.createPayment(apiContext);
		paymentServiceImpl.retrievePaymentByTransactionId(apiContext, "2155149110");
		paymentServiceImpl.retrievePaymentByOrder(apiContext, "1601915231376");
	}


}
