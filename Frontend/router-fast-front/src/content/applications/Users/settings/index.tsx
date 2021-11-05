import * as React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import { Container, Grid, Card, CardHeader, CardContent, Divider, Box } from '@mui/material';

function createData(dni, nombre, apPaterno, apMaterno, correo, tipo) {
  return { dni, nombre, apPaterno, apMaterno, correo, tipo };
}

const rows = [
  createData('75276939', 'Aaron', 'Chávarry', 'Añanca', 'aaron.chavarry@pucp.edu.pe','Administrativo'),
  createData('72204477', 'Franco', 'Gamarra', 'Cardenas', 'franco.inf13L@pucp.edu.pe','Analista')
]

function ManagementUserSettings() {
  return (
    <>
    <Box mt={2} mx={10}>

          <Card>
              <CardHeader title="Lista de Usuarios" />
              <Divider />
              <CardContent>
              <Container maxWidth="xl">
        <Grid
          container
          direction="row"
          justifyContent="center"
          alignItems="stretch"
          spacing={1}
        >
          <Grid item xs={10}></Grid>
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 200 }} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell>DNI</TableCell>
            <TableCell align="right">Nombres</TableCell>
            <TableCell align="right">Apellido Paterno</TableCell>
            <TableCell align="right">Apellido Materno</TableCell>
            <TableCell align="right">Correo</TableCell>
            <TableCell align="right">Tipo</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((row) => (
            <TableRow
              key={row.dni}
              sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
            >
              <TableCell component="th" scope="row">
                {row.dni}
              </TableCell>
              <TableCell align="right">{row.nombre}</TableCell>
              <TableCell align="right">{row.apPaterno}</TableCell>
              <TableCell align="right">{row.apMaterno}</TableCell>
              <TableCell align="right">{row.correo}</TableCell>
              <TableCell align="right">{row.tipo}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
    </Grid>
    </Container>

              </CardContent>
    </Card>
    </Box>
    </>
  );
}
export default ManagementUserSettings;
