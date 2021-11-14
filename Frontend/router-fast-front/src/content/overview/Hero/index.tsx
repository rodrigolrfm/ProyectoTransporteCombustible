import {
  Box,
  Button,
  Container,
  Grid,
  Typography
} from '@mui/material';

import { Link as RouterLink } from 'react-router-dom';
import TextField from '@mui/material/TextField';
import { styled } from '@mui/material/styles';

const TypographyH1 = styled(Typography)(
  ({ theme }) => `
    font-size: ${theme.typography.pxToRem(50)};
`
);

const TypographyH2 = styled(Typography)(
  ({ theme }) => `
    font-size: ${theme.typography.pxToRem(17)};
`
);

const LabelWrapper = styled(Box)(
  ({ theme }) => `
    background-color: ${theme.colors.success.main};
    color: ${theme.palette.success.contrastText};
    font-weight: bold;
    border-radius: 30px;
    text-transform: uppercase;
    display: inline-block;
    font-size: ${theme.typography.pxToRem(11)};
    padding: ${theme.spacing(.5)} ${theme.spacing(1.5)};
    margin-bottom: ${theme.spacing(2)};
`
);


const logox =
{

  img: '/static/images/avatars/logo.png'
};
function Hero() {
  return (
    <Container maxWidth="xl" sx={{ textAlign: "center" }}>
      <Grid
        spacing={{ xs: 4, md: 8 }}
        justifyContent="center"
        alignItems="center"
        container
      >
        
        <Grid item md={10} lg={8} mx="auto">
          <LabelWrapper color="success">Grupo 2B</LabelWrapper>
          <TypographyH1 sx={{ mb: 2 }} variant="h1">
            Router Fast
          </TypographyH1>
          <Container maxWidth="sm">
          <img src={logox.img} alt="" />
            </Container>
          
          <TypographyH2
            sx={{ lineHeight: 1.5, pb: 4 }}
            variant="h2"
            color="text.primary"
            fontWeight="normal"
          >
            Iniciar sesión
          </TypographyH2>
                <Box
                  component="form"
                  sx={{
                    '& .MuiTextField-root': { m: 2, width: '40ch' },
                  }}
                  noValidate
                  autoComplete="off"
                >
                  <div>
                  <TextField required id="outlined-search" label="Usuario o correo electrónico" type="input" />
                    <TextField
                      id="outlined-password-input"
                      label="Contraseña"
                      type="password"
                      required
                      autoComplete="current-password"
                    />                                     
                    
                  </div>
                  <div>                    
                  </div>
                </Box>
          <Button
            component={RouterLink}
            to="/logistica/simulacion"
            size="large"
            variant="contained"
            sx={{my:2,px:14}}
          >
            Iniciar sesión
          </Button>

          <Grid container spacing={3} mt={5}></Grid>
        </Grid>
      </Grid>
    </Container>
  );
}

export default Hero;
