import { makeStyles } from '@mui/styles';
import HomeIcon from '@mui/icons-material/Home';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import { useEffect,  useState,useCallback } from 'react';
import EmojiPeopleIcon from '@mui/icons-material/EmojiPeople';
import axios from 'axios';
import BlockIcon from '@mui/icons-material/Block';
import { IconButton } from '@mui/material';
import simulacion3Dias from '../ServerEvents/serverMapR';
import { HomeWork } from '@mui/icons-material';
import ModalR from '../Custom/ModalR';
import bloqueosData from './BloqueosR';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import { Dialog, Button, Card } from '@mui/material';

//1 hora es -> 12.5 segundos
const vectorX = 70;
const vectorY = 50;
const path = [ ];
const intervaloTiempo=500; // 500, 200
const avance = 286* 1000; // 288 , 115
const hojasR=[];
//500 es a 15 minutos
// 1 es tiempo real

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
              {"destino": 1, "x": 28, "y": 8},
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
    "startTime": "2021-12-21T01:22:00.000-05:00",
    "endTime": "2021-12-21T02:05:00.000-05:00"
   },
   { "path": [
      {"destino": 0, "x": 12, "y": 8},
      {"destino": 0, "x": 12, "y": 9},
      {"destino": 0, "x": 12, "y": 10},
      {"destino": 0, "x": 12, "y": 11},
      {"destino": 0, "x": 12, "y": 12},
      {"destino": 0, "x": 12, "y": 13}
             ],
    "startTime": "2021-12-21T02:22:00.000-05:00",
    "endTime": "2021-12-21T03:35:00.000-05:00"
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
    {"destino": 1, "x": 14, "y": 8},
    {"destino": 0, "x": 13, "y": 8},
    {"destino": 0, "x": 12, "y": 8},
   ],
"startTime": "2021-12-21T03:22:00.000-05:00",
"endTime": "2021-12-21T04:30:00.000-05:00"
},
]

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
      bottom: '-14px',
      zIndex: 1,
      transform: 'rotate(90deg)',
    },
    iconb:{
      position: 'absolute',
      right: '0px',
      // top: '-15px',
      bottom: '0px',
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
    const [openInfo, setOpenInfo] = useState(false);
    const classes = useStyles();
    const map = [];
    let Relativo=0;
    Relativo=(new Date(paths[0]?.dateStart)).getTime();


    useEffect(() => { 
      /*  
      //console.log("antes del if",primero); 
      if (primero && !isNaN(Relativo)){
        setRelativo(Relativo);
        setPrimero(false);
        //console.log("fin del if",primero); 
      }

      else if(!primero){
       //console.log("relativo antes",relativo);
      Relativo = relativo + avance;
      setRelativo(Relativo);
      const blocksList = bloqueosData.filter((item)=>Relativo>(new Date(item.startTime)).getTime() && Relativo<(new Date(item.endTime)).getTime()).map((item)=>({x:item.bloqueo.x, y:item.bloqueo.y}))
       //console.log("dentro de intervalo",bloqueosData.map((item)=>({start: (new Date(item.startTime)).getTime(),end: (new Date(item.endTime)).getTime()})),Relativo);
      // console.log(blocksList);
       //setBloqueos(blocksList);
      
     // console.log("path",paths[0]);
      }
      */
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
          //let tiempoRelativo = now.getTime() - (nowFixed.getTime() - dateStart.getTime());
            //console.log(boolTiempo,boolTiempo.current);
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

   /* 
  const findBlocks = useCallback(
      (relativo) => {
         //console.log("relativo",new Date(relativo));
      //if(!primero){
        //console.log("dentro");
       const interval2 = setInterval(() => {
         const blocksList = bloqueosData.filter((item)=>relativo>(new Date(item.startTime)).getTime() && relativo<(new Date(item.endTime)).getTime()).map((item)=>({x:item.bloqueo.x, y:item.bloqueo.y}))
         //console.log("dentro de intervalo",bloqueosData.map((item)=>({start: (new Date(item.startTime)).getTime(),end: (new Date(item.endTime)).getTime()})),relativo);
         //console.log(blocksList);
         //setBloqueos(blocksList);
       }, 5000);
       return () => {clearInterval(interval2)};
      // }
 
      },[],
      )   
      */
     useEffect(()=>{
      console.log("Bloqueos Y:", bloqueos); 

     },[bloqueos]);

    useEffect(() => {
       console.log("Mapa 3 días");
       //con server event
       const funcionRequest= (data)=>{
        data = JSON.parse(data);
        hojasR.push(data.arregloRutas);
        setBloqueos(data.bloqueoTresDias);
        console.log("Bloqueos:", data.bloqueoTresDias);
        console.log("Bloqueos x:", bloqueos);
        let newData = data.arregloRutas.paths?.map((path) => {
          return {
            ...path.path,
            ruta: obtenerRuta(path.path),
            pos: 0,
            date: implementarFecha(data.arregloRutas.paths[0].startTime,path.startTime),
            dateStart: data.arregloRutas.paths[0].startTime,
            nowFixed: new Date(),
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

      const handleClose = () => {
        setOpenInfo(false);
      };
           
      
      const handleClickOpen = (ruta) => {
        setOpenInfo(true);
      }
      
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
            {bloqueos?.find((bloqueo) => { 
              //console.log("Bloques XY", bloqueo.bloqueo);
              return (bloqueo.bloqueo.x === j && bloqueo.bloqueo.y === i)}) && (
            <div className={classes.iconb}>
              <IconButton>
                <BlockIcon style={{ color: 'red', fontSize: '20px' }} />
              </IconButton>
            </div>
          )}
            {ruta?.find(({ x, y }) => x === j && y === i)?.destino?(
            <div className={classes.icon} style={{ transform: 'rotate(0deg)' }}>
              <EmojiPeopleIcon style={{ color: '#424774'}} />
            </div>
          ) : null}
          {paths?.map((path) => {
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
                <HomeWork style={{ color: '#35737D' }} />
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
      <>
      
        <div className={classes.map}>{map}</div>
        <div>
        <Card sx={{ minWidth: 275 , mt: 1 }}>
      <CardContent>
        <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
          Hoja de Ruta
        </Typography>
        <Typography variant="h5" component="div">

        </Typography>
      </CardContent>
    </Card>
        </div>
        
      </>
    );
  }
  
  export default MapR;