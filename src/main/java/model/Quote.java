package model;

/**
 * Represents a quote in the financial messaging system.
 */
public class Quote {
    private long publisherId;
    private long timestamp;
    private String symbol;
    private boolean isIndicative;
    private int bidSize;
    private double bidPrice;
    private double askPrice;
    private int askSize;

    // Getters and setters

    /**
     * Gets the publisher ID.
     *
     * @return The publisher ID
     */
    public long getPublisherId() { return publisherId; }

    /**
     * Sets the publisher ID.
     *
     * @param publisherId The publisher ID to set
     */
    public void setPublisherId(long publisherId) { this.publisherId = publisherId; }

    /**
     * Gets the timestamp of the quote.
     *
     * @return The timestamp
     */
    public long getTimestamp() { return timestamp; }

    /**
     * Sets the timestamp of the quote.
     *
     * @param timestamp The timestamp to set
     */
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    /**
     * Gets the symbol of the quote.
     *
     * @return The symbol
     */
    public String getSymbol() { return symbol; }

    /**
     * Sets the symbol of the quote.
     *
     * @param symbol The symbol to set
     */
    public void setSymbol(String symbol) { this.symbol = symbol; }

    /**
     * Checks if the quote is indicative.
     *
     * @return True if the quote is indicative, false otherwise
     */
    public boolean isIndicative() { return isIndicative; }

    /**
     * Sets the indicative flag for the quote.
     *
     * @param indicative The indicative flag to set
     */
    public void setIndicative(boolean indicative) { isIndicative = indicative; }

    /**
     * Gets the bid size of the quote.
     *
     * @return The bid size
     */
    public int getBidSize() { return bidSize; }

    /**
     * Sets the bid size of the quote.
     *
     * @param bidSize The bid size to set
     */
    public void setBidSize(int bidSize) { this.bidSize = bidSize; }

    /**
     * Gets the bid price of the quote.
     *
     * @return The bid price
     */
    public double getBidPrice() { return bidPrice; }

    /**
     * Sets the bid price of the quote.
     *
     * @param bidPrice The bid price to set
     */
    public void setBidPrice(double bidPrice) { this.bidPrice = bidPrice; }

    /**
     * Gets the ask price of the quote.
     *
     * @return The ask price
     */
    public double getAskPrice() { return askPrice; }

    /**
     * Sets the ask price of the quote.
     *
     * @param askPrice The ask price to set
     */
    public void setAskPrice(double askPrice) { this.askPrice = askPrice; }

    /**
     * Gets the ask size of the quote.
     *
     * @return The ask size
     */
    public int getAskSize() { return askSize; }

    /**
     * Sets the ask size of the quote.
     *
     * @param askSize The ask size to set
     */
    public void setAskSize(int askSize) { this.askSize = askSize; }
}
