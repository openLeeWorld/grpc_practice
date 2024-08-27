package ecommerce;

import io.grpc.Status;
import io.grpc.StatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//import ecommerce.ProductInfoOuterClass;
//import ecommerce.ProductInfoGrpc;

public class ProductInfoImpl extends ProductInfoGrpc.ProductInfoImplBase {
    // extend the abstract class that is generated from plug-in
    @SuppressWarnings("rawtypes")
    private Map productMap = new HashMap<String, ProductInfoOuterClass.Product>();

    @SuppressWarnings("unchecked")
    @Override
    public void addProduct(
        ProductInfoOuterClass.Product request,
        io.grpc.stub.StreamObserver
        <ProductInfoOuterClass.ProductID> responseObserver) {
        // reponseObserer는 client에게 응답을 보내고, 스트림을 닫는 용도
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        productMap.put(randomUUIDString, request); // 새로 상품 넣음
        ProductInfoOuterClass.ProductID id = // id 새로 발급
            ProductInfoOuterClass.ProductID.newBuilder()
                            .setValue(randomUUIDString).build();
        responseObserver.onNext(id); // client에게 응답 보냄
        responseObserver.onCompleted(); // close the stream
    }

    @Override
    public void getProduct(
        ProductInfoOuterClass.ProductID request,
        io.grpc.stub.StreamObserver
        <ProductInfoOuterClass.Product> responseObserver) {
            String id = request.getValue(); // id를 꺼냄
            if (productMap.containsKey(id)) { // 해당 id가 있다면
                responseObserver.onNext( // 해당 상품을 응답으로 보내줌
                    (ProductInfoOuterClass.Product) productMap.get(id));
                responseObserver.onCompleted(); // close the stream
            } else {
                responseObserver.onError(new StatusException(Status.NOT_FOUND));
            } // 실패시 에러를 보내줌 
    }    
}
