import grpc
import os
import sys
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
from concurrent import futures
import mensagem_pb2
import mensagem_pb2_grpc
from comum.lamport import LamportClock
from comum.relogio import RelogioFisico
import time

# Função simples de log para testes
def log_event(processo, evento, ts_logico, ts_fisico):
    with open(f"{processo}_log.txt", "a") as f:
        f.write(f"[{ts_fisico:.2f} | {ts_logico}] {evento}\n")


class MensagemServiceImpl(mensagem_pb2_grpc.MensagemServiceServicer):
    def __init__(self):
        self.clock_logico = LamportClock()
        self.clock_fisico = RelogioFisico()

    def EnviarMensagem(self, request, context):
        ts_logico = self.clock_logico.update(request.timestamp)
        ts_fisico = self.clock_fisico.agora()

        evento = f"Mensagem recebida de {request.de} para {request.para}: {request.conteudo}"
        log_event("servidor", evento, ts_logico, ts_fisico)

        return mensagem_pb2.Confirmacao(recebido=True)

    def ChatMensagens(self, request_iterator, context):
        for mensagem in request_iterator:
            ts_logico = self.clock_logico.update(mensagem.timestamp)
            ts_fisico = self.clock_fisico.agora()

            evento = f"[Chat] {mensagem.de} para {mensagem.para}: {mensagem.conteudo}"
            log_event("servidor", evento, ts_logico, ts_fisico)

            yield mensagem_pb2.Mensagem(
                de=mensagem.para,
                para=mensagem.de,
                conteudo=f"Eco: {mensagem.conteudo}",
                timestamp=ts_logico
            )

    def ObterTempoVivo(self, request, context):
        ts_fisico = self.clock_fisico.agora()
        return mensagem_pb2.Tempo(tempo=ts_fisico)

    def AjustarTempo(self, request, context):
        self.clock_fisico.ajustar(request.delta)
        return mensagem_pb2.Vazio()  # Ajustado para retornar Vazio conforme proto


def serve():
    porta = os.getenv("PORTA_SERVIDOR", "50052")
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    mensagem_pb2_grpc.add_MensagemServiceServicer_to_server(MensagemServiceImpl(), server)
    server.add_insecure_port(f"[::]:{porta}")
    print(f"Servidor gRPC ativo em porta {porta}...")
    server.start()
    server.wait_for_termination()


if __name__ == "__main__":
    serve()
