import { makeStyles } from '@mui/styles';
import HomeIcon from '@mui/icons-material/Home';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import PersonPinIcon from '@mui/icons-material/PersonPin';
import { useEffect, useRef, useState,useCallback} from 'react';
import EmojiPeopleIcon from '@mui/icons-material/EmojiPeople';
import url from  'src/utils/constant';
import axios from 'axios';
import BlockIcon from '@mui/icons-material/Block';
import { IconButton } from '@mui/material';
import LinearDeterminate from '../Bars/BarsR';
import ModalMonitoreo from '../Custom/ModalMonitoreo';
import { SettingsOverscanTwoTone } from '@mui/icons-material';
import simulacion3Dias from '../ServerEvents/serverMapR';


//import { tiempo,prueba} from "src/content/applications/Transactions/PageHeader";
//import { tiempo } from '../Tiempo/tiempo';

//1 hora es -> 12.5 segundos

const vectorX = 70;
const vectorY = 50;
const path = [ ];



const intervaloTiempo=500;
// 1 es tiempo real

/*
const bloqueosData = [
  {
  "bloqueo": 
  {x: 10, y: 30},
  
  startTime: "2021-11-11T01:30:59.969+00:00",
  endTime: "2021-11-11T02:30:51.969+00:00"
  },
  {
  "bloqueo": 
  {x: 20, y: 20},
  
  startTime: "2021-11-11T03:30:59.969+00:00",
  endTime: "2021-11-11T04:30:51.969+00:00"
  },
  {
    "bloqueo": 
    {x: 35, y: 35},
    
    startTime: "2021-11-11T02:30:59.969+00:00",
    endTime: "2021-11-11T03:30:51.969+00:00"
    }
]
*/


//const pathsaux=[]
//const intervaloTiempo = prueba;

const bloqueosActuales=[]

/*
//json de rutas y bloqueos
const pathsaux =[

  { "path": [ 

              {"destino": 0, "x": 12, "y": 8},
              {"destino": 0, "x": 13, "y": 8},
              {"destino": 0, "x": 14, "y": 8},
              {"destino": 0, "x": 15, "y": 8},
              {"destino": 0, "x": 16, "y": 8},
              {"destino": 0, "x": 17, "y": 8},
              {"destino": 0, "x": 18, "y": 8},
              {"destino": 0, "x": 19, "y": 8},
              {"destino": 0, "x": 20, "y": 8},
              {"destino": 0, "x": 21, "y": 8},
              {"destino": 0, "x": 22, "y": 8},
              {"destino": 0, "x": 23, "y": 8},
              {"destino": 0, "x": 24, "y": 8},
              {"destino": 0, "x": 25, "y": 8},
              {"destino": 0, "x": 26, "y": 8},
              {"destino": 0, "x": 27, "y": 8},
              {"destino": 0, "x": 28, "y": 8},
              {"destino": 0, "x": 29, "y": 8},
              {"destino": 0, "x": 30, "y": 8},
              {"destino": 0, "x": 31, "y": 8},
              {"destino": 0, "x": 32, "y": 8},
              {"destino": 0, "x": 33, "y": 8},
              {"destino": 0, "x": 34, "y": 8},
              {"destino": 0, "x": 35, "y": 8},
              {"destino": 0, "x": 36, "y": 8},
              {"destino": 0, "x": 37, "y": 8},
              {"destino": 0, "x": 36, "y": 8},
              {"destino": 0, "x": 35, "y": 8},
              {"destino": 0, "x": 34, "y": 8},
              {"destino": 0, "x": 33, "y": 8},
              {"destino": 0, "x": 32, "y": 8},
              {"destino": 0, "x": 31, "y": 8},
              {"destino": 0, "x": 30, "y": 8},
              {"destino": 0, "x": 29, "y": 8},
              {"destino": 0, "x": 28, "y": 8},
              {"destino": 0, "x": 27, "y": 8},
              {"destino": 0, "x": 26, "y": 8},
              {"destino": 0, "x": 25, "y": 8},
              {"destino": 0, "x": 24, "y": 8},
              {"destino": 0, "x": 23, "y": 8},
              {"destino": 0, "x": 22, "y": 8},
              {"destino": 0, "x": 21, "y": 8},
              {"destino": 0, "x": 20, "y": 8},
              {"destino": 0, "x": 19, "y": 8},
              {"destino": 0, "x": 18, "y": 8},
              {"destino": 0, "x": 17, "y": 8},
              {"destino": 0, "x": 16, "y": 8},
              {"destino": 0, "x": 15, "y": 8},
              {"destino": 0, "x": 14, "y": 8},
              {"destino": 0, "x": 13, "y": 8},
              {"destino": 0, "x": 12, "y": 8},
             ],
    "startTime": "2021-11-11T01:30:51.969+00:00",
    "endTime": "2021-11-11T02:05:51.969+00:00",
    "bloqueo": 
     {x: 20, y: 30},
  
    startTime: "2021-11-11T01:30:59.969+00:00",
    endTime: "2021-11-11T02:30:51.969+00:00"
  
   },
   { "path": [
      {"destino": 0, "x": 12, "y": 8},
      {"destino": 0, "x": 12, "y": 9},
      {"destino": 0, "x": 12, "y": 10},
      {"destino": 0, "x": 12, "y": 11},
      {"destino": 0, "x": 12, "y": 12},
      {"destino": 0, "x": 12, "y": 13}
             ],
    "startTime": "2021-11-11T02:30:51.969+00:00",
    "endTime": "2021-11-11T02:35:51.969+00:00",
    "bloqueo": 
    {x: 20, y: 20},
  
    startTime: "2021-11-11T03:30:59.969+00:00",
    endTime: "2021-11-11T04:30:51.969+00:00"

   },
   { "path": [ 

    {"destino": 0, "x": 12, "y": 8},
    {"destino": 0, "x": 13, "y": 8},
    {"destino": 0, "x": 14, "y": 8},
    {"destino": 0, "x": 15, "y": 8},
    {"destino": 0, "x": 16, "y": 8},
    {"destino": 0, "x": 17, "y": 8},
    {"destino": 0, "x": 18, "y": 8},
    {"destino": 0, "x": 19, "y": 8},
    {"destino": 0, "x": 20, "y": 8},
    {"destino": 0, "x": 21, "y": 8},
    {"destino": 0, "x": 22, "y": 8},
    {"destino": 0, "x": 23, "y": 8},
    {"destino": 0, "x": 24, "y": 8},
    {"destino": 0, "x": 25, "y": 8},
    {"destino": 0, "x": 26, "y": 8},
    {"destino": 0, "x": 27, "y": 8},
    {"destino": 0, "x": 28, "y": 8},
    {"destino": 0, "x": 29, "y": 8},
    {"destino": 0, "x": 30, "y": 8},
    {"destino": 0, "x": 31, "y": 8},
    {"destino": 0, "x": 32, "y": 8},
    {"destino": 0, "x": 33, "y": 8},
    {"destino": 0, "x": 34, "y": 8},
    {"destino": 0, "x": 35, "y": 8},
    {"destino": 0, "x": 36, "y": 8},
    {"destino": 0, "x": 37, "y": 8},
    {"destino": 0, "x": 36, "y": 8},
    {"destino": 0, "x": 35, "y": 8},
    {"destino": 0, "x": 34, "y": 8},
    {"destino": 0, "x": 33, "y": 8},
    {"destino": 0, "x": 32, "y": 8},
    {"destino": 0, "x": 31, "y": 8},
    {"destino": 0, "x": 30, "y": 8},
    {"destino": 0, "x": 29, "y": 8},
    {"destino": 0, "x": 28, "y": 8},
    {"destino": 0, "x": 27, "y": 8},
    {"destino": 0, "x": 26, "y": 8},
    {"destino": 0, "x": 25, "y": 8},
    {"destino": 0, "x": 24, "y": 8},
    {"destino": 0, "x": 23, "y": 8},
    {"destino": 0, "x": 22, "y": 8},
    {"destino": 0, "x": 21, "y": 8},
    {"destino": 0, "x": 20, "y": 8},
    {"destino": 0, "x": 19, "y": 8},
    {"destino": 0, "x": 18, "y": 8},
    {"destino": 0, "x": 17, "y": 8},
    {"destino": 0, "x": 16, "y": 8},
    {"destino": 0, "x": 15, "y": 8},
    {"destino": 0, "x": 14, "y": 8},
    {"destino": 0, "x": 13, "y": 8},
    {"destino": 0, "x": 12, "y": 8},
   ],
"startTime": "2021-11-11T03:30:51.969+00:00",
"endTime": "2021-11-11T04:30:51.969+00:00",
"bloqueo": 
    {x: 35, y: 35},
    
    startTime: "2021-11-11T02:30:59.969+00:00",
    endTime: "2021-11-11T03:30:51.969+00:00"
    
},
]
*/

const bloqueosData = [
  {"bloqueo":
        {x: 29,
        y: 30},
        startTime: "2021-12-11T01:22:00.000-05:00",
        endTime: "2021-12-11T01:42:00.000-05:00"
    },
    {"bloqueo":
        {x: 30,
        y: 21},
        startTime: "2021-12-11T01:22:00.000-05:00",
        endTime: "2021-12-11T01:42:00.000-05:00"
    },
    {"bloqueo":
        {x: 39,
        y: 20},
        startTime: "2021-12-11T01:22:00.000-05:00",
        endTime: "2021-12-11T01:42:00.000-05:00"
    },
    {"bloqueo":
        {x: 40,
        y: 11},
        startTime: "2021-12-11T01:22:00.000-05:00",
        endTime: "2021-12-11T01:42:00.000-05:00"
    },
    {"bloqueo":
        {x: 40,
        y: 10},
        startTime: "2021-12-11T01:22:00.000-05:00",
        endTime: "2021-12-11T01:42:00.000-05:00"
    },
    {"bloqueo":
        {x: 35,
        y: 26},
        startTime: "2021-12-11T03:15:00.000-05:00",
        endTime: "2021-12-11T07:20:00.000-05:00"
    },
    {"bloqueo":
        {x: 44,
        y: 25},
        startTime: "2021-12-11T03:15:00.000-05:00",
        endTime: "2021-12-11T07:20:00.000-05:00"
    },
    {"bloqueo":
        {x: 45,
        y: 25},
        startTime: "2021-12-11T03:15:00.000-05:00",
        endTime: "2021-12-11T07:20:00.000-05:00"
    },
    {"bloqueo":
        {x: 34,
        y: 10},
        startTime: "2021-12-11T05:35:00.000-05:00",
        endTime: "2021-12-11T11:27:00.000-05:00"
    },
    {"bloqueo":
        {x: 35,
        y: 10},
        startTime: "2021-12-11T05:35:00.000-05:00",
        endTime: "2021-12-11T11:27:00.000-05:00"
    },
    {"bloqueo":
        {x: 44,
        y: 45},
        startTime: "2021-12-11T19:15:00.000-05:00",
        endTime: "2021-12-11T21:13:00.000-05:00"
    },
    {"bloqueo":
        {x: 45,
        y: 41},
        startTime: "2021-12-11T19:15:00.000-05:00",
        endTime: "2021-12-11T21:13:00.000-05:00"
    },
    {"bloqueo":
        {x: 45,
        y: 40},
        startTime: "2021-12-11T19:15:00.000-05:00",
        endTime: "2021-12-11T21:13:00.000-05:00"
    },

   
    {"bloqueo":
        {x: 10,
        y: 39},
        startTime: "2021-12-11T21:02:00.000-05:00",
        endTime: "2021-12-12T01:19:00.000-05:00"
    },
    {"bloqueo":
        {x: 34,
        y: 40},
        startTime: "2021-12-11T21:02:00.000-05:00",
        endTime: "2021-12-12T01:19:00.000-05:00"
    },
    {"bloqueo":
        {x: 35,
        y: 40},
        startTime: "2021-12-11T21:02:00.000-05:00",
        endTime: "2021-12-12T01:19:00.000-05:00"
    },
    {"bloqueo":
        {x: 5,
        y: 34},
        startTime: "2021-12-11T22:26:00.000-05:00",
        endTime: "2021-12-12T03:58:00.000-05:00"
    },
    {"bloqueo":
        {x: 5,
        y: 35},
        startTime: "2021-12-11T22:26:00.000-05:00",
        endTime: "2021-12-12T03:58:00.000-05:00"
    },

    // Dia 12
    {"bloqueo":
        {x: 20,
        y: 34},
        startTime: "2021-12-12T00:17:00.000-05:00",
        endTime: "2021-12-12T04:52:00.000-05:00"
    },
    {"bloqueo":
        {x: 20,
        y: 35},
        startTime: "2021-12-12T00:17:00.000-05:00",
        endTime: "2021-12-12T04:52:00.000-05:00"
    },
    {"bloqueo":
        {x: 29,
        y: 30},
        startTime: "2021-12-12T02:32:00.000-05:00",
        endTime: "2021-12-12T03:12:00.000-05:00"
    },
    {"bloqueo":
        {x: 30,
        y: 21},
        startTime: "2021-12-12T02:32:00.000-05:00",
        endTime: "2021-12-12T03:12:00.000-05:00"
    },
    {"bloqueo":
        {x: 39,
        y: 20},
        startTime: "2021-12-12T02:32:00.000-05:00",
        endTime: "2021-12-12T03:12:00.000-05:00"
    },
    {"bloqueo":
        {x: 40,
        y: 11},
        startTime: "2021-12-12T02:32:00.000-05:00",
        endTime: "2021-12-12T03:12:00.000-05:00"
    },
    {"bloqueo":
        {x: 40,
        y: 10},
        startTime: "2021-12-12T02:32:00.000-05:00",
        endTime: "2021-12-12T03:12:00.000-05:00"
    },
    {"bloqueo":
        {x: 35,
        y: 26},
        startTime: "2021-12-12T04:16:00.000-05:00",
        endTime: "2021-12-12T04:24:00.000-05:00"
    },
    {"bloqueo":
        {x: 44,
        y: 25},
        startTime: "2021-12-12T04:16:00.000-05:00",
        endTime: "2021-12-12T04:24:00.000-05:00"
    },
    {"bloqueo":
        {x: 45,
        y: 25},
        startTime: "2021-12-12T04:16:00.000-05:00",
        endTime: "2021-12-12T04:24:00.000-05:00"
    },
    {"bloqueo":
        {x: 34,
        y: 10},
        startTime: "2021-12-12T05:38:00.000-05:00",
        endTime: "2021-12-12T11:45:00.000-05:00"
    },
    {"bloqueo":
        {x: 35,
        y: 10},
        startTime: "2021-12-12T05:38:00.000-05:00",
        endTime: "2021-12-12T11:45:00.000-05:00"
    },
    {"bloqueo":
        {x: 10,
        y: 39},
        startTime: "2021-12-12T20:04:00.000-05:00",
        endTime: "2021-12-13T03:16:00.000-05:00"
    },
    {"bloqueo":
        {x: 34,
        y: 40},
        startTime: "2021-12-12T20:04:00.000-05:00",
        endTime: "2021-12-13T03:16:00.000-05:00"
    },
    {"bloqueo":
        {x: 35,
        y: 40},
        startTime: "2021-12-12T20:04:00.000-05:00",
        endTime: "2021-12-13T03:16:00.000-05:00"
    },
    {"bloqueo":
        {x: 15,
        y: 34},
        startTime: "2021-12-12T22:17:00.000-05:00",
        endTime: "2021-12-13T05:25:00.000-05:00"
    },
    {"bloqueo":
        {x: 15,
        y: 35},
        startTime: "2021-12-12T22:17:00.000-05:00",
        endTime: "2021-12-13T05:25:00.000-05:00"
    },
    {"bloqueo":
        {x: 44,
        y: 35},
        startTime: "2021-12-13T00:50:00.000-05:00",
        endTime: "2021-12-13T03:58:00.000-05:00"
    },
    {"bloqueo":
        {x: 45,
        y: 35},
        startTime: "2021-12-13T00:50:00.000-05:00",
        endTime: "2021-12-13T03:58:00.000-05:00"
    },
    {"bloqueo":
        {x: 29,
        y: 30},
        startTime: "2021-12-13T02:30:00.000-05:00",
        endTime: "2021-12-13T04:57:00.000-05:00"
    },
    {"bloqueo":
        {x: 30,
        y: 21},
        startTime: "2021-12-13T02:30:00.000-05:00",
        endTime: "2021-12-13T04:57:00.000-05:00"
    },
    {"bloqueo":
        {x: 39,
        y: 20},
        startTime: "2021-12-13T02:30:00.000-05:00",
        endTime: "2021-12-13T04:57:00.000-05:00"
    },
    {"bloqueo":
        {x: 40,
        y: 11},
        startTime: "2021-12-13T02:30:00.000-05:00",
        endTime: "2021-12-13T04:57:00.000-05:00"
    },
    {"bloqueo":
        {x: 40,
        y: 10},
        startTime: "2021-12-13T02:30:00.000-05:00",
        endTime: "2021-12-13T04:57:00.000-05:00"
    },
    {"bloqueo":
        {x: 35,
        y: 26},
        startTime: "2021-12-13T04:57:00.000-05:00",
        endTime: "2021-12-13T09:12:00.000-05:00"
    },
    {"bloqueo":
        {x: 44,
        y: 25},
        startTime: "2021-12-13T04:57:00.000-05:00",
        endTime: "2021-12-13T09:12:00.000-05:00"
    },
    {"bloqueo":
        {x: 45,
        y: 25},
        startTime: "2021-12-13T04:57:00.000-05:00",
        endTime: "2021-12-13T09:12:00.000-05:00"
    },
    {"bloqueo":
        {x: 44,
        y: 45},
        startTime: "2021-12-13T19:20:00.000-05:00",
        endTime: "2021-12-14T00:56:00.000-05:00"
    },
    {"bloqueo":
        {x: 45,
        y: 41},
        startTime: "2021-12-13T19:20:00.000-05:00",
        endTime: "2021-12-14T00:56:00.000-05:00"
    },
    {"bloqueo":
        {x: 45,
        y: 40},
        startTime: "2021-12-13T19:20:00.000-05:00",
        endTime: "2021-12-14T00:56:00.000-05:00"
    },
    {"bloqueo":
        {x: 10,
        y: 39},
        startTime: "2021-12-13T21:28:00.000-05:00",
        endTime: "2021-12-14T02:02:00.000-05:00"
    },
    {"bloqueo":
        {x: 34,
        y: 40},
        startTime: "2021-12-13T21:28:00.000-05:00",
        endTime: "2021-12-14T02:02:00.000-05:00"
    },
    {"bloqueo":
        {x: 35,
        y: 40},
        startTime: "2021-12-13T21:28:00.000-05:00",
        endTime: "2021-12-14T02:02:00.000-05:00"
    },
    {"bloqueo":
        {x: 5,
        y: 34},
        startTime: "2021-12-13T23:14:00.000-05:00",
        endTime: "2021-12-14T03:14:00.000-05:00"
    },
    {"bloqueo":
        {x: 5,
        y: 35},
        startTime: "2021-12-13T23:14:00.000-05:00",
        endTime: "2021-12-14T03:14:00.000-05:00"
    },



]
const pathsaux=[];
/*
const pathsaux =[

  { "path": [ 

              {"destino": 0, "x": 12, "y": 8},
              {"destino": 0, "x": 13, "y": 8},
              {"destino": 0, "x": 14, "y": 8},
              {"destino": 0, "x": 15, "y": 8},
              {"destino": 0, "x": 16, "y": 8},
              {"destino": 0, "x": 17, "y": 8},
              {"destino": 0, "x": 18, "y": 8},
              {"destino": 0, "x": 19, "y": 8},
              {"destino": 0, "x": 20, "y": 8},
              {"destino": 0, "x": 21, "y": 8},
              {"destino": 0, "x": 22, "y": 8},
              {"destino": 0, "x": 23, "y": 8},
              {"destino": 0, "x": 24, "y": 8},
              {"destino": 0, "x": 25, "y": 8},
              {"destino": 0, "x": 26, "y": 8},
              {"destino": 0, "x": 27, "y": 8},
              {"destino": 0, "x": 28, "y": 8},
              {"destino": 0, "x": 29, "y": 8},
              {"destino": 0, "x": 30, "y": 8},
              {"destino": 0, "x": 31, "y": 8},
              {"destino": 0, "x": 32, "y": 8},
              {"destino": 0, "x": 33, "y": 8},
              {"destino": 0, "x": 34, "y": 8},
              {"destino": 0, "x": 35, "y": 8},
              {"destino": 0, "x": 36, "y": 8},
              {"destino": 0, "x": 37, "y": 8},
              {"destino": 0, "x": 36, "y": 8},
              {"destino": 0, "x": 35, "y": 8},
              {"destino": 0, "x": 34, "y": 8},
              {"destino": 0, "x": 33, "y": 8},
              {"destino": 0, "x": 32, "y": 8},
              {"destino": 0, "x": 31, "y": 8},
              {"destino": 0, "x": 30, "y": 8},
              {"destino": 0, "x": 29, "y": 8},
              {"destino": 0, "x": 28, "y": 8},
              {"destino": 0, "x": 27, "y": 8},
              {"destino": 0, "x": 26, "y": 8},
              {"destino": 0, "x": 25, "y": 8},
              {"destino": 0, "x": 24, "y": 8},
              {"destino": 0, "x": 23, "y": 8},
              {"destino": 0, "x": 22, "y": 8},
              {"destino": 0, "x": 21, "y": 8},
              {"destino": 0, "x": 20, "y": 8},
              {"destino": 0, "x": 19, "y": 8},
              {"destino": 0, "x": 18, "y": 8},
              {"destino": 0, "x": 17, "y": 8},
              {"destino": 0, "x": 16, "y": 8},
              {"destino": 0, "x": 15, "y": 8},
              {"destino": 0, "x": 14, "y": 8},
              {"destino": 0, "x": 13, "y": 8},
              {"destino": 0, "x": 12, "y": 8},
             ],
    "startTime": "2021-12-11T01:22:00.000-05:00",
    "endTime": "2021-12-11T02:05:00.000-05:00"
   },
   { "path": [
      {"destino": 0, "x": 12, "y": 8},
      {"destino": 0, "x": 12, "y": 9},
      {"destino": 0, "x": 12, "y": 10},
      {"destino": 0, "x": 12, "y": 11},
      {"destino": 0, "x": 12, "y": 12},
      {"destino": 0, "x": 12, "y": 13}
             ],
    "startTime": "2021-12-11T02:22:00.000-05:00",
    "endTime": "2021-12-11T03:35:00.000-05:00"
   },
   { "path": [ 

    {"destino": 0, "x": 12, "y": 8},
    {"destino": 0, "x": 13, "y": 8},
    {"destino": 0, "x": 14, "y": 8},
    {"destino": 0, "x": 15, "y": 8},
    {"destino": 0, "x": 16, "y": 8},
    {"destino": 0, "x": 17, "y": 8},
    {"destino": 0, "x": 18, "y": 8},
    {"destino": 0, "x": 19, "y": 8},
    {"destino": 0, "x": 20, "y": 8},
    {"destino": 0, "x": 21, "y": 8},
    {"destino": 0, "x": 22, "y": 8},
    {"destino": 0, "x": 23, "y": 8},
    {"destino": 0, "x": 24, "y": 8},
    {"destino": 0, "x": 25, "y": 8},
    {"destino": 0, "x": 26, "y": 8},
    {"destino": 0, "x": 27, "y": 8},
    {"destino": 0, "x": 28, "y": 8},
    {"destino": 0, "x": 29, "y": 8},
    {"destino": 0, "x": 30, "y": 8},
    {"destino": 0, "x": 31, "y": 8},
    {"destino": 0, "x": 32, "y": 8},
    {"destino": 0, "x": 33, "y": 8},
    {"destino": 0, "x": 34, "y": 8},
    {"destino": 0, "x": 35, "y": 8},
    {"destino": 0, "x": 36, "y": 8},
    {"destino": 0, "x": 37, "y": 8},
    {"destino": 0, "x": 36, "y": 8},
    {"destino": 0, "x": 35, "y": 8},
    {"destino": 0, "x": 34, "y": 8},
    {"destino": 0, "x": 33, "y": 8},
    {"destino": 0, "x": 32, "y": 8},
    {"destino": 0, "x": 31, "y": 8},
    {"destino": 0, "x": 30, "y": 8},
    {"destino": 0, "x": 29, "y": 8},
    {"destino": 0, "x": 28, "y": 8},
    {"destino": 0, "x": 27, "y": 8},
    {"destino": 0, "x": 26, "y": 8},
    {"destino": 0, "x": 25, "y": 8},
    {"destino": 0, "x": 24, "y": 8},
    {"destino": 0, "x": 23, "y": 8},
    {"destino": 0, "x": 22, "y": 8},
    {"destino": 0, "x": 21, "y": 8},
    {"destino": 0, "x": 20, "y": 8},
    {"destino": 0, "x": 19, "y": 8},
    {"destino": 0, "x": 18, "y": 8},
    {"destino": 0, "x": 17, "y": 8},
    {"destino": 0, "x": 16, "y": 8},
    {"destino": 0, "x": 15, "y": 8},
    {"destino": 0, "x": 14, "y": 8},
    {"destino": 0, "x": 13, "y": 8},
    {"destino": 0, "x": 12, "y": 8},
   ],
"startTime": "2021-12-11T03:22:00.000-05:00",
"endTime": "2021-12-11T04:30:00.000-05:00"
},
]

*/
const obtenerRuta = (path) => {
    const ruta = [];
    for (let i = 0; i < path.length - 1; i++) {
      if (path[i].x === path[i + 1].x) {
        if (path[i].y > path[i + 1].y) ruta.push({ ...path[i], next: 'up' });
        else ruta.push({ ...path[i], next: 'down' });
      } else {
        if (path[i].x > path[i + 1].x) ruta.push({ ...path[i], next: 'left' });
        else ruta.push({ ...path[i], next: 'right' });
      }
    }

    return ruta;
  };

const obtenerBloqueo = (path) => {
    const bloqueo = [
      {x:20 , y:30},
    ];
    return bloqueo;
  };
  
  
const ruta = obtenerRuta(path); 
/*Función para proporcionar el tiempo de simulación */
const implementarFecha = (startTime, dateTime) => {
  const startTimeX = new Date(startTime);
  const dateTimeX = new Date(dateTime);
  const resultado = startTimeX.getTime() + (dateTimeX.getTime() - startTimeX.getTime())/intervaloTiempo;
  return new Date (resultado);
  
};

const useStyles = makeStyles((theme) => ({
    cuadrado: {
      borderColor: '#D89F7B',
      borderWidth: '0px 1.5px 1.5px 0px',
      border: 'solid',
      width: '17px',
      height: '17px',
      flexShrink: 0,
      position: 'relative',
    },
  
    map: {
      backgroundColor: '#F8DBB1',
      marginTop: '32px',
    },
    row: {
      display: 'flex',
    },
    icon: {
      position: 'absolute',
      right: '-14px',
      // top: '-15px',
      bottom: '-16px',
      zIndex: 1,
      transform: 'rotate(90deg)',
    },
  }));

interface simulacion{simulacion : number}; // Integer

const MapR=(props: simulacion )=>{

    const [ruta, setRuta] = useState([]);
    const [paths, setPaths] = useState([]);
    const [bloqueos, setBloqueos] = useState([]);
    const [relativo, setRelativo] = useState(0);
    const [primero, setPrimero] = useState(true);

    const classes = useStyles();
    const map = [];
    const a = useRef<any>(null);
    const b = useRef<any>(null);
    //const timerBool = useRef<any>(true);
    const boolTiempo = useRef<any>(true);

    

    let Relativo=0;
    Relativo=(new Date(paths[0]?.dateStart)).getTime();
    //setRelativo(Relativo || 0);
    

    useEffect(() => {   
      //console.log("antes del if",primero); 
      if (primero && !isNaN(Relativo)){
        
        setRelativo(Relativo);
        setPrimero(false);
        //console.log("fin del if",primero); 
      }
      else if(!primero){
        
       //console.log("relativo antes",relativo);
      Relativo=relativo+266000;
      setRelativo(Relativo);
      const blocksList = bloqueosData.filter((item)=>Relativo>(new Date(item.startTime)).getTime() && Relativo<(new Date(item.endTime)).getTime()).map((item)=>({x:item.bloqueo.x, y:item.bloqueo.y}))
       console.log("dentro de intervalo",bloqueosData.map((item)=>({start: (new Date(item.startTime)).getTime(),end: (new Date(item.endTime)).getTime()})),Relativo);
       console.log(blocksList);
       setBloqueos(blocksList);
      
      
     // console.log("path",paths[0]);

      }
      //console.log("relativo",new Date(relativo));
      const intervalV=50;
      const interval = setInterval(() => {
      let arr;
      

      arr = paths.map((path) => {

          const now = new Date();
          const date = new Date(path.date);
          const nowFixed = new Date(path.nowFixed);
          const dateStart = new Date(path.dateStart);
          let rest = now.getTime() - date.getTime() - (nowFixed.getTime() - dateStart.getTime());
          const posAux = Math.floor( (rest/60000) * ((intervalV*intervaloTiempo)/60)); // aumentando la velocidad posición del arreglo 
            let tiempoRelativo = now.getTime() - (nowFixed.getTime() - dateStart.getTime());
            //console.log(boolTiempo,boolTiempo.current);
           /*
            if(boolTiempo.current){
              a.current = new Date(tiempoRelativo);
              b.current = setInterval(() => {
                console.log("entraa dentro del if");
                a.current = new Date( a.current.getTime() + 100000);
              }, 10000);
              boolTiempo.current = false;   
              console.log("a",a.current);
           }
            */   
           
           
          //console.log("a",a.current);
          if (posAux === path.ruta.length) {
            setRuta(null);
            return null;
          } else return { ...path, pos: posAux };
        });

        setPaths(arr.filter((el) => el != null));
        // setPaths(...paths, pos)
      }, 500);

      return () => {clearInterval(interval)};
    }, [paths]);
    
    
    // const findBlocks = useCallback(
    //   (relativo) => {
    //      //console.log("relativo",new Date(relativo));
    //   //if(!primero){
    //     //console.log("dentro");
    //    const interval2 = setInterval(() => {
    //      const blocksList = bloqueosData.filter((item)=>relativo>(new Date(item.startTime)).getTime() && relativo<(new Date(item.endTime)).getTime()).map((item)=>({x:item.bloqueo.x, y:item.bloqueo.y}))
    //      console.log("dentro de intervalo",bloqueosData.map((item)=>({start: (new Date(item.startTime)).getTime(),end: (new Date(item.endTime)).getTime()})),relativo);
    //      console.log(blocksList);
    //      setBloqueos(blocksList);
 
    //    }, 5000);
    //    return () => {clearInterval(interval2)};
    //   // }
 
    //   },
    //   [],
    // )

   
    

    // useEffect(()=>{
    //   // console.log("relativo",new Date(relativo));

    //   if(!primero){
    //   findBlocks(relativo);
    //   }

    // },[primero,findBlocks,relativo]);
   
    

    useEffect(() => {
       console.log("Mapa 3 días");
      
       //con server event
       
       const funcionRequest= (data)=>{
        data = JSON.parse(data);
      
        let newData = data.paths?.map((path) => {
          return {
            ...path.path,
            ruta: obtenerRuta(path.path),
            pos: 0,
            date: implementarFecha(data.paths[0].startTime,path.startTime),
            dateStart: data.paths[0].startTime,
            nowFixed: new Date(),
            primerPedido: pathsaux[0].startTime,
          };
        });
        if(newData)
          setPaths(newData);
        };


        simulacion3Dias(funcionRequest);
      
      
      // sin server events 

       /*
        axios
        .get(url + "/ejecutar/obtenerTresDias")
        .then((e) => {
          setPaths(
            e.data.paths.map((path) => {
              return {
                ...path.path,
                ruta: obtenerRuta(path.path),
                pos: 0,
                date: implementarFecha(e.data.paths[0].startTime,path.startTime),
                dateStart: e.data.paths[0].startTime,
                nowFixed: new Date(),
              };
            })
          );
        });
      
        */

        //hardcodeo
        
        /*
          setPaths(
            pathsaux.map((path) => {
              return {
                ...path.path,
                ruta: obtenerRuta(path.path),
                pos: 0,
                date: implementarFecha(pathsaux[0].startTime,path.startTime),
                dateStart: pathsaux[0].startTime,
                nowFixed: new Date(),
                primerPedido: pathsaux[0].startTime,
                
              };
            })
          );

          */
         
          
        
      }, []);


      /*
    useEffect(() => {
      console.log('mostrando bloqueos');

      const interval = setInterval(() => {
         axios.get(url+ ' se requiere servicio de la ruta que mande los bloqueos en la fecha designada').then((e) => { 
           setBloqueos(e.data);
         });
       },);
       return () => clearInterval(interval);
     }, []);
    */

    //BLOQUEOS
   
  

    for (let i = 0; i < vectorY; i++) { //50
      const squareRows = [];
      for (let j = 0; j < vectorX; j++) { //70
        squareRows.push(
          <div
            className={classes.cuadrado}
            style={{
              borderRightColor:
                ruta?.find(({ x, y, next }) => x === j &&  y === i- 1 && next === 'down') ||
                ruta?.find(({ x, y, next }) => x === j && y === i && next === 'up')
                  ? '#424774'
                  : '#D89F7B',
  
              borderBottomColor:
                ruta?.find(({ x, y, next }) => x === j - 1 && y === i && next === 'right') ||
                ruta?.find(({ x, y, next }) => x === j && y === i && next === 'left')
                  ? '#424774'
                  : '#D89F7B',
            }}
          >
            {bloqueos?.find(({ x, y }) => x === j && y === i) && (
            <div className={classes.icon}>
              <IconButton>
                <BlockIcon style={{ color: 'red', fontSize: '20px' }} />
              </IconButton>
            </div>
          )}

            {ruta?.find(({ x, y }) => x === j && y === i)?.destino ? (
            <div className={classes.icon} style={{ transform: 'rotate(0deg)' }}>
              <EmojiPeopleIcon style={{ color: '#424774'}} />
            </div>
          ) : null}
          {paths?.map((path) => {
            // let aux = new Date() - path.date;
            if (path?.ruta[path.pos] && path?.ruta[path.pos].x === j && path?.ruta[path.pos].y === i) {
              let dir = path.ruta[path.pos].next;
              return (
                <div
                  className={classes.icon}
                  style={{
                    transform:
                      dir === 'down'
                        ? 'rotate(90deg)'
                        : dir === 'up'
                        ? 'rotate(270deg)'
                        : dir === 'left'
                        ? 'rotate(180deg)'
                        : 'rotate(0deg)',
                  }}                  
                >
                  <LocalShippingIcon style={{ color: '#35737D' }} onClick={() => setRuta(path.ruta)} />
                </div>
              );
            }
          })}
            {i === 8 && j === 12 && (
              <div className={classes.icon} style={{ transform: 'rotate(0deg)' }}>
                <HomeIcon style={{ color: '#35737D' }} />
              </div>
            )}
            {i === 42 && j === 42 && (
              <div className={classes.icon} style={{ transform: 'rotate(0deg)' }}>
                <HomeIcon style={{ color: '#35737D' }} />
              </div>
            )}
            {i === 3 && j === 63 && (
              <div className={classes.icon} style={{ transform: 'rotate(0deg)' }}>
                <HomeIcon style={{ color: '#35737D' }} />
              </div>
            )}
          </div>
        );
      }
      map.push(<div className={classes.row}>{squareRows}</div>);
    }

    return (
        <div className={classes.map}>{map}</div>
    );
  }
  
  export default MapR;