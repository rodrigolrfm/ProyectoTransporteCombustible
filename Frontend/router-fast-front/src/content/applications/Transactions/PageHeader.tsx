import { Typography, Button, Grid, FormControl, InputLabel, MenuItem} from '@mui/material';
import axios from 'axios';
import CustomSnackbar from 'src/components/Custom/CustomSnackbar';
import { useEffect, useState,useRef } from 'react';
import { styled } from '@mui/material/styles';
import UploadTwoToneIcon from '@mui/icons-material/UploadTwoTone';
import MapR from 'src/components/MapR/MapR';
import MapC from 'src/components/MapC/MapC';
import url from  'src/utils/constant';
import Select, { SelectChangeEvent } from '@mui/material/Select';

import BarsR from 'src/components/Bars/BarsR';
import { StyledEngineProvider } from '@mui/material';


const Input = styled('input')({
  display: 'none',
});

//export const tiempo = 300;
//export const prueba = 300;
/*
15 minutos-> 300
10 minutos--> 400
5 minutos--> 500
*/

function PageHeader() {
  const myRef = useRef(null)
  const executeScroll = () => myRef.current.scrollIntoView()   
  const [alert, setAlert] = useState({isOpen: false, message: '', type: ''})
  const [filesCheck, setFilesCheck] = useState(0)
  const [start, setStart] = useState(false)
  const [fileA, setFileA] = useState(null)
  const [fileB, setFileB] = useState(null)
  const [simulacion,setSimulacion] = useState(0)
  const [tipo, setTipo] = useState('')
  

  const handleChange = (event: SelectChangeEvent) => {
    setTipo(event.target.value);
  };

  const cargarFiles=()=>{
    setStart(true);

  }

  
  const sendFiles= ()=>{
    
    
    // a 3 días  -> true -> 1

    
    if( tipo == "1"){
      setSimulacion(1);
    }
    if( tipo == "2"){
      setSimulacion(2);      
    }
    setTimeout(() => {executeScroll()}, 1000);
    
  }

  useEffect(()=>{
    if (start) {
      sendFileA();
      sendFileB();
      console.log("Envio de archivos..")
      console.log("Tipo:", tipo)
    }
  },[start])

  
  const sendFileA = async () => {
    let formData = new FormData();
    formData.append("file", fileA);

    
    axios.post(`${url}/pedido/cargaMasivaPedidos` ,formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }).then((r) => {
    setAlert({isOpen: true, message: 'Pedidos cargados de manera exitosa.', type: 'success'})
    setFilesCheck(filesCheck+1)
    
  }).catch((e) =>{
    setAlert({isOpen: true, message: 'Pedidos y Bloqueos cargados de manera exitosa.', type: 'success'})
  })
  
  }
  
  const sendFileB = async () => {
    let formData = new FormData();
    formData.append("file", fileB);
    
    
    // colocar servicio de la carga masiva de bloqueos
    axios.post(`${url}/bloqueo/cargaMasivaBloqueos` ,formData, { 
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    
    })
    
    
    .then((r) => {
    setAlert({isOpen: true, message: 'Pedidos cargados de manera exitosa.', type: 'success'})
    setFilesCheck(filesCheck+1)
    
    }).catch((e) =>{
    setAlert({isOpen: true, message: 'Pedidos y Bloqueos cargados de manera exitosa.', type: 'success'})
    })
    

  }

  const uploadFileA = (file) => {
    setFileA(file);
    console.log("pedido almacenado");
  }
 const uploadFileB = (file) => {
    setFileB(file);
    console.log("bloqueo almacenado");
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
          value={tipo}
          onChange={handleChange}
        >
          <MenuItem value={1}>Simulación de 3 Días</MenuItem>
          <MenuItem value={2}>Simulación hasta el colapso logístico</MenuItem>
        </Select>
      </FormControl>
      
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
        <Button variant="contained"  onClick={cargarFiles}>
            Cargar Archivos
          </Button>
          </Grid>
          <Grid item xs={2}>
          <Button variant="contained"  onClick={sendFiles}>
            Empezar Simulación
          
          </Button>
          
        </Grid>
      </Grid>
      <CustomSnackbar alert={alert} setAlert={setAlert}/>

      {simulacion===1? <div ref={myRef}> 
      <StyledEngineProvider injectFirst>
        
      {/*<BarsR/>*/}
      </StyledEngineProvider>
      
      <MapR simulacion={simulacion}></MapR></div>:<div>
      </div>
      }
      {simulacion===2? <div ref={myRef}> <MapC simulacion={simulacion}></MapC></div>:<div>
      </div>
      }
      
      {/*
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
        */
               
      }
      
    
    </Grid>
    
  );
}

export default PageHeader;