import { ReactNode } from 'react';


import TableChartTwoToneIcon from '@mui/icons-material/TableChartTwoTone';
import AccountCircleTwoToneIcon from '@mui/icons-material/AccountCircleTwoTone';
import BallotTwoToneIcon from '@mui/icons-material/BallotTwoTone';
import VerifiedUserTwoToneIcon from '@mui/icons-material/VerifiedUserTwoTone';

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
        name: 'Operación Día a Día',
        icon: BallotTwoToneIcon,
        link: '/logistica/operacion'
      },
      {
        name: 'Simulación',
        icon: TableChartTwoToneIcon,
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
