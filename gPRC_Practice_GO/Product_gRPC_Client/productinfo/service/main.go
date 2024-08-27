package main

// 클라이언트 메인 네트워크 구현
import (
	"context"
	"log"
	"time"

	// import the protocol buffer generated code
	pb "productinfo/service/ecommerce"

	"google.golang.org/grpc"
)

const (
	address = "localhost:50051"
)

func main() {
	conn, err := grpc.Dial(address, grpc.WithInsecure()) // setup HTTP 연결
	if err != nil {
		log.Fatalf("did not connect: %v", err)
	}
	defer conn.Close()                 // stream 닫기 예약
	c := pb.NewProductInfoClient(conn) // PASS THE CONN and create the stub

	name := "Apple iPhone 11"
	description := `Meet Apple iPhone 11`

	ctx, cancel := context.WithTimeout(context.Background(), time.Second)
	// 컨텍스트 생성 (타임아웃)
	defer cancel() // 컨텍스트 취소 메서드 예약

	r, err := c.AddProduct(ctx,
		&pb.Product{Name: name, Description: description})
	// 서버 API에 맞게 호출
	if err != nil {
		log.Fatalf("Could not add product: %v", err)
	}
	log.Printf("Product ID: %s added successfully", r.Value)

	product, err := c.GetProduct(ctx, &pb.ProductID{Value: r.Value})
	// 서버 API에 맞게 호출
	if err != nil {
		log.Fatalf("Could not get product: %v", err)
	}
	log.Printf("Product: ", product.String())

}
