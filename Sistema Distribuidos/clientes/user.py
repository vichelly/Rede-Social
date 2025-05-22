class User:
    _usuarios = {}  # Simula um "banco de dados" em memória

    def __init__(self, nome):
        self.nome = nome
        self.seguindo = set()
        User._usuarios[nome] = self

    @classmethod
    def criar_usuario(cls, nome):
        if nome in cls._usuarios:
            print("Usuário já existe.")
            return None
        return cls(nome)

    @classmethod
    def autenticar(cls, nome):
        return cls._usuarios.get(nome)

    @classmethod
    def listar_usuarios(cls):
        return list(cls._usuarios.keys())

    def listar_seguindo(self):
        if not self.seguindo:
            print("Você não está seguindo ninguém.")
        else:
            print("Você está seguindo:", ", ".join(self.seguindo))

    def seguir(self, outro_nome):
        if outro_nome in User._usuarios and outro_nome != self.nome:
            self.seguindo.add(outro_nome)
            print(f"Agora você está seguindo {outro_nome}.")
        else:
            print("Usuário não encontrado.")
# Criação automática de usuários padrão ao carregar o módulo
for nome in ["juan", "alice", "lucas", "maria","vitor","tales"]:
    User.criar_usuario(nome)