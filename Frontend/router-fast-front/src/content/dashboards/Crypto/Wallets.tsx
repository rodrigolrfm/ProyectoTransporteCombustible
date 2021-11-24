import {
  Button,
  Card,
  Grid,
  Box,
  CardContent,
  Typography,
  Avatar,
  Tooltip,
  CardActionArea
} from '@mui/material';

import { styled } from '@mui/material/styles';
import AddTwoToneIcon from '@mui/icons-material/AddTwoTone';

const AvatarWrapper = styled(Avatar)(
  ({ theme }) => `
        background: transparent;
        margin-left: -${theme.spacing(0.5)};
        margin-bottom: ${theme.spacing(1)};
        margin-top: ${theme.spacing(2)};
`
);

const AvatarAddWrapper = styled(Avatar)(
  ({ theme }) => `
        background: ${theme.colors.alpha.black[5]};
        color: ${theme.colors.primary.main};
        width: ${theme.spacing(8)};
        height: ${theme.spacing(8)};
`
);

const CardAddAction = styled(Card)(
  ({ theme }) => `
        border: ${theme.colors.primary.main} dashed 1px;
        height: 100%;
        color: ${theme.colors.primary.main};
        
        .MuiCardActionArea-root {
          height: 100%;
          justify-content: center;
          align-items: center;
          display: flex;
        }
        
        .MuiTouchRipple-root {
          opacity: .2;
        }
        
        &:hover {
          border-color: ${theme.colors.alpha.black[100]};
        }
`
);

function Wallets() {

  return (
    <>
      <Box
        display="flex"
        alignItems="center"
        justifyContent="space-between"
        sx={{ pb: 3 }}
      >
        <Typography variant="h3">Datos Relevantes</Typography>
       
      </Box>
      <Grid container spacing={3}>
        <Grid xs={12} sm={6} md={3} item>
          <Card sx={{ px: 1 }}>
            <CardContent>
              <AvatarWrapper>
                <img
                  alt="BTC"
                  src="/static/images/placeholders/logo/bitcoin.png"
                />
              </AvatarWrapper>
              <Typography variant="h5" noWrap>
                Flota de Camiones
              </Typography>
              
              <Box sx={{ pt: 3 }}>
                <Typography variant="h3" gutterBottom noWrap>
                  20
                </Typography>
                <Typography variant="subtitle2" noWrap>
                  Disponibles
                </Typography>
              </Box>
            </CardContent>
          </Card>
        </Grid>
        <Grid xs={12} sm={6} md={3} item>
          <Card sx={{ px: 1 }}>
            <CardContent>
              <AvatarWrapper>
                <img
                  alt="Ripple"
                  src="/static/images/placeholders/logo/ripple.png"
                />
              </AvatarWrapper>
              <Typography variant="h5" noWrap>
                Tamaño del mapa
              </Typography>
              <Typography variant="subtitle1" noWrap>
                
              </Typography>
              <Box sx={{ pt: 3 }}>
                <Typography variant="h3" gutterBottom noWrap>
                  50 x 70
                </Typography>
                <Typography variant="subtitle2" noWrap>
                  ancho x largo
                </Typography>
              </Box>
            </CardContent>
          </Card>
        </Grid>
        <Grid xs={12} sm={6} md={3} item>
          <Card sx={{ px: 1 }}>
            <CardContent>
              <AvatarWrapper>
                <img
                  alt="Cardano"
                  src="/static/images/placeholders/logo/cardano.png"
                />
              </AvatarWrapper>
              <Typography variant="h5" noWrap>
                Horario de trabajo
              </Typography>
              <Typography variant="subtitle1" noWrap>
              
              </Typography>
              <Box sx={{ pt: 3 }}>
                <Typography variant="h3" gutterBottom noWrap>
                  24 horas
                </Typography>
                <Typography variant="subtitle2" noWrap>
                  7 dias
                </Typography>
              </Box>
            </CardContent>
          </Card>
        </Grid>
        <Grid xs={12} sm={6} md={3} item>
          <Tooltip arrow title="Click to add a new wallet">
            <CardAddAction>
              <CardActionArea sx={{ px: 1 }}>
                <CardContent>
                  <AvatarAddWrapper>
                    <AddTwoToneIcon fontSize="large" />
                  </AvatarAddWrapper>
                </CardContent>
              </CardActionArea>
            </CardAddAction>
          </Tooltip>
        </Grid>
      </Grid>
    </>
  );
}

export default Wallets;
