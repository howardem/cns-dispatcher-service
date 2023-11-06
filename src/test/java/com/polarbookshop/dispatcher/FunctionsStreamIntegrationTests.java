package com.polarbookshop.dispatcher;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polarbookshop.dispatcher.event.OrderAcceptedMessage;
import com.polarbookshop.dispatcher.event.OrderDispatchedMessage;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class FunctionsStreamIntegrationTests {

	@Autowired
	private InputDestination input;
	
	@Autowired
	private OutputDestination output;

	@Autowired
	private ObjectMapper objetcMapper;

	@Test
	void whenOrderAcceptedThenDispatched() throws IOException {
		long orderId = 121;
		
		Message<OrderAcceptedMessage> inputMessage = MessageBuilder.withPayload(new OrderAcceptedMessage(orderId)).build();
		Message<OrderDispatchedMessage> expectedOutputMessage = MessageBuilder.withPayload(new OrderDispatchedMessage(orderId)).build();

		this.input.send(inputMessage);
		assertThat(this.objetcMapper.readValue(this.output.receive().getPayload(), OrderDispatchedMessage.class))
			.isEqualTo(expectedOutputMessage.getPayload());
	}
}
