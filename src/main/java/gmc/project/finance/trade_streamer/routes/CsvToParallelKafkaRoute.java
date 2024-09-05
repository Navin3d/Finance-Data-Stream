package gmc.project.finance.trade_streamer.routes;

import gmc.project.finance.trade_streamer.processors.CSVHeaderProcessor;
import gmc.project.finance.trade_streamer.processors.CsvToJsonProcessor;
import gmc.project.finance.trade_streamer.processors.KafkaTopicProcessor;
import gmc.project.finance.trade_streamer.strategies.FileAggregationStrategy;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CsvToParallelKafkaRoute extends RouteBuilder {

    private final String ROUTE_ID = this.getClass().getName();

    @Value("${camel.component.kafka.brokers}")
    private String kafkaBroker;

    @Override
    public void configure() throws Exception {
        from("file:Financial_Data?recursive=true&noop=true&include=.*.csv") // Process CSV files in nested directories
                .routeId(ROUTE_ID)
                .split(body(), new FileAggregationStrategy()) // Split files and process in parallel
                .parallelProcessing() // Enable parallel processing
                .process(new KafkaTopicProcessor()) // Dynamically assign Kafka topic based on file name
                .unmarshal().csv() // Parse CSV
                .process(new CSVHeaderProcessor()) // Extract CSV header
                .split(body()) // Split rows of the CSV file
                .parallelProcessing() // Parallel processing for rows too
                .process(new CsvToJsonProcessor()) // Convert each CSV row to JSON
                .to("kafka:topic?brokers=" + kafkaBroker) // Stream data to Kafka
                .log("Sent CSV row to Kafka topic: ${header.CamelFileNameOnly}")
                .end(); // End of the split for parallel processing
    }
}
