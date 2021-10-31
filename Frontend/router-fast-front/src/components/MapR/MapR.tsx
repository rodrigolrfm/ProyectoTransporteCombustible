import { makeStyles } from '@mui/styles';
import HomeIcon from '@mui/icons-material/Home';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import PersonPinIcon from '@mui/icons-material/PersonPin';
import { useEffect, useState } from 'react';
const boardX = 70;
const boardY = 50;
const path = [
  { x: 12, y: 42, destino: 0 },
  { x: 13, y: 42, destino: 0 },
  { x: 14, y: 42, destino: 0 },
  { x: 15, y: 42, destino: 0 },
  { x: 16, y: 42, destino: 0 },
  { x: 17, y: 42, destino: 0 },
  { x: 18, y: 42, destino: 0 },
  { x: 19, y: 42, destino: 0 },
  { x: 20, y: 42, destino: 1 },
  { x: 21, y: 42, destino: 0 },
  { x: 22, y: 42, destino: 0 },
  { x: 23, y: 42, destino: 0 },
  { x: 24, y: 42, destino: 0 },
  { x: 25, y: 42, destino: 0 },
  { x: 26, y: 42, destino: 0 },
  { x: 27, y: 42, destino: 0 },
  { x: 28, y: 42, destino: 0 },
  { x: 29, y: 42, destino: 1 },
  { x: 29, y: 42, destino: 0 },
  { x: 28, y: 42, destino: 0 },
  { x: 27, y: 42, destino: 0 },
  { x: 26, y: 42, destino: 0 },
  { x: 25, y: 42, destino: 0 },
  { x: 24, y: 42, destino: 0 },
  { x: 23, y: 42, destino: 0 },
  { x: 22, y: 42, destino: 0 },
  { x: 21, y: 42, destino: 0 },
  { x: 20, y: 42, destino: 0 },
  { x: 19, y: 42, destino: 0 },
  { x: 18, y: 42, destino: 0 },
  { x: 17, y: 42, destino: 0 },
  { x: 16, y: 42, destino: 0 },
  { x: 15, y: 42, destino: 0 },
  { x: 14, y: 42, destino: 0 },
  { x: 13, y: 42, destino: 0 },
  { x: 12, y: 42, destino: 0 },
];

const vehiculo = { codigo: 'INF-13L', conductor: 'Franco Gamarra' };
const pathsAux = [
  { vehiculo: vehiculo, ruta: path
    , date: new Date(), pos: 0 },
];

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


const useStyles = makeStyles((theme) => ({
    square: {
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
      transform: 'rotate(0deg)',
    },
  }));

const MapR=()=>{

    const [ruta, setRuta] = useState([]);
    const [paths, setPaths] = useState([]);
    const classes = useStyles();
    const map = [];
    useEffect(() => {
      const interval = setInterval(() => {
        let arr;
        arr = paths.map((path) => {
          const now = new Date();
          const date = new Date(path.date);
          console.log(now.getMilliseconds());
          console.log(date.getMilliseconds());
          const posAux = Math.floor((now.getTime() - date.getTime()) / 1500);
          console.log(posAux);
          if (posAux === path.ruta.length) {
            setRuta(null);
            return null;
          } else return { ...path, pos: posAux };
        });
        setPaths(arr.filter((el) => el != null));
        // setPaths(...paths, pos)
      }, 1500);
      return () => clearInterval(interval);
    }, [paths]);
    useEffect(() => {
      setPaths(
        pathsAux.map((path) => {
          return { ...path, ruta: obtenerRuta(path.ruta) };
        })
      );
    }, []);
    for (let i = 0; i < boardY; i++) {
      const squareRows = [];
      for (let j = 0; j < boardX; j++) {
        squareRows.push(
          <div
            className={classes.square}
            style={{
              borderRightColor:
                ruta?.find(({ x, y }) => x === j && y === i - 1)?.next === 'down'
                  ? '#D89F7B'
                  : ruta?.find(({ x, y }) => x === j && y === i)?.next === 'up'
                  ? '#ff0000'
                  : '#D89F7B',
              borderBottomColor:
                ruta?.find(({ x, y }) => x === j - 1 && y === i)?.next === 'right'
                  ? '#ff0000'
                  : ruta?.find(({ x, y }) => x === j && y === i)?.next === 'left'
                  ? '#ff0000'
                  : '#D89F7B',
            }}
          >
            {ruta?.find(({ x, y }) => x === j && y === i)?.destino ? (
            <div className={classes.icon} style={{ transform: 'rotate(0deg)' }}>
              <PersonPinIcon style={{ color: '#ff0000'}} />
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
            {i === 42 && j === 12 && (
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