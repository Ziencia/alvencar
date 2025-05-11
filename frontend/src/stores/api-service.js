import axios from 'axios';

const host = 'http://localhost:8083/';
const API_CLIENTES = host + "api/clientes";

export function cambiarHttpPorHttps(enlace) {
    return enlace.replace(/^http:/, 'https:');
}

function llamadaAPI(method, body, path) {
  let config = {
    method: method ?? "get",
    maxBodyLength: Infinity,
    url: path,
    headers: {}
  };
  if (body) {
    config.data = body;
    config.headers["Content-Type"] = "application/json";
  }
  return axios.request(config);
}

export function getClientes() {
  return llamadaAPI("get", null, API_CLIENTES);
}
export function postCliente(data) {
    return llamadaAPI("post", data, API_CLIENTES)
}
export function deleteCliente(href) {
    return llamadaAPI("delete", null, href);
  }  
export function updateCliente(href, data) {
    return llamadaAPI("put", data, href);
}