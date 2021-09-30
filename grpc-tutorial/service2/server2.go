package main

import (
	"context"
	"log"

	pb "grpc-tutorial/server/addressbook"

	"google.golang.org/grpc"
)

func main() {
	conn, err := grpc.Dial("localhost:50051", grpc.WithInsecure(), grpc.WithBlock())
	if err != nil {
		log.Fatal("Could not connect to service")
	}
	defer conn.Close()

	client := pb.NewAddressBookServiceClient(conn)

	if _, err = client.AddPerson(context.Background(), &pb.Person{
		Name: "Yasser",
		Id:   5,
	}); err != nil {
		log.Fatal("Could not add person")
	}
}
