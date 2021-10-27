import { ReactNode } from 'react';

import DesignServicesTwoToneIcon from '@mui/icons-material/DesignServicesTwoTone';
import BrightnessLowTwoToneIcon from '@mui/icons-material/BrightnessLowTwoTone';
import MmsTwoToneIcon from '@mui/icons-material/MmsTwoTone';
import TableChartTwoToneIcon from '@mui/icons-material/TableChartTwoTone';
import AccountCircleTwoToneIcon from '@mui/icons-material/AccountCircleTwoTone';
import BallotTwoToneIcon from '@mui/icons-material/BallotTwoTone';
import BeachAccessTwoToneIcon from '@mui/icons-material/BeachAccessTwoTone';
import EmojiEventsTwoToneIcon from '@mui/icons-material/EmojiEventsTwoTone';
import FilterVintageTwoToneIcon from '@mui/icons-material/FilterVintageTwoTone';
import HowToVoteTwoToneIcon from '@mui/icons-material/HowToVoteTwoTone';
import LocalPharmacyTwoToneIcon from '@mui/icons-material/LocalPharmacyTwoTone';
import RedeemTwoToneIcon from '@mui/icons-material/RedeemTwoTone';
import SettingsTwoToneIcon from '@mui/icons-material/SettingsTwoTone';
import TrafficTwoToneIcon from '@mui/icons-material/TrafficTwoTone';
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
