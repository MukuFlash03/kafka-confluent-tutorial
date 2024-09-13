package main

import (
	"fmt"
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/confluentinc/confluent-kafka-go/v2/kafka"
	"github.com/joho/godotenv"
)

func main() {
	err := godotenv.Load()
	if err != nil {
		log.Fatal("Error loading .env file")
	}
	
	bootstrapServerUrl := os.Getenv("BOOTSTRAP_SERVER_URL")
	confluentClusterApiKey := os.Getenv("CONFLUENT_CLUSTER_API_KEY")
	confluentClusterApiSecret := os.Getenv("CONFLUENT_CLUSTER_API_SECRET")

	c, err := kafka.NewConsumer(&kafka.ConfigMap{
		"bootstrap.servers": bootstrapServerUrl,
		"sasl.username": confluentClusterApiKey,
		"sasl.password": confluentClusterApiSecret,

		"security.protocol": "SASL_SSL",
		"sasl.mechanisms": "PLAIN",
		"group.id": "kafka-go-getting-started",
		"auto.offset.reset": "earliest",
	})

	if err != nil {
		fmt.Printf("Failed to create consumer: %s\n", err)
		os.Exit(1)
	}

	topic := "purchases"
	err = c.SubscribeTopics([]string{topic}, nil)
	
	// Set up a channel for handling Ctrl-C, etc.
	sigchan := make(chan os.Signal, 1)
	signal.Notify(sigchan,syscall.SIGINT, syscall.SIGTERM)

	// Process messages
	run := true
	for run {
		select {
		case sig := <-sigchan:
			fmt.Printf("Caught signal %v: terminating\n", sig)
			run = false
		default: 
			ev, err := c.ReadMessage(100 * time.Millisecond)
			if err != nil {
				// No message available within timeout
				// Errors are informational and automatically handled by the consumer
				continue
			}
			fmt.Printf("Consumed event from topic %s: key = %-10s value = %s\n",
				*ev.TopicPartition.Topic, string(ev.Key), string(ev.Value))
		}
	}

	c.Close()
}