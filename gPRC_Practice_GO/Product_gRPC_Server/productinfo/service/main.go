package main

// 같은 main 패키지라서 server 타입 정의를 import 안해도 됨
// 서버 메인 네트워크 구현
import (
	"log"
	"net"
	pb "productinfo/service/ecommerce" // import the protocol buffer generated code

	"google.golang.org/grpc"
)

const (
	port = ":50051"
)

func main() {
	lis, err := net.Listen("tcp", port) // tcp Listener of gRPC server
	if err != nil {
		log.Fatalf("faild to listen: %v", err)
	}
	s := grpc.NewServer() // new gRPC server instance by gRPC Go APIs
	pb.RegisterProductInfoServer(s, &server{})
	// 서버 인스턴스가 server 타입(구현된 서비스)에 맞춰 등록

	log.Printf("Starting gRPC listener on port " + port)
	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
}
