import grpc
import os
import sys
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
import mensagem_pb2
import mensagem_pb2_grpc
from comum.lamport import LamportClock
from comum.relogio import RelogioFisico
import time

# Função de log simples
def log_event(usuario, evento, ts_logico, ts_fisico):
    with open(f"{usuario}_log.txt", "a") as f:
        f.write(f"[{ts_fisico:.2f} | {ts_logico}] {evento}\n")

def enviar_mensagem_publica(usuario, conteudo, stub, clock_logico, clock_fisico):
    ts = clock_logico.tick()
    msg = mensagem_pb2.Mensagem(de=usuario, para="todos", conteudo=conteudo, timestamp=ts)
    resposta = stub.EnviarMensagem(msg)

    ts_fisico = clock_fisico.agora()
    log_event(usuario, f"Enviou mensagem pública: {conteudo}", ts, ts_fisico)
    print("Mensagem pública enviada com sucesso!")

def chat_privado(usuario, destinatario, stub, clock_logico, clock_fisico):
    def gerar_mensagens():
        while True:
            texto = input(f"{usuario} ➜ {destinatario}: ")
            if texto.lower() == "sair":
                break
            ts = clock_logico.tick()
            msg = mensagem_pb2.Mensagem(de=usuario, para=destinatario, conteudo=texto, timestamp=ts)
            yield msg
            ts_fisico = clock_fisico.agora()
            log_event(usuario, f"Enviou (privado): {texto}", ts, ts_fisico)

    resposta_stream = stub.ChatMensagens(gerar_mensagens())
    for resposta in resposta_stream:
        ts_fisico = clock_fisico.agora()
        log_event(usuario, f"Recebeu (privado): {resposta.conteudo}", resposta.timestamp, ts_fisico)
        print(f"{resposta.de}: {resposta.conteudo}")

def main():
    usuario = input("Seu nome de usuário: ")
    servidor = input("Endereço do servidor (ex: localhost:50051): ")

    clock_logico = LamportClock()
    clock_fisico = RelogioFisico()

    channel = grpc.insecure_channel(servidor)
    stub = mensagem_pb2_grpc.MensagemServiceStub(channel)

    while True:
        print("\n1. Enviar mensagem pública")
        print("2. Iniciar chat privado")
        print("3. Sair")
        opcao = input("Escolha uma opção: ")

        if opcao == "1":
            conteudo = input("Digite a mensagem: ")
            enviar_mensagem_publica(usuario, conteudo, stub, clock_logico, clock_fisico)
        elif opcao == "2":
            destinatario = input("Destinatário: ")
            chat_privado(usuario, destinatario, stub, clock_logico, clock_fisico)
        elif opcao == "3":
            print("Encerrando cliente.")
            break
        else:
            print("Opção inválida.")

if __name__ == "__main__":
    main()