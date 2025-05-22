import grpc
import time
import threading

import PostService_pb2 as post_pb2
import PostService_pb2_grpc as post_pb2_grpc

import MessageService_pb2 as message_pb2
import MessageService_pb2_grpc as message_pb2_grpc



def stream_posts():
    channel = grpc.insecure_channel('localhost:9090')
    stub = post_pb2_grpc.PostServiceStub(channel)

    print("Conectando ao stream de posts...")
    try:
        for post in stub.SubscribePosts(post_pb2.SubscribeRequest(username="")):
            print(f"[Post] {post.username}: {post.content} ({post.timestamp})")
    except grpc.RpcError as e:
        print(f"[Post] Erro no streaming:\nStatus code: {e.code()}\nDetails: {e.details()}")


def stream_messages(username):
    channel = grpc.insecure_channel('localhost:9090')  # ou a porta do serviço de mensagens
    stub = message_pb2_grpc.MessageServiceStub(channel)

    print(f"Conectando ao stream de mensagens para {username}...")
    try:
        for msg in stub.SubscribeMessages(message_pb2.MessageSubscriptionRequest(username=username)):
            print(f"[Mensagem] {msg.sender} -> {msg.receiver}: {msg.content} ({msg.timestamp})")
    except grpc.RpcError as e:
        print(f"[Mensagem] Erro no streaming:\nStatus code: {e.code()}\nDetails: {e.details()}")

def send_message(sender, receiver, content):
    channel = grpc.insecure_channel('localhost:9090')
    stub = message_pb2_grpc.MessageServiceStub(channel)

    request = message_pb2.PrivateMessageRequest(
        sender=sender,
        receiver=receiver,
        content=content
    )

    response = stub.SendMessage(request)
    print(f"Mensagem enviada: {response.sender} -> {response.receiver}: {response.content}")

if __name__ == '__main__':
    send_message("bob", "alice", "Oi Alice!")

