import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICliente, NewCliente } from '../cliente.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICliente for edit and NewClienteFormGroupInput for create.
 */
type ClienteFormGroupInput = ICliente | PartialWithRequiredKeyOf<NewCliente>;

type ClienteFormDefaults = Pick<NewCliente, 'id' | 'possuiBeneficioAtivo' | 'tags'>;

type ClienteFormGroupContent = {
  id: FormControl<ICliente['id'] | NewCliente['id']>;
  name: FormControl<ICliente['name']>;
  foto: FormControl<ICliente['foto']>;
  fotoContentType: FormControl<ICliente['fotoContentType']>;
  dataNascimento: FormControl<ICliente['dataNascimento']>;
  possuiBeneficioAtivo: FormControl<ICliente['possuiBeneficioAtivo']>;
  rendaBruta: FormControl<ICliente['rendaBruta']>;
  responsavelLegal: FormControl<ICliente['responsavelLegal']>;
  tags: FormControl<ICliente['tags']>;
};

export type ClienteFormGroup = FormGroup<ClienteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClienteFormService {
  createClienteFormGroup(cliente: ClienteFormGroupInput = { id: null }): ClienteFormGroup {
    const clienteRawValue = {
      ...this.getFormDefaults(),
      ...cliente,
    };
    return new FormGroup<ClienteFormGroupContent>({
      id: new FormControl(
        { value: clienteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(clienteRawValue.name, {
        validators: [Validators.required],
      }),
      foto: new FormControl(clienteRawValue.foto),
      fotoContentType: new FormControl(clienteRawValue.fotoContentType),
      dataNascimento: new FormControl(clienteRawValue.dataNascimento),
      possuiBeneficioAtivo: new FormControl(clienteRawValue.possuiBeneficioAtivo),
      rendaBruta: new FormControl(clienteRawValue.rendaBruta),
      responsavelLegal: new FormControl(clienteRawValue.responsavelLegal),
      tags: new FormControl(clienteRawValue.tags ?? []),
    });
  }

  getCliente(form: ClienteFormGroup): ICliente | NewCliente {
    return form.getRawValue() as ICliente | NewCliente;
  }

  resetForm(form: ClienteFormGroup, cliente: ClienteFormGroupInput): void {
    const clienteRawValue = { ...this.getFormDefaults(), ...cliente };
    form.reset(
      {
        ...clienteRawValue,
        id: { value: clienteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ClienteFormDefaults {
    return {
      id: null,
      possuiBeneficioAtivo: false,
      tags: [],
    };
  }
}
