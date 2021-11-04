import { Typography, Button, Grid, FormControl, Card, InputLabel, Select, MenuItem} from '@mui/material';
import axios from 'axios';
import CustomSnackbar from 'src/components/Custom/CustomSnackbar';
import { useState } from 'react';
import { styled } from '@mui/material/styles';
import UploadTwoToneIcon from '@mui/icons-material/UploadTwoTone';
import { parse } from 'papaparse';
import { Container, CardHeader, CardContent, Divider } from '@mui/material';
import MapR from 'src/components/MapR/MapR';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import url from  'src/utils/constant';

const Input = styled('input')({
  display: 'none',
});

function PageHeader() {
  const [alert, setAlert] = useState({isOpen: false, message: '', type: ''})
  const [filesCheck, setFilesCheck] = useState(0)

  const uploadFile = async (file) => {
    let formData = new FormData();
    formData.append("file", file);
    axios.post(`${url}/upload` ,formData, { /* UPLOAD PEDIDOS, PERO Y BLOQUEOS?????  */
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
  const uploadFileB = async (file) => {
    console.log("asldkhasld");
    let formData = new FormData();
    formData.append("file", file);
    axios.post('upload_file', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
  }).then((r) => {
    setAlert({isOpen: true, message: 'Bloqueos cargados de manera exitosa.', type: 'success'})
    setFilesCheck(filesCheck+1)
  }).catch((e) =>{
    setAlert({isOpen: true, message: 'Hubo un error al cargar el archivo.', type: 'error'})
  })
  


    /*
    axios.post(process.env.REACT_APP_API_URL + "/inboundOrders/import", result).then((r) => {
      setAlert({isOpen: true, message: 'Pedidos cargados de manera exitosa.', type: 'success'})
      axios.get(process.env.REACT_APP_API_URL + "/inboundOrders").then((r) => {
        setInboundOrders(r.data);
        
      });
    });   */
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
            <Input accept="text/csv,.csv,.txt"  hidden multiple type="file" onChange={(e) => uploadFile(e.target.files[0])} />
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
          <Button variant="contained" disabled={filesCheck<2}>
            Empezar Simulación
          </Button>
        </Grid>
      </Grid>
      <CustomSnackbar alert={alert} setAlert={setAlert}/>
      
      <MapR></MapR>
      <CardContent>
                <Card sx={{ maxWidth: 250}}>
                  <CardContent>
                  
                    <Typography gutterBottom variant="h5" component="div">
                    <LocalShippingIcon style={{ color: '#35737D'}} />
                      Chófer: Franco Gamarra <br/>
                      Placa: INF-13L <br/>
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