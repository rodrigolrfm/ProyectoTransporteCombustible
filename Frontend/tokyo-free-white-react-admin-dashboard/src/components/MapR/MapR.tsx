import { makeStyles } from '@mui/styles';
import HomeIcon from '@mui/icons-material/Home';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
const boardX = 50;
const boardY = 35;
const path = [
  { x: 0, y: 0 },
  { x: 0, y: 1 },
  { x: 0, y: 2 },
  { x: 1, y: 2 },
  { x: 2, y: 2 },
  { x: 3, y: 2 },
  { x: 3, y: 3 },
  { x: 2, y: 3 },
  { x: 2, y: 2 },
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
      width: '19px',
      height: '19px',
      flexShrink: 0,
      position: 'relative',
  
      // background-color: white;
      // border: 2px solid;
    },
  
    map: {
      backgroundColor: '#F8DBB1',
      marginTop: '74px',
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
    const classes = useStyles();
    const map = [];
    for (let i = 0; i < boardY; i++) {
      const squareRows = [];
      for (let j = 0; j < boardX; j++) {
        squareRows.push(
          <div
            className={classes.square}
            style={{
              borderRightColor:
                ruta.find(({ x, y }) => x === j && y === i - 1)?.next === 'down'
                  ? '#D89F7B'
                  : ruta.find(({ x, y }) => x === j && y === i)?.next === 'up'
                  ? '#D89F7B'
                  : 'D89F7B',
              borderBottomColor:
                ruta.find(({ x, y }) => x === j - 1 && y === i)?.next === 'right'
                  ? '#D89F7B'
                  : ruta.find(({ x, y }) => x === j && y === i)?.next === 'left'
                  ? '#D89F7B'
                  : 'D89F7B',
            }}
          >
            {i === 0 && j === 0 && (
              <div className={classes.icon} style={{ transform: 'rotate(90deg)' }}>
                <LocalShippingIcon style={{ color: '#35737D' }} />
              </div>
            )}
            {i === 2 && j === 5 && (
              <div className={classes.icon} style={{ transform: 'rotate(0deg)' }}>
                <LocalShippingIcon style={{ color: '#35737D' }} />
              </div>
            )}
            {i === 30 && j === 30 && (
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