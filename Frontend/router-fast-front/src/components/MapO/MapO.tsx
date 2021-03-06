import { makeStyles } from '@mui/styles';
import HomeIcon from '@mui/icons-material/Home';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import PersonPinIcon from '@mui/icons-material/PersonPin';
import { useCallback, useEffect, useRef, useState } from 'react';
import url from  'src/utils/constant';
import axios from 'axios';
import BlockIcon from '@mui/icons-material/Block';
import { Card, CardContent, IconButton ,CardActionArea,CardMedia, Typography} from '@mui/material';
import simulacionDia from '../ServerEvents/serverEvents';
import Grid from '@mui/material/Grid';
import ModalMonitoreo from '../Custom/ModalMonitoreo';
import { HomeWork } from '@mui/icons-material';
import EmojiPeople from '@mui/icons-material/EmojiPeople';
import { Dialog } from '@mui/material';
import * as moment from 'moment';

import sendBlock from '../bloqueos/bloquedia';

const vectorX = 70;
const vectorY = 50;
const path = [ ];

/*
const data = [
  {x: 20, y: 30},
  {x: 25, y: 30},
  {x: 30, y: 30}
  
]
*/
/*
Del dia 13:
10,20,10,40,35,40
*/

const bloqueosData = [
    {x: 20, y: 30},
    {x: 25, y: 30},
    {x: 30, y: 30}
  ]
const obtenerRuta = (path) => {
    const ruta = [];
    for (let i = 0; i < path.length - 1; i++) {
      if (path[i].x === path[i + 1].x) {
        if (path[i].y > path[i + 1].y) ruta.push({ ...path[i], next: 'up' });
        else ruta.push({ ...path[i], next: 'down' });
      } else {
        if (path[i].x > path[i + 1].xA) ruta.push({ ...path[i], next: 'left' });
        else ruta.push({ ...path[i], next: 'right' });
      }
    }
    return ruta;
  };
  
const ruta = obtenerRuta(path);
/*
const obtenerPosicion = (path, speed) => {
  let pos;
  
  if (path.startOfBreak && new Date(path.startOfBreak) < new Date() && new Date(path.endOfBreak) > new Date()) {
    pos = Math.floor(((new Date(path.startOfBreak) - new Date(path.startTime)) / 60000) * (speed / 60));
  } else if (path.endOfBreak && new Date(path.endOfBreak) < new Date()) {
    pos = Math.floor(((new Date() - new Date(path.startTime) - 3600000) / 60000) * (speed / 60));
  } else pos = Math.floor(((new Date() - new Date(path.startTime)) / 60000) * (speed / 60));
  return pos;
};

*/
/*Funci??n para proporcionar el tiempo de simulaci??n */
const implementarFecha = (startTime, dateTime) => {
  const startTimeX = new Date(startTime);
  const dateTimeX = new Date(dateTime);
  const resultado = startTimeX.getTime() + (dateTimeX.getTime() - startTimeX.getTime())/500;
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
      marginTop: '35px',
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

const MapO=(props: simulacion )=>{
    const pedAtendidos=1;
    const pedPendientes=2;
    const camiones=3;
    const pedPenalizados=1;

    const [openInfo, setOpenInfo] = useState(false);
    const [ruta, setRuta] = useState([]);
    const [caminos, setCaminos] = useState([]);
    const [bloqueos, setBloqueos] = useState([]);
    const [sendRequest, setSendRequest] = useState(false);
    const classes = useStyles();
    const map = [];
    /*
    const listaInt = useRef([]);
    const lista = useCallback(()=> {
      console.log("entro al callback"); 
      if(!caminos.length && listaInt.current.length){
      setCaminos(listaInt.current.shift()); 
    }
  },[listaInt]);
  */
    /*
    useEffect(() => {
      //const pos=5;
      const intervalTime = 500;
      //console.log(paths);
      const arr = caminos.map((path) => {
        const now = new Date();
        const date = new Date(path.date);
        const nowFixed = new Date(path.nowFixed);
        const dateStart = new Date(path.dateStart);
        
        let rest = now.getTime() - date.getTime() - (nowFixed.getTime() - dateStart.getTime());
        
        //console.log("asdlkasjdlsad");
        const posAux = Math.floor(rest / intervalTime); // restar los milisegundos para igual
        //al tiempo de inicio del primer pedido 
        //console.log(posAux);
        if (posAux === path.ruta.length) {
          setRuta(null);
          return null;
        } else return { ...path, pos: posAux };
      });

      //console.log(arr);
      setTimeout(() => {
        setCaminos(arr.filter((el) => el != null));
      }, 2500)
      

    
    }, [caminos]);

    */
    useEffect(() => {
      /*
      Tiempo real:
      
      1 min -> 60000
      60 seg -> 60 000
      60 min ->3600000
      */
      const intervalTime = 72000; //velocidad del cami??n //   1 
      const interval = setInterval(() => {
        let arr;
        /*
        
        if(!caminos.length && listaInt.current.length){
          setCaminos(listaInt.current.shift());
        }
        */

        //console.log(paths);
        arr = caminos.map((path) => {


          const now = new Date();
          const date = new Date(path.date);
          const nowFixed = new Date(path.nowFixed);
          const dateStart = new Date(path.dateStart);
          
          let rest = now.getTime() - date.getTime() - (nowFixed.getTime() - dateStart.getTime());
          let posAux = Math.floor(rest / intervalTime); // restar los milisegundos para igual
          //camion averiado
          /*
          if (path.vehicle.status === 'DAMAGED') {
            posAux = path.route?.findIndex(({ posX, posY }) => posX === path.breakdownX && posY === path.breakdownY);
            return posAux;
          }
          */
          //al tiempo de inicio del primer pedido 
          //console.log(posAux);
          if (posAux === path.ruta.length) {
            setRuta(null);
            return null;
          } else return { ...path, pos: posAux };
        });

        setCaminos(arr.filter((el) => el != null));
        //console.log(caminos);
        // setPaths(...paths, pos)

      }, intervalTime);
      return () => clearInterval(interval);
    }, [caminos]);

    useEffect(() => {
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
          };
        });
        if(newData?.length)
          //listaInt.current.push(newData);
          setCaminos(newData);
        };
        console.log("enviando");
        simulacionDia(funcionRequest);
    },[]);
        
    /*
    export default funcion= () => {
      const [sendRequest, setSendRequest] = useState(false);
    
      useEffect(() => {
        if(sendRequest){
           //send the request
           setSendRequest(false);
        }
      },
      [sendRequest]);
    
      return (
        <input type="button" disabled={sendRequest} onClick={() => setSendRequest(true)}></input>
      );
      }
    */
    /*
    useEffect(() => {
      console.log('mostrando bloqueos');

      const interval = setInterval(() => {
         axios.get(url+ ' se requiere servicio de la ruta que mande los bloqueos en la fecha designada').then((e) => { 
           setBloqueos(e.data);
         });
       }, 20000);
       return () => clearInterval(interval);
     }, []);
    */

    useEffect(() => {
      //console.log('mostrando bloqueos');
      //var dd=new Date();
      //var date=dd.getFullYear() +' ' + dd.getMonth()
      const semana=7;
      const fechaPedido=moment.default().format('YYYY/MM/DD HH:mm');
      const fechaFin=moment.default().add(semana,"days").format('YYYY/MM/DD HH:mm');
      
      //console.log(fechaPedido);
      //console.log(fechaFin);

      const interval = setInterval(() => {
      
        const mario = {
              fechaInicio: fechaPedido,
              fechaFin: fechaFin
            }
            axios.post(url + '/bloqueo/getBloqueosFechas',JSON.stringify(mario) , {
                headers: { 'Content-Type': 'application/json' }
            })
            .then((r) => {
              //console.log("entra al axios de bloqueos")
              setBloqueos(r.data);
              //console.log("r:",r.data);
              //console.log(data);
              //console.log("Bloqueos agregado exitosamente.");         
            });
       }, 200000);
       return () => clearInterval(interval);
     }, []);

    const handleClose = () => {
      setOpenInfo(false);
    };

    const handleClickOpen = (ruta) => {
      setRuta(ruta);
      setOpenInfo(true);
    }
   //console.log("bloqueos data",bloqueos);
    
    //setPedidos(ruta?.filter(nodo => nodo.destino));
    for (let i = 0; i < vectorY; i++) { //50
      const squareRows = [];
      for (let j = 0; j < vectorX; j++) { //70
        squareRows.push(
          <div
            className={classes.cuadrado}
            style={{
              borderRightColor:
                ruta?.find(({ x, y, next }) => x === j && y === i - 1 && next === 'down') ||
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
          {bloqueos?.find(({ coordenadaX, coordenadaY }) => coordenadaX === j && coordenadaY === i) && (
            <div className={classes.icon}>
              <IconButton>
                <BlockIcon style={{ color: 'red', fontSize: '20px' }} />
              </IconButton>
            </div>
          )}
            {ruta?.find(({ x, y }) => x === j && y === i)?.destino ? (
            <div className={classes.icon} style={{ transform: 'rotate(0deg)' }}>
              <EmojiPeople style={{ color: '#424774'}} />
            </div>
          ) : null}
          {caminos?.map((path) => { 
            // let aux = new Date() - path.date;
            if (path?.ruta[path.pos] && path?.ruta[path.pos].x === j && path?.ruta[path.pos].y ===i) {
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
    
                      {/*<LocalShippingIcon
                        style={{
                          color: path.vehicle.status === 'DAMAGED' ? '#D3D3D3' : 'orange',
                          fontSize: '30px',
                        }}
          
                      />*/}
                

                  <LocalShippingIcon style={{ color: '#35737D' }} onClick={() => handleClickOpen(path.ruta)} />
                  
                  
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
    {/* <Grid item xs={4}>
        <Card sx={{ my:2 }}>
        <CardActionArea sx={{p:2}}>
          <Typography variant="h4">Pedidos Atendidos : {pedAtendidos}</Typography>
          <Typography variant="h4">Pedidos Pendientes : {pedPendientes}</Typography>
          <Typography variant="h4">Camiones: {camiones}</Typography>
          <Typography variant="h4">Pedidos Penalizados: {pedPenalizados}</Typography>
        </CardActionArea>
        </Card>
        </Grid> */}
        
        <Grid item xs={10}>
        <Card sx={{ my:2 }}>
        <CardActionArea sx={{p:2}}>
          <img
                    alt="BTC"
                    width="80%"
                    src="/static/images/placeholders/logo/leyendadiaBeibi.png"
                  />
        </CardActionArea>
        </Card>
        </Grid>
        <Dialog onClose={handleClose} open={openInfo} >
          <ModalMonitoreo onClose={handleClose} ruta={ruta}/>
        </Dialog>
        </>
    );
  }
  
  export default MapO;