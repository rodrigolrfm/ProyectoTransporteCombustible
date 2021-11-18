import { Helmet } from 'react-helmet-async';
import { useState } from 'react';

import { Container, Grid, Card, CardHeader, CardContent, Divider } from '@mui/material';


import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import MenuItem from '@mui/material/MenuItem';
import { pink } from '@mui/material/colors';
import Checkbox from '@mui/material/Checkbox';

import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormControl from '@mui/material/FormControl';
import FormLabel from '@mui/material/FormLabel';

import Stack from '@mui/material/Stack';
import Slider from '@mui/material/Slider';
import VolumeDown from '@mui/icons-material/VolumeDown';
import VolumeUp from '@mui/icons-material/VolumeUp';

import Switch from '@mui/material/Switch';



function Forms() {

  const [currency, setCurrency] = useState('EUR');

  const handleChange = (event) => {
    setCurrency(event.target.value);
  };

  const [value, setValue] = useState(30);

  const handleChange2 = (event, newValue) => {
    setValue(newValue);
  };


  return (
    <>
    <Box mt={5}></Box>
      <Helmet>
        <title>Usuarios - Registrar Usuario</title>
      </Helmet>
      <Grid container spacing={1}></Grid>
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
              <CardHeader title="Registrar Usuario" />
              <Divider />
              <CardContent>
                <Box
                  component="form"
                  sx={{
                    '& .MuiTextField-root': { m: 1, width: '35ch' },
                  }}
                  noValidate
                  autoComplete="off"
                >
                  <div>
                    <TextField
                      required
                      id="registerName"
                      label="Nombres completos"
                      variant="standard"
                    />
                    <TextField
                      required
                      id="registerApPat"
                      label="Apellido Paterno"
                      variant="standard"
                    />
                    <TextField
                      required
                      id="registerApMat"
                      label="Apellido Materno"
                      variant="standard"
                    />
                    <TextField
                      required
                      id="registerDNI"
                      label="DNI"
                      type = "number"
                      variant="standard"
                    />
                    <TextField
                      required
                      id="registerEmail"
                      label="Correo electrónico"
                      variant="standard"
                    />
                    <TextField
                      required
                      id="standard-password-input"
                      label="Contraseña"
                      type="password"
                      variant="standard"
                    />
                    <Box mt={1} ml={1}>
                    <FormControl component="fieldset">
                  <FormLabel component="legend">Tipo de usuario</FormLabel>
                  <RadioGroup row aria-label="tipos" name="row-radio-buttons-group">
                    <FormControlLabel value="Administrativo" control={<Radio />} label="Administrativo" />
                    <FormControlLabel value="Analista" control={<Radio />} label="Analista" />
                  </RadioGroup>
                </FormControl>
                </Box>
                  </div>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </Container>
    </>
  );
}

export default Forms;
