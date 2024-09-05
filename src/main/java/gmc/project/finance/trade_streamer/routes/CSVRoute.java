package gmc.project.finance.trade_streamer.routes;

import gmc.project.finance.trade_streamer.processors.KafkaMessageProcessor;
import gmc.project.finance.trade_streamer.processors.KafkaTopicProcessor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CSVRoute extends RouteBuilder {

    @Value("${camel.component.kafka.brokers}")
    private String kafkaBroker;

    @Override
    public void configure() throws Exception {
        from("file:Financial_Data?recursive=true&noop=true&include=.*.csv") // Process CSV files in nested directories
                .routeId("CsvToKafkaParallelRoute")
                .split(body()) // Split each file for parallel processing
                .parallelProcessing() // Enable parallel processing of multiple CSV files
                .process(new KafkaTopicProcessor())
                .unmarshal().csv() // Unmarshal (parse) CSV
                .split(body()) // Split rows of the CSV file
                .process(new KafkaMessageProcessor())
                .to("kafka:topic?brokers=" + kafkaBroker) // Stream data to Kafka
                .log("Sent CSV row to Kafka topic: ${header.CamelFileNameOnly}")
                .end(); // End of the split for parallel processing
    }
}
