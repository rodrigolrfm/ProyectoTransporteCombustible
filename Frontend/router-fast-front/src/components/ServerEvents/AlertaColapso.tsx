import * as React from 'react';
import Alert from '@mui/material/Alert';
import Stack from '@mui/material/Stack';

function AlertaColapso() {
  return (
    <Stack sx={{ width: '100%' }} spacing={2}>
      <Alert severity="info">¡Se ha alcanzado el colaso logístico!</Alert>
      
    </Stack>
  );
}
export default AlertaColapso;