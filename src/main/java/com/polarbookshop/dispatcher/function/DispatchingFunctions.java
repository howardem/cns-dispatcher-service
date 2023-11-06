package com.polarbookshop.dispatcher.function;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.polarbookshop.dispatcher.event.OrderAcceptedMessage;
import com.polarbookshop.dispatcher.event.OrderDispatchedMessage;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Configuration
@Slf4j
public class DispatchingFunctions {

	@Bean
	public Function<OrderAcceptedMessage, Long> pack() {
		return orderAcceptedMessage -> {
			log.info("The order with id {} is packed.", orderAcceptedMessage.orderId());
			return orderAcceptedMessage.orderId();
		};
	}
	
	@Bean
	public Function<Flux<Long>, Flux<OrderDispatchedMessage>> label() {
		return orderStream -> orderStream.map(orderId -> {
			log.info("The order with id {} is labeled.", orderId);
			return new OrderDispatchedMessage(orderId);
		}); 
	}

}
