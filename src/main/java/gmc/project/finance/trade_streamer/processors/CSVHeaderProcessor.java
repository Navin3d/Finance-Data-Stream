package gmc.project.finance.trade_streamer.processors;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.kafka.KafkaConstants;

import java.util.List;

@Slf4j
public class CSVHeaderProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        // Extract the body as a List of List<String>
        List<List<String>> csvData = exchange.getIn().getBody(List.class);

        // Get the first row as the header (it's a List<String>)
        List<String> headerRow = csvData.getFirst(); // First row is the header

        // Convert the List<String> (header) to String[] to store in the property
        String[] headers = headerRow.toArray(new String[0]);

        // Store the headers in exchange properties for later use
        exchange.setProperty("csvHeaders", headers);

        // Remove the header row from the body so that only data rows remain
        csvData.removeFirst();

        // Set the remaining rows (without header) as the new body
        exchange.getIn().setBody(csvData);

        Thread.sleep(3000);
    }
}
