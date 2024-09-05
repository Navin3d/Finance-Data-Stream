package gmc.project.finance.trade_streamer.processors;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.kafka.KafkaConstants;

@Slf4j
public class KafkaMessageProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        // Get the file-specific Kafka topic from the property
        String topicName = exchange.getProperty("topicName", String.class);

        // Convert each CSV row into a JSON-like structure for Kafka (you can modify based on your structure)
        String[] row = exchange.getIn().getBody(String[].class);
        String json = String.format("{\"field1\": \"%s\", \"field2\": \"%s\"}", row[0], row[1]);

        // Set the message body to the processed JSON
        exchange.getIn().setBody(json);

        // Dynamically set Kafka topic from the file name
        exchange.getIn().setHeader(KafkaConstants.TOPIC, topicName);

        Thread.sleep(3000);
    }
}
