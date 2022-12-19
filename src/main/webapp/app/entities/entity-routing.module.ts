import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cliente',
        data: { pageTitle: 'clientetagdevsapplicationApp.cliente.home.title' },
        loadChildren: () => import('./cliente/cliente.module').then(m => m.ClienteModule),
      },
      {
        path: 'tag',
        data: { pageTitle: 'clientetagdevsapplicationApp.tag.home.title' },
        loadChildren: () => import('./tag/tag.module').then(m => m.TagModule),
      },
      {
        path: 'responsavel-legal',
        data: { pageTitle: 'clientetagdevsapplicationApp.responsavelLegal.home.title' },
        loadChildren: () => import('./responsavel-legal/responsavel-legal.module').then(m => m.ResponsavelLegalModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
