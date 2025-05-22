import grpc
import PostService_pb2
import PostService_pb2_grpc

def run():
    channel = grpc.insecure_channel('localhost:9090')
    stub = PostService_pb2_grpc.PostServiceStub(channel)
    response = stub.CreatePost(PostService_pb2.CreatePostRequest(username='user1', content='Olá do Python!'))
    print(f"Response: success={response.success if hasattr(response, 'success') else 'N/A'}, info='{response.info if hasattr(response, 'info') else 'N/A'}'")
    print(f"Response details: username={response.username}, content={response.content}, timestamp={response.timestamp}")

def listen_for_posts(stub):
    for post_response in stub.SubscribePosts(SubscribeRequest(username="user1")):
        print(f"Nova postagem de {post_response.username}: {post_response.content} às {post_response.timestamp}")

if __name__ == "__main__":
    channel = grpc.insecure_channel("localhost:9090")
    run()
    stub = post_pb2_grpc.PostServiceStub(channel)
    listen_for_posts(stub)
