package gmc.project.finance.trade_streamer.processors;

import com.google.gson.Gson;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.HashMap;
import java.util.Map;

public class CsvToJsonProcessor implements Processor {

    private final Gson gson = new Gson();

    @Override
    public void process(Exchange exchange) throws Exception {
        // Retrieve the header row from the exchange property
        String[] headers = exchange.getProperty("csvHeaders", String[].class);

        // Extract the current CSV row (as an array)
        String[] row = exchange.getIn().getBody(String[].class);

        // Create a map to store CSV data using the headers dynamically
        Map<String, String> jsonMap = new HashMap<>();

        // Dynamically map each CSV value to the corresponding header
        for (int i = 0; i < headers.length; i++) {
            jsonMap.put(headers[i], row[i]);
        }

        // Convert the map to a JSON string using Gson
        String json = gson.toJson(jsonMap);

        // Set the JSON string as the message body
        exchange.getIn().setBody(json);
    }
}