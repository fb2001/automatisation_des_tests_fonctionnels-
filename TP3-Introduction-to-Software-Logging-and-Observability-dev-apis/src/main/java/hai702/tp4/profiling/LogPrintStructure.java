package hai702.tp4.profiling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

public class LogPrintStructure {

    private LocalDateTime timestamp;
    private String level;
    private String className;
    private String rawMessage;

    private String operationType;
    private String eventCategory;
    private Integer userId;
    private Integer productId;
    private Double price;
    private Double threshold;
    private String methodName;

    private LogPrintStructure() {}


    public static class Builder {
        private final LogPrintStructure lps = new LogPrintStructure();

        public Builder setTimestamp(LocalDateTime ts) {
            lps.timestamp = ts;
            return this;
        }

        public Builder setLevel(String level) {
            lps.level = level;
            return this;
        }

        public Builder setClassName(String name) {
            lps.className = name;
            return this;
        }

        public Builder setRawMessage(String msg) {
            lps.rawMessage = msg;
            return this;
        }

        public Builder setOperationType(String op) {
            lps.operationType = op;
            return this;
        }

        public Builder setEventCategory(String cat) {
            lps.eventCategory = cat;
            return this;
        }

        public Builder setUserId(Integer id) {
            lps.userId = id;
            return this;
        }

        public Builder setProductId(Integer id) {
            lps.productId = id;
            return this;
        }

        public Builder setPrice(Double price) {
            lps.price = price;
            return this;
        }

        public Builder setThreshold(Double threshold) {
            lps.threshold = threshold;
            return this;
        }

        public Builder setMethodName(String method) {
            lps.methodName = method;
            return this;
        }

        public LogPrintStructure build() {
            return lps;
        }
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getLevel() { return level; }
    public String getClassName() { return className; }
    public String getRawMessage() { return rawMessage; }
    public String getOperationType() { return operationType; }
    public String getEventCategory() { return eventCategory; }
    public Integer getUserId() { return userId; }
    public Integer getProductId() { return productId; }
    public Double getPrice() { return price; }
    public Double getThreshold() { return threshold; }
    public String getMethodName() { return methodName; }

    public String toFormattedString() {
        return String.format(
                "Timestamp: %s\nLevel: %s\nClass: %s\nMessage: %s\n" +
                        "Operation: %s (%s)\nUserId: %s\nProductId: %s\nMethod: %s\nPrice: %s\nThreshold: %s",
                timestamp,
                level,
                className,
                rawMessage,
                operationType,
                eventCategory,
                userId,
                productId,
                methodName,
                price,
                threshold
        );
    }


    public String toJSON() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{\"error\": \"Cannot convert LPS to JSON\"}";
        }
    }

    @Override
    public String toString() {
        return toFormattedString();
    }
}
