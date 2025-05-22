import grpc
import os
import sys
import re
import time

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
import mensagem_pb2
import mensagem_pb2_grpc
from comum.lamport import LamportClock
from comum.relogio import RelogioFisico
from user import User

# Função de log simples
def log_event(usuario, evento, ts_logico, ts_fisico):
    with open(f"{usuario}_log.txt", "a") as f:
        f.write(f"[{ts_fisico:.2f} | {ts_logico}] {evento}\n")

# Enviar mensagem pública
def enviar_mensagem_publica(usuario, conteudo, stub, clock_logico, clock_fisico):
    ts = clock_logico.tick()
    msg = mensagem_pb2.Mensagem(de=usuario, para="todos", conteudo=conteudo, timestamp=ts)
    resposta = stub.EnviarMensagem(msg)
    ts_fisico = clock_fisico.agora()
    log_event(usuario, f"Enviou mensagem pública: {conteudo}", ts, ts_fisico)
    print("Mensagem pública enviada com sucesso!")

# Chat privado com fluxo bidirecional
def chat_privado(usuario, destinatario, stub, clock_logico, clock_fisico):
    def gerar_mensagens():
        while True:
            texto = input(f"{usuario} ➜ {destinatario} (ou 'sair' para voltar): ")
            if texto.lower() == "sair":
                break
            ts = clock_logico.tick()
            msg = mensagem_pb2.Mensagem(de=usuario, para=destinatario, conteudo=texto, timestamp=ts)
            yield msg
            ts_fisico = clock_fisico.agora()
            log_event(usuario, f"Enviou (privado): {texto}", ts, ts_fisico)

    mensagens = gerar_mensagens()
    try:
        for resposta in stub.ChatMensagens(mensagens):
            ts_fisico = clock_fisico.agora()
            log_event(usuario, f"Recebeu (privado) de {resposta.de}: {resposta.conteudo}", resposta.timestamp, ts_fisico)
            print(f"{resposta.de}: {resposta.conteudo}")
    except grpc.RpcError as e:
        print("Conexão encerrada ou erro:", e.details())

# Ver mensagens públicas dos logs de todos os usuários
def listar_mensagens_publicas():
    print("\n🌐 Mensagens Públicas:")
    for filename in os.listdir():
        if filename.endswith("_log.txt"):
            with open(filename, "r") as f:
                for linha in f:
                    if "Enviou mensagem pública" in linha:
                        print(f"{filename[:-9]} ➜ {linha.strip()}")

# Ver mensagens privadas recebidas
def listar_mensagens_privadas(usuario):
    log_file = f"{usuario}_log.txt"
    if not os.path.exists(log_file):
        print("Nenhuma mensagem privada encontrada.")
        return

    encontrou = False
    print(f"\n🔒 Mensagens Privadas Recebidas ({usuario}):")
    with open(log_file, "r") as f:
        for linha in f:
            match = re.search(r"Recebeu \(privado\) de (\w+): (.+)", linha)
            if match:
                remetente = match.group(1)
                conteudo = match.group(2)
                print(f"De {remetente}: {conteudo}")
                encontrou = True

    if not encontrou:
        print("Nenhuma mensagem privada recebida encontrada.")

# Programa principal
def main():
    print("1. Criar novo usuário")
    print("2. Fazer login")
    escolha = input("Escolha uma opção: ")

    if escolha == "1":
        nome = input("Digite seu nome de usuário: ")
        user = User.criar_usuario(nome)
        if not user:
            return
    elif escolha == "2":
        nome = input("Digite seu nome de usuário: ")
        user = User.autenticar(nome)
        if not user:
            print("Usuário não encontrado.")
            return
    else:
        print("Opção inválida.")
        return

    usuario = user.nome
    servidor = input("Endereço do servidor (ex: localhost:50051): ")

    clock_logico = LamportClock()
    clock_fisico = RelogioFisico()

    channel = grpc.insecure_channel(servidor)
    stub = mensagem_pb2_grpc.MensagemServiceStub(channel)

    while True:
        print("\n1. Enviar mensagem pública")
        print("2. Iniciar chat privado")
        print("3. Seguir um usuário")
        print("4. Listar usuários")
        print("5. Listar quem estou seguindo")
        print("6. Ver mensagens públicas")
        print("7. Ver mensagens privadas recebidas")
        print("8. Sair")
        opcao = input("Escolha uma opção: ")

        if opcao == "1":
            conteudo = input("Digite a mensagem: ")
            enviar_mensagem_publica(usuario, conteudo, stub, clock_logico, clock_fisico)
        elif opcao == "2":
            destinatario = input("Destinatário: ")
            chat_privado(usuario, destinatario, stub, clock_logico, clock_fisico)
        elif opcao == "3":
            seguir_nome = input("Usuário a seguir: ")
            user.seguir(seguir_nome)
        elif opcao == "4":
            print("Usuários existentes:", ", ".join(User.listar_usuarios()))
        elif opcao == "5":
            user.listar_seguindo()
        elif opcao == "6":
            listar_mensagens_publicas()
        elif opcao == "7":
            listar_mensagens_privadas(usuario)
        elif opcao == "8":
            print("Encerrando cliente.")
            break
        else:
            print("Opção inválida.")

if __name__ == "__main__":
    main()
