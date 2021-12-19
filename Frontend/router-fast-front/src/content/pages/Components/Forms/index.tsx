import { Helmet } from 'react-helmet-async';

import { useState } from 'react';
import { Container, Grid, Card, CardHeader, CardContent, Divider , Button } from '@mui/material';

import MapO from 'src/components/MapO/MapO';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import { styled } from '@mui/material/styles';
import FormControl from '@mui/material/FormControl';
import * as React from 'react';
import DateTimePicker from '@mui/lab/DateTimePicker';
import axios from 'axios';
import url from  'src/utils/constant';
import * as moment from 'moment';
import CustomSnackbar from 'src/components/Custom/CustomSnackbar';
import simulacionDia from 'src/components/ServerEvents/serverEvents';
import sendBlock from 'src/components/bloqueos/bloquedia';

const Input = styled('input')({
  display: 'none',
}); 


function Forms() {

  const [posicionX,setPosicionX] = useState('')
  const [posicionY,setPosicionY] = useState('')
  const [capacidad,setCapacidad] = useState('')
  const [alert, setAlert] = useState({isOpen: false, message: '', type: ''})
  //const [value, setValue] = React.useState(new Date());
  //const [horaslimite, setHoraLimite] = React.useState(new Date());
  const [value, setValue] = React.useState(new Date());
  const [horaslimite, setHoraLimite] = useState('')


  const sendData= async ()=>{
    console.log(posicionX);
    console.log(posicionY);
    console.log(capacidad);
    console.log(value);
    console.log(horaslimite);
    const data = {
            coordenadaX: posicionX,
            coordenadaY: posicionY,
            fechaPedido: moment.default(value).format('YYYY/MM/DD HH:mm'),
            horasLimite:moment.default(value).add(horaslimite,"hours").format('YYYY/MM/DD HH:mm'),
            cantidadGLP: capacidad
        }
        axios.post(url + '/pedido/insertarPedido',JSON.stringify(data) , {
            headers: { 'Content-Type': 'application/json' }
        })
        
        .then((r) => {
          setAlert({isOpen: true, message: 'Pedido agregado exitosamente.', type: 'success'})
          //console.log(r);
          //console.log(data);
          //console.log("Pedido agregado exitosamente.");         
        
        });
        setPosicionX(" ");
        setPosicionY(" ");
        setCapacidad(" ");
        setHoraLimite(" ");
  }

 

  return (
    <>
      <Helmet>
        <title>Forms - Components</title>
      </Helmet>
      <FormControl id="pedido-form"  fullWidth sx={{ mt: 3 }}/>
      <Container maxWidth="lg">
        <Grid
          container
          direction="row"
          justifyContent="center"
          alignItems="stretch"
          spacing={3}
        >
          <Grid item xs={12}>
            <Card>
              <CardHeader title="Ingrese un nuevo pedido" />
              <Divider />
              <CardContent>
                <Box
                  component="form"
                  sx={{
                    '& .MuiTextField-root': { m: 1, width: '25ch' },
                  }}
                  noValidate
                  autoComplete="off"
                >
                    <TextField
                      required
                      id="outlined-required"
                      label="Posición X"
                      type="number"
                      style = {{width:'15ch'}}             
                      value= {posicionX}
                      name = {posicionX}
                      onChange={e => setPosicionX(e.target.value)}
                      InputLabelProps={{
                        shrink: true,
                      }}
                    />
                    <TextField
                      required
                      id="outlined-required"
                      label="Posición Y"
                      type="number"
                      style = {{width:'15ch'}}    
                      value= {posicionY}
                      name = {posicionY}
                      onChange={e => setPosicionY(e.target.value)}              
                      InputLabelProps={{
                        shrink: true,
                      }}
                    />
                    <TextField
                      required
                      id="outlined-required"
                      label="Capacidad"
                      type="number"
                      style = {{width:'15ch'}}    
                      value= {capacidad}
                      name = {capacidad}
                      onChange={e => setCapacidad(e.target.value)}   
                      InputLabelProps={{
                        shrink: true,
                      }}
                    />
                    {/*
                    <!--TextField
                    required
                    id="outlined-number"
                    label="Hora de entrega"
                    defaultValue = "DD:MM:YYYY:HH:MM:SS"
                    InputLabelProps={{
                      shrink: true,
                    }}
                  */}                 
                    <DateTimePicker

                        renderInput={(props) => <TextField {...props} />}
                        label="Fecha Pedido"
                        value={value}
                        onChange={(newValue) => {
                        setValue(newValue);
                        }}
                    />
                    <TextField
                      required
                      id="outlined-required"
                      label="Horas Limite"
                      type="number"
                      style = {{width:'15ch'}}    
                      value= {horaslimite}
                      name = {horaslimite}
                      onChange={e => setHoraLimite(e.target.value)}   
                      InputLabelProps={{
                        shrink: true,
                      }}
                    />

                    {/*<DateTimePicker
                      renderInput={(props) => <TextField {...props} />}
                      label="Horas Limite"
                      value={value}
                      onChange={(newValue) => {
                      setHoraLimite(newValue);
                      }}
                    />*/}

                    <Button variant="contained" onClick={sendData} sx={{mt:1, mx:2, height: 50 , width:'15ch'}} >
                  Agregar Pedido
                  </Button>
                  {/*<Button variant="contained" onClick={simulacionDia} sx={{mt:1, mx:2, height: 50, width:'15ch'}} >
                  Empezar Operación
                  </Button>
                  <Button variant="contained" onClick={sendBlock} sx={{mt:1, mx:2, height: 50}} >
                  Pa probar tu bloqueo p
                  </Button>*/}
                </Box>
              </CardContent>
            </Card>
          </Grid>
          <CustomSnackbar alert={alert} setAlert={setAlert}/>
          <MapO simulacion={1}></MapO>
        </Grid>
        
      </Container>
      
    </>
  );
}

export default Forms;