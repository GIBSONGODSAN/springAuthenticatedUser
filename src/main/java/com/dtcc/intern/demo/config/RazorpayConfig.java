package com.dtcc.intern.demo.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Configuration
public class RazorpayConfig {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    public RazorpayConfig(Environment env) {
        // Validate key existence using Spring's Environment (optional)
        if (env.getProperty("razorpay.key_id") == null || env.getProperty("razorpay.key_secret") == null) {
            throw new IllegalArgumentException("Razorpay keys not found in configuration!");
        }
    }
    @Bean
    public RazorpayClient getRazorpayClient() throws RazorpayException {
      return new RazorpayClient(keyId, keySecret);
    }
}
