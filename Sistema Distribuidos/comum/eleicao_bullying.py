import random
import time

def eleicao_bullying(ids_servidores):
    """
    Simulação da eleição por bullying.
    Escolhe o maior ID disponível como coordenador.
    """
    print("[ELEIÇÃO] Iniciando eleição por bullying...")
    time.sleep(1)

    # Simula que todos os servidores estão ativos
    coordenador = max(ids_servidores)

    print(f"[ELEIÇÃO] Servidor {coordenador} venceu a eleição.")
    return coordenador
