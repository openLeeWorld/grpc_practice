package ecommerce;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ProductInfoServer {
    public static void main(String[] args) throws IOException, InterruptedException  {
        int port = 50051;
        Server server = ServerBuilder.forPort(port)
                                    .addService(new ProductInfoImpl())
                                    .build()
                                    .start();
        System.out.println("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Shutting down gRPC server since JVM is " + "shutting down");
            if (server != null) {
                server.shutdown();
            }
            System.err.println("Server shutdown");
        }));
        server.awaitTermination(); 
        // server thread in held until the server gets terminated
        // 서버가 종료될 때까지 메인 스레드를 블록합니다. 이는 서버가 계속 실행 상태를 유지
    }
}
