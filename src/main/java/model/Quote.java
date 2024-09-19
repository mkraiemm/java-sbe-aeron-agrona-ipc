package model;

public class Quote {
    private long publisherId;
    private long timestamp;
    private String symbol;
    private boolean isIndicative;
    private int bidSize;
    private float bidPrice;
    private float askPrice;
    private int askSize;

    // Getters and setters

    public long getPublisherId() { return publisherId; }
    public void setPublisherId(long publisherId) { this.publisherId = publisherId; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public boolean isIndicative() { return isIndicative; }
    public void setIndicative(boolean indicative) { isIndicative = indicative; }

    public int getBidSize() { return bidSize; }
    public void setBidSize(int bidSize) { this.bidSize = bidSize; }

    public float getBidPrice() { return bidPrice; }
    public void setBidPrice(float bidPrice) { this.bidPrice = bidPrice; }

    public float getAskPrice() { return askPrice; }
    public void setAskPrice(float askPrice) { this.askPrice = askPrice; }

    public int getAskSize() { return askSize; }
    public void setAskSize(int askSize) { this.askSize = askSize; }
}
