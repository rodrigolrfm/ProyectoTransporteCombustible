import { Typography, Button, Grid } from '@material-ui/core';

import AddTwoToneIcon from '@material-ui/icons/AddTwoTone';

function PageHeader() {

  const user =
  {
    name: 'Catherine Pikes',
    avatar: '/static/images/avatars/1.jpg'
  };
  return (
    <Grid container justifyContent="space-between" alignItems="center">
      <Grid item>
        <Typography variant="h3" component="h3" gutterBottom>
          Lista de Usuarios
        </Typography>
        <Typography variant="subtitle2">
          {user.name}, esta es la lista de usuarios creados
        </Typography>
      </Grid>
      <Grid item>
        <Button
          sx={{ mt: { xs: 2, md: 0 } }}
          variant="contained"
          startIcon={<AddTwoToneIcon fontSize="small" />}
        >
          Crear usuario
        </Button>
      </Grid>
    </Grid>
  );
}

export default PageHeader;
