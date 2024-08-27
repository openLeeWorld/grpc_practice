package ecommerce;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

//import java.util.logging.Logger;

public class ProductInfoClient {
    public static void main(String[] args) throws InterruptedException  {
        ManagedChannel channel = ManagedChannelBuilder
                                .forAddress("localhost", 50051) // 소켓 설정
                                .usePlaintext() //HTTP
                                .build();

        ProductInfoGrpc.ProductInfoBlockingStub stub =
            ProductInfoGrpc.newBlockingStub(channel);

        ProductInfoOuterClass.ProductID productID = stub.addProduct(
            ProductInfoOuterClass.Product.newBuilder()
                                        .setName("Apple iPhone 11")
                                        .setDescription("Meet Apple iPhone 11")
                                        .build());
        System.out.println(productID.getValue());
            
        ProductInfoOuterClass.Product product = stub.getProduct(productID);
        System.out.println(product.toString());
        channel.shutdown();
    }
}
