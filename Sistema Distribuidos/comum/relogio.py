import time

class RelogioFisico:
    def __init__(self):
        self.delta_ajuste = 0  # Correção acumulada no relógio

    def ajustar(self, delta):
        """Aplica um ajuste positivo ou negativo ao relógio físico."""
        self.delta_ajuste += delta

    def agora(self):
        """Retorna o tempo atual corrigido pelo delta do ajuste."""
        return time.time() + self.delta_ajuste
