package com.engine.fakau.moncash.MonCash;

import org.apache.http.HttpStatus;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.digicelgroup.moncash.APIContext;
import com.digicelgroup.moncash.exception.MonCashRestException;
import com.digicelgroup.moncash.http.Constants;
import com.digicelgroup.moncash.payments.OrderId;
import com.digicelgroup.moncash.payments.Payment;
import com.digicelgroup.moncash.payments.PaymentCapture;
import com.digicelgroup.moncash.payments.PaymentCreator;
import com.digicelgroup.moncash.payments.TransactionId;

@SpringBootApplication
public class MonCashApplication {
	private static final Logger logger = Logger.getLogger(MonCashApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MonCashApplication.class, args);
	}
	
	@Bean
	public void init(){
		BasicConfigurator.configure();
		APIContext apiContext = new APIContext(Utility.clientId, Utility.clientSecret, Constants.SANDBOX);
		createPaiement( apiContext);
		retrievePaiementByTransactionId(apiContext, "2155149110");
		retrievePaiementByOrder(apiContext, "1601915231376");
	} 
	private void createPaiement(APIContext apiContext){
		try{
			PaymentCreator paymentCreator = new PaymentCreator();
			Payment payment = new Payment();
			payment.setOrderId(String.valueOf(System.currentTimeMillis()));
			payment.setAmount(50);
			PaymentCreator creator = paymentCreator.execute(apiContext,PaymentCreator.class, payment);
			if(creator.getStatus() !=null && creator.getStatus().compareTo(String.valueOf(HttpStatus.SC_ACCEPTED))==0){
			  logger.info("redirect to the link below");
			  String  redirectURl = creator.redirectUri();
			  System.out.println(redirectURl);   // method return the payment gateway url
			  logger.info(redirectURl);
			}else if(creator.getStatus()==null){
			  logger.error("Error");
			  logger.error(creator.getError());
			  logger.error(creator.getError_description());
			}else{
			  logger.error("Error");
			  logger.error(creator.getStatus());
			  logger.error(creator.getError());
			  logger.error(creator.getMessage());
			  logger.error(creator.getPath());
			}
		}catch(Exception ex){
			System.out.println("CreatePaiementException: "+ex.getMessage());
		}
	}
	private void retrievePaiementByTransactionId(APIContext apiContext, final String noTransaction){
		try{
			PaymentCapture paymentCapture = new PaymentCapture();
			TransactionId transactionId = new TransactionId();
			transactionId.setTransactionId(noTransaction);
			PaymentCapture capture = paymentCapture.execute(apiContext,PaymentCapture.class,transactionId);
			if(capture.getStatus() !=null && capture.getStatus().compareTo(String.valueOf(HttpStatus.SC_OK))==0){
			 logger.info("Transaction");
			 logger.info(capture.getPayment().getMessage());
			 logger.info("transactio_id = "+capture.getPayment().getTransaction_id());
			 logger.info("Payer="+capture.getPayment().getPayer());
			 logger.info("amount = "+capture.getPayment().getCost());
			} else {
			  logger.info(capture.getStatus());
			}
		}catch(Exception ex){
			System.out.println("RetrievePaiementException "+ex.getMessage());
		}

	}
	
	private void retrievePaiementByOrder(APIContext apiContext, final String orderReference){
		try{
			PaymentCapture paymentCapture = new PaymentCapture();
			OrderId orderId = new OrderId();
			orderId.setOrderId(orderReference);
			PaymentCapture capture = paymentCapture.execute(apiContext,PaymentCapture.class, orderId);
			if(capture.getStatus() !=null && capture.getStatus().compareTo(String.valueOf(HttpStatus.SC_OK))==0){
			 logger.info("Transaction");
			 logger.info(capture.getPayment().getMessage());
			 logger.info("transactio_id = "+capture.getPayment().getTransaction_id());
			 logger.info("Payer = "+capture.getPayment().getPayer());
			 logger.info("amount = "+capture.getPayment().getCost());
			} else {
			  logger.info(capture.getStatus());
			}
		}catch(Exception ex){
			System.out.println("RetrievePaiementByOrderIdException: "+ex.getMessage());
		}

	}

}
