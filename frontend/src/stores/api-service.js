import axios from 'axios';

//const host = 'https://alvencar-ac4e236c0ceb.herokuapp.com/api/';
const host = 'http://localhost:8083/api/';
const API_CLIENTES = host + "clientes";
const API_VEHICULOS = host + "vehiculos";
const API_FACTURAS = host + "facturas";
const API_VENTAS = host +"ventas";
const API_TRANSACCIONES = host + "transacciones";
const API_OFERTAS = host + "ofertas";

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
export function getVehiculosNoVendidos() {
  return llamadaAPI("get", null, API_VEHICULOS +  "/search/vendido?vendido=false");
}
export function getVehiculoOferta(href){
  return llamadaAPI("get",null, href);
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

export function getTransacciones() {
  return llamadaAPI("get", null, API_TRANSACCIONES);
}
export function deleteTransaccionPorId(id) {
  const url = API_TRANSACCIONES+`/eliminar/${id}`;
  return llamadaAPI("delete", null, url);
}

export function getOfertas() {
  return llamadaAPI("get", null, API_OFERTAS);
}
export function postOferta(data) {
    return llamadaAPI("post", data, API_OFERTAS)
}
export function deleteOferta(href) {
    return llamadaAPI("delete", null, href);
  }  
export function updateOferta(href, data) {
    return llamadaAPI("put", data, href);
}