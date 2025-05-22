const grpc = require('@grpc/grpc-js');
const protoLoader = require('@grpc/proto-loader');
const fs = require('fs');

const PROTO_PATH = './mensagem.proto';
const packageDefinition = protoLoader.loadSync(PROTO_PATH);
const proto = grpc.loadPackageDefinition(packageDefinition).mensagens;

let clockLogico = 0;
let deltaAjuste = 0;

function agora() {
  return Date.now() / 1000 + deltaAjuste;
}

function logEvent(processo, evento, tsLogico, tsFisico) {
  fs.appendFileSync(`${processo}_log.txt`, `[${tsFisico.toFixed(2)} | ${tsLogico}] ${evento}\n`);
}

function EnviarMensagem(call, callback) {
  clockLogico = Math.max(clockLogico, call.request.timestamp) + 1;
  const tsFisico = agora();
  logEvent("servidor", `Mensagem recebida de ${call.request.de} para ${call.request.para}: ${call.request.conteudo}`, clockLogico, tsFisico);
  callback(null, { recebido: true });
}

function ChatMensagens(call) {
  call.on('data', (mensagem) => {
    clockLogico = Math.max(clockLogico, mensagem.timestamp) + 1;
    const tsFisico = agora();

    // Log no servidor
    logEvent("servidor", `[Chat] ${mensagem.de} para ${mensagem.para}: ${mensagem.conteudo}`, clockLogico, tsFisico);

    // Log no remetente
    logEvent(mensagem.de, `Enviou (privado): ${mensagem.conteudo}`, clockLogico, tsFisico);

    // Log no destinatário
    logEvent(mensagem.para, `Recebeu (privado) de ${mensagem.de}: ${mensagem.conteudo}`, clockLogico, tsFisico);

    call.write({
      de: mensagem.para,
      para: mensagem.de,
      conteudo: `Eco: ${mensagem.conteudo}`,
      timestamp: clockLogico
    });
  });

  call.on('end', () => {
    call.end();
  });
}


function ObterTempoVivo(call, callback) {
  callback(null, { tempo: agora() });
}

function AjustarTempo(call, callback) {
  deltaAjuste += call.request.delta;
  callback(null, {}); // Retorna mensagem Vazio
}

function main() {
  const servidor = new grpc.Server();
  servidor.addService(proto.MensagemService.service, {
    EnviarMensagem,
    ChatMensagens,
    ObterTempoVivo,
    AjustarTempo
  });

  const porta = process.env.PORTA_SERVIDOR || '50052';
  servidor.bindAsync(`0.0.0.0:${porta}`, grpc.ServerCredentials.createInsecure(), () => {
    console.log(`Servidor gRPC em JavaScript rodando na porta ${porta}`);
    servidor.start();
  });
}

main();
