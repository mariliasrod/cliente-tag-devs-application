import { IResponsavelLegal, NewResponsavelLegal } from './responsavel-legal.model';

export const sampleWithRequiredData: IResponsavelLegal = {
  id: 29803,
  nome: 'Pizza Architect',
};

export const sampleWithPartialData: IResponsavelLegal = {
  id: 59030,
  nome: 'panel',
};

export const sampleWithFullData: IResponsavelLegal = {
  id: 42230,
  nome: 'channels Roupas',
};

export const sampleWithNewData: NewResponsavelLegal = {
  nome: 'Central Macao',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
