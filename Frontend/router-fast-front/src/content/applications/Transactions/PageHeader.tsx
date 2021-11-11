import { Typography, Button, Grid, FormControl, Card, InputLabel, Select, MenuItem} from '@mui/material';
import axios from 'axios';
import CustomSnackbar from 'src/components/Custom/CustomSnackbar';
import { useEffect, useState } from 'react';
import { styled } from '@mui/material/styles';
import UploadTwoToneIcon from '@mui/icons-material/UploadTwoTone';
import { parse } from 'papaparse';
import { Container, CardHeader, CardContent, Divider } from '@mui/material';
import MapR from 'src/components/MapR/MapR';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import url from  'src/utils/constant';
import { Send } from '@mui/icons-material';

const Input = styled('input')({
  display: 'none',
});

function PageHeader() {
  const [alert, setAlert] = useState({isOpen: false, message: '', type: ''})
  const [filesCheck, setFilesCheck] = useState(0)
  const [start, setStart] = useState(false)
  const [fileA, setFileA] = useState(null)
  const [fileB, setFileB] = useState(null)
  const [simulacion,setSimulacion] = useState(false);

  const sendFiles= ()=>{
    setStart(true);
    // a 3 días  -> true -> 1 
    setSimulacion(true);
  }

  useEffect(()=>{
    if (start) {
      sendFileA();
      sendFileB();

    }
  },[start])

  const sendFileA = async () => {
    let formData = new FormData();
    formData.append("file", fileA);
    axios.post(`${url}/archivos/upload` ,formData, { /* UPLOAD PEDIDOS */
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then((r) => {
    setAlert({isOpen: true, message: 'Pedidos cargados de manera exitosa.', type: 'success'})
    setFilesCheck(filesCheck+1)
    
  }).catch((e) =>{
    setAlert({isOpen: true, message: 'Hubo un error al cargar el archivo.', type: 'error'})
  })
  }

  const sendFileB = async () => {
    let formData = new FormData();
    formData.append("file", fileA);
    axios.post(`${url}/archivos/bloqueos` ,formData, { /* UPLOAD  BLOQUEOS  */
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then((r) => {
    setAlert({isOpen: true, message: 'Pedidos cargados de manera exitosa.', type: 'success'})
    setFilesCheck(filesCheck+1)
    
  }).catch((e) =>{
    setAlert({isOpen: true, message: 'Hubo un error al cargar el archivo.', type: 'error'})
  })
  }


  const uploadFileA = (file) => {
    setFileA(file);

  }
 const uploadFileB = (file) => {
    setFileB(file);

  }
  
  return (
    <Grid container justifyContent="space-between" alignItems="center">
      <Grid item>
        <Typography variant="h3" component="h3" gutterBottom>
          Empezar simulación
        </Typography>
      </Grid>
      <FormControl fullWidth sx={{ mt: 3 }}>
        <InputLabel id="demo-simple-select-label">
          Escoga el tipo de simulación
        </InputLabel>
        <Select
          labelId="demo-simple-select-label"
          id="select-simulacion"
          label="Seleccione el tipo de simulación"
        >
          <MenuItem value={1}>Simulación de 3 Días</MenuItem>
          <MenuItem value={2}>Simulación hasta el colapso logístico</MenuItem>
        </Select>
      </FormControl>
      {/*<Grid item>
        
        <Button
          sx={{ mt: { xs: 2, md: 0 } }}
          variant="contained"
          startIcon={<AddTwoToneIcon fontSize="small" />}
        >
          Crear usuario
        </Button>
      </Grid>
      */}
      
      <Grid container spacing={2} sx={{ mt: 2 }}>
        <Grid item xs={2}>
          <label htmlFor="change-cover">
            <Button
            startIcon={<UploadTwoToneIcon />} 
            variant="contained" id="bt-subir-pedidos" component="label">
            <Input accept="text/csv,.csv,.txt"  hidden multiple type="file" onChange={(e) => uploadFileA(e.target.files[0])} />
              Subir Pedidos
            </Button>
          </label>
        </Grid>
        <Grid item xs={2}>
          <label htmlFor="change-cover">
            <Button 
            startIcon={<UploadTwoToneIcon />} 
            variant="contained" component="label">
            <Input accept="text/csv,.csv,.txt"  hidden multiple type="file" onChange={(e) => uploadFileB(e.target.files[0])} />
              Subir Bloqueos
            </Button>
          </label>
        </Grid>

        <Grid item xs={2}>
          <Button variant="contained"  onClick={sendFiles}>
            Empezar Simulación
          </Button>
        </Grid>
      </Grid>
      <CustomSnackbar alert={alert} setAlert={setAlert}/>
      
      <MapR simulacion={simulacion}></MapR>
      <CardContent>
                <Card sx={{ maxWidth: 250}}>
                  <CardContent>
                  
                    <Typography gutterBottom variant="h5" component="div">
                    <LocalShippingIcon style={{ color: '#35737D'}} />
                      Chófer: Franco Gamarra <br/>
                      Pedidos: <br/>
                    </Typography>
                    <Typography variant="body2" color="green">
                      A : Entregado <br/>
                    </Typography>
                    <Typography variant="body2" color="red"> 
                      B : En ruta (15' min de retraso)
                    </Typography>
                  </CardContent>
                </Card>
              </CardContent>
      <CardContent>
                <Card sx={{ maxWidth: 120 }}>

                  <CardContent>
                  <LocalShippingIcon style={{ color: '#35737D' }} />
                    <Typography gutterBottom variant="h5" component="div">
                      Chófer Mario Andonaire:
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      Placa: INF-123
                      Pedido 1 : Entregado
                      Pedido 2 : En ruta
                    </Typography>
                  </CardContent>
                </Card>
              </CardContent>
    
    </Grid>
    
  );
}

export default PageHeader;