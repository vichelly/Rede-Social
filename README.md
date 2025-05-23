# Rede Social Distribuída (gRPC Python TypeScript Javascript)

Este projeto implementa uma rede social distribuída com suporte a mensagens públicas e privadas, seguindo usuários e controle de tempo lógico e físico. A comunicação entre componentes é feita usando gRPC.

## 📋 Pré-requisitos

Antes de iniciar, certifique-se de ter instalado:

- Python 3.10 ou superior
- Node.js e npm (versão 18+ recomendada)
- pip (gerenciador de pacotes Python)

## 📦 Instalação de dependências

Instale as dependências do Python:

```bash
pip install grpcio grpcio-tools
```

Instale as dependências do TypeScript:
```bash
cd servidores
npm install
npm install -D typescript ts-node @types/node

```

## ▶️ Execução

Abra três terminais diferentes para iniciar os servidores:

🧩 Terminal 1 – Servidor 1 (Python):

```bash
python servidores/servidor1_grpc.py
```
🧩 Terminal 2 – Servidor 2 (Javascript):

```bash
node servidores/servidor2_grpc.js
```

🧩 Terminal 3 – Servidor 3 (Typescript):

```bash
$env:PORTA_SERVIDOR="50053"; npx ts-node servidores/servidor3_grpc.ts
```
🧩 Terminal 4 – Coordenador 4 (Python):
```bash
python servidores/coordenador.py
```

🧩 Terminal 5 – Servidor 5 (Python):

```bash
python clientes/cliente.py
```


Você verá um menu interativo.
## 🧪 Testando o sistema

    Execute o cliente em dois terminais distintos (ex: Alice e Bob)

    Um usuário pode enviar mensagens públicas ou privadas

    Usuários podem se seguir e logs serão criados com base nas mensagens recebidas

## 📁 Logs

Cada usuário possui seu próprio arquivo de log no formato:
<nome_do_usuário>_log.txt

Também tem o log do servidor
servidor.log

