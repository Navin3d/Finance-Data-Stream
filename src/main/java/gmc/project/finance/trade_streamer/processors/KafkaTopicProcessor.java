package gmc.project.finance.trade_streamer.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class KafkaTopicProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        // Get the file name and extract it for the Kafka topic
        String fileName = exchange.getIn().getHeader(Exchange.FILE_NAME, String.class);
        String fileNameOnly = fileName.substring(0, fileName.lastIndexOf('.')); // Get file name without extension
        exchange.setProperty("topicName", fileNameOnly); // Save topic name as a property

        Thread.sleep(3000);
    }
}
