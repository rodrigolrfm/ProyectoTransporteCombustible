import { Helmet } from 'react-helmet-async';
import PageTitle from 'src/components/PageTitle';
import { useState } from 'react';

import PageTitleWrapper from 'src/components/PageTitleWrapper';
import { Container, Grid, Card, CardHeader, CardContent, Divider , Button } from '@mui/material';

import MapO from 'src/components/MapO/MapO';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import { styled } from '@mui/material/styles';
import FormControl from '@mui/material/FormControl';
import * as React from 'react';
import AdapterDateFns from '@mui/lab/AdapterDateFns';
import LocalizationProvider from '@mui/lab/LocalizationProvider';
import DateTimePicker from '@mui/lab/DateTimePicker';
import axios from 'axios';
import url from  'src/utils/constant';
import * as moment from 'moment';


const Input = styled('input')({
  display: 'none',
}); 


function Forms() {

  const [posicionX,setPosicionX] = useState('')
  const [posicionY,setPosicionY] = useState('')
  const [capacidad,setCapacidad] = useState('')
  //const [value, setValue] = React.useState(new Date());
  //const [horaslimite, setHoraLimite] = React.useState(new Date());
  const [value, setValue] = React.useState(new Date());
  const [horaslimite, setHoraLimite] = useState('')

  const sendData= ()=>{
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
            console.log(r);
            console.log(data);
            
            
        });
  }

  return (
    <>
      <Helmet>
        <title>Forms - Components</title>
      </Helmet>
      <FormControl fullWidth sx={{ mt: 3 }}/>
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
                  <Box>
                    <TextField
                      required
                      id="outlined-required"
                      label="Posición X"
                      type="number"
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

                    <Button variant="contained" onClick={sendData} sx={{mt:1, mx:2, height: 50}} >
                  Agregar Pedido
                  </Button>
                  </Box>
                </Box>
              </CardContent>
            </Card>
          </Grid>
          <MapO simulacion={1}></MapO>
        </Grid>
        
      </Container>
      
    </>
  );
}

export default Forms;