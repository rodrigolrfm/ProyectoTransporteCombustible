import { Typography, Button, Grid, FormControl, InputLabel, Select, MenuItem} from '@mui/material';
import axios from 'axios';
import CustomSnackbar from 'src/components/Custom/CustomSnackbar';
import { useState } from 'react';
import { styled } from '@mui/material/styles';
import { parse } from 'papaparse';
import MapR from 'src/components/MapR/MapR';
const Input = styled('input')({
  display: 'none',
});




function PageHeader() {
  const [alert, setAlert] = useState({isOpen: false, message: '', type: ''})
  const [filesCheck, setFilesCheck] = useState(0)

  const uploadFile = async (file) => {
    let formData = new FormData();
    formData.append("file", file);
    axios.post('upload_file', formData, {
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
            <Button variant="contained" id="bt-subir-pedidos" component="label">
            <Input accept="text/csv,.csv,.txt"  hidden multiple type="file" onChange={(e) => uploadFile(e.target.files[0])} />
              Subir Pedidos
            </Button>
          </label>
        </Grid>
        <Grid item xs={2}>
          <label htmlFor="change-cover">
            <Button variant="contained" component="label">
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
    
    </Grid>
    
  );
}

export default PageHeader;