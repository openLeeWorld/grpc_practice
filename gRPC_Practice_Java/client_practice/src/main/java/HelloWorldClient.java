import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import example.GreeterGrpc;
import example.Example.HelloRequest;
import example.Example.HelloResponse;

public class HelloWorldClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                                                        .usePlaintext()
                                                        .build();

        GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);

        HelloResponse response = stub.sayHello(HelloRequest.newBuilder().setName("World").build());

        System.out.println("Greeter client received: " + response.getMessage());

        channel.shutdown();
    }
}
