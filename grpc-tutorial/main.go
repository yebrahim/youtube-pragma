package main

import (
	"log"
	"net"

	"grpc-tutorial/server"
	pb "grpc-tutorial/server/addressbook"

	"google.golang.org/grpc"
	"google.golang.org/grpc/reflection"
)

func main() {
	lis, err := net.Listen("tcp", "localhost:50051")
	if err != nil {
		log.Fatal("Could not listen on port")
	}

	grpcServer := grpc.NewServer()
	reflection.Register(grpcServer)
	pb.RegisterAddressBookServiceServer(grpcServer, server.NewAddressbookServer())
	log.Println("Starting server...")
	grpcServer.Serve(lis)
}
