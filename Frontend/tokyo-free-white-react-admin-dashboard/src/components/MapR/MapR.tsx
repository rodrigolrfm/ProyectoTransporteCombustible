import { makeStyles } from '@mui/styles';

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
      borderColor: '#DBA581',
      borderWidth: '0px 1.2px 1.2px 0px',
      border: 'solid',
      width: '21px',
      height: '21px',
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
      right: '-15px',
      top: '-15px',
      transform: 'rotate(0deg)',
    },
  }));

const MapR=()=>{
    const classes = useStyles();
    const map = [];
    for (let i = 1; i < boardY; i++) {
        const squareRows = [];
        for (let j = 1; j < boardX; j++) {
          squareRows.push(
            <div
              className={classes.square}
              style={{
                borderRightColor: ruta.find(({ x, y }) => (x === i) && (y === j)) ? 'red' : '#DBA581',
              }}
            >
              <div className={classes.icon}>{/* <LocalShippingIcon /> */}</div>
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