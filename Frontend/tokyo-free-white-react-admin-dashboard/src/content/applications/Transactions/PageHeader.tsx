import {
  Typography,
  Button,
  Grid,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  IconButton,
  Tooltip,
} from "@material-ui/core";
import { experimentalStyled } from "@material-ui/core/styles";

const Input = experimentalStyled("input")({
  display: "none",
});



function PageHeader() {

  (document.getElementById('bt-subir-pedidos') as HTMLInputElement).disabled = false;
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
        >
          <MenuItem value={1}>Operación Día a Día</MenuItem>
          <MenuItem value={2}>Simulación de 3 Días</MenuItem>
          <MenuItem value={3}>Simulación hasta el colapso logístico</MenuItem>
        </Select>
      </FormControl>
      {/*<Grid item>
        
        <Button
          sx={{ mt: { xs: 2, md: 0 } }}
          variant="contained"
          startIcon={<AddTwoToneIcon fontSize="small" />}
        >
          Crear usuario
        </Button>
      </Grid>
      */}
      <Grid container spacing={2} sx={{ mt: 2 }}>
        <Grid item xs={2}>
          <Input accept="text/plain,.csv"  multiple type="file" />
          <label htmlFor="change-cover">
            <Button variant="contained" id="bt-subir-pedidos" component="span">
              Subir Pedidos
            </Button>
          </label>
        </Grid>
        <Grid item xs={2}>
          <Input accept="text/plain,.csv" id="change-cover" multiple type="file" />
          <label htmlFor="change-cover">
            <Button variant="contained" component="span">
              Subir Vehículos
            </Button>
          </label>
        </Grid>
        <Grid item xs={2}>
          <Input accept="text/plain,.csv" id="change-cover" multiple type="file" />
          <label htmlFor="change-cover">
            <Button variant="contained" component="span">
              Subir Bloqueos
            </Button>
          </label>
        </Grid>
        <Grid item xs={2}>
          <Input accept="text/plain,.csv" id="change-cover" multiple type="file" />
          <label htmlFor="change-cover">
            <Button variant="contained" component="span">
              Subir Averías
            </Button>
          </label>
        </Grid>

        <Grid item xs={2}>
          <Button variant="contained" disabled>
            Empezar Simulación
          </Button>
        </Grid>
      </Grid>
    </Grid>
  );
}

export default PageHeader;
