import axios from 'axios';

const host = 'https://alvencar-ac4e236c0ceb.herokuapp.com/api/';
const API_CLIENTES = host + "clientes";
const API_VEHICULOS = host + "vehiculos";
const API_FACTURAS = host + "facturas";
const API_GENERARFACTURA = host + "generarfactura";
const API_VENTAS = host + "ventas";
const API_ALQUILERES = host + "alquileres";
const API_TRANSACCIONES = host + "transacciones";
const API_OFERTAS = host + "ofertas";

export function cambiarHttpPorHttps(enlace) {
  return enlace.replace(/^http:/, 'https:');
}

function llamadaAPI(method, body, path, contentType = "application/json", axiosConfig = {}) {
  let config = {
    method: method ?? "get",
    maxBodyLength: Infinity,
    url: path,
    headers: {},
    ...axiosConfig
  };
  if (body) {
    config.data = body;
    config.headers["Content-Type"] = contentType;
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
export function getVehiculoId(id) {
  return llamadaAPI("get",null, API_VEHICULOS + `/${id}`);
}
export function getVehiculosNoVendidos() {
  return llamadaAPI("get", null, API_VEHICULOS + "/search/venta");
}
//https://developer.mozilla.org/en-US/docs/Web/API/URLSearchParams/append llamada con varios parametros a la API
export function getVehiculosAlquiler(inicio, fin) {
  const parametros = new URLSearchParams();
  if (inicio) parametros.append("inicio", inicio);
  if (fin) parametros.append("fin", fin);
  return llamadaAPI("get", null, `${API_VEHICULOS}/alquiler?${parametros.toString()}`);
}
export function getVehiculoOfertas(vehiculoId) {
  return llamadaAPI("get", null, API_VEHICULOS + `/${vehiculoId}/ofertas`);
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
export function generarfactura(facturaId) {
  return llamadaAPI("get", null, `${API_GENERARFACTURA}/${facturaId}`,undefined, { responseType: "blob" });
}

export function postVenta(data) {
  return llamadaAPI("post", data, API_VENTAS);
}

export function postAlquiler(data) {
  return llamadaAPI("post", data, API_ALQUILERES);
}

export function getTransacciones() {
  return llamadaAPI("get", null, API_TRANSACCIONES);
}
export function deleteTransaccionPorId(id) {
  const url = API_TRANSACCIONES + `/eliminar/${id}`;
  return llamadaAPI("delete", null, url);
}
export function updateTransaccion(href, data) {
  return llamadaAPI("put", data, href);
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
export function updateOfertaVehiculo(href, data) {
  const hrefOfertaVehiculos = href + "/vehiculo";
  return llamadaAPI("put", data, hrefOfertaVehiculos, "text/uri-list");
}