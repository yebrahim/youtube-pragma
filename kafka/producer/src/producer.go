package main

import (
	"fmt"
	"log"
	"time"

	"github.com/Shopify/sarama"
)

const (
	kafkaHost = "localhost:9092"
	topic     = "fancy-topic"
)

func main() {
	config := sarama.NewConfig()
	config.Producer.Return.Successes = true
	config.Metadata.AllowAutoTopicCreation = false

	conn, err := sarama.NewSyncProducer([]string{kafkaHost}, config)
	if err != nil {
		log.Fatal("Could not connect to Kafka: ", err)
	}

	defer conn.Close()

	i := 0
	for {
		msg := &sarama.ProducerMessage{
			Topic: topic,
			Key:   sarama.StringEncoder(fmt.Sprint(i)),
			Value: sarama.StringEncoder(fmt.Sprintf("message #%v", i)),
		}

		partition, offset, err := conn.SendMessage(msg)
		if err != nil {
			log.Fatal("Could not send message", err)
		}
		log.Printf("Sent message: %v, partition: %v, offset: %v", msg.Value, partition, offset)
		i++
		time.Sleep(time.Second)
	}
}
