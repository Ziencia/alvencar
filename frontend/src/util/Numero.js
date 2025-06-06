// https://stackoverflow.com/questions/49724537/intl-numberformat-either-0-or-two-fraction-digits

export function formatoMoneda(numero) {
  if (numero == null || isNaN(numero) || numero === 0 || numero === undefined ) return '';
  return new Intl.NumberFormat('de-DE', {
    style: 'currency',
    currency: 'EUR',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
    trailingZeroDisplay: 'stripIfInteger'
  }).format(numero);
}

export function formatoNumero(numero) {
  if (numero == null || isNaN(numero)) return '';
  return Number(numero).toLocaleString('de-DE');
}
// es-ES no muestra el punto para <10000