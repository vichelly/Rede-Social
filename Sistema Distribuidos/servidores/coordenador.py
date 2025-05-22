import time
import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
import threading
import grpc
import mensagem_pb2
import mensagem_pb2_grpc
from comum.eleicao_bullying import eleicao_bullying
from comum.relogio import RelogioFisico

# Lista de servidores participantes (endereços gRPC)
SERVIDORES = {
    1: "localhost:50051",
    2: "localhost:50052",
    3: "localhost:50053"
}

def servidor_ativo(endereco):
    try:
        with grpc.insecure_channel(endereco) as canal:
            grpc.channel_ready_future(canal).result(timeout=1)
        return True
    except grpc.FutureTimeoutError:
        return False

def obter_tempo_servidor_grpc(endereco):
    try:
        with grpc.insecure_channel(endereco) as canal:
            stub = mensagem_pb2_grpc.MensagemServiceStub(canal)
            resposta = stub.ObterTempoVivo(mensagem_pb2.Vazio())
            return resposta.tempo
    except Exception as e:
        print(f"[ERRO] Falha ao obter tempo de {endereco}: {e}")
        return None

def ajustar_tempo_servidor(endereco, delta):
    try:
        with grpc.insecure_channel(endereco) as canal:
            stub = mensagem_pb2_grpc.MensagemServiceStub(canal)
            stub.AjustarTempo(mensagem_pb2.DeltaTempo(delta=delta))
    except Exception as e:
        print(f"[ERRO] Falha ao ajustar tempo de {endereco}: {e}")

def sincronizar_relogios(coordenador_id):
    print(f"[Coordenador {coordenador_id}] Iniciando sincronização com Berkeley...")
    endereco_coord = SERVIDORES[coordenador_id]
    local_time = obter_tempo_servidor_grpc(endereco_coord)
    tempos_outros = []

    for sid, endereco in SERVIDORES.items():
        if sid == coordenador_id:
            continue
        tempo = obter_tempo_servidor_grpc(endereco)
        if tempo is not None:
            tempos_outros.append(tempo)

    # Filtra possíveis None antes de calcular
    tempos_validos = [local_time] + tempos_outros
    tempos_validos = [t for t in tempos_validos if t is not None]

    if tempos_validos:
        novo_tempo = sum(tempos_validos) / len(tempos_validos)
    else:
        print("[ERRO] Não foi possível obter tempos válidos para sincronização.")
        return

    delta = novo_tempo - local_time

    for sid, endereco in SERVIDORES.items():
        ajustar_tempo_servidor(endereco, delta)

    print(f"[Coordenador {coordenador_id}] Sincronização concluída. Delta: {delta:.4f} segundos\n")

def coordenador_loop():
    while True:
        servidores_ativos = [sid for sid, end in SERVIDORES.items() if servidor_ativo(end)]
        if not servidores_ativos:
            print("[ERRO] Nenhum servidor ativo. Aguardando...")
            time.sleep(5)
            continue

        coordenador = eleicao_bullying(servidores_ativos)
        print(f"[ELEIÇÃO] Coordenador eleito: Servidor {coordenador}\n")

        sincronizar_relogios(coordenador)
        time.sleep(10)

if __name__ == "__main__":
    print("[Iniciando Coordenador]")
    coordenador_thread = threading.Thread(target=coordenador_loop)
    coordenador_thread.start()
