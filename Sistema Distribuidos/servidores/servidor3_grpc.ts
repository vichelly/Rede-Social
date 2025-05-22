import * as grpc from '@grpc/grpc-js';
import * as protoLoader from '@grpc/proto-loader';
import path from 'path';

// ==== Relógios ====

class LamportClock {
  private time = 0;
  tick() {
    return ++this.time;
  }
  update(receivedTime: number) {
    this.time = Math.max(this.time, receivedTime) + 1;
    return this.time;
  }
}

class RelogioFisico {
  private delta = 0;
  ajustar(delta: number) {
    this.delta += delta;
  }
  agora(): number {
    return Date.now() / 1000 + this.delta;
  }
}

// ==== Carregar .proto ====

const PROTO_PATH = path.join(__dirname, '..', 'mensagem.proto');
const packageDefinition = protoLoader.loadSync(PROTO_PATH, {
  keepCase: true,
  longs: String,
  enums: String,
  defaults: true,
  oneofs: true
});
const proto = grpc.loadPackageDefinition(packageDefinition).mensagens as any;

// ==== Servidor gRPC ====

const lamport = new LamportClock();
const relogio = new RelogioFisico();

function logEvent(tag: string, evento: string, tsLogico: number, tsFisico: number) {
  const fs = require('fs');
  fs.appendFileSync(`${tag}_log.txt`, `[${tsFisico.toFixed(2)} | ${tsLogico}] ${evento}\n`);
}

const servidor: any = {
  EnviarMensagem: (call: any, callback: any) => {
    const ts = lamport.update(call.request.timestamp);
    const fisico = relogio.agora();
    const evento = `Mensagem recebida de ${call.request.de} para ${call.request.para}: ${call.request.conteudo}`;
    logEvent('servidor', evento, ts, fisico);
    callback(null, { recebido: true });
  },

  ChatMensagens: (call: any) => {
    call.on('data', (mensagem: any) => {
      const ts = lamport.update(mensagem.timestamp);
      const fisico = relogio.agora();
      logEvent('servidor', `[Chat] ${mensagem.de} para ${mensagem.para}: ${mensagem.conteudo}`, ts, fisico);

      call.write({
        de: mensagem.para,
        para: mensagem.de,
        conteudo: `Eco: ${mensagem.conteudo}`,
        timestamp: ts
      });
    });

    call.on('end', () => call.end());
  },

  ObterTempoVivo: (_: any, callback: any) => {
    const ts = relogio.agora();
    callback(null, { tempo: ts });
  },

  AjustarTempo: (call: any, callback: any) => {
    relogio.ajustar(call.request.delta);
    callback(null, {});
  }
};

// ==== Inicializar servidor ====

function main() {
  const server = new grpc.Server();
  server.addService(proto.MensagemService.service, servidor);
  const porta = process.env.PORTA_SERVIDOR || '50053';
  server.bindAsync(`0.0.0.0:${porta}`, grpc.ServerCredentials.createInsecure(), () => {
    console.log(`Servidor gRPC TypeScript rodando na porta ${porta}`);
    server.start();
  });
}

main();
