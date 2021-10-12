import { ReactNode } from 'react';


import AccountCircleTwoToneIcon from '@material-ui/icons/AccountCircleTwoTone';
import BallotTwoToneIcon from '@material-ui/icons/BallotTwoTone';

import VerifiedUserTwoToneIcon from '@material-ui/icons/VerifiedUserTwoTone';

export interface MenuItem {
  link?: string;
  icon?: ReactNode;
  badge?: string;
  items?: MenuItem[];
  name: string;
}

export interface MenuItems {
  items: MenuItem[];
  heading: string;
}

const menuItems: MenuItems[] = [

  {
    heading: 'Logística',
    items: [
      {
        name: 'Simulación',
        icon: BallotTwoToneIcon,
        link: '/logistica/simulacion'
      },
    ]
  },
  {
    heading: 'Gestión',
    items: [
      
      {
        name: 'Usuarios',
        icon: AccountCircleTwoToneIcon,
        link: '/gestion/usuario',
        items: [
          {
            name: 'Crear Usuario',
            link: '/gestion/usuario/crear'
          },
          {
            name: 'Lista de usuarios',
            link: '/gestion/usuario/listar'
          }
        ]
      }
    ]
  },
  {
    heading: 'Extra Pages',
    items: [
      {
        name: 'Cerrar Sesión',
        icon: VerifiedUserTwoToneIcon,
        link: '/overview',
        
      }
    ]
  }
];

export default menuItems;
