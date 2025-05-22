class LamportClock:
    def __init__(self):
        self.timestamp = 0

    def tick(self):
        """Incrementa o relógio local em 1, indicando um evento local."""
        self.timestamp += 1
        return self.timestamp

    def update(self, received_timestamp):
        """
        Atualiza o relógio lógico ao receber um timestamp externo.
        Garante que o relógio local seja maior que o recebido.
        """
        self.timestamp = max(self.timestamp, received_timestamp) + 1
        return self.timestamp

    def get_time(self):
        """Retorna o timestamp lógico atual."""
        return self.timestamp
