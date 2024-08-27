package main

// 서버가 구현해야 할 서버 서비스 로직 구현
import (
	"context"
	pb "productinfo/service/ecommerce" // import the protocol buffer generated code

	"github.com/gofrs/uuid"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

// server is used to implement ecommerce/product_info
type server struct {
	pb.UnimplementedProductInfoServer // Embed the unimplemented server
	productMap                        map[string]*pb.Product
} // 서버 객체가 가질 구조체 타입 선언

// ProductInfoServer 인터페이스의 모든 메서드를 구현하도록 합니다. (gRPC 요구사항)
// UnimplementedProductInfoServer를 임베딩하면 기본적으로 인터페이스의
// 모든 메서드를 포함하게 되어, 인터페이스를 구현한 것으로 간주됩니다.

// AddProduct implements ecommerce.AddProduct
func (s *server) AddProduct(ctx context.Context, in *pb.Product) (*pb.ProductID, error) {
	out, err := uuid.NewV4()
	if err != nil {
		return nil, status.Errorf(codes.Internal, "Error while generating Product ID", err)
	}
	in.Id = out.String()
	if s.productMap == nil {
		s.productMap = make(map[string]*pb.Product) // map 생성 후 할당
	}
	s.productMap[in.Id] = in // id 당 in(상품 포인터) 할당
	return &pb.ProductID{Value: in.Id}, status.New(codes.OK, "").Err()
}

// GetProduct implements ecommerce.GetProduct
func (s *server) GetProduct(ctx context.Context, in *pb.ProductID) (*pb.Product, error) {
	value, exists := s.productMap[in.Value]
	if exists {
		return value, status.New(codes.OK, "").Err()
	}
	return nil, status.Errorf(codes.NotFound, "Product does not exist.", in.Value)
}
