# Financial Messaging System

## Overview
This project implements a high-performance financial messaging system using **Aeron** for inter-process communication and **Simple Binary Encoding (SBE)** for message encoding.

It includes:
- **Process 1**: Contains 3 `Publisher` instances that generate quotes and 1 `Processor` instance that processes the quotes and generates orders.
- **Process 2**: Contains 1 `Processor` instance that receives quotes from Process 1 and sends orders back to the publishers in Process 1.
- **Process 3**: A `DropCopier` that listens to all quote and order messages and logs them. This can run on a separate machine to monitor the message flow.

## Project Structure

- **`FinancialMessagingApp`**: Represents the first two processes (Process 1 and Process 2).
    - **Process 1**: Runs 3 publishers and 1 processor.
    - **Process 2**: Runs 1 processor, which communicates with the publishers in Process 1.
    
- **`ProcessThreeApp`**: Represents Process 3 (Drop Copier). This can run on a separate machine or the same machine as Processes 1 and 2.
    
### Key Components:
- **Publisher**: Generates and publishes quotes.
- **Processor**: Receives and processes quotes and sends orders back to the publishers.
- **DropCopier**: Listens for both quotes and order responses and logs the received messages, including message latency.

## How to Run the Project

### Prerequisites

Ensure you have the following:
- Java 17 or higher installed
- Maven or Gradle for project management (Gradle build is configured)
- Aeron and SBE setup (included in the project dependencies)

### Steps to Run

1. **Run `FinancialMessagingApp`**:
   - This will start both **Process 1** and **Process 2**.
   - Process 1 includes 3 `Publisher` instances and 1 `Processor` instance.
   - Process 2 includes 1 `Processor` instance that communicates with the publishers from Process 1.
   
   Example steps in your IDE (e.g., IntelliJ):
   - Run the `FinancialMessagingApp` class from your IDE or using a command line:
     ```bash
     ./gradlew run --args="FinancialMessagingApp"
     ```
   - This process will start generating and processing quotes and orders.
   
2. **(Optional) Run `ProcessThreeApp`**:
   - If you want to run **Process 3** (Drop Copier), you can run the `ProcessThreeApp` class.
   - The `DropCopier` will subscribe to the quotes and order responses and log the latency between messages.
   
   Example steps:
   - Run the `ProcessThreeApp` class from your IDE or use the command:
     ```bash
     ./gradlew run --args="ProcessThreeApp"
     ```
   - This process will log all the messages (quotes and orders) and their latencies.

### Important Notes

- Ensure the **Aeron media driver** is running for both `FinancialMessagingApp` and `ProcessThreeApp` to communicate.
- If running on separate machines, make sure the Aeron channel configuration supports network communication (e.g., UDP multicast).
- The **Simple Binary Encoding (SBE)** schema used for encoding and decoding messages is located in the `resources/sbe` folder.

### Additional Configuration

- You can modify the Aeron communication settings in the `MessagingUtil` class if you need to change the channel configuration or communication methods (IPC, UDP, etc.).

### Example Log Output

After running `FinancialMessagingApp` and `ProcessThreeApp`, you should see logs similar to this:
```text
[12:01:23.345] [Publisher-1] Quote sent successfully, result: 256
[12:01:23.350] [Processor-1] Processed quote: Quote: AAPL Price: 110.23 Size: 200 Timestamp: 2645876975893 | Latency: 25 µs
[12:01:23.355] [DropCopier] Received: Quote: AAPL Price: 110.23 Size: 200 Timestamp: 2645876975893 | Latency: 30 µs
