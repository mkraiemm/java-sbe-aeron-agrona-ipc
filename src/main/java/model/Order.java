package model;

import common.ConcreteIdGenerator;

/**
 * Represents an order in the financial messaging system.
 */
public class Order {
    private long publisherId;
    private long timestamp;
    private long clOrdId;
    private OrderSide side; 
    private int quantity;
    private double price;
    private char timeInForce;

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
     * Gets the timestamp of the order.
     *
     * @return The timestamp
     */
    public long getTimestamp() { return timestamp; }

    /**
     * Sets the timestamp of the order.
     *
     * @param timestamp The timestamp to set
     */
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    /**
     * Gets the unique order ID.
     *
     * @return The unique order ID
     */
    public long getClOrdId() { return clOrdId; }

    /**
     * Sets the unique order ID.
     *
     * @param clOrdId The unique order ID to set
     */
    public void setClOrdId(long clOrdId) { this.clOrdId = clOrdId; }

    /**
     * Gets the side of the order (buy/sell).
     *
     * @return The side of the order
     */
    public OrderSide getSide() { return side; }

    /**
     * Sets the side of the order.
     *
     * @param side The side of the order to set
     */
    public void setSide(OrderSide side) { this.side = side; }

    /**
     * Gets the quantity of the order.
     *
     * @return The quantity
     */
    public int getQuantity() { return quantity; }

    /**
     * Sets the quantity of the order.
     *
     * @param quantity The quantity to set
     */
    public void setQuantity(int quantity) { this.quantity = quantity; }

    /**
     * Gets the price of the order.
     *
     * @return The price
     */
    public double getPrice() { return price; }

    /**
     * Sets the price of the order.
     *
     * @param price The price to set
     */
    public void setPrice(double price) { this.price = price; }

    /**
     * Gets the time in force for the order.
     *
     * @return The time in force
     */
    public char getTimeInForce() { return timeInForce; }

    /**
     * Sets the time in force for the order.
     *
     * @param timeInForce The time in force to set
     */
    public void setTimeInForce(char timeInForce) { this.timeInForce = timeInForce; }

    public void generateAndSetClOrdId(ConcreteIdGenerator idGenerator) {
        this.clOrdId = idGenerator.nextId(); // Generate and set the unique order ID
    }
}
