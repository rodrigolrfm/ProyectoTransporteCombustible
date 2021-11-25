import { useEffect, useState } from 'react';
import { makeStyles } from '@mui/styles';
import CloseIcon from '@mui/icons-material/Close';
import { IconButton, Typography } from '@mui/material';

const useStyles = makeStyles((theme) => ({
  container: {
    height: '800px',
    width: '400px',
  },
  header: {
    display: 'flex',
    padding: '0 10px 0 20px',
    justifyContent: 'space-between',
    alignItems: 'center',
    fontSize: '24px',
    color: theme.palette.common.white,
    backgroundColor: theme.palette.primary.main,
    height: '60px',
  },
  infoContainer: {
    padding: '20px',
  },
  infoTitle: {
    fontWeight: 'bold',
    fontSize: '18px',
  },
  infoSubtitle: {
    fontWeight: 'bold',
    fontSize: '24px',
  },
  info: {
    padding: '15px',
    backgroundColor: '#E5E5E5',
    borderRadius: '10px',
  },
}));

const ModalMonitoreo = ({ onClose, ruta }) => {
  const [pedidos, setPedidos] = useState();

  useEffect(() => {
    setPedidos(ruta?.filter(nodo => nodo.destino));
  }, [])

  const classes = useStyles();
  return (
    <div className={classes.container}>
      <div className={classes.header}>
        <Typography variant="h5">
          Hoja de ruta
        </Typography>
        <IconButton style={{ color: 'white' }} onClick={onClose}>
          <CloseIcon />
        </IconButton>
      </div>
      <div className={classes.infoContainer}>
        <Typography fontWeight='bold' fontSize={16} mb={3}>Puntos de entrega:</Typography>
        {pedidos?.map((nodo, index) => {
          return (
            <>
              <Typography className={classes.infoTitle}>Pedido {index + 1}:</Typography>
              <Typography className={classes.info} mb={2} mt={1}>x: {nodo.x}; y: {nodo.y}</Typography>
            </>
        )})}
      </div>
    </div>
  );
};

export default ModalMonitoreo;
