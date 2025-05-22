import zmq
import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

import time
from comum.relogio import RelogioFisico

ID_SERVIDOR = int(input("ID deste servidor: "))
porta = 6000 + ID_SERVIDOR

relogio = RelogioFisico()
context = zmq.Context()
socket = context.socket(zmq.REP)
socket.bind(f"tcp://*:{porta}")

print(f"Servidor {ID_SERVIDOR} escutando na porta {porta}...")

while True:
    msg = socket.recv_string()
    if msg == "TEMPO?":
        socket.send_string(str(relogio.agora()))
    elif msg.startswith("AJUSTE:"):
        delta = float(msg.split(":")[1])
        relogio.ajustar(delta)
        socket.send_string("OK")
