import grpc
import time
import PostService_pb2 as post_pb2
import PostService_pb2_grpc as post_pb2_grpc


def run():
    channel = grpc.insecure_channel('localhost:9090')
    stub = post_pb2_grpc.PostServiceStub(channel)

    print("Conectando ao stream de posts...")
    try:
        for post in stub.SubscribePosts(post_pb2.SubscribeRequest(username="")):
            print(f"[gRPC] {post.username}: {post.content} ({post.timestamp})")
    except grpc.RpcError as e:
        print(f"Erro no streaming:\nStatus code: {e.code()}\nDetails: {e.details()}")

if __name__ == '__main__':
    run()