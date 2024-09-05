package gmc.project.finance.trade_streamer.strategies;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class FileAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        // If it's the first message, return it as is
        if (oldExchange == null) {
            return newExchange;
        }
        return oldExchange; // No specific aggregation needed; just proceed with parallel processing
    }
}
