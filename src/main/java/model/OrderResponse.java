package model;

/**
 * Represents a response to an order in the financial messaging system.
 */
public class OrderResponse {
    private long publisherId;
    private long timestamp;
    private OrderSide side; // Use enum instead of char
    private int quantity;
    private double price;
    private int filledQuantity;
    private double filledPrice;

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
     * Gets the timestamp of the order response.
     *
     * @return The timestamp
     */
    public long getTimestamp() { return timestamp; }

    /**
     * Sets the timestamp of the order response.
     *
     * @param timestamp The timestamp to set
     */
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    /**
     * Gets the side of the order response (buy/sell).
     *
     * @return The side of the order response
     */
    public OrderSide getSide() { return side; }

    /**
     * Sets the side of the order response.
     *
     * @param side The side of the order response to set
     */
    public void setSide(OrderSide side) { this.side = side; }

    /**
     * Gets the quantity of the order response.
     *
     * @return The quantity
     */
    public int getQuantity() { return quantity; }

    /**
     * Sets the quantity of the order response.
     *
     * @param quantity The quantity to set
     */
    public void setQuantity(int quantity) { this.quantity = quantity; }

    /**
     * Gets the price of the order response.
     *
     * @return The price
     */
    public double getPrice() { return price; }

    /**
     * Sets the price of the order response.
     *
     * @param price The price to set
     */
    public void setPrice(double price) { this.price = price; }

    /**
     * Gets the filled quantity of the order response.
     *
     * @return The filled quantity
     */
    public int getFilledQuantity() { return filledQuantity; }

    /**
     * Sets the filled quantity of the order response.
     *
     * @param filledQuantity The filled quantity to set
     */
    public void setFilledQuantity(int filledQuantity) { this.filledQuantity = filledQuantity; }

    /**
     * Gets the filled price of the order response.
     *
     * @return The filled price
     */
    public double getFilledPrice() { return filledPrice; }

    /**
     * Sets the filled price of the order response.
     *
     * @param filledPrice The filled price to set
     */
    public void setFilledPrice(double filledPrice) { this.filledPrice = filledPrice; }
}
