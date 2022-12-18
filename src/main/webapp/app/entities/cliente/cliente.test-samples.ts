import dayjs from 'dayjs/esm';

import { ICliente, NewCliente } from './cliente.model';

export const sampleWithRequiredData: ICliente = {
  id: 45820,
  name: 'Pará',
};

export const sampleWithPartialData: ICliente = {
  id: 51727,
  name: 'Rua portals',
  rendaBruta: 34112,
};

export const sampleWithFullData: ICliente = {
  id: 7570,
  name: 'Acre synthesize Bicicleta',
  foto: '../fake-data/blob/hipster.png',
  fotoContentType: 'unknown',
  dataNascimento: dayjs('2022-12-18'),
  possuiBeneficioAtivo: true,
  rendaBruta: 34781,
};

export const sampleWithNewData: NewCliente = {
  name: 'streamline workforce',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
