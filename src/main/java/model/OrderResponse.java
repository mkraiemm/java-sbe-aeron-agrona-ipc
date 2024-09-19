package model;

public class OrderResponse {
    private long publisherId;
    private long timestamp;
    private long clOrdId;
    private OrderSide side;
    private int quantity;
    private float price;
    private int filledQuantity;
    private float filledPrice;
    private char status;

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

    public int getFilledQuantity() { return filledQuantity; }
    public void setFilledQuantity(int filledQuantity) { this.filledQuantity = filledQuantity; }

    public float getFilledPrice() { return filledPrice; }
    public void setFilledPrice(float filledPrice) { this.filledPrice = filledPrice; }

    public char getStatus() { return status; }
    public void setStatus(char status) { this.status = status; }
}
