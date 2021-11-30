import { Card, Box, Typography, Avatar } from '@mui/material';

import { styled } from '@mui/material/styles';
import Label from 'src/components/Label';
import Text from 'src/components/Text';
import WatchListColumn1Chart from './WatchListColumn1Chart';


const AvatarWrapper = styled(Avatar)(
  ({ theme }) => `
        background: transparent;
        margin-right: ${theme.spacing(0.5)};
`
);

const WatchListColumn1ChartWrapper = styled(WatchListColumn1Chart)(
  ({ theme }) => `
        height: 130px;
`
);

function WatchListColumn1() {
  let nped=145;
  let fecha="25/11/2021";
  let a = new Array(7); for (let i=0; i<7; ++i) a[i] = 0; 
  const price = {
    week: {
      labels: a,
      data: [10, 20, 42, 51, 15, 25, 37]
    }
  };
  /*
  const price = {
    week: {
      labels: [
        'Monday',
        'Tueday',
        'Wednesday',
        'Thursday',
        'Friday',
        'Saturday',
        'Sunday'
      ],
      data: [10, 20, 42, 51, 15, 25, 37]
    }
  };
  */
  return (
    <Card>
      <Box sx={{ p: 3 }}>
        <Box display="flex" alignItems="center">
          <AvatarWrapper>
            <img alt="BTC" src="/static/images/placeholders/logo/simulacion.png" />
          </AvatarWrapper>
          <Box>
            <Typography variant="h4" noWrap>
              Pedidos Entregados
            </Typography>
            <Typography variant="subtitle1" noWrap>
              Última Simulación de 3 dias
            </Typography>
          </Box>
        </Box>
        <Box
          sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'flex-start',
            pt: 3
          }}
        >
          <Typography variant="h2" sx={{ pr: 1, mb: 1 }}>
            {nped}
          </Typography>
          <Text color="primary">
            <b>pedidos</b>
          </Text>
        </Box>
        <Box
          sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'flex-start'
          }}
        >
          <Label color="primary">Dia de prueba</Label>
          <Typography variant="body2" color="text.secondary" sx={{ pl: 1 }}>
            {fecha}
          </Typography>
        </Box>
      </Box>
      <Box height={130} sx={{ ml: -1.5 }}>
        <WatchListColumn1ChartWrapper
          data={price.week.data}
          labels={price.week.labels}
        />
      </Box>
    </Card>
  );
}

export default WatchListColumn1;
