package model;

import common.ConcreteIdGenerator;

public class Order {
    private long publisherId;
    private long timestamp;
    private long clOrdId;
    private OrderSide side;
    private int quantity;
    private float price;
    private TimeInForce timeInForce;

    // Getters and setters

    public long getPublisherId() { return publisherId; }
    public void setPublisherId(long publisherId) { this.publisherId = publisherId; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public long getClOrdId() { return clOrdId; }
    public void setClOrdId(long clOrdId) { this.clOrdId = clOrdId; }

    public OrderSide getSide() { return side; }
    public void setSide(OrderSide side) { this.side = side; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public TimeInForce getTimeInForce() { return timeInForce; }
    public void setTimeInForce(TimeInForce timeInForce) { this.timeInForce = timeInForce; }

    public void generateAndSetClOrdId(ConcreteIdGenerator idGenerator) {
        this.clOrdId = idGenerator.nextId();
    }
}
