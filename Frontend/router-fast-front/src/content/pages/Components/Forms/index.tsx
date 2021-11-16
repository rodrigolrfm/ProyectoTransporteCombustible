import { Helmet } from 'react-helmet-async';
import PageTitle from 'src/components/PageTitle';
import { useState } from 'react';

import PageTitleWrapper from 'src/components/PageTitleWrapper';
import { Container, Grid, Card, CardHeader, CardContent, Divider , Button } from '@mui/material';

import MapO from 'src/components/MapO/MapO';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';

import FormControl from '@mui/material/FormControl';






function Forms() {


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
                      InputLabelProps={{
                        shrink: true,
                      }}
                    />
                    <TextField
                      required
                      id="outlined-required"
                      label="Posición Y"
                      type="number"
                      InputLabelProps={{
                        shrink: true,
                      }}
                    />
                    <TextField
                      required
                      id="outlined-required"
                      label="Capacidad"
                      type="number"
                      InputLabelProps={{
                        shrink: true,
                      }}
                    />
                    
                    <TextField
                      required
                      id="outlined-number"
                      label="Hora de entrega"
                      defaultValue = "DD:MM:YYYY:HH:MM:SS"
                      InputLabelProps={{
                        shrink: true,
                      }}
                    />
                    <Button variant="contained" sx={{mt:1, mx:2, height: 50}}>
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