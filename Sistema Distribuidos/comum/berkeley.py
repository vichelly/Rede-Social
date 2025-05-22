def berkeley_sync(tempo_local, tempos_outros):
    n = len(tempos_outros)
    deltas = [t - tempo_local for t in tempos_outros]
    ajuste = sum(deltas) / (n + 1)
    return tempo_local + ajuste
