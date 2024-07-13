package com.dtcc.intern.demo.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.razorpay.RazorpayClient;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.dtcc.intern.demo.config.RazorpayConfig;
import com.dtcc.intern.demo.model.CreateOrderRequest;
import com.dtcc.intern.demo.model.PaymentSignatureVerification;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private Environment env;

    @Autowired
    private RazorpayConfig razorpayConfig;

    @PostMapping(value = "/order", produces = "application/json")
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            String razorpayKeyId = env.getProperty("razorpay.key_id"); // Access key ID from application.properties
            RazorpayClient razorpayClient = razorpayConfig.getRazorpayClient(); // Pass key ID to config

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", request.getAmount());
            orderRequest.put("currency", request.getCurrency());
            orderRequest.put("receipt", request.getReceipt());

            Order order = razorpayClient.orders.create(orderRequest);
            String orderId = order.get("id");

            // Create a JSON object with just the order ID (excluding key ID for security)
            JSONObject response = new JSONObject();
            response.put("orderId", orderId);
            response.put("keyId", razorpayKeyId);

            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        } catch (RazorpayException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = "/verify", produces = "application/json")
    public ResponseEntity<String> verifyPaymentSignature(@RequestBody PaymentSignatureVerification request) throws RazorpayException {
    String razorpaySecretKey = env.getProperty("razorpay.key_secret"); 

      String generatedSignature = hmac_sha256(request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId(), razorpaySecretKey);
      System.out.println(generatedSignature+" "+request.getRazorpaySignature());
      if (generatedSignature.equals(request.getRazorpaySignature())) {
        return new ResponseEntity<>("Payment signature verified", HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Payment signature verification failed", HttpStatus.BAD_REQUEST);
      }
  }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    private static String hmac_sha256(String data, String secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] hash = sha256_HMAC.doFinal(data.getBytes());
            return bytesToHex(hash);
        } catch (Exception e) {
            return null;
        } 
    }

}

