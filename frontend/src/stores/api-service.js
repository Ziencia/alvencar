import axios from 'axios';

const host = 'http://localhost:8083/';
const API_CLIENTES = host + "api/clientes";
const API_VEHICULOS = host + "api/vehiculos";
const API_FACTURAS = host + "api/facturas";
const API_VENTAS = host +"api/ventas";

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

export function getVehiculos() {
  return llamadaAPI("get", null, API_VEHICULOS);
}
export function postVehiculo(data) {
    return llamadaAPI("post", data, API_VEHICULOS)
}
export function deleteVehiculo(href) {
    return llamadaAPI("delete", null, href);
  }  
export function updateVehiculo(href, data) {
    return llamadaAPI("put", data, href);
}

export function getFacturas() {
  return llamadaAPI("get", null, API_FACTURAS);
}
export function postFactura(data) {
    return llamadaAPI("post", data, API_FACTURAS);
}
export function updateFactura(href, data) {
    return llamadaAPI("put", data, href);
}
export function postVenta(data) {
  return llamadaAPI("post", data, API_VENTAS);
}