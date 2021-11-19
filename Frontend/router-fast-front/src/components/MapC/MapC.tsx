import { makeStyles } from '@mui/styles';
import HomeIcon from '@mui/icons-material/Home';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import PersonPinIcon from '@mui/icons-material/PersonPin';
import { useEffect, useState } from 'react';
import url from  'src/utils/constant';
import axios from 'axios';
const vectorX = 70;
const vectorY = 50;
const path = [ ];

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
  
//const ruta = obtenerRuta(path);
/*Función para proporcionar el tiempo de simulación */
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
      marginTop: '72px',
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

const MapC=(props: simulacion )=>{

    const [ruta, setRuta] = useState([]);
    const [paths, setPaths] = useState([]);
    const classes = useStyles();
    const map = [];
    useEffect(() => {

      const intervalTime = 500;
      const interval = setInterval(() => {
        let arr;
        
        //console.log(paths);
        arr = paths.map((path) => {
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
        setPaths(arr.filter((el) => el != null));
        // setPaths(...paths, pos)
      }, intervalTime);
      return () => clearInterval(interval);
    }, [paths]);
    useEffect(() => {

      /*

      let serverURL = new EventSource(url + "/archivos/simularRutas");
      let data = [];
          
      // Inicializa el sentEvent 
      serverURL.onopen=function (event) {
          console.log ("Conexión cliente servidor");
      }
      serverURL.addEventListener("MAPAS", function(e) {
          console.log(event);
          data = JSON.parse(event.data);
          if(data.flag == 3){
            // Cerrar conexión
              serverURL.close();
          }
          setPaths(
            data.paths.map((path) => {
              return {
                ...path.path,
                ruta: obtenerRuta(path.path),
                pos: 0,
                date: implementarFecha(data.paths[0].startTime,path.startTime),
                dateStart: data.paths[0].startTime,
                nowFixed: new Date(),
              };
            })
        })

      serverURL.οnerrοr=function (event) {
          serverURL.close();
      }

      /*
      */
        console.log("Mapa Colapso");
        axios
        .post(url + "/ejecutar/simularRutasColapso")
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
    }, []);   
    for (let i = 0; i  < vectorY; i ++) {
      const squareRows = [];
      for (let j = 0; j < vectorX; j++) {
        squareRows.push(
          <div
            className={classes.cuadrado}
            style={{
              borderRightColor:
                ruta?.find(({ x, y, next }) => x === j && y === i  - 1 && next === 'down') ||
                ruta?.find(({ x, y, next }) => x === j && y === i  && next === 'up')
                  ? '#424774'
                  : '#D89F7B',
  
              borderBottomColor:
                ruta?.find(({ x, y, next }) => x === j - 1 && y === i  && next === 'right') ||
                ruta?.find(({ x, y, next }) => x === j && y === i  && next === 'left')
                  ? '#424774'
                  : '#D89F7B',
            }}
          >
            {ruta?.find(({ x, y }) => x === j && y === i )?.destino ? (
            <div className={classes.icon} style={{ transform: 'rotate(0deg)' }}>
              <PersonPinIcon style={{ color: '#424774'}} />
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
            {i  === 8 && j === 12 && (
              <div className={classes.icon} style={{ transform: 'rotate(0deg)' }}>
                <HomeIcon style={{ color: '#35737D' }} />
              </div>
            )}
            {i  === 42 && j === 42 && (
              <div className={classes.icon} style={{ transform: 'rotate(0deg)' }}>
                <HomeIcon style={{ color: '#35737D' }} />
              </div>
            )}
            {i  === 3 && j === 63 && (
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
  
  export default MapC;