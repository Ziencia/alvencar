//https://community.esri.com/t5/arcgis-javascript-maps-sdk-questions/javascript-date-now-in-local-time/m-p/494668#M45957
export function getFechaHoraActual() {
    const actual = new Date();
    const diferencia = actual.getTimezoneOffset();
    const local = new Date(actual.getTime() - diferencia * 60 * 1000);
    return local.toISOString().slice(0, 16);
}


