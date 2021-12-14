import { makeStyles } from '@mui/styles';
import HomeIcon from '@mui/icons-material/Home';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import PersonPinIcon from '@mui/icons-material/PersonPin';
import { useEffect, useState } from 'react';
import EmojiPeopleIcon from '@mui/icons-material/EmojiPeople';
import url from  'src/utils/constant';
import axios from 'axios';
import BlockIcon from '@mui/icons-material/Block';
import { IconButton } from '@mui/material';
import LinearDeterminate from '../Bars/BarsR';
import ModalMonitoreo from '../Custom/ModalMonitoreo';

//import { tiempo,prueba} from "src/content/applications/Transactions/PageHeader";
//import { tiempo } from '../Tiempo/tiempo';

const vectorX = 70;
const vectorY = 50;
const path = [ ];

const intervaloTiempo=300;


const bloqueosData = [
  {
  "bloqueo": [
  {x: 20, y: 30},
  ],
  startTime: "2021-11-11T01:30:51.969+00:00",
  endTime: "2021-11-11T02:30:51.969+00:00"
  },
  {
  "bloqueo": [
  {x: 45, y: 50},
  ],
  startTime: "2021-11-11T01:30:51.969+00:00",
  endTime: "2021-11-11T02:30:51.969+00:00"
  },
]

const pathsaux=[]
//const intervaloTiempo = prueba;
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
             ],
    "startTime": "2021-11-11T01:30:51.969+00:00",
    "endTime": "2021-11-11T02:05:51.969+00:00"
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
    "endTime": "2021-11-11T02:35:51.969+00:00"
   }
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
    console.log("xxxxxxxxxxxxxxx");
    console.log(ruta);
    return ruta;
  };
const obtenerBloqueo = (path) => {
    const bloqueo = [
      {x:20 , y:30},
    ];
    console.log(bloqueo);
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


    const classes = useStyles();
    const map = [];
    useEffect(() => {
      

      const intervalV=500;
      const interval = setInterval(() => {
        let arr;
       
        //console.log(paths);
        arr = paths.map((path) => {
          const now = new Date();
          const date = new Date(path.date);
          const nowFixed = new Date(path.nowFixed);
          const dateStart = new Date(path.dateStart);
          
          let rest = now.getTime() - date.getTime() - (nowFixed.getTime() - dateStart.getTime());

          const posAux = Math.floor( (rest/60000) * ((intervalV*intervaloTiempo)/60)); // aumentando la velocidad
         
          //console.log(posAux);
          

          if (posAux === path.ruta.length) {
            setRuta(null);
            return null;
          } else return { ...path, pos: posAux };
        });
        setPaths(arr.filter((el) => el != null));
        // setPaths(...paths, pos)
      }, 500);
       
      return () => clearInterval(interval);
    }, [paths]);
    
   //bloqueos

   useEffect(() => {

    const intervalV=50;
    const interval = setInterval(() => {
      let arr;
     
      //console.log(paths);
      arr = paths.map((bloqueo) => {
        const now = new Date();
        const date = new Date(bloqueo.date);
        const nowFixed = new Date(bloqueo.nowFixed);
        const dateStart = new Date(bloqueo.dateStart);
        let rest = now.getTime() - date.getTime() - (nowFixed.getTime() - dateStart.getTime());
       // const pos = Math.floor(((new Date(path.startOfBreak) - new Date(path.startTime)) / 60000) * (speed / 60));
        //console.log("asdlkasjdlsad");
        const bloqueoAux = Math.floor( (rest/60000) * ((intervalV*intervaloTiempo)/60)); // aumentando la velocidad
        //console.log(posAux);
        if (bloqueoAux === bloqueo.ruta.length) {
            setRuta(null);
            return null;
          } else return { ...path, pos: bloqueoAux };
        });
        setBloqueos(arr.filter((el) => el != null));
    }, 500);
    return () => clearInterval(interval);
  }, [paths]);

    useEffect(() => {

    
       console.log("Mapa 3 días");
       
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
        

          
          setPaths(
            pathsaux.map((path) => {
              return {
                ...path.path,
                ruta: obtenerRuta(path.path),
                pos: 0,
                date: implementarFecha(pathsaux[0].startTime,path.startTime),
                dateStart: pathsaux[0].startTime,
                nowFixed: new Date(),
              };
            })
          );
          
          setBloqueos(
            bloqueosData.map((bloqueo) => {
              return {
                ...bloqueo.bloqueo,
                bloqueos: obtenerBloqueo(bloqueo.bloqueo),
                pos: 0,
                date: implementarFecha(bloqueosData[0].startTime,bloqueo.startTime),
                dateEnd: implementarFecha(bloqueosData[0].startTime,bloqueo.endTime),
                dateStart: bloqueosData[0].startTime,
                nowFixed: new Date(),
              };
            })
          );
           
      }, []);   
    

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